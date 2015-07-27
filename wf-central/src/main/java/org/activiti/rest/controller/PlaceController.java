package org.activiti.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.wf.dp.dniprorada.base.dao.BaseEntityDao;
import org.wf.dp.dniprorada.base.util.JsonRestUtils;
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

    @Autowired
    private PlaceDao placeDao;

    @Autowired
    private BaseEntityDao baseEntityDao;

    /**
     * @param deep != 0, if deep = 0 then i wasn't specified
     */
    @RequestMapping(value   = "/getPlacesTree",
            method  = RequestMethod.GET,
            headers = {"Accept=application/json"})
    public  @ResponseBody Tree<Place> getPlacesTree(
            @RequestParam(value = "nID",            required = false) Long      placeId,
            @RequestParam(value = "sID_UA",         required = false) String    uaId,
            @RequestParam(value = "nID_PlaceType",  required = false) Long      typeId,
            @RequestParam(value = "bArea",          required = false) Boolean   area,
            @RequestParam(value = "bRoot",          required = false) Boolean   root,
            @RequestParam(value = "nDeep",          required = false) Integer   deep
    ) {
        return placeDao.findPlacesTreeBy(placeId, uaId, typeId, area, root, deep);
    }


    @RequestMapping(value   = "/getPlace",
            method  = RequestMethod.GET,
            headers = {"Accept=application/json"})
    public  @ResponseBody List<Place> getPlace(
            @RequestParam(value = "nID",    required = false) Long      placeId,
            @RequestParam(value = "sID_UA", required = false) String    uaId,
            @RequestParam(value = "bTree ", required = false) Boolean   tree
    ) {
        return placeDao.findBy(placeId, uaId, tree);
    }

    @RequestMapping(value   = "/getPlaceType",
            method  = RequestMethod.GET,
            headers = { "Accept=application/json" })
    public  @ResponseBody PlaceType getPlaceType(
            @RequestParam(value = "nID") Long placeTypeId
    ) {
        return baseEntityDao.getById(PlaceType.class, placeTypeId);
    }

    @RequestMapping(value   = "/setPlaceType",
            method  = RequestMethod.POST,
            headers = { "Accept=application/json" })
    public  @ResponseBody void setPlaceType( @RequestBody String entity ) {
        baseEntityDao.saveOrUpdate( JsonRestUtils.readObject(entity, PlaceType.class) );
    }

}