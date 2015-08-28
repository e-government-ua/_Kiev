package org.wf.dp.dniprorada.model.document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Base64;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.wf.dp.dniprorada.dao.DocumentAccessDao;
import org.wf.dp.dniprorada.dao.DocumentDao;
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
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Dmytro Tsapko on 8/22/2015.
 */
public class DocumentAccessHandler_PB implements DocumentAccessHandler {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentAccessHandler_PB.class);
    private String accessCode;
    private String password;
    private Long documentTypeId;

    @Autowired
    GeneralConfig generalConfig;

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
        return access;
    }


    public Document getDocument() {
        Document doc = null;
        String sessionId = null;
        String uriDoc = generalConfig.sURL_DocumentKvitancii();
        String callBackKey = "&callbackUrl=";
        String callBackValue = generalConfig.sURL_DocumentKvitanciiCallback();
        String keyID = this.accessCode;
        String finalUri = uriDoc + keyID + callBackKey + callBackValue;
        if (this.documentTypeId != 0) {
            LOG.error("DocumentTypeId = " + this.documentTypeId);
            throw new DocumentTypeNotSupportedException("DocumentTypeId = " + this.documentTypeId);
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
            headers.setAccept(Arrays.asList(new MediaType[]{MediaType.ALL}));
            headers.set("Authorization", "Basic " + authHeaderEncoded);

            ResponseEntity<String> documentEntity = new RestRequest().getEntity(finalUri,
                    null, StandardCharsets.UTF_8, String.class, headers);

            String contentType = documentEntity.getHeaders().getContentType().toString();
            String contentDispositionHeader = documentEntity.getHeaders().get("Content-Disposition").get(0);
            ContentDisposition header = new ContentDisposition(contentDispositionHeader);
            String documentName = header.getParameter("name");
            if (documentName == null || "".equals(documentName)) {
                documentName = header.getParameter("filename");
            }

            doc = new Document();
            doc.setFile(documentEntity.getBody());
            doc.setName(documentName);
            doc.setContentType(contentType);

        } catch (Exception e) {
            LOG.error("Can't get document: ", e);
            throw new DocumentNotFoundException("Can't get document: ", e);
        }

        return doc;
    }

    private String getSessionId() {
        String sessionId = null;
        String login = generalConfig.getSID_login();
        String password = generalConfig.getSID_password();
        String uriSid = generalConfig.sURL_GenerationSID() + "?lang=UA";


        String xml = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?>\n" +
                "<session><user auth='EXCL' login='" + login + "' password='" + password + "'/></session>";

        if (generalConfig.bTest()) {
            SSLCertificateValidation.disable();
        }

        String xmlResponse = new RestRequest().post(uriSid, xml, MediaType.TEXT_XML,
                StandardCharsets.UTF_8, String.class, null);
        sessionId = getSidFromXml(xmlResponse);

        return sessionId;
    }


    private String getSidFromXml(String xmlDocument) {

        String result = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        org.w3c.dom.Document doc = null;
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

}



