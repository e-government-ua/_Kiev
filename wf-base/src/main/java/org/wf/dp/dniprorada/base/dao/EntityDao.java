package org.wf.dp.dniprorada.base.dao;

import org.wf.dp.dniprorada.base.model.Entity;

import java.util.List;

/**
 * User: goodg_000
 * Date: 05.05.2015
 * Time: 22:31
 */
public interface EntityDao<T extends Entity> {

   List<T> getAll();

   void saveOrUpdate(T entity);

   T getById(Integer identification);
}
