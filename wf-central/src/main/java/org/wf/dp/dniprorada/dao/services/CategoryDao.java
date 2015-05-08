package org.wf.dp.dniprorada.dao.services;

import org.wf.dp.dniprorada.dao.EntityDao;
import org.wf.dp.dniprorada.model.Category;
import org.wf.dp.dniprorada.model.Service;

public interface CategoryDao extends EntityDao<Category>{

   void saveOrUpdateAll(Category[] categories);
}
