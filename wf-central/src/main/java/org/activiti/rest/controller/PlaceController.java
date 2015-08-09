package org.activiti.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wf.dp.dniprorada.base.dao.BaseEntityDao;
import org.wf.dp.dniprorada.dao.PlaceDao;
import org.wf.dp.dniprorada.model.Place;
import org.wf.dp.dniprorada.model.PlaceType;
import org.wf.dp.dniprorada.util.Tree;

import java.util.List;

/**
 * @author dgroup
 * @since  20.07.2015
 */
@Controller
public class PlaceController {
    private static final String JSON_TYPE = "Accept=application/json";

    @Autowired
    private PlaceDao placeDao;

    @Autowired
    private BaseEntityDao baseEntityDao;

    /**
     * @param deep != 0, if deep = 0 then i wasn't specified
     */
    @RequestMapping(value   = "/getPlacesTree",
                    method  = RequestMethod.GET, headers = { JSON_TYPE })
    public  @ResponseBody Tree<Place> getPlacesTree(
            @RequestParam(value = "nID",            required = false) Long      placeId,
            @RequestParam(value = "sID_UA",         required = false) String    uaId,
            @RequestParam(value = "nID_PlaceType",  required = false) Long      typeId,
            @RequestParam(value = "bArea",          required = false) Boolean   area,
            @RequestParam(value = "bRoot",          required = false) Boolean   root,
            @RequestParam(value = "nDeep",          required = false) Integer   deep
    ) {
        return placeDao.getPlaces(placeId, uaId, typeId, area, root, deep);
    }


    @RequestMapping(value   = "/getPlace",
                    method  = RequestMethod.GET, headers = { JSON_TYPE })
    public  @ResponseBody List<Place> getPlace(
            @RequestParam(value = "nID",    required = false) Long      placeId,
            @RequestParam(value = "sID_UA", required = false) String    uaId,
            @RequestParam(value = "bTree ", required = false) Boolean   tree
    ) {
        return placeDao.findBy(placeId, uaId, tree);
    }


    @RequestMapping(value   = "/getPlaceType",
                    method  = RequestMethod.GET, headers = { JSON_TYPE })
    public  @ResponseBody PlaceType getPlaceType(
            @RequestParam(value = "nID") Long placeTypeId
    ) {
        return baseEntityDao.getById(PlaceType.class, placeTypeId);
    }


    @RequestMapping(value   = "/setPlaceType",
                    method  = RequestMethod.POST, headers = { JSON_TYPE })
    public  @ResponseBody void setPlaceType(
            @RequestParam(value = "nID",    required = false)   Long    placeTypeId,
            @RequestParam(value = "sName",  required = false)   String  name,
            @RequestParam(value = "nOrder", required = false)   Long    order,
            @RequestParam(value = "bArea",  required = false)   Boolean area,
            @RequestParam(value = "bRoot",  required = false)   Boolean root
    ) {
        baseEntityDao.saveOrUpdate( new PlaceType(placeTypeId, name, order, area, root) );
    }

    @RequestMapping(value   = "/removePlaceType",
                    method  = RequestMethod.POST, headers = { JSON_TYPE })
    public  @ResponseBody void removePlaceType(
            @RequestParam(value = "nID") Long placeTypeId
    ) {
        baseEntityDao.remove(baseEntityDao.getById(PlaceType.class, placeTypeId));
    }
}