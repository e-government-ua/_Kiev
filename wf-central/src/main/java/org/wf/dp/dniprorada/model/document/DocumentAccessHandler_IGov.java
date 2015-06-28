package org.wf.dp.dniprorada.model.document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wf.dp.dniprorada.dao.DocumentAccessDao;
import org.wf.dp.dniprorada.model.DocumentAccess;

/**
 * @author dgroup
 * @since 28.06.15
 */
@Service
public class DocumentAccessHandler_IGov implements DocumentAccessHandler {
    private String  accessCode;
    private String  password;

    @Autowired
    private DocumentAccessDao documentAccessDao;

    public DocumentAccessHandler setAccessCode(String sCode_DocumentAccess) {
        accessCode = sCode_DocumentAccess;
        return this;
    }

    public DocumentAccessHandler setPassword(String password) {
        this.password = password;
        return this;
    }



    @Override
    public DocumentAccess getAccess() {
        DocumentAccess access = documentAccessDao.getDocumentAccess(accessCode);

        if (access == null)
            throw new DocumentNotFoundException("Document Access not found");

        if (access.getsCodeType() == null || access.getsCodeType().trim().isEmpty())
            return access;

        // TODO change to Enum or something like that
        if ("SMS".equalsIgnoreCase(access.getsCodeType()) &&
            access.getAnswer().equalsIgnoreCase(password))
            return access;
        else
            throw new DocumentAccessException("Document Access wrong password");
    }
}
