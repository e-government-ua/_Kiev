package org.wf.dp.dniprorada.util;

import org.activiti.rest.controller.ActivitiRestApiController;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.*;
import java.util.List;

public class BankIDUtils {

    private static final String EMPTY_JSON = "{}";
    private static final Logger log = LoggerFactory.getLogger(ActivitiRestApiController.class);

    public static String checkECP(String clientId, String clientSecret, String redirectUrl, byte[] fileByteArray,
            String fileName) {

        log.info("clientID:" + clientId + " clientSecret:" + clientSecret + " redirectUrl:" + redirectUrl);

        try {
            HttpClientContext context = HttpClientContext.create();

            CloseableHttpClient httpClient = HttpClientBuilder.create().build();

            String code = doAuthorizeCall(clientId, clientSecret, redirectUrl, context, httpClient);

            String accessToken = doGetAccessToken(clientId, clientSecret, redirectUrl, context, httpClient, code);

            if (accessToken != null) {
                String json = submitDocumentForCheckingECP(fileByteArray, fileName, accessToken);

                if (json != null) {
                    JSONParser parser = new JSONParser();
                    JSONObject ecpJson = (JSONObject) parser.parse(json);
                    if (ecpJson.containsKey("state") && ecpJson.get("state").equals("ok")) {
                        // correct ecp
                        return json;
                    }
                }
                log.info("ecp is not found. returning empty json string");
                return EMPTY_JSON;
            }

        } catch (ClientProtocolException e) {
            log.error("Error occured while checking ECP:" + e.getMessage());
        } catch (IOException e) {
            log.error("Error occured while checking ECP:" + e.getMessage());
        } catch (URISyntaxException e) {
            log.error("Error occured while checking ECP:" + e.getMessage());
        } catch (ParseException e) {
            log.error("Error occured while checking ECP:" + e.getMessage());
        }
        return EMPTY_JSON;
    }

    protected static String submitDocumentForCheckingECP(byte[] fileByteArray,
            String fileName, String accessToken) throws MalformedURLException,
            IOException, ProtocolException {
        StringWriter writer;
        String url = "https://bankid.privatbank.ua/ResourceService/checked/signatureData";
        URL urlAddr = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlAddr
                .openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);

        ByteArrayBody fileBody = new ByteArrayBody(fileByteArray, fileName);
        HttpEntity multiPartEntity = MultipartEntityBuilder.create().addPart("file", fileBody).build();

        connection.setRequestProperty("Content-Type", multiPartEntity.getContentType().getValue());
        connection.setRequestProperty("Authorization", "Bearer " + accessToken + ", Id testIgov");
        connection.setRequestProperty("Accept", "application/json");

        OutputStream out = connection.getOutputStream();
        multiPartEntity.writeTo(out);
        out.close();

        writer = new StringWriter();
        IOUtils.copy(connection.getInputStream(), writer,
                connection.getContentEncoding() == null ? "UTF-8" : connection.getContentEncoding());
        String json = writer.toString();
        log.info("JSON with ECP: " + json);
        return json;
    }

    protected static String doGetAccessToken(String clientID, String clientSecret, String redirectUrl,
            HttpClientContext context,
            CloseableHttpClient httpClient, String code)
            throws URISyntaxException, IOException, ClientProtocolException, ParseException {
        String sha1 = DigestUtils.sha1Hex(clientID + clientSecret + code);

        URI uri2 = getGettingAccessToeknURI(clientID, redirectUrl, code, sha1);

        HttpGet getRequestAcceessToken = new HttpGet(uri2);

        CloseableHttpResponse response1 = httpClient.execute(getRequestAcceessToken, context);

        HttpEntity entity = response1.getEntity();

        StringWriter writer = new StringWriter();
        IOUtils.copy(entity.getContent(), writer, "UTF-8");
        String responseString = writer.toString();

        JSONParser parser = new JSONParser();
        JSONObject accessTokenJson = (JSONObject) parser.parse(responseString);
        String accessToken = null;
        if (accessTokenJson.containsKey("access_token")) {
            accessToken = (String) accessTokenJson.get("access_token");
            log.info("Successfully received access token");
        } else if (accessTokenJson.containsKey("error")) {
            log.error("Error occurred while getting access token" + accessTokenJson.get("error"));
        }
        return accessToken;
    }

    protected static String doAuthorizeCall(String clientID, String clientSecret, String redirectUrl,
            HttpClientContext context,
            CloseableHttpClient httpClient) throws URISyntaxException, IOException, ClientProtocolException {

        URI uri = createAuthorizeURI(clientID, clientSecret, redirectUrl);

        HttpGet getAuthorizeRequest = new HttpGet(uri);

        httpClient.execute(getAuthorizeRequest, context);

        List<URI> redirectLocations = context.getRedirectLocations();

        String code = null;
        for (URI uriCurr : redirectLocations) {
            if (uriCurr.getQuery().contains("code=")) {
                code = StringUtils.substringAfter(uriCurr.getQuery(), "code=");
                break;
            }
        }
        log.info("Successfully received code from bank ID");
        return code;
    }

    protected static URI getGettingAccessToeknURI(String clientID,
            String redirectUrl, String code, String sha1)
            throws URISyntaxException {
        URI uri2 = new URIBuilder().setScheme("https")
                .setHost("bankid.privatbank.ua")
                .setPath("/DataAccessService/oauth/token")
                .setParameter("grant_type", "authorization_code")
                .setParameter("client_id", clientID)
                .setParameter("client_secret", sha1)
                .setParameter("code", code)
                .setParameter("edsVerify", "true")
                .setParameter("redirect_uri", redirectUrl).build();
        return uri2;
    }

    protected static URI createAuthorizeURI(String clientID,
            String clientSecret, String redirectUrl) throws URISyntaxException {
        URI uri = new URIBuilder().setScheme("https")
                .setHost("bankid.privatbank.ua")
                .setPath("/DataAccessService/das/authorize")
                .setParameter("response_type", "code")
                .setParameter("client_id", clientID)
                .setParameter("client_secret", clientSecret)
                .setParameter("edsVerify", "true")
                .setParameter("redirect_uri", redirectUrl).build();
        return uri;
    }

}
