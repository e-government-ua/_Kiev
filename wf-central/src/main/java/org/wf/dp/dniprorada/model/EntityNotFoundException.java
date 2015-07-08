package org.wf.dp.dniprorada.model;

import org.wf.dp.dniprorada.base.model.Entity;

/**
 * @author dgroup
 * @since 29.06.15
 */
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityNotFoundException(Throwable cause) {
        super(cause);
    }

    public EntityNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public static void assertPresence(Entity entity, String msg){
        if (entity == null)
            throw new EntityNotFoundException(msg);
    }
}