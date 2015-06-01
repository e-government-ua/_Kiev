package org.activiti.rest.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.wf.dp.dniprorada.dao.DocumentContentTypeDao;
import org.wf.dp.dniprorada.dao.DocumentDao;
import org.wf.dp.dniprorada.dao.HistoryEventDao;
import org.wf.dp.dniprorada.model.Document;
import org.wf.dp.dniprorada.model.DocumentContentType;
import org.wf.dp.dniprorada.model.HistoryEvent;
import org.wf.dp.dniprorada.util.Util;
import org.wf.dp.dniprorada.constant.HistoryEventType;


//import org.springframework.mock.web.MockMultipartFile;


@Controller
@RequestMapping(value = "/services")
public class ActivitiRestDocumentController {

    @Autowired
    private DocumentDao documentDao;

    @Autowired
    private HistoryEventDao historyEventDao;

    @Autowired
    private DocumentContentTypeDao documentContentTypeDao;

    @RequestMapping(value = "/getDocument", method = RequestMethod.GET)
    public
    @ResponseBody
    Document getDocument(@RequestParam(value = "nID") Long id) {
        return documentDao.getDocument(id);
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
    String getDocumentContent(@RequestParam(value = "nID") Long id) {
        return Util.contentByteToString(documentDao.getDocumentContent(id)); // ????
    }

    @RequestMapping(value = "/getHistoryEvents", method = RequestMethod.GET)
    public
    @ResponseBody
    List<HistoryEvent> getHistoryEvents(
            @RequestParam(value = "nID_Subject") String nID_Subject) {
        return historyEventDao.getHistoryEvents(nID_Subject);
    }

    @RequestMapping(value = "/setHistoryEvent", method = RequestMethod.POST)
    public
    @ResponseBody
    Long setHistoryEvent(
            @RequestParam(value = "nID_Subject") Long nID_Subject,
            @RequestParam(value = "nID_HistoryEventType") Long nID_HistoryEventType,
            @RequestParam(value = "sEventName_Custom") String sEventName_Custom,
            @RequestParam(value = "sMessage") String sMessage,
            @RequestParam(value = "sDate", required = false) String sDate,

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
                           HttpServletRequest request, HttpServletResponse httpResponse) {
        Document document = documentDao.getDocument(id);
        byte[] content = documentDao.getDocumentContent(document
                .getСontentKey());
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
            @RequestParam(value = "sID_Subject") String sID_Subject) {
        return documentDao.getDocuments(sID_Subject);
    }

    @RequestMapping(value = "/setDocument", method = RequestMethod.GET)
    public
    @ResponseBody
    Long setDocument(
            @RequestParam(value = "nID_Subject_Upload", required = false) Long nID_Subject_Upload,
            //Todo: убрать, когда клиент отцепится
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

        return documentDao.setDocument(
                nID_Subject_Upload,
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
            @RequestParam(value = "nID_Subject_Upload", required = false) Long nID_Subject_Upload,
            //Todo: убрать, когда клиент отцепится
            @RequestParam(value = "sID_Subject_Upload") String sID_Subject_Upload,
            @RequestParam(value = "sSubjectName_Upload") String sSubjectName_Upload,
            @RequestParam(value = "sName") String sName,
            //@RequestParam(value = "sFile", required = false) String fileName,
            @RequestParam(value = "nID_DocumentType") Integer nID_DocumentType,
            @RequestParam(value = "nID_DocumentContentType", required = false) Integer nID_DocumentContentType,
            @RequestParam(value = "oFile", required = false) MultipartFile oFile,
            //@RequestBody byte[] content,
            HttpServletRequest request, HttpServletResponse httpResponse) throws IOException {

        String sFileName = oFile.getName();
        String sFileContentType = oFile.getContentType();
        byte[] aoContent = oFile.getBytes();

        return documentDao
                .setDocument(
                        nID_Subject_Upload,
                        sID_Subject_Upload,
                        sSubjectName_Upload,
                        sName,
                        nID_DocumentType,
                        nID_DocumentContentType,
                        sFileName,
                        sFileContentType,
                        aoContent);
    }

}
