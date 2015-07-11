package org.activity.rest.security;

import org.springframework.security.authentication.*;

/**
 * @author tasman
 */
public class BadAccessKeyCredentialsException extends AccountStatusException {
    public BadAccessKeyCredentialsException(String msg) {
        super(msg);
    }
}
