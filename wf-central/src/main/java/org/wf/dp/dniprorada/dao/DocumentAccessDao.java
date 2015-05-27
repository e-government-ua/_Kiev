package org.wf.dp.dniprorada.dao;

import org.wf.dp.dniprorada.model.DocumentAccess;


public interface DocumentAccessDao {
	public String setDocumentLink(Long nID_Document, String sFIO,
			String sTarget, String sTelephone, Long nMS, String sMail) throws Exception;
	public DocumentAccess getDocumentLink(Long nID_Access, String sSecret) throws Exception;
	public String setDocumentAccess(Long nID_Access, String sSecret, String sAnswer) throws Exception;
	public String getDocumentAccess(Long nID_Access, String sSecret) throws Exception;
}
