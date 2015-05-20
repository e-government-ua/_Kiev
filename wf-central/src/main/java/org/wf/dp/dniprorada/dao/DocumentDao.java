package org.wf.dp.dniprorada.dao;

import java.util.List;

import org.wf.dp.dniprorada.model.Document;

public interface DocumentDao {
	
	public Document getDocument(Long id);
	public byte[] getDocumentContent(Long id);
	public byte[] getDocumentContent(String contentKey);
	public List<Document> getDocuments(String subject_Upload);
	public void setDocument(String subject_Upload, String subjectName_Upload,
			String name, String file, Integer documentTypeId, Integer contentTypeId, byte[] content);

}
