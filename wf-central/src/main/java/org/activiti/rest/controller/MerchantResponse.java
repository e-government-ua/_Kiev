package org.activiti.rest.controller;

import org.springframework.http.HttpStatus;

public class MerchantResponse {
    private String message;
    private HttpStatus httpStatus;

    public MerchantResponse(String message, HttpStatus httpStatus) {
        this.setHttpStatus(httpStatus);
        this.setMessage(message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
