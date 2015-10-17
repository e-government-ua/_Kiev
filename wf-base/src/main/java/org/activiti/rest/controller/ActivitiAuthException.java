package org.activiti.rest.controller;

import org.springframework.http.HttpStatus;

/**
 * Created by diver on 4/20/15.
 */
public class ActivitiAuthException extends ActivitiRestException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ActivitiAuthException(Error error, String message) {
        super(error.getErrorCode(), message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }

    public enum Error {

        LOGIN_ERROR("LI_0001"),
        LOGOUT_ERROR("LO_0001");

        private String errorCode;

        private Error(String errorCode) {
            this.errorCode = errorCode;
        }

        public String getErrorCode() {
            return errorCode;
        }
    }
}
