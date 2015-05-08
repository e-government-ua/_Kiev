package org.wf.dp.dniprorada.dao.services;

import org.wf.dp.dniprorada.dao.AbstractEntityDao;
import org.wf.dp.dniprorada.model.Category;
import org.wf.dp.dniprorada.model.Service;

public class ServiceDaoImpl extends AbstractEntityDao<Service> implements
		ServiceDao {

	protected ServiceDaoImpl() {
		super(Service.class);
	}

}
