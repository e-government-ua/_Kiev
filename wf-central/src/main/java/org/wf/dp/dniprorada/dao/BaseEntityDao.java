package org.wf.dp.dniprorada.dao;

import org.wf.dp.dniprorada.model.Category;
import org.wf.dp.dniprorada.model.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * User: goodg_000
 * Date: 09.05.2015
 * Time: 18:25
 */
public interface BaseEntityDao {

   <T extends Entity> T getById(Class<T> entityType, Serializable id);

   <T extends Entity> List<T> getAll(Class<T> entityClass);

   <T extends Entity> void saveOrUpdate(T entity);

   <T extends Entity> void saveOrUpdateAll(T[] entities);
}
