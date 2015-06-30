package org.wf.dp.dniprorada.model.document;

/**
 * @author dgroup
 * @since 29.06.15
 */
public class DocumentOrganNotFoundException extends RuntimeException {

    public DocumentOrganNotFoundException(String message) {
        super(message);
    }

    public DocumentOrganNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocumentOrganNotFoundException(Throwable cause) {
        super(cause);
    }

    public DocumentOrganNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
