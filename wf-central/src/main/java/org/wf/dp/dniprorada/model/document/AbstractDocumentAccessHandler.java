package org.wf.dp.dniprorada.model.document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author dgroup
 * @since 29.08.2015
 */
@Component
public abstract class AbstractDocumentAccessHandler implements DocumentAccessHandler {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractDocumentAccessHandler.class);

    protected String accessCode;
    protected String password;
    protected Long documentTypeId;
    protected Boolean withContent;
    protected Long nID_Subject;

    public DocumentAccessHandler setAccessCode(String sCode_DocumentAccess) {
        LOG.info("sCode_DocumentAccess = {} ", sCode_DocumentAccess);
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

    public DocumentAccessHandler setWithContent(Boolean bWithContent) {
        this.withContent = bWithContent;
        return this;
    }

    public DocumentAccessHandler setIdSubject(Long nID_Subject) {
        this.nID_Subject = nID_Subject;
        return this;
    }

}
