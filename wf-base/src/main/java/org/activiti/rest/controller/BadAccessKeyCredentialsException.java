package org.activiti.rest.controller;

import org.springframework.security.authentication.*;

/**
 * @author tasman
 */
public class BadAccessKeyCredentialsException extends AccountStatusException {
    public BadAccessKeyCredentialsException(String msg) {
        super(msg);
    }
}
