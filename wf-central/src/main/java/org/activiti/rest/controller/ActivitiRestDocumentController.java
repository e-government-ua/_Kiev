package org.activiti.rest.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.task.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.wf.dp.dniprorada.dao.DocumentContentTypeDao;
import org.wf.dp.dniprorada.dao.DocumentDao; 
import org.wf.dp.dniprorada.model.Document;
import org.wf.dp.dniprorada.model.DocumentContentType;
import org.wf.dp.dniprorada.util.Util;

//import org.springframework.mock.web.MockMultipartFile;


@Controller
@RequestMapping(value = "/services")
public class ActivitiRestDocumentController {

	@Autowired
	private DocumentDao documentDao;
	
	@Autowired
	private DocumentContentTypeDao documentContentTypeDao;

	@RequestMapping(value = "/getDocument", method = RequestMethod.GET)
	public @ResponseBody
	Document getDocument(@RequestParam(value = "nID") Long id) {
		return documentDao.getDocument(id);
	}

	@RequestMapping(value = "/getDocumentContent", method = RequestMethod.GET)
	public @ResponseBody
	String getDocumentContent(@RequestParam(value = "nID") Long id) {
		return Util.contentByteToString(documentDao.getDocumentContent(id)); // ????
	}

	@RequestMapping(value = "/getDocumentFile", method = RequestMethod.GET)
	public @ResponseBody
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
	public @ResponseBody
	List<Document> getDocuments(
			@RequestParam(value = "sID_Subject") String sID_Subject) {
		return documentDao.getDocuments(sID_Subject);
	}

	@RequestMapping(value = "/setDocument", method = RequestMethod.POST)
	public @ResponseBody
	Long setDocument(
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
                        
                        Integer nID_DocumentContentType;
                        documentContentTypeName = request.getHeader("Content-Type") != null ? request.getHeader("filename") :  documentContentTypeName; 
                        if(documentContentTypeName != null){
                        	nID_DocumentContentType = documentContentTypeDao.getDocumentContentType(documentContentTypeName).getId();
                        } else{
                        	throw new ActivitiObjectNotFoundException(
                                    "RequestParam 'nID_DocumentContentType' not found!", DocumentContentType.class);
                        } 
                        
                        return documentDao.setDocument(
                        		sID_Subject_Upload,
                        		sSubjectName_Upload,
                        		sName,
                        		nID_DocumentType,
                        		nID_DocumentContentType,
                                sFileName,
                                sFileContentType,
		                        aoContent);
						//oFile
						//fileName == null ? request.getHeader("filename"): fileName,
						//documentContentType == null ? 2 : documentContentType, content);
                                                
	}
        
	@RequestMapping(value = "/setDocumentFile", method = RequestMethod.POST)
	public @ResponseBody
	Long setDocumentFile(
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
						sID_Subject_Upload,
						sSubjectName_Upload,
						sName,
						nID_DocumentType,
						nID_DocumentContentType,
                                                sFileName,
                                                sFileContentType,
						aoContent
						//oFile
						//fileName == null ? request.getHeader("filename"): fileName,
						//documentContentType == null ? 2 : documentContentType, content);
                                                );
	}

}
