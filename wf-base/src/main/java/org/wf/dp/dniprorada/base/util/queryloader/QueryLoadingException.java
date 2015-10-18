package org.wf.dp.dniprorada.base.util.queryloader;

/**
 * @author dgroup
 * @since 02.08.2015
 */
public class QueryLoadingException extends RuntimeException {

    public QueryLoadingException(String message) {
        super(message);
    }

    public QueryLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public QueryLoadingException(Throwable cause) {
        super(cause);
    }

    public QueryLoadingException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
