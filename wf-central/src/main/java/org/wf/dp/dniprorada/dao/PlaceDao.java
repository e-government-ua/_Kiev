package org.wf.dp.dniprorada.dao;

import org.springframework.stereotype.Service;
import org.wf.dp.dniprorada.dao.place.PlaceHierarchyRecord;
import org.wf.dp.dniprorada.dao.place.PlaceHierarchyTree;
import org.wf.dp.dniprorada.model.Place;

import java.util.List;

/**
 * @author dgroup
 * @since  20.07.2015
 */
@Service
public interface PlaceDao {
    List<Place> findBy(Long placeId, String uaId, Boolean tree);

    PlaceHierarchyTree getTreeDown(PlaceHierarchyRecord rootRecord);
    PlaceHierarchyTree getTreeUp  (Long placeId, String uaId, Boolean tree);
}