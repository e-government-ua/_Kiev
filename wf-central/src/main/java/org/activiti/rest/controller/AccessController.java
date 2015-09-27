package org.activiti.rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wf.dp.dniprorada.base.dao.AccessDataDao;

@Controller
@RequestMapping(value = "/services")
public class AccessController {

    private static final Logger log = LoggerFactory.getLogger(AccessController.class);
    
    @Autowired
    private AccessDataDao accessDataDao;
    
    @RequestMapping(value = "/getAccessKey", method = RequestMethod.GET)
    public @ResponseBody String getAccessKey(
            @RequestParam(value = "sLogin") String sLogin,
            @RequestParam(value = "sAccessContract") String sAccessContract,
            @RequestParam(value = "sData") String sData
            ) throws ActivitiRestException{
        return accessDataDao.setAccessData(sData); 
    }
}
