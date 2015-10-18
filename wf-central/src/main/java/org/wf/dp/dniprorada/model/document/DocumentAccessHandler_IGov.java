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
 * @since 28.06.15
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
        LOG.info("[getAccess]accessCode = {} ", accessCode);
        DocumentAccess oDocumentAccess = documentAccessDao.getDocumentAccess(accessCode);

        if (oDocumentAccess == null)
            throw new DocumentNotFoundException("Document Access not found");// (accessCode="+accessCode+")

        if (isBlank(oDocumentAccess.getsCodeType()))
            return oDocumentAccess;

        if (isBlank(password) || !isNumeric(password)) {
            if ("SMS".equalsIgnoreCase(oDocumentAccess.getsCodeType())) {
                handleSMS();
            } else {
                throw new DocumentAccessException(
                        "Document Access password wrong (no SMS:" + oDocumentAccess.getsCodeType() + ")");
            }
        }

        int currPass = Integer.valueOf(oDocumentAccess.getAnswer());
        int userPass = Integer.valueOf(password);
        if ("SMS".equalsIgnoreCase(oDocumentAccess.getsCodeType()) && userPass == currPass)
            return oDocumentAccess;
        else
            throw new DocumentAccessException("Document Access password wrong");
    }

    private void handleSMS() {
        try {
            LOG.info("Got {}", accessCode);
            String sPhone = documentAccessDao.sSentDocumentAccessOTP_Phone(accessCode);
            if (sPhone == null) {
                throw new DocumentAccessException("Document Access password need - cant send SMS");
            } else {
                throw new DocumentAccessException("Document Access password need - sent SMS (" + sPhone + ")");
            }
        } catch (Exception ex) { // TODO WTF: why sSentDocumentAccessOTP_Phone throw a general exception?
            throw new DocumentAccessException("Document Access password need - UNKNOWN:" + ex.getMessage(), ex);
        }
    }

    public Document getDocument() {
        Document doc = documentDao.getDocument(getAccess().getID_Document());

        LOG.info("Document doc.getDocumentType().getId(): {}.", doc.getDocumentType().getId());

        if (documentTypeId != null && !documentTypeId.equals(doc.getDocumentType().getId())) {
            LOG.info("Document not found. Access code {}, type {}.", accessCode, documentTypeId);
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