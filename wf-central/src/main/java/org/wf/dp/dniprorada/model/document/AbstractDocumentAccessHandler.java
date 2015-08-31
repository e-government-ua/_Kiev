package org.wf.dp.dniprorada.model.document;

/**
 * @author dgroup
 * @since  29.08.2015
 */
public abstract class AbstractDocumentAccessHandler implements DocumentAccessHandler {
    protected String  accessCode;
    protected String  password;
    protected Long documentTypeId;


    public DocumentAccessHandler setAccessCode(String sCode_DocumentAccess) {
        this.accessCode = sCode_DocumentAccess;
        return this;
    }


    public DocumentAccessHandler setPassword(String password) {
        this.password = password;
        return this;
    }


    public DocumentAccessHandler setDocumentType(Long docTypeID) {
        this.documentTypeId = docTypeID;
        return this;
    }
}
