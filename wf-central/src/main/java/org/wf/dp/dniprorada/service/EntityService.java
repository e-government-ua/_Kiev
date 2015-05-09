package org.wf.dp.dniprorada.service;

import net.sf.brunneng.jom.MergingContext;
import net.sf.brunneng.jom.diff.ChangeType;
import net.sf.brunneng.jom.diff.apply.IBeanFinder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.wf.dp.dniprorada.dao.BaseEntityDao;
import org.wf.dp.dniprorada.model.Entity;

import java.io.Serializable;

/**
 * User: goodg_000
 * Date: 09.05.2015
 * Time: 19:36
 */
public class EntityService implements InitializingBean {

   private BaseEntityDao baseEntityDao;

   private MergingContext mergingContext;

   @Required
   public BaseEntityDao getBaseEntityDao() {
      return baseEntityDao;
   }

   public void setBaseEntityDao(BaseEntityDao baseEntityDao) {
      this.baseEntityDao = baseEntityDao;
   }

   @Override

   public void afterPropertiesSet() throws Exception {
      mergingContext = new MergingContext();
      mergingContext.forBeansOfClass(Entity.class).forMatchedProperties().all().skipPropertyChanges(ChangeType.DELETE);
      mergingContext.addBeanFinder(new IBeanFinder() {
         @Override
         public Class getFoundBeanSuperclass() {
            return Entity.class;
         }

         @Override
         public Object find(Class targetBeanClass, Object identifier) {
            return baseEntityDao.getById(targetBeanClass, (Serializable) identifier);
         }
      });
   }

   public <T extends Entity> T update(T sourceEntity) {
      T originalEntity = (T)baseEntityDao.getById(sourceEntity.getClass(), sourceEntity.getId());

      mergingContext.map(sourceEntity, originalEntity);
      baseEntityDao.saveOrUpdate(originalEntity);
      return originalEntity;
   }

}
