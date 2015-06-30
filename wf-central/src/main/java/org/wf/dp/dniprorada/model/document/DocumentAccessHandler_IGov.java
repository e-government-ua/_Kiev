package org.wf.dp.dniprorada.model.document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wf.dp.dniprorada.dao.DocumentAccessDao;
import org.wf.dp.dniprorada.dao.DocumentDao;
import org.wf.dp.dniprorada.model.Document;
import org.wf.dp.dniprorada.model.DocumentAccess;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNumeric;

/**
 * @author dgroup
 * @since 28.06.15
 */
@Component
public class DocumentAccessHandler_IGov implements DocumentAccessHandler {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentAccessHandler_IGov.class);
    private String  accessCode;
    private String  password;
    private Integer documentTypeId;

    @Autowired
    private DocumentAccessDao documentAccessDao;

    @Autowired
    private DocumentDao documentDao;


    public DocumentAccessHandler setAccessCode(String sCode_DocumentAccess) {
        this.accessCode = sCode_DocumentAccess;
        return this;
    }

    public DocumentAccessHandler setPassword(String password) {
        this.password = password;
        return this;
    }

    public DocumentAccessHandler setDocumentType(Integer docTypeID) {
        this.documentTypeId = docTypeID;
        return this;
    }


    @Override
    public DocumentAccess getAccess() {
        DocumentAccess access = documentAccessDao.getDocumentAccess(accessCode);

        if (access == null)
            throw new DocumentNotFoundException("Document Access not found");

        if (isBlank(access.getsCodeType()))
            return access;

        if (isBlank(password) || !isNumeric(password))
            throw new DocumentAccessException("Document Access wrong password");

        int userPass = Integer.valueOf(password);
        int currPass = Integer.valueOf(access.getAnswer());

        if ("SMS".equalsIgnoreCase(access.getsCodeType()) && userPass == currPass)
            return access;
        else
            throw new DocumentAccessException("Document Access wrong password");
    }



    public Document getDocument() {
        Document doc = documentDao.getDocument( getAccess().getID_Document() );

        if (documentTypeId != null &&
            !documentTypeId.equals(doc.getDocumentType().getId())) {
            LOG.debug("Document not found. Access code {}, type {}.", accessCode, documentTypeId);
            throw new DocumentNotFoundException("Document Access not found");
        }

        return doc;
    }
}