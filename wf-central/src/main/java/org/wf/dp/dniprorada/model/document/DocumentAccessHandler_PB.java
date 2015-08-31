package org.wf.dp.dniprorada.model.document;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Node;
import org.wf.dp.dniprorada.dao.DocumentAccessDao;
import org.wf.dp.dniprorada.dao.DocumentDao;
import org.wf.dp.dniprorada.dao.SubjectDao;
import org.wf.dp.dniprorada.model.*;
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
import java.util.Collections;

import static org.apache.commons.lang3.StringUtils.isBlank;


/**
 * Created by Dmytro Tsapko on 8/22/2015.
 */
@SuppressWarnings("FieldCanBeLocal")
public class DocumentAccessHandler_PB implements DocumentAccessHandler {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentAccessHandler_PB.class);
    private String accessCode;
    private String password;
    private Long documentTypeId;
    private Boolean withContent;
    private Long nID_Subject;


    @Autowired
    GeneralConfig generalConfig;

    @Autowired
    private DocumentAccessDao documentAccessDao;

    @Autowired
    private SubjectDao subjectDao;

    public DocumentAccessHandler setAccessCode(String sCode_DocumentAccess) {
        this.accessCode = sCode_DocumentAccess;
        return this;
    }

    public DocumentAccessHandler setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public DocumentAccessHandler setWithContent(Boolean bWithContent) {
        this.withContent = bWithContent;
        return this;
    }

    @Override
    public DocumentAccessHandler setIdSubject(Long nID_Subject) {
        this.nID_Subject = nID_Subject;
        return this;
    }

    public DocumentAccessHandler setDocumentType(Long docTypeID) {
        this.documentTypeId = docTypeID;
        return this;
    }


    @Override
    public DocumentAccess getAccess() {
        return documentAccessDao.getDocumentAccess(accessCode);
    }


    public Document getDocument() {
        Document doc = new Document();
        String sessionId;
        String uriDoc = generalConfig.sURL_DocumentKvitancii();
        String keyIdParam = "?keyID=";
        String callBackKey = "&callbackUrl=";
        String callBackValue = generalConfig.sURL_DocumentKvitanciiCallback();
        String keyID = this.accessCode;
        String finalUri = uriDoc + keyIdParam + keyID + callBackKey + callBackValue;
        if (this.documentTypeId != 0) {
            LOG.error("DocumentTypeId = " + this.documentTypeId);
            throw new DocumentTypeNotSupportedException("Incorrect DocumentTypeId. DocumentTypeId = " + this.documentTypeId);
        }

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

            ResponseEntity<String> documentEntity = new RestRequest().getEntity(finalUri,
                    null, StandardCharsets.UTF_8, String.class, headers);

            String contentType = documentEntity.getHeaders().getContentType().toString();
            String contentDispositionHeader = documentEntity.getHeaders().get("Content-Disposition").get(0);
            ContentDisposition header = new ContentDisposition(contentDispositionHeader);
            String documentName = header.getParameter("name");

            if (isBlank(documentName)) {
                documentName = header.getParameter("filename");
            }

            if (this.withContent) {
                doc.fileBody = getFileFromRespEntity(documentEntity);
            }

            DocumentType docType = new DocumentType();
            docType.setId(this.documentTypeId);
            Subject oSubject = subjectDao.getSubject(this.nID_Subject);
            doc.setSubject(oSubject);
            doc.setName(documentName);
            doc.setContentType(contentType);
            doc.setDate_Upload(DateTime.now());
            doc.setsID_subject_Upload(null);
            doc.setContentKey(null);
            doc.setoSignData(null);

        } catch (ParseException e) {
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

    private MultipartFile getFileFromRespEntity(ResponseEntity<String> documentEntity) throws ParseException {
        String contentType = documentEntity.getHeaders().getContentType().toString();
        String contentDispositionHeader = documentEntity.getHeaders().get("Content-Disposition").get(0);
        ContentDisposition header = new ContentDisposition(contentDispositionHeader);
        String documentName = header.getParameter("name");
        if (isBlank(documentName)) {
            documentName = header.getParameter("filename");
        }
        String[] parts = contentType.split("/");
        String fileExtension = parts.length < 2 ? "" : parts[1];

        return new ByteArrayMultipartFileOld(new ByteArrayInputStream(documentEntity.getBody().getBytes()),
                documentName, documentName, contentType + ";" + fileExtension);

    }
}


