package org.wf.dp.dniprorada.service;

import net.sf.brunneng.jom.MergingContext;
import net.sf.brunneng.jom.configuration.bean.MatchedBeanPropertyMetadata;
import net.sf.brunneng.jom.diff.ChangeType;
import net.sf.brunneng.jom.diff.apply.IBeanFinder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wf.dp.dniprorada.base.dao.BaseEntityDao;
import org.wf.dp.dniprorada.base.model.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * User: goodg_000
 * Date: 09.05.2015
 * Time: 19:36
 */
@Service
public class EntityService implements InitializingBean {

    @Autowired
    private BaseEntityDao baseEntityDao;

    private MergingContext updateOnlyMergingContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        initUpdateOnlyMergingContext();
    }

    private void initUpdateOnlyMergingContext() {
        updateOnlyMergingContext = new MergingContext();
        MatchedBeanPropertyMetadata allProperties = updateOnlyMergingContext.forBeansOfClass(
                Entity.class).forMatchedProperties().all();

        allProperties.skipPropertyChanges(ChangeType.DELETE);
        allProperties.skipContainerEntryChanges(ChangeType.DELETE);

        updateOnlyMergingContext.addBeanFinder(new IBeanFinder() {
            @Override
            public Class getFoundBeanSuperclass() {
                return Entity.class;
            }

            @Override
            public Object find(Class targetBeanClass, Object identifier) {
                return baseEntityDao.findById(targetBeanClass, (Long) identifier);
            }
        });
    }

    public <T extends Entity> T update(T sourceEntity) {
        T originalEntity = (T) baseEntityDao.findById(sourceEntity.getClass(), sourceEntity.getId());

        updateOnlyMergingContext.map(sourceEntity, originalEntity);
        return baseEntityDao.saveOrUpdate(originalEntity);
    }

    public <T extends Entity> List<T> update(List<T> entities) {
        List<T> res = new ArrayList<>();
        for (T entity : entities) {
            res.add(update(entity));
        }

        return res;
    }

}
