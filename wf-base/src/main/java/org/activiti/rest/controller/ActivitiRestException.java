package org.activiti.rest.controller;

import org.springframework.http.HttpStatus;

/**
 * Created by diver on 4/6/15.
 */
public class ActivitiRestException extends Exception {

    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    private String errorCode;

    public ActivitiRestException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ActivitiRestException(String errorCode, Throwable throwable) {
        super(throwable);
        this.errorCode = errorCode;
    }

    public ActivitiRestException(String errorCode, String message, Throwable throwable) {
        super(message, throwable);
        this.errorCode = errorCode;
    }

    public ActivitiRestException(String errorCode, String message, HttpStatus httpStatus) {
        this(errorCode, message);
        setHttpStatus(httpStatus);
    }

    public ActivitiRestException(String errorCode, String message, Throwable throwable, HttpStatus httpStatus) {
        this(errorCode, message, throwable);
        setHttpStatus(httpStatus);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
