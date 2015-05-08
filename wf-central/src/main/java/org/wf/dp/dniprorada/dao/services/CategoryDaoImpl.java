package org.wf.dp.dniprorada.dao.services;

import org.wf.dp.dniprorada.dao.AbstractEntityDao;
import org.wf.dp.dniprorada.model.Category;

public class CategoryDaoImpl extends AbstractEntityDao<Category> implements CategoryDao {

	protected CategoryDaoImpl() {
		super(Category.class);
	}

	@Override
	public void saveOrUpdateAll(Category[] categories) {
		for (Category category : categories) {
			saveOrUpdate(category);
		}
	}
}
