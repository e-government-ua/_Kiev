package org.wf.dp.dniprorada.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Required;
import org.wf.dp.dniprorada.model.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * User: goodg_000
 * Date: 09.05.2015
 * Time: 18:26
 */
public class BaseEntityDaoImpl implements BaseEntityDao {

   private SessionFactory sessionFactory;

   protected Session getSession() {
      return sessionFactory.getCurrentSession();
   }

   @Required
   public SessionFactory getSessionFactory() {
      return sessionFactory;
   }

   public void setSessionFactory(SessionFactory sessionFactory) {
      this.sessionFactory = sessionFactory;
   }

   public <T extends Entity> T getById(Class<T> entityType, Serializable id) {
      return (T)getSession().get(entityType, id);
   }

   @Override
   public <T extends Entity> List<T> getAll(Class<T> entityType) {
      return DetachedCriteria.forClass(entityType).getExecutableCriteria(getSession()).list();
   }

   @Override
   public <T extends Entity> void saveOrUpdate(T entity) {
      getSession().saveOrUpdate(entity);
   }

   @Override
   public <T extends Entity> void saveOrUpdateAll(T[] entities) {
      for (T e : entities) {
         saveOrUpdate(e);
      }
   }
}
