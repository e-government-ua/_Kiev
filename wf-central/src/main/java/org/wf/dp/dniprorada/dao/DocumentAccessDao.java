package org.wf.dp.dniprorada.dao;

import org.wf.dp.dniprorada.model.DocumentAccess;


public interface DocumentAccessDao {
	public String setDocumentLink(Integer nID_Document, String sFIO,
			String sTarget, String sTelephone, Long nDays, String sMail);
	public DocumentAccess getDocumentLink(String nID_Access, String sSecret);
}
