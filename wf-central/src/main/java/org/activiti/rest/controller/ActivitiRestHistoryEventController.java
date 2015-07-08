package org.activiti.rest.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wf.dp.dniprorada.dao.HistoryEvent_ServiceDao;
import org.wf.dp.dniprorada.model.HistoryEvent_Service;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/services")
public class ActivitiRestHistoryEventController {

    private static final Logger log = Logger.getLogger(ActivitiRestHistoryEventController.class);

    @Autowired
    private HistoryEvent_ServiceDao historyEventServiceDao;

   /* 3.1) getHistoryEvent_Service - получает объект п.2, параметры: sID - ИД-строка (в урл-е) Если не найдена запись возвращать объект ошибки со значением "Record not found"
            3.2) addHistoryEvent_Service - добавляет объект п.2, параметры: nID_Task - ИД-номер задачи (long) nID_Subject - ИД-номер (long) //опциональный
            sStatus - строка-статус (long) sID_Status - строка-статус (long) //опциональный для авто-генерации значения поля sID:
    использовать алгоритм Луна (10 символов)
    закладывать nID в основу - как логин
    закладывать случайно-сгенерированный код в основу - как логин
    3.3) updateHistoryEvent_Service - добавляет/обновляет объект п.2,
    параметры:
    nID - ИД-номер
    sStatus - строка-статус
    sID_Status - строка-статус (long) //опциональный
    Если не найдена запись возвращать объект ошибки со значением "Record not found"
*/
    /**
     * update the object of HistoryEvent_Service to db
     * @param nID -- ID of event
     * @param sStatus -- string of status
     * @param sID_Status -- string-status (optional)
     * @param response
     * @return nothing or "Record not found"
     */
    @RequestMapping(value = "/updateHistoryEvent_Service", method = RequestMethod.GET)
    public
    @ResponseBody
    void updateHistoryEvent_Service(
            @RequestParam(value = "nID") Long nID,
            @RequestParam(value = "sStatus") String sStatus,
            @RequestParam(value = "sID_Status", required = false) String sID_Status,
            HttpServletResponse response) {

        try {
            historyEventServiceDao.updateHistoryEvent_Service(nID, sStatus, sID_Status);
        } catch (Exception e) {
            response.setStatus(403);
            response.setHeader("Reason", "Record not found");
        }
    }

    /**
     * add the object of HistoryEvent_Service to db
     * @param nID_Task -- ID of Task
     * @param sStatus -- string of status
     * @param nID_Subject -- SubjectID (optional)
     * @param sID_Status -- string-status (optional for algorithm Luna
     * @param response
     * @return
     */
    @RequestMapping(value = "/addHistoryEvent_Service", method = RequestMethod.GET)
    public
    @ResponseBody
    String addHistoryEvent_Service(
            @RequestParam(value = "nID_Task") Long nID_Task,
            @RequestParam(value = "sStatus") String sStatus,
            @RequestParam(value = "nID_Subject", required = false) Long nID_Subject,
            @RequestParam(value = "sID_Status", required = false) String sID_Status,
            HttpServletResponse response) {

        String sID = "";
        try {
            sID = historyEventServiceDao.addHistoryEvent_Service(nID_Task, sStatus, nID_Subject, sID_Status);
        } catch (Exception e) {
            response.setStatus(403);
            response.setHeader("Reason", "Error during adding new record\n" + e.getMessage());
        }
        return sID;
    }

    /**
     * return the object of HistoryEvent_Service
     * @param sID -- string ID of event
     * @return the object or "Record not found"
     */
    @RequestMapping(value = "/getHistoryEvent_Service", method = RequestMethod.GET)
    public
    @ResponseBody
    HistoryEvent_Service getHistoryEvent_Service(
            @RequestParam(value = "sID") String sID,
            HttpServletResponse response) {

        HistoryEvent_Service event_service = null;
        try {
            event_service = historyEventServiceDao.getHistoryEvent_ServiceBysID(sID);
            if (event_service == null) {
                response.setStatus(403);
                response.setHeader("Reason", "Record not found");
            }
        } catch (Exception e) {
            response.setStatus(403);
            response.setHeader("Reason", "Error during finding record\n" + e.getMessage());
        }
        return event_service;
    }
}
