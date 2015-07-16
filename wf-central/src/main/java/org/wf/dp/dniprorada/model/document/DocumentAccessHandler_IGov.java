package org.wf.dp.dniprorada.model.document;

import java.util.logging.Level;
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
    private Long documentTypeId;

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

    public DocumentAccessHandler setDocumentType(Long docTypeID) {
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

        if (isBlank(password) || !isNumeric(password)){
            //throw new DocumentAccessException("Document Access wrong password");
            if ("SMS".equalsIgnoreCase(access.getsCodeType())){
                try {
                    LOG.info("[getAccess]accessCode="+accessCode);
                    if(documentAccessDao.bSentDocumentAccessOTP(accessCode)){
                        throw new DocumentAccessException("Document Access need password - sent SMS");
                    }else{
                        throw new DocumentAccessException("Document Access need password - cant send SMS");
                    }
                } catch (DocumentAccessException ex) {
                    throw ex;
                } catch (Exception ex) {
                    LOG.error("[getAccess]", ex);
                    throw new DocumentAccessException("Document Access need password - UNKNOWN:"+ex.getMessage());
                }
            }else{
                throw new DocumentAccessException("Document Access wrong password (no SMS:"+access.getsCodeType()+")");
            }
        }
        
        int currPass = Integer.valueOf(access.getAnswer());
        int userPass = Integer.valueOf(password);
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