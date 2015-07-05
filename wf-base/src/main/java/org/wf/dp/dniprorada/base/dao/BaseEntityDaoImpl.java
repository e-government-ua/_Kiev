package org.wf.dp.dniprorada.base.dao;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;
import org.wf.dp.dniprorada.base.model.Entity;

import java.beans.PropertyDescriptor;
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

   public <T extends Entity> T getById(Class<T> entityClass, Serializable id) {
      T res = (T)getSession().get(entityClass, id);
      Assert.notNull(res, entityClass.getSimpleName() + " with id=" + id + " is not found!");

      return res;
   }

   private <T extends Entity> boolean hasOrderField(Class<T> entityClass, String fieldName) {
      PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(entityClass);

      boolean res = false;
      for (PropertyDescriptor pd : propertyDescriptors) {
         if (pd.getName().equals(fieldName)) {
            res = true;
            break;
         }
      }

      return res;
   }

   @Override
   public <T extends Entity> List<T> getAll(Class<T> entityClass) {
      DetachedCriteria criteria = DetachedCriteria.forClass(entityClass);

      final String orderFieldName = "order";
      if (hasOrderField(entityClass, orderFieldName)) {
         criteria.addOrder(Order.asc(orderFieldName));
      }

      return criteria.getExecutableCriteria(getSession()).list();
   }

   @Override
   public <T extends Entity> void saveOrUpdate(T entity) {
      getSession().saveOrUpdate(entity);
   }

   @Override
   public <T extends Entity> void remove(T entity) {
      getSession().delete(entity);
   }

   @Override
   public <T extends Entity> void saveOrUpdateAll(T[] entities) {
      for (T e : entities) {
         saveOrUpdate(e);
      }
   }
}
