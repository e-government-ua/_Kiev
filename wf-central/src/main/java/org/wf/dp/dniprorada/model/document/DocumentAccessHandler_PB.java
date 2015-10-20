package org.wf.dp.dniprorada.model.document;

import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Node;
import org.wf.dp.dniprorada.dao.DocumentAccessDao;
import org.wf.dp.dniprorada.dao.DocumentTypeDao;
import org.wf.dp.dniprorada.dao.SubjectDao;
import org.wf.dp.dniprorada.model.ByteArrayMultipartFileOld;
import org.wf.dp.dniprorada.model.Document;
import org.wf.dp.dniprorada.model.DocumentAccess;
import org.wf.dp.dniprorada.util.GeneralConfig;
import org.wf.dp.dniprorada.util.rest.RestRequest;
import org.wf.dp.dniprorada.util.rest.SSLCertificateValidation;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.mail.internet.ContentDisposition;
import javax.mail.internet.ParseException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by Dmytro Tsapko on 8/22/2015.
 */

public class DocumentAccessHandler_PB extends AbstractDocumentAccessHandler {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentAccessHandler_PB.class);

    @Autowired
    GeneralConfig generalConfig;

    @Autowired
    private DocumentAccessDao documentAccessDao;

    @Autowired
    private SubjectDao subjectDao;

    @Autowired
    private DocumentTypeDao documentTypeDao;

    @Override
    public DocumentAccess getAccess() {
        return documentAccessDao.getDocumentAccess(accessCode);
    }

    public Document getDocument() {
        Document doc = new Document();
        String sessionId;
        String keyIdParam;
        String callBackKey = "&callbackUrl=";
        String callBackValue = generalConfig.sURL_DocumentKvitanciiCallback();
        String keyID = this.accessCode;
        Collection<Long> correctDocTypes = Lists.newArrayList(0L, 1L);
        String uriDoc;

        if (this.documentTypeId == null || !correctDocTypes.contains(this.documentTypeId)) {
            LOG.error("DocumentTypeId = " + this.documentTypeId);
            throw new DocumentTypeNotSupportedException(
                    "Incorrect DocumentTypeId. DocumentTypeId = " + this.documentTypeId);
        } else {
            uriDoc = Long.valueOf(0L).equals(this.documentTypeId) ?
                    generalConfig.sURL_DocumentKvitanciiForIgov() : generalConfig.sURL_DocumentKvitanciiForAccounts();

            keyIdParam = Long.valueOf(0L).equals(this.documentTypeId) ? "?keyID=" : "?id=";
        }

        String finalUri = uriDoc + keyIdParam + keyID + callBackKey + callBackValue;

        if (generalConfig.bTest()) {
            SSLCertificateValidation.disable();
        }

        try {
            sessionId = getSessionId();
            String authHeader = "sid:" + sessionId;
            byte[] authHeaderBytes = Base64.encode(authHeader.getBytes(StandardCharsets.UTF_8));
            String authHeaderEncoded = new String(authHeaderBytes);

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.ALL));
            headers.set("Authorization", "Basic " + authHeaderEncoded);

            ResponseEntity<byte[]> documentEntity = new RestRequest().getEntity(finalUri,
                    null, StandardCharsets.UTF_8, byte[].class, headers);

            String contentType = documentEntity.getHeaders().getContentType().toString();
            String contentDispositionHeader = documentEntity.getHeaders().get("Content-Disposition").get(0);
            ContentDisposition header = new ContentDisposition(contentDispositionHeader);
            String documentName = header.getParameter("name");

            if (isBlank(documentName)) {
                documentName = header.getParameter("filename");
            }

            if (this.withContent) {
                doc.setFileBody(getFileFromRespEntity(documentEntity));
            }

            doc.setDocumentType(documentTypeDao.findByIdExpected(0L));
            doc.setSubject(subjectDao.getSubject(this.nID_Subject));
            doc.setFile(documentName);
            doc.setContentType(contentType);
            doc.setDate_Upload(DateTime.now());
            doc.setsID_subject_Upload(null);
            doc.setContentKey(null);
            doc.setoSignData(null);

        } catch (ParseException | ResourceAccessException e) {
            LOG.error("Can't get document: ", e);
            throw new DocumentNotFoundException("Can't get document: ", e);
        }

        return doc;
    }

    private String getSessionId() {
        String sessionId;
        String login = generalConfig.getSID_login();
        String password = generalConfig.getSID_password();
        String uriSid = generalConfig.sURL_GenerationSID() + "?lang=UA";

        String xml = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?>\n" +
                "<session><user auth='EXCL' login='" + login + "' password='" + password + "'/></session>";

        String xmlResponse = new RestRequest().post(uriSid, xml, MediaType.TEXT_XML,
                StandardCharsets.UTF_8, String.class, null);
        sessionId = getSidFromXml(xmlResponse);

        return sessionId;
    }

    private String getSidFromXml(String xmlDocument) {
        //todo simplify parsing
        String result;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        org.w3c.dom.Document doc;
        try {
            builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xmlDocument));
            doc = builder.parse(is);
            Node nodeId = doc.getElementsByTagName("id").item(0);
            result = nodeId.getAttributes().getNamedItem("value").getNodeValue();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new DocumentNotFoundException("Can't parse Session ID.", e);
        }
        return result;

    }

    private MultipartFile getFileFromRespEntity(ResponseEntity<byte[]> documentEntity) throws ParseException {
        String contentType = documentEntity.getHeaders().getContentType().toString();
        String contentDispositionHeader = documentEntity.getHeaders().get("Content-Disposition").get(0);
        ContentDisposition header = new ContentDisposition(contentDispositionHeader);
        String documentName = header.getParameter("name");
        if (isBlank(documentName)) {
            documentName = header.getParameter("filename");
        }
        String[] parts = contentType.split("/");
        String fileExtension = parts.length < 2 ? "" : parts[1];

        return new ByteArrayMultipartFileOld(new ByteArrayInputStream(documentEntity.getBody()),
                documentName, documentName, contentType + ";" + fileExtension);

    }
}


