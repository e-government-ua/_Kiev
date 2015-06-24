package org.activiti.rest.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.redis.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.wf.dp.dniprorada.dao.*;
import org.wf.dp.dniprorada.model.Document;
import org.wf.dp.dniprorada.model.DocumentContentType;
import org.wf.dp.dniprorada.model.HistoryEvent;
import org.wf.dp.dniprorada.model.Subject;
import org.wf.dp.dniprorada.util.Util;

@Controller
@RequestMapping(value = "/services")
public class ActivitiRestDocumentController {

    @Autowired
    private DocumentDao documentDao;
    
    @Autowired
    private SubjectDao subjectDao;
    
    @Autowired
    private SubjectOrganDao subjectOrganDao;

    @Autowired
    private HistoryEventDao historyEventDao;

    @Autowired
    private DocumentContentTypeDao documentContentTypeDao;

    @RequestMapping(value = "/getDocument", method = RequestMethod.GET)
    public
    @ResponseBody
    Document getDocument(@RequestParam(value = "nID") Long id,
            @RequestParam(value = "nID_Subject") long nID_Subject) throws ActivitiRestException{
        Document document = documentDao.getDocument(id);
        if(nID_Subject != document.getSubject().getnID()){  
            throw new ActivitiRestException("401", "You don't have access! Yuor nID = " + nID_Subject + " Document's Subject's nID = " + document.getSubject().getnID());
        } else{
            return  document;
        }
    }

    @RequestMapping(value = "/getHistoryEvent", method = RequestMethod.GET)
    public
    @ResponseBody
    HistoryEvent getHistoryEvent(@RequestParam(value = "nID") Long id) {
        return historyEventDao.getHistoryEvent(id);
    }

    @RequestMapping(value = "/getDocumentContent", method = RequestMethod.GET)
    public
    @ResponseBody
    String getDocumentContent(@RequestParam(value = "nID") Long id, 
            @RequestParam(value = "nID_Subject") long nID_Subject) throws ActivitiRestException {
        Document document = documentDao.getDocument(id);
        if(nID_Subject != document.getSubject().getnID()){
            throw new ActivitiRestException("401", "You don't have access!");
        } else{
            return Util.contentByteToString(documentDao.getDocumentContent(document.getСontentKey())); // ????
        }
    }

    @RequestMapping(value = "/getHistoryEvents", method = RequestMethod.GET)
    public
    @ResponseBody
    List<HistoryEvent> getHistoryEvents(
            @RequestParam(value = "nID_Subject") long nID_Subject) {
        return historyEventDao.getHistoryEvents(nID_Subject);
    }

    @RequestMapping(value = "/setHistoryEvent", method = RequestMethod.POST)
    public
    @ResponseBody
    Long setHistoryEvent(
            @RequestParam(value = "nID_Subject", required = false) long nID_Subject,
            @RequestParam(value = "nID_HistoryEventType", required = false) Long nID_HistoryEventType,
            @RequestParam(value = "sEventName", required = false) String sEventName_Custom,
            @RequestParam(value = "sMessage") String sMessage,

            HttpServletRequest request, HttpServletResponse httpResponse) throws IOException {


        return historyEventDao.setHistoryEvent(
                nID_Subject,
                nID_HistoryEventType,
                sEventName_Custom,
                sMessage);

    }


    @RequestMapping(value = "/getDocumentFile", method = RequestMethod.GET)
    public
    @ResponseBody
    byte[] getDocumentFile(@RequestParam(value = "nID") Long id,
            @RequestParam(value = "nID_Subject") long nID_Subject,
                           HttpServletRequest request, HttpServletResponse httpResponse) 
                           throws ActivitiRestException{
        Document document = documentDao.getDocument(id);
        if(nID_Subject != document.getSubject().getnID()){
            throw new ActivitiRestException("401", "You don't have access!");
        } 
        byte[] content = documentDao.getDocumentContent(document
                .getСontentKey());
        //byte[] content = "".getBytes();
        
        httpResponse.setHeader("Content-disposition", "attachment; filename="
                + document.getFile());
        //httpResponse.setHeader("Content-Type", document.getDocumentContentType()
        //		.getName() + ";charset=UTF-8");
        httpResponse.setHeader("Content-Type", document.getContentType() + ";charset=UTF-8");
        httpResponse.setContentLength(content.length);
        return content;
    }

