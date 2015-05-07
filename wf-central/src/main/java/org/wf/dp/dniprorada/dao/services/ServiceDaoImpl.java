package org.wf.dp.dniprorada.dao.services;

import org.wf.dp.dniprorada.dao.AbstractEntityDao;
import org.wf.dp.dniprorada.model.Category;

public class ServiceDaoImpl extends AbstractEntityDao<Category> implements
		ServiceDao {

	protected ServiceDaoImpl() {
		super(Category.class);
	}

}
