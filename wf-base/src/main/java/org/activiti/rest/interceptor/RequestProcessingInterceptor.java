/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.activiti.rest.interceptor; 

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.rest.controller.adapter.MultiReaderHttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.wf.dp.dniprorada.rest.HttpRequester;
import org.wf.dp.dniprorada.util.GeneralConfig;

/**
 *
 * @author olya
 */
public class RequestProcessingInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory
            .getLogger(RequestProcessingInterceptor.class);
    
    @Autowired
    GeneralConfig generalConfig;

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {

        long startTime = System.currentTimeMillis();
        logger.info("[preHandle] Request URL = " + request.getRequestURL().toString()
                + ":: Start Time = " + System.currentTimeMillis());
        request.setAttribute("startTime", startTime);
        testReadFromRequest(request, response, false);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        //logger.info("Request URL::" + request.getRequestURL().toString()
        //        + " Sent to Handler :: Current Time=" + System.currentTimeMillis());
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        logger.info("[afterCompletion] Request URL = " + request.getRequestURL().toString()
                + ":: Time Taken = " + (System.currentTimeMillis() - (Long) request.getAttribute("startTime")));
        response = ((MultiReaderHttpServletResponse)request.getAttribute("responseMultiRead") != null ? 
        		(MultiReaderHttpServletResponse)request.getAttribute("responseMultiRead") : response);
        testReadFromRequest(request, response, true);
    }

    private void testReadFromRequest(HttpServletRequest request, HttpServletResponse response, boolean saveHistory) throws IOException {
        Map mParamRequest = new HashMap();
        Enumeration paramsName = request.getParameterNames();
        while (paramsName.hasMoreElements()) {
            String sKey = (String) paramsName.nextElement();
            mParamRequest.put(sKey, request.getParameter(sKey));
        }

        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        if (reader != null) {
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            //mParamRequest.put("requestBody", buffer.toString()); 
            //TODO temp
        }

        logger.info("mParamRequest: " + mParamRequest);
        String responseBody  = response.toString();
        logger.info("sResponseBody: " + responseBody);
        
        try{
        	logger.info("checkSaveHistoryEvent!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        	if(saveHistory && (request.getRequestURL().toString().indexOf("/form/form-data") > 0
        			|| request.getRequestURL().toString().indexOf("/start-process/") > 0
        			|| (request.getRequestURL().toString().indexOf("runtime/process-instances") > 0 
        					&& "POST".equalsIgnoreCase(request.getMethod().trim())))){
        		JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(responseBody);
                String sID_Task = (String) jsonObject.get("id");
                if (sID_Task != null) {
                	Map<String, String> params = new HashMap<String, String>();
            		params.put("nID_Task", sID_Task);
            		params.put("sStatus", "Заявка подана");
            		//params.put("nID_Subject", mParamRequest.get("nID_Subject"));
            		//params.put("sID_Status", "");
            		logger.info("addHistoryEvent_Service: " + generalConfig.sHostCentral() + "/wf-central/services/addHistoryEvent_Service " + params);
            		String soResponse = HttpRequester.get(//"https://poligon.igov.org.ua" 
            				generalConfig.sHostCentral() + "/wf-central/services/addHistoryEvent_Service", params);
            		logger.info("soJSON = " + soResponse);
                }
        	}
        }
        catch(Exception ex){
        	logger.error("************************Error!!!!", ex);
        }
    }
}
