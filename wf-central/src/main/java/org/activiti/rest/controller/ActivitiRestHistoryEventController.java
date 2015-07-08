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

    /**
     * add the object of HistoryEvent_Service to db
     * @param nID_Task -- ID of Task
     * @param sStatus -- string of status
     * @param nID_Subject -- SubjectID (optional)
     * @param sID_Status -- string-status (optional, for algorithm Luna)
     * @param response
     * @return the created object
     */
    @RequestMapping(value = "/addHistoryEvent_Service", method = RequestMethod.GET)
    public
    @ResponseBody
    HistoryEvent_Service addHistoryEvent_Service(
            @RequestParam(value = "nID_Task") Long nID_Task,
            @RequestParam(value = "sStatus") String sStatus,
            @RequestParam(value = "nID_Subject", required = false) Long nID_Subject,
            @RequestParam(value = "sID_Status", required = false) String sID_Status,
            HttpServletResponse response) {

        return historyEventServiceDao.addHistoryEvent_Service(nID_Task, sStatus, nID_Subject, sID_Status);
    }

    /**
     * check the correctness of nID_Protected (by algorithm Luna)
     * and return the object of HistoryEvent_Service
     * @param nID_Protected -- string ID of event
     * @return the object (if nID is correct and record exists)
     * otherwise return 403. CRC Error (wrong nID) or 403. "Record not found"
     */
    @RequestMapping(value = "/getHistoryEvent_Service", method = RequestMethod.GET)
    public
    @ResponseBody
    HistoryEvent_Service getHistoryEvent_Service(
            @RequestParam(value = "nID_Protected") Long nID_Protected,
            HttpServletResponse response) {

        HistoryEvent_Service event_service = null;
        try {
            event_service = historyEventServiceDao.getHistoryEvent_ServiceByID_Protected(nID_Protected);
        } catch (RuntimeException e) {
            response.setStatus(403);
            response.setHeader("Reason", e.getMessage());
        }
        return event_service;
    }

    /**
     * check the correctness of nID_Protected (by algorithm Luna)
     * and update the object of HistoryEvent_Service in db
     * @param nID_Protected -- nID_Protected of event_service
     * @param sStatus -- string of status
     * @param sID_Status -- string-status (optional)
     * @param response
     * return 200ok or "Record not found"
     */
    @RequestMapping(value = "/updateHistoryEvent_Service", method = RequestMethod.GET)
    public
    @ResponseBody
    void updateHistoryEvent_Service(
            @RequestParam(value = "nID") Long nID_Protected,
            @RequestParam(value = "sStatus") String sStatus,
            @RequestParam(value = "sID_Status", required = false) String sID_Status,
            HttpServletResponse response) {

        try {
            historyEventServiceDao.updateHistoryEvent_Service(nID_Protected, sStatus, sID_Status);
        } catch (RuntimeException e) {
            response.setStatus(403);
            response.setHeader("Reason", e.getMessage());
        }
    }




}
