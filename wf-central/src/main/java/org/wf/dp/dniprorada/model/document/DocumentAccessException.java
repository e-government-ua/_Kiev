package org.wf.dp.dniprorada.model.document;

/**
 * @author dgroup
 * @since 28.06.15
 */
public class DocumentAccessException extends RuntimeException {

    public DocumentAccessException(String message) {
        super(message);
    }

    public DocumentAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocumentAccessException(Throwable cause) {
        super(cause);
    }

    public DocumentAccessException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
