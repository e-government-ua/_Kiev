package org.wf.dp.dniprorada.model.document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.wf.dp.dniprorada.dao.DocumentAccessDao;
import org.wf.dp.dniprorada.dao.DocumentDao;
import org.wf.dp.dniprorada.model.ByteArrayMultipartFileOld;
import org.wf.dp.dniprorada.model.Document;
import org.wf.dp.dniprorada.model.DocumentAccess;

import java.io.ByteArrayInputStream;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNumeric;

/**
 * @author dgroup
 * @since  28.06.15
 */
@Component
public class DocumentAccessHandler_IGov extends AbstractDocumentAccessHandler {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentAccessHandler_IGov.class);

    @Autowired
    private DocumentAccessDao documentAccessDao;

    @Autowired
    private DocumentDao documentDao;


    @Override
    public DocumentAccess getAccess() {
        DocumentAccess access = documentAccessDao.getDocumentAccess(accessCode);

        if (access == null)
            throw new DocumentNotFoundException("Document Access not found");

        if (isBlank(access.getsCodeType()))
            return access;

        if (isBlank(password) || !isNumeric(password)){
            if ("SMS".equalsIgnoreCase(access.getsCodeType())){
                handleSMS();
            } else {
                throw new DocumentAccessException("Document Access password wrong (no SMS:"+access.getsCodeType()+")");
            }
        }
        
        int currPass = Integer.valueOf(access.getAnswer());
        int userPass = Integer.valueOf(password);
        if ("SMS".equalsIgnoreCase(access.getsCodeType()) && userPass == currPass)
            return access;
        else
            throw new DocumentAccessException("Document Access password wrong");
    }

    private void handleSMS() {
        try {
            LOG.info("Got {}", accessCode);
            String sPhone=documentAccessDao.sSentDocumentAccessOTP_Phone(accessCode);
            if(sPhone ==null){
                throw new DocumentAccessException("Document Access password need - cant send SMS");
            } else{
                throw new DocumentAccessException("Document Access password need - sent SMS ("+sPhone+")");
            }
        } catch (Exception ex) { // TODO WTF: why sSentDocumentAccessOTP_Phone throw a general exception?
            throw new DocumentAccessException("Document Access password need - UNKNOWN:"+ex.getMessage(), ex);
        }
    }


    public Document getDocument() {
        Document doc = documentDao.getDocument( getAccess().getID_Document() );

        if (documentTypeId != null &&
            !documentTypeId.equals(doc.getDocumentType().getId())) {
            LOG.debug("Document not found. Access code {}, type {}.", accessCode, documentTypeId);
            throw new DocumentNotFoundException("Document Access not found");
        }

        MultipartFile documentBody = getFileBody(doc);
        doc.setFileBody(documentBody);

        return doc;
    }

    private MultipartFile getFileBody(Document document) {
        byte[] content = documentDao.getDocumentContent(document.getContentKey());

        String documentName = document.getFile();
        String contentType = document.getContentType();

        String[] parts = contentType.split("/");
        String fileExtension = parts.length < 2 ? "" : parts[1];

        return new ByteArrayMultipartFileOld(new ByteArrayInputStream(content),
                documentName, documentName, contentType + ";" + fileExtension);
    }
}