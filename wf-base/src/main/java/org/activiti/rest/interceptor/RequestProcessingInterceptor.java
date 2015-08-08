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

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
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

    @Autowired
    private HistoryService historyService;

    @Autowired
    HttpRequester httpRequester;

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {

        long startTime = System.currentTimeMillis();
        logger.info("[preHandle] Request URL = " + request.getRequestURL().toString()
                + ":: Start Time = " + System.currentTimeMillis());
        request.setAttribute("startTime", startTime);
        saveHistory(request, response, false);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        logger.info("[afterCompletion] Request URL = " + request.getRequestURL().toString()
                + ":: Time Taken = " + (System.currentTimeMillis() - (Long) request.getAttribute("startTime")));
        response = ((MultiReaderHttpServletResponse) request.getAttribute("responseMultiRead") != null
                ? (MultiReaderHttpServletResponse) request.getAttribute("responseMultiRead") : response);
        saveHistory(request, response, true);
    }

    private void saveHistory(HttpServletRequest request, HttpServletResponse response, boolean saveHistory) throws IOException {
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
        String sRequestBody = buffer.toString();

        logger.info("mParamRequest: " + mParamRequest);

        String sResponseBody = response.toString();

        if (generalConfig.bTest()) {
            if (sResponseBody != null) {
                logger.info("sResponseBody: " + sResponseBody.substring(0, sResponseBody.length() < 100 ? sResponseBody.length() : 99));
            } else {
                logger.info("sResponseBody: null");
            }
            logger.info("sResponseBody: " + sResponseBody);
        } else {
            logger.info("sResponseBody: " + (sResponseBody != null ? sResponseBody.length() : "null"));
        }

        try {
            boolean setTask = sResponseBody != null && request.getRequestURL().toString().indexOf("/form/form-data") > 0
                    && "POST".equalsIgnoreCase(request.getMethod().trim());
            boolean closeTask = sResponseBody == null && request.getRequestURL().toString().indexOf("/form/form-data") > 0
                    && "POST".equalsIgnoreCase(request.getMethod().trim());
            boolean updateTask = request.getRequestURL().toString().indexOf("/runtime/tasks") > 0
                    && "PUT".equalsIgnoreCase(request.getMethod().trim());
            logger.info("sRequestBody: " + sRequestBody);
            if (saveHistory && (setTask || closeTask || updateTask)) {
                logger.info("call service HistoryEvent_Service!!!!!!!!!!!");
                JSONParser parser = new JSONParser();
                JSONObject jsonObjectRequest = null, jsonObjectResponse = null;

                if (sRequestBody != null) {
                    jsonObjectRequest = (JSONObject) parser.parse(sRequestBody);
                }

                if (sResponseBody != null) {
                    jsonObjectResponse = (JSONObject) parser.parse(sResponseBody);
                }

                String sID_Proccess = null, serviceName = null, taskName = null;
                Map<String, String> params = new HashMap<String, String>();
                if (setTask) {
                    sID_Proccess = (String) jsonObjectResponse.get("id");
                    serviceName = "addHistoryEvent_Service";
                    taskName = "Заявка подана";
                    HistoricProcessInstance historicProcessInstance = 
                            historyService.createHistoricProcessInstanceQuery().processInstanceId(sID_Proccess).singleResult();
                    params.put("sProcessInstanceName", historicProcessInstance.getName());
                } else if (closeTask) {
                    //sID_Proccess = (String) jsonObjectRequest.get("id");
                    serviceName = "updateHistoryEvent_Service";
                    String task_ID = (String) jsonObjectRequest.get("taskId");
                    HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(task_ID).singleResult();
                    sID_Proccess = historicTaskInstance.getProcessInstanceId();
                    taskName = "Заявка выполнена";
                    //params.put("nID_Task", task_ID);
                } else if (updateTask) {
                    
                    serviceName = "updateHistoryEvent_Service";
                    sID_Proccess = (String) jsonObjectResponse.get("processInstanceId");
                    taskName = (String) jsonObjectResponse.get("name");
                    //Long nID_Task = Long.parseLong((String) jsonObjectResponse.get("id"));
                    //params.put("nID_Task", String.valueOf(nID_Task));
                } 

                if (serviceName != null && sID_Proccess != null) {
                    String URL = generalConfig.sHostCentral() + "/wf-central/service/services/" + serviceName;
                    params.put("nID_Proccess", sID_Proccess);
                    params.put("nID_Subject", String.valueOf((Long) jsonObjectRequest.get("nID_Subject")));
                    params.put("sID_Status", taskName);
                    logger.info(URL + ": " + params);
                    String soResponse = httpRequester.get(URL, params);
                    logger.info("ok! soJSON = " + soResponse);
                }

            }
        } catch (Exception ex) {
            logger.error("************************Error!!!!", ex);
        }
    }
}
