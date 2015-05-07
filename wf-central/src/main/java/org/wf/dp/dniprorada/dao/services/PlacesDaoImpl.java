package org.wf.dp.dniprorada.dao.services;

import java.util.List;

import org.wf.dp.dniprorada.dao.AbstractEntityDao;
import org.wf.dp.dniprorada.model.Region;

public class PlacesDaoImpl extends AbstractEntityDao<Region> implements PlacesDao{

	protected PlacesDaoImpl() {
		super(Region.class);
	}
}
