package org.wf.dp.dniprorada.base.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.io.Serializable;

/**
 * User: goodg_000
 * Date: 16.06.2015
 * Time: 23:44
 */
public class SerializableResponseEntity<T extends Serializable> implements Serializable {

    private T body;
    private MultiValueMap<String, String> headers;
    private HttpStatus statusCode;

    public SerializableResponseEntity() {
    }

    public SerializableResponseEntity(ResponseEntity<T> responseEntity) {
        this.body = responseEntity.getBody();
        this.headers = responseEntity.getHeaders();
        this.statusCode = responseEntity.getStatusCode();
    }

    public ResponseEntity<T> toResponseEntity() {
        return new ResponseEntity<T>(body, headers, statusCode);
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public MultiValueMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(MultiValueMap<String, String> headers) {
        this.headers = headers;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }
}
