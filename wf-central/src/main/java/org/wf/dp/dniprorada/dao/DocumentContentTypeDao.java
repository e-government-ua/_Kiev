package org.wf.dp.dniprorada.dao;

import org.wf.dp.dniprorada.base.dao.EntityDao;
import org.wf.dp.dniprorada.model.DocumentContentType;

import java.util.List;

public interface DocumentContentTypeDao extends EntityDao<DocumentContentType> {

    public DocumentContentType getDocumentContentType(String documentContentTypeName);

    public Long setDocumentContent(DocumentContentType documentContentType);

    public List<DocumentContentType> getDocumentContentTypes();

    public DocumentContentType setDocumentContentType(Long nID, String sName);

    public DocumentContentType getDocumentContentType(Long nID);

    public void removeDocumentContentType(Long nID);
}
