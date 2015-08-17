package org.activiti.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wf.dp.dniprorada.base.dao.BaseEntityDao;
import org.wf.dp.dniprorada.dao.PlaceDao;
import org.wf.dp.dniprorada.dao.place.PlaceHierarchyRecord;
import org.wf.dp.dniprorada.dao.place.PlaceHierarchyTree;
import org.wf.dp.dniprorada.model.PlaceType;

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


    @RequestMapping(value   = "/getPlacesTree",
                    method  = RequestMethod.GET, headers = { JSON_TYPE })
    public  @ResponseBody PlaceHierarchyTree getPlacesTree (
            @RequestParam(value = "nID",            required = false)   long    placeId,
            @RequestParam(value = "sID_UA",         required = false)   String  uaId,
            @RequestParam(value = "nID_PlaceType",  required = false)   Long    typeId,
            @RequestParam(value = "bArea",          required = false)   Boolean area, // для фильтра
            @RequestParam(value = "bRoot",          required = false)   Boolean root, // для фильтра
            @RequestParam(value = "nDeep",          defaultValue = "1") Long    deep
    ) {
        PlaceHierarchyRecord rootRecord = new PlaceHierarchyRecord();
        rootRecord.setPlaceId(placeId);
        rootRecord.setTypeId(typeId);
        rootRecord.setUaID(uaId);
        rootRecord.setArea(area);
        rootRecord.setRoot(root);
        rootRecord.setDeep(deep);
        return placeDao.getTreeDown(rootRecord);
    }


    @RequestMapping(value   = "/getPlace",
                    method  = RequestMethod.GET, headers = { JSON_TYPE })
    public  @ResponseBody PlaceHierarchyTree getPlace(
            @RequestParam(value = "nID",    required = false)       Long    placeId,
            @RequestParam(value = "sID_UA", required = false)       String  uaId,
            @RequestParam(value = "bTree ", defaultValue = "false") Boolean tree
    ) {
        return placeDao.getTreeUp(placeId, uaId, tree);
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
            @RequestParam(value = "nID",    required = false)       Long    placeTypeId,
            @RequestParam(value = "sName",  required = false)       String  name,
            @RequestParam(value = "nOrder", required = false)       Long    order,
            @RequestParam(value = "bArea",  defaultValue = "false") Boolean area,
            @RequestParam(value = "bRoot",  defaultValue = "false") Boolean root
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