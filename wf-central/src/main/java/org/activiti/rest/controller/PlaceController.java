package org.activiti.rest.controller;

import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wf.dp.dniprorada.base.dao.EntityDao;
import org.wf.dp.dniprorada.base.dao.EntityNotFoundException;
import org.wf.dp.dniprorada.base.model.Entity;
import org.wf.dp.dniprorada.dao.PlaceDao;
import org.wf.dp.dniprorada.dao.PlaceTypeDao;
import org.wf.dp.dniprorada.dao.place.PlaceHibernateHierarchyRecord;
import org.wf.dp.dniprorada.dao.place.PlaceHierarchy;
import org.wf.dp.dniprorada.model.Place;
import org.wf.dp.dniprorada.model.PlaceType;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author dgroup
 * @since 20.07.2015
 */
@Controller
public class PlaceController {
    private static final String JSON_TYPE = "Accept=application/json";

    @Autowired
    private PlaceDao placeDao;

    @Autowired
    private PlaceTypeDao placeTypeDao;

    private static boolean positive(Long value) {
        return value != null && value > 0;
    }

    @RequestMapping(value = "/getPlacesTree",
            method = RequestMethod.GET, headers = { JSON_TYPE })
    public
    @ResponseBody
    PlaceHierarchy getPlacesTree(
            @RequestParam(value = "nID", required = false) Long placeId,
            @RequestParam(value = "sID_UA", required = false) String uaId,
            @RequestParam(value = "nID_PlaceType", required = false) Long typeId,
            @RequestParam(value = "bArea", required = false) Boolean area,
            @RequestParam(value = "bRoot", required = false) Boolean root,
            @RequestParam(value = "nDeep", defaultValue = "1") Long deep) {

        return placeDao.getTreeDown(new PlaceHibernateHierarchyRecord(placeId, typeId, uaId, area, root, deep));
    }

    @RequestMapping(value = "/getPlace",
            method = RequestMethod.GET, headers = { JSON_TYPE })
    public
    @ResponseBody
    PlaceHierarchy getPlace(
            @RequestParam(value = "nID", required = false) Long placeId,
            @RequestParam(value = "sID_UA", required = false) String uaId,
            @RequestParam(value = "bTree", defaultValue = "false") Boolean tree) {

        return placeDao.getTreeUp(placeId, uaId, tree);
    }

    @RequestMapping(value = "/setPlace",
            method = RequestMethod.POST, headers = { JSON_TYPE })
    public
    @ResponseBody
    void setPlace(
            @RequestParam(value = "nID", required = false) Long placeId,
            @RequestParam(value = "sName", required = false) String name,
            @RequestParam(value = "nID_PlaceType", required = false) Long typeId,
            @RequestParam(value = "sID_UA", required = false) String uaId,
            @RequestParam(value = "sNameOriginal", required = false) String originalName) {

        Place place = new Place(placeId, name, typeId, uaId, originalName);

        if (positive(placeId) && !swap(place, placeDao.findById(placeId), placeDao)) {
            throw new EntityNotFoundException(placeId);

        } else if (!swap(place, placeDao.findBy("sID_UA", uaId), placeDao)) {
            placeDao.saveOrUpdate(place);
        }
    }

    @RequestMapping(value = "/getPlaceEntity",
            method = RequestMethod.GET, headers = { JSON_TYPE })
    public
    @ResponseBody
    Place getPlace(
            @RequestParam(value = "nID", required = false) Long placeId,
            @RequestParam(value = "sID_UA", required = false) String uaId) {

        return positive(placeId)
                ? placeDao.findByIdExpected(placeId)
                : placeDao.findByExpected("sID_UA", uaId);
    }

    @RequestMapping(value = "/removePlace",
            method = RequestMethod.POST, headers = { JSON_TYPE })
    public
    @ResponseBody
    void removePlace(
            @RequestParam(value = "nID", required = false) Long placeId,
            @RequestParam(value = "sID_UA", required = false) String uaId) {

        if (positive(placeId)) {
            placeDao.delete(placeId);

        } else if (isNotBlank(uaId)) {
            Optional<Place> place = placeDao.findBy("sID_UA", uaId);
            if (place.isPresent()) {
                placeDao.delete(place.get());
            }
        }
    }

    @RequestMapping(value = "/getPlaceTypes",
            method = RequestMethod.GET, headers = { JSON_TYPE })
    public
    @ResponseBody
    List<PlaceType> getPlaceTypes(
            @RequestParam(value = "bArea") Boolean area,
            @RequestParam(value = "bRoot") Boolean root) {

        return placeTypeDao.getPlaceTypes(area, root);
    }

    @RequestMapping(value = "/getPlaceType",
            method = RequestMethod.GET, headers = { JSON_TYPE })
    public
    @ResponseBody
    PlaceType getPlaceType(@RequestParam(value = "nID") Long placeTypeId) {

        return placeTypeDao.findByIdExpected(placeTypeId);
    }

    @RequestMapping(value = "/setPlaceType",
            method = RequestMethod.POST, headers = { JSON_TYPE })
    public
    @ResponseBody
    void setPlaceType(
            @RequestParam(value = "nID", required = false) Long placeTypeId,
            @RequestParam(value = "sName", required = false) String name,
            @RequestParam(value = "nOrder", required = false) Long order,
            @RequestParam(value = "bArea", defaultValue = "false") Boolean area,
            @RequestParam(value = "bRoot", defaultValue = "false") Boolean root) {

        PlaceType placeType = new PlaceType(placeTypeId, name, order, area, root);

        if (positive(placeTypeId)) {
            swap(placeType, placeTypeDao.findById(placeTypeId), placeTypeDao);

        } else {
            placeTypeDao.saveOrUpdate(placeType);
        }
    }

    @RequestMapping(value = "/removePlaceType",
            method = RequestMethod.POST, headers = { JSON_TYPE })
    public
    @ResponseBody
    void removePlaceType(@RequestParam(value = "nID") Long placeTypeId) {

        placeTypeDao.delete(placeTypeId);
    }

    /**
     * This method allows to swap two entities by Primary Key (PK).
     *
     * @param entity          - entity with new parameters
     * @param persistedEntity - persisted entity with registered PK in DB
     * @param dao             - type-specific dao implementation
     **/
    @SuppressWarnings("unchecked")
    private <T extends Entity> boolean swap(T entity, Optional<T> persistedEntity, EntityDao dao) {
        if (persistedEntity.isPresent()) {
            entity.setId(persistedEntity.get().getId());
            dao.saveOrUpdate(entity);
            return true;
        }
        return false;
    }
}