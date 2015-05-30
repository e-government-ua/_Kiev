package org.wf.dp.dniprorada.dao;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Required;
import org.wf.dp.dniprorada.model.Entity;
import org.wf.dp.dniprorada.model.Service;

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
      return (T)getSession().get(entityClass, id);
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
