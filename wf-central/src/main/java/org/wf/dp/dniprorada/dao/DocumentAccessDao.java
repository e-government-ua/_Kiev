package org.wf.dp.dniprorada.dao;

import org.wf.dp.dniprorada.base.dao.EntityDao;
import org.wf.dp.dniprorada.model.DocumentAccess;

public interface DocumentAccessDao extends EntityDao<DocumentAccess> {
    public Long getIdAccess() throws Exception;

    public String setDocumentLink(Long nID_Document, String sFIO, String sTarget, String sTelephone, Long nMS,
            String sMail) throws Exception;

    public DocumentAccess getDocumentLink(Long nID_Access, String sSecret) throws Exception;

    //public boolean bSentDocumentAccessOTP(String sCode) throws Exception;
    public String sSentDocumentAccessOTP_Phone(String sCode) throws Exception;

    public String setDocumentAccess(Long nID_Access, String sSecret, String sAnswer) throws Exception;

    public String getDocumentAccess(Long nID_Access, String sSecret) throws Exception;

    public DocumentAccess getDocumentAccess(String sCode_DocumentAccess);
}
