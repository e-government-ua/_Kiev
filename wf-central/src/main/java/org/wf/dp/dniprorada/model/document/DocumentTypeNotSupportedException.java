package org.wf.dp.dniprorada.model.document;

/**
 * @author dgroup
 * @since 28.06.15
 */
public class DocumentTypeNotSupportedException extends RuntimeException {

    public DocumentTypeNotSupportedException(String message) {
        super(message);
    }

    public DocumentTypeNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocumentTypeNotSupportedException(Throwable cause) {
        super(cause);
    }

    public DocumentTypeNotSupportedException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
