package org.wf.dp.dniprorada.base.dao;

import com.google.common.base.Optional;
import org.wf.dp.dniprorada.base.model.Entity;

import java.util.List;

/**
 * Base CRUD DAO
 *
 * @param <T> entities
 */
public interface EntityDao<T extends Entity> {

    /**
     * Find entity by id
     *
     * @param id
     * @return absent if none found
     */
    Optional<T> findById(Long id);

    /**
     * Find by id or throw exception if none found
     *
     * @param id
     * @return entity
     * @throws EntityNotFoundException if not found
     */
    T findByIdExpected(Long id);

    /**
     * Returns first found entities.
     *
     * @param attribute
     * @param value
     * @return Optional
     * @see EntityDao#findAllBy(String, Object)
     */
    Optional<T> findBy(String attribute, Object value);

    /**
     * Returns first found entity or throw exception is none found.
     *
     * @param attribute
     * @param value
     * @return Optional
     * @throws EntityNotFoundException
     * @see EntityDao#findAllBy(String, Object)
     */
    T findByExpected(String attribute, Object value);

    /**
     * Search for entities which have attributes with a given value
     * Note! nested properties are supported, e.g. foo.bar.pro
     *
     * @param field e.g. "nID", "foo.bar.pro"
     * @param value e.g. "nid123"
     * @return found entities or empty List if nothing found
     */
    List<T> findAllBy(String field, Object value);

    /**
     * Returns all entities from DB.
     *
     * @return empty List if table is empty.
     */
    List<T> findAll();

    /**
     * Find all entities by ids.
     *
     * @param ids
     * @return
     */
    List<T> findAll(List<Long> ids);

    /**
     * @param entity
     * @return updated or created entity
     * @see org.hibernate.Session#saveOrUpdate(Object)
     */
    T saveOrUpdate(T entity);

    /**
     * @param entities
     * @return
     * @see EntityDao#saveOrUpdate(Entity)
     */
    List<T> saveOrUpdate(List<T> entities);

    /**
     * @param id
     * @see EntityDao#delete(Entity)
     */
    void delete(Long id);

    /**
     * @param field
     * @param value
     * @return number of deleted entities
     * @see EntityDao#delete(Entity)
     * @see EntityDao#findBy(String, Object)
     */
    int deleteBy(String field, Object value);

    /**
     * @param entity
     * @throws EntityNotFoundException if entity does not exist
     */
    void delete(T entity);

    /**
     * Delete all given entities
     *
     * @param entities
     * @return not deleted entities
     */
    List<T> delete(List<T> entities);

    /**
     * Delete all entities from DB.
     */
    void deleteAll();

    /**
     * Checks if entity with given id exists
     *
     * @param id
     * @return true - if exists
     */
    boolean exists(Long id);

    /**
     * Returns class of entity
     *
     * @return
     */
    Class<T> getEntityClass();
}
