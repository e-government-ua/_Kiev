package org.wf.dp.dniprorada.dao;

import org.wf.dp.dniprorada.base.dao.EntityDao;
import org.wf.dp.dniprorada.dao.place.PlaceHibernateHierarchyRecord;
import org.wf.dp.dniprorada.dao.place.PlaceHierarchy;
import org.wf.dp.dniprorada.model.Place;

/**
 * @author dgroup
 * @since 20.07.2015
 */
public interface PlaceDao extends EntityDao<Place> {
    PlaceHierarchy getTreeDown(PlaceHibernateHierarchyRecord rootRecord);

    PlaceHierarchy getTreeUp(Long placeId, String uaId, Boolean tree);

    Place getRoot(Place place);
}