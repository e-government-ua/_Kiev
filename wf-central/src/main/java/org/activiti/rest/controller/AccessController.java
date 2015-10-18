package org.activiti.rest.controller;

import org.activity.rest.security.AuthenticationTokenSelector;
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
    public
    @ResponseBody
    String getAccessKey(
            //@RequestParam(value = "sAccessLogin") String sAccessLogin,
            @RequestParam(value = AuthenticationTokenSelector.ACCESS_LOGIN) String sAccessLogin,
            //@RequestParam(value = "sAccessContract") String sAccessContract,
            @RequestParam(value = AuthenticationTokenSelector.ACCESS_CONTRACT) String sAccessContract,
            @RequestParam(value = "sData") String sData
    ) throws ActivitiRestException {

        //public static final String ACCESS_CONTRACT_REQUEST = "Request";
        //public static final String ACCESS_CONTRACT_REQUEST_AND_LOGIN = "RequestAndLogin";
        if (AuthenticationTokenSelector.ACCESS_CONTRACT_REQUEST_AND_LOGIN.equals(sAccessContract)) {
            //if(sData!=null && !"".equals(sData.trim()) && !sData.trim().endsWith("?")){
            if (sData != null && !"".equals(sData.trim())) {
                sData = sData + (sData.contains("?") ? "&" : "?") + AuthenticationTokenSelector.ACCESS_LOGIN + "="
                        + sAccessLogin;
            }
            //}else if(ACCESS_CONTRACT_REQUEST.equals(sAccessContract)){
            //}else{
        }
        return accessDataDao.setAccessData(sData);
    }
}
