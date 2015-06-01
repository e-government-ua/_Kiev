package org.wf.dp.dniprorada.dao;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import org.wf.dp.dniprorada.model.Document;

public interface DocumentDao {
	
	public Document getDocument(Long id);
	public byte[] getDocumentContent(Long id);
	public byte[] getDocumentContent(String contentKey);
	public List<Document> getDocuments(String subject_Upload);
	//public Long setDocument(String subject_Upload, String subjectName_Upload,
	//		String name, String file, Integer documentTypeId, Integer contentTypeId, byte[] content);
	//public Long setDocument(String sID_Subject_Upload, String sSubjectName_Upload,
	//		String sName, Integer nID_DocumentType,
	//		Integer nID_DocumentContentType, MultipartFile oFile) throws IOException;
	public Long setDocument(Long nID_Subject_Upload, String sID_Subject_Upload, String sSubjectName_Upload,
			String sName, Integer nID_DocumentType,
			Integer nID_DocumentContentType, String sFileName, String sFileContentType, byte[] aoContent) throws IOException;

}
