package org.wf.dp.dniprorada.dao;

import org.springframework.stereotype.Service;
import org.wf.dp.dniprorada.model.Place;
import org.wf.dp.dniprorada.model.PlaceTree;
import org.wf.dp.dniprorada.model.PlaceType;
import org.wf.dp.dniprorada.util.Tree;

import java.util.List;

/**
 * @author dgroup
 * @since  20.07.2015
 */
@Service
public interface PlaceDao {
    List<Place> findBy(Long placeId, String uaId, Boolean tree);
    Tree<Place> findPlacesTreeBy(Long placeId, String uaId, Long typeId, Boolean area, Boolean root, Integer deep);
}
