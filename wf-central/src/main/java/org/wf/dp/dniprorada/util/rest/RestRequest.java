package org.wf.dp.dniprorada.util.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Created by Dmytro Tsapko on 8/23/2015.
 */
public class RestRequest {
    private static final Logger LOG = LoggerFactory.getLogger(RestRequest.class);

    public <T> T post(String url, String requestBody, MediaType contentType, Charset charset,
            Class<T> clazz, HttpHeaders httpHeaders) throws RestClientException {

        if ("".equals(url) || url == null || clazz == null) {
            LOG.error("url: " + url + " clazz: " + clazz);
            throw new IllegalArgumentException("url: " + url + " clazz: " + clazz);
        }
        T xmlResponse = null;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new ResponseErrHandler());

        httpHeaders = httpHeaders == null ? new HttpHeaders() : httpHeaders;
        if (contentType != null) {
            httpHeaders.setContentType(contentType);
        }
        if (charset != null) {
            httpHeaders.setAcceptCharset(Arrays.asList(new Charset[] { charset }));
        }
        HttpEntity httpEntity = new HttpEntity(requestBody, httpHeaders);
        LOG.debug("Sending POST to rest resource: " + url + " HttpEntity: " + httpEntity);
        xmlResponse = restTemplate.postForObject(url, httpEntity, clazz);

        return xmlResponse;

    }

    public <T> T get(String url, MediaType contentType, Charset charset,
            Class<T> clazz, HttpHeaders httpHeaders) throws RestClientException {

        if ("".equals(url) || url == null || clazz == null) {//todo add convertors
            LOG.error("url: " + url + " clazz: " + clazz);
            throw new IllegalArgumentException("url: " + url + " clazz: " + clazz);
        }

        T response = null;
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory() {
            @Override
            protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
                super.prepareConnection(connection, httpMethod);
                connection.setInstanceFollowRedirects(false);
                connection.setRequestMethod(httpMethod);
            }
        };
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(factory);
        restTemplate.setErrorHandler(new ResponseErrHandler());

        httpHeaders = httpHeaders == null ? new HttpHeaders() : httpHeaders;

        if (contentType != null) {
            httpHeaders.setContentType(contentType);
        }
        if (charset != null) {
            httpHeaders.setAcceptCharset(Arrays.asList(new Charset[] { charset }));
        }
        HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);

        LOG.info("Sending GET to rest resource: " + url + " HttpEntity: " + httpEntity);
        ResponseEntity<T> entity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, clazz);
        LOG.info("we got: " + entity);
        if (entity.getStatusCode().is3xxRedirection()) {
            LOG.info("Sending GET agter redirect to rest resource: " + url + " HttpEntity: " + httpEntity);
            entity = restTemplate.exchange(entity.getHeaders().getLocation().toString(),
                    HttpMethod.GET, httpEntity, clazz);
        }

        return (T) entity.getBody();

    }

    public <T> ResponseEntity<T> getEntity(String url, MediaType contentType, Charset charset,
            Class<T> clazz, HttpHeaders httpHeaders) throws RestClientException {
        //todo just write httpconverter to transfotrm hhtp respomsee to class Document
        if ("".equals(url) || url == null || clazz == null) {
            LOG.error("url: " + url + " clazz: " + clazz);
            throw new IllegalArgumentException("url: " + url + " clazz: " + clazz);
        }

        T response = null;
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory() {
            @Override
            protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
                super.prepareConnection(connection, httpMethod);
                connection.setInstanceFollowRedirects(false);
                connection.setRequestMethod(httpMethod);
            }
        };
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(factory);
        restTemplate.setErrorHandler(new ResponseErrHandler());

        httpHeaders = httpHeaders == null ? new HttpHeaders() : httpHeaders;

        if (contentType != null) {
            httpHeaders.setContentType(contentType);
        }
        if (charset != null) {
            httpHeaders.setAcceptCharset(Arrays.asList(new Charset[] { charset }));
        }
        HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);

        LOG.info("Sending GET to rest resource: " + url + " HttpEntity: " + httpEntity.getHeaders());
        ResponseEntity<T> entity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, clazz);
        LOG.info("we got: " + entity);
        if (entity.getStatusCode().is3xxRedirection()) {
            LOG.info("Sending GET agter redirect to rest resource: " + url + " HttpEntity: " + httpEntity.getHeaders());
            entity = restTemplate.exchange(entity.getHeaders().getLocation().toString(),
                    HttpMethod.GET, httpEntity, clazz);
        }

        return entity;

    }

}
