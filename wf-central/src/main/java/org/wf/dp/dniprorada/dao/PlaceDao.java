package org.wf.dp.dniprorada.dao;

import org.wf.dp.dniprorada.base.dao.EntityDao;
import org.wf.dp.dniprorada.dao.place.PlaceHierarchyRecord;
import org.wf.dp.dniprorada.dao.place.PlaceHierarchyTree;
import org.wf.dp.dniprorada.model.Place;

/**
 * @author dgroup
 * @since  20.07.2015
 */
public interface PlaceDao extends EntityDao<Place> {
    PlaceHierarchyTree getTreeDown(PlaceHierarchyRecord rootRecord);
    PlaceHierarchyTree getTreeUp  (Long placeId, String uaId, Boolean tree);
}