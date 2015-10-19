package org.wf.dp.dniprorada.base.util.queryloader;

/**
 * @author dgroup
 * @since 02.08.2015
 */
public class MissingResourceException extends RuntimeException {

    public MissingResourceException(String message) {
        super(message);
    }

    public MissingResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingResourceException(Throwable cause) {
        super(cause);
    }

    public MissingResourceException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}