/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wf.dp.dniprorada.exchange;

import org.activity.rest.security.AuthenticationTokenSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wf.dp.dniprorada.base.dao.AccessDataDao;
import org.wf.dp.dniprorada.rest.HttpRequester;
import org.wf.dp.dniprorada.util.GeneralConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ольга
 */
@Component()
public class AccessCover {

    @Autowired
    HttpRequester httpRequester;

    @Autowired
    GeneralConfig generalConfig;

    @Autowired
    private AccessDataDao accessDataDao;

    public String getAccessKeyCentral(String sData) throws Exception {
        String sURI = "/wf/service/services/getAccessKey";
        Map<String, String> mParam = new HashMap<String, String>();

        //mParam.put("sAccessLogin", "activiti-master");
        mParam.put(AuthenticationTokenSelector.ACCESS_LOGIN, generalConfig.sAuthLogin());//"activiti-master"
        //param.put("sAccessContract", "Request");
        mParam.put(AuthenticationTokenSelector.ACCESS_CONTRACT,
                AuthenticationTokenSelector.ACCESS_CONTRACT_REQUEST_AND_LOGIN);//"RequestAndLogin"
        mParam.put("sData", sData);
        //String soJSON_Merchant = 
        return httpRequester.get(generalConfig.sHostCentral() + sURI, mParam);
        //JSONParser parser = new JSONParser();
        //JSONObject jsonObject = (JSONObject) parser.parse(soJSON_Merchant);
        //return (String) jsonObject.get("string");
    }

    public String getAccessKey(String sData) throws Exception {
        return accessDataDao.setAccessData(sData);
        
        /*String sURI = "/wf/service/services/getAccessKey";
        Map<String, String> mParam = new HashMap<String, String>();
        
        mParam.put(AuthenticationTokenSelector.ACCESS_LOGIN, generalConfig.sAuthLogin());//"activiti-master"
        mParam.put(AuthenticationTokenSelector.ACCESS_CONTRACT, AuthenticationTokenSelector.ACCESS_CONTRACT_REQUEST_AND_LOGIN);//"RequestAndLogin"
        mParam.put("sData", sData);
        return httpRequester.get(generalConfig.sHost() + sURI, mParam);
        */
    }

}
