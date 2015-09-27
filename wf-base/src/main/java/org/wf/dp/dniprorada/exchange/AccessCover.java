/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wf.dp.dniprorada.exchange;

import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wf.dp.dniprorada.rest.HttpRequester;
import org.wf.dp.dniprorada.util.GeneralConfig;

/**
 *
 * @author Ольга
 */
@Component()
public class AccessCover {

    @Autowired
    HttpRequester httpRequester;
    
    @Autowired
    GeneralConfig generalConfig;

    public String getAccessKey(String sData) throws Exception {
        String URI = "/wf/service/services/getAccessKey";
        Map<String, String> param = new HashMap<String, String>();
        param.put("sLogin", "activiti-master");
        param.put("sAccessContract", "Request");
        param.put("sData", sData);
        //String soJSON_Merchant = 
                return httpRequester.get(generalConfig.sHostCentral() + URI, param);
        //JSONParser parser = new JSONParser();
        //JSONObject jsonObject = (JSONObject) parser.parse(soJSON_Merchant);
        //return (String) jsonObject.get("string");
    }
}
