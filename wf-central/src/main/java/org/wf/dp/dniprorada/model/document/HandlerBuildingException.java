package org.wf.dp.dniprorada.model.document;

/**
 * @author dgroup
 * @since 28.06.15
 */
public class HandlerBuildingException extends RuntimeException {

    public HandlerBuildingException(String message) {
        super(message);
    }

    public HandlerBuildingException(String message, Throwable cause) {
        super(message, cause);
    }

    public HandlerBuildingException(Throwable cause) {
        super(cause);
    }

    public HandlerBuildingException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
