package org.activiti.rest.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wf.dp.dniprorada.dao.DocumentDao; 
import org.wf.dp.dniprorada.model.Document;
import org.wf.dp.dniprorada.util.Util;

@Controller
@RequestMapping(value = "/services")
public class ActivitiRestDocumentController {

	@Autowired
	//@Qualifier(value = "documentDao")
	private DocumentDao documentDao;

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
		httpResponse.setHeader("Content-Type", document.getDocumentType()
				.getName() + ";charset=UTF-8");
		httpResponse.setContentLength(content.length);
		return content;
	}

	@RequestMapping(value = "/getDocuments", method = RequestMethod.GET)
	public @ResponseBody
	List<Document> getDocuments(
			@RequestParam(value = "sID_Subject") String subject_Upload) {
		return documentDao.getDocuments(subject_Upload);
	}

	@RequestMapping(value = "/setDocumentContent", method = RequestMethod.GET)
	public @ResponseBody
	Long setDocument(
			@RequestParam(value = "sID_SubjectUpload") String subject_Upload,
			@RequestParam(value = "sSubjectName_Upload") String subjectName_Upload,
			@RequestParam(value = "sName") String documentName,
			@RequestParam(value = "sFile", required = false) String fileName,// defaultValue															
			@RequestParam(value = "nID_DocumentType") Integer documentType,
			@RequestParam(value = "nID_DocumentContentType") Integer documentContentType,
			@RequestParam(value = "soDocumentContent") String content,
			HttpServletRequest request, HttpServletResponse httpResponse) {
		
		return documentDao.setDocument(subject_Upload, subjectName_Upload,
				documentName, fileName == null ? request.getHeader("filename"): fileName, 
						documentType, documentContentType, Util.contentStringToByte(content));
	}

	@RequestMapping(value = "/setDocument", method = RequestMethod.GET)
	public @ResponseBody
	Long setDocumentFile(
			@RequestParam(value = "sID_SubjectUpload") String subject_Upload,
			@RequestParam(value = "sSubjectName_Upload") String subjectName_Upload,
			@RequestParam(value = "sName") String documentName,
			@RequestParam(value = "sFile", required = false) String fileName,
			@RequestParam(value = "nID_DocumentType") Integer documentType,
			@RequestParam(value = "nID_DocumentContentType", required = false) Integer documentContentType,
			@RequestBody byte[] content,
			HttpServletRequest request, HttpServletResponse httpResponse) {
		
		return documentDao
				.setDocument(
						subject_Upload,
						subjectName_Upload,
						documentName,
						fileName == null ? request.getHeader("filename"): fileName,
						documentType,
						documentContentType == null ? 2 : documentContentType, content);
	}

}
