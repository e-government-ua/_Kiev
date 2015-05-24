package org.wf.dp.dniprorada.dao;

import org.wf.dp.dniprorada.model.DocumentContentType;

public interface DocumentContentTypeDao {

	public DocumentContentType getDocumentContentType(String documentContentTypeName);
	
	public Integer setDocumentContent(DocumentContentType documentContentType);
}
