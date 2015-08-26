package org.wf.dp.dniprorada.base.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.wf.dp.dniprorada.base.model.Entity;

/**
 * Thrown when entity with given id does not exist.
 *
 * @see EntityDao#delete(Entity)
 * @see EntityDao#findByIdExpected(Long)
 */
public class EntityNotFoundException extends EmptyResultDataAccessException {
    public EntityNotFoundException(Long id) {
        this(String.format("Entity with id=%s does not exist", id));
    }

    public EntityNotFoundException(String message) {
        super(message, 1);
    }

    public EntityNotFoundException(String message, Throwable ex) {
        super(message, 1, ex);
    }

    public EntityNotFoundException(Long id, Throwable ex) {
        this(String.format("Entity with id=%s does not exist", id), ex);
    }
}
