package org.activiti.rest.interceptor.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.Map;

/**
 * User: goodg_000
 * Date: 25.08.2015
 * Time: 21:01
 */
public final class JsonRequestDataResolver {

    public static Map<String, String> getProperties(JSONObject requestJson) throws ParseException {

        Map<String, String> res = new HashMap<>();

        JSONArray jsonArray = (JSONArray) requestJson.get("properties");
        for (int i = 0; i < jsonArray.size(); ++i) {
            JSONObject property = (JSONObject) jsonArray.get(i);
            res.put((String) property.get("id"), (String) property.get("value"));

        }

        return res;
    }

    public static String getEmail(JSONObject requestJson) throws ParseException {
        return getProperties(requestJson).get("email");
    }
}
