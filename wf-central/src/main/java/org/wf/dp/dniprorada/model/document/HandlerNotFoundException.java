package org.wf.dp.dniprorada.model.document;

/**
 * @author dgroup
 * @since 28.06.15
 */
public class HandlerNotFoundException extends RuntimeException {

    public HandlerNotFoundException(String message) {
        super(message);
    }

    public HandlerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public HandlerNotFoundException(Throwable cause) {
        super(cause);
    }

    public HandlerNotFoundException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
