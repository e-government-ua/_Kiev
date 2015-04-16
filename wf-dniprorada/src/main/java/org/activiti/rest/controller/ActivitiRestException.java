package org.activiti.rest.controller;

/**
 * Created by diver on 4/6/15.
 */
public class ActivitiRestException extends Exception {

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

    public String getErrorCode() {
        return errorCode;
    }
}
