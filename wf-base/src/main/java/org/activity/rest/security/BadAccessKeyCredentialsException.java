package org.activity.rest.security;

import org.springframework.security.authentication.AccountStatusException;

/**
 * @author tasman edited by Olga Turenko & Belyavtsev Vladimir (BW)
 */
public class BadAccessKeyCredentialsException extends AccountStatusException {
    public BadAccessKeyCredentialsException(String sMessage) {
        super(sMessage);
    }
}