    @RequestMapping(value = "/getDocuments", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Document> getDocuments(
            @RequestParam(value = "nID_Subject") long nID_Subject) {
        return documentDao.getDocuments(nID_Subject);
    }

    @RequestMapping(value = "/setDocument", method = RequestMethod.GET)
    public
    @ResponseBody
    Long setDocument(
            @RequestParam(value = "nID_Subject", required = false) long nID_Subject,
            @RequestParam(value = "sID_Subject_Upload") String sID_Subject_Upload,
            @RequestParam(value = "sSubjectName_Upload") String sSubjectName_Upload,
            @RequestParam(value = "sName") String sName,
            //@RequestParam(value = "sFile", required = false) String fileName,
            @RequestParam(value = "nID_DocumentType") Integer nID_DocumentType,
            //@RequestParam(value = "nID_DocumentContentType", required = false) Integer nID_DocumentContentType,
            @RequestParam(value = "sDocumentContentType", required = false) String documentContentTypeName,
            @RequestParam(value = "soDocumentContent") String sContent,
            //@RequestParam(value = "oFile", required = false) MultipartFile oFile,
            //@RequestBody byte[] content,
            HttpServletRequest request, HttpServletResponse httpResponse) throws IOException {

        //MultipartFile oFile = new MockMultipartFile("filename.txt", "fullfilename.txt", "text/plain", sContent.getBytes());
        String sFileName = "filename.txt";
        String sFileContentType = "text/plain";
        byte[] aoContent = sContent.getBytes();


        documentContentTypeName = request.getHeader("Content-Type") != null ? request.getHeader("filename") : documentContentTypeName;
        DocumentContentType documentContentType = null;
        if (documentContentTypeName != null) {
            documentContentType = documentContentTypeDao.getDocumentContentType(documentContentTypeName);
            if (documentContentType == null) {
                documentContentType = new DocumentContentType();
                documentContentType.setName(documentContentTypeName);
                documentContentType.setId(documentContentTypeDao.setDocumentContent(documentContentType));
            }
        } else {
            throw new ActivitiObjectNotFoundException(
                    "RequestParam 'nID_DocumentContentType' not found!", DocumentContentType.class);
        }
        
        Subject subject_Upload = syncSubject_Upload(sID_Subject_Upload);

        return documentDao.setDocument(
                nID_Subject,
                subject_Upload.getnID(),
                sID_Subject_Upload,
                sSubjectName_Upload,
                sName,
                nID_DocumentType,
                documentContentType.getId(),
                sFileName,
                sFileContentType,
                aoContent);

    }

    @RequestMapping(value = "/setDocumentFile", method = RequestMethod.POST)
    public
    @ResponseBody
    Long setDocumentFile(
            @RequestParam(value = "nID_Subject", required = false) long nID_Subject,
            @RequestParam(value = "sID_Subject_Upload") String sID_Subject_Upload,
            @RequestParam(value = "sSubjectName_Upload") String sSubjectName_Upload,
            @RequestParam(value = "sName") String sName,
            //@RequestParam(value = "sFile", required = false) String fileName,
            @RequestParam(value = "nID_DocumentType") Integer nID_DocumentType,
            @RequestParam(value = "nID_DocumentContentType", required = false) Integer nID_DocumentContentType,
            @RequestParam(value = "oFile", required = true) MultipartFile oFile,
            //@RequestBody byte[] content,
            HttpServletRequest request, HttpServletResponse httpResponse) throws IOException {

        //String sFileName = oFile.getName();
        //String sFileName = oFile.getOriginalFilename();
         //Content-Disposition:attachment; filename=passport.zip
        String sFileName = request.getHeader("filename");
        if(sFileName==null||"".equals(sFileName.trim())){
            String originalFilename = oFile.getOriginalFilename();
            String fileExp = RedisUtil.getFileExp(originalFilename);
            fileExp = fileExp != null ? fileExp : ".zip.zip";
            sFileName = originalFilename + fileExp;
        }
        String sFileContentType = oFile.getContentType();
        byte[] aoContent = oFile.getBytes();

        Subject subject_Upload = syncSubject_Upload(sID_Subject_Upload);
        
        return documentDao
                .setDocument(
                        nID_Subject,
                        subject_Upload.getnID(),
                        sID_Subject_Upload,
                        sSubjectName_Upload,
                        sName,
                        nID_DocumentType,
                        nID_DocumentContentType,
                        sFileName,
                        sFileContentType,
                        aoContent);
    }
    
    private Subject syncSubject_Upload(String sID_Subject_Upload){
    	Subject subject_Upload = subjectDao.getSubject(sID_Subject_Upload);
    	if(subject_Upload == null){
    		subject_Upload = subjectOrganDao.setSubjectOrgan(sID_Subject_Upload).getoSubject();
    	}
    	return subject_Upload;
    }

}
