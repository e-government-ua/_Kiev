package org.wf.dp.dniprorada.dao;

import org.springframework.stereotype.Service;
import org.wf.dp.dniprorada.dao.place.PlaceHierarchyRecord;
import org.wf.dp.dniprorada.dao.place.PlaceHierarchyTree;

/**
 * @author dgroup
 * @since  20.07.2015
 */
@Service
public interface PlaceDao {
    PlaceHierarchyTree getTreeDown(PlaceHierarchyRecord rootRecord);
    PlaceHierarchyTree getTreeUp  (Long placeId, String uaId, Boolean tree);
}