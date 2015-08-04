package org.activiti.rest.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wf.dp.dniprorada.base.util.JsonRestUtils;
import org.wf.dp.dniprorada.dao.HistoryEvent_ServiceDao;
import org.wf.dp.dniprorada.model.EntityNotFoundException;
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
    ResponseEntity<HistoryEvent_Service> addHistoryEvent_Service(
            @RequestParam(value = "nID_Task") Long nID_Task,
            @RequestParam(value = "sStatus") String sStatus,
            @RequestParam(value = "nID_Subject", required = false) Long nID_Subject,
            @RequestParam(value = "sID_Status", required = false) String sID_Status,
            HttpServletResponse response) {

        return JsonRestUtils.toJsonResponse(
                historyEventServiceDao.addHistoryEvent_Service(
                        nID_Task, sStatus, nID_Subject, sID_Status));
    }


    /**
     * check the correctness of nID_Protected (by algorithm Luna)
     * and return the object of HistoryEvent_Service
     * @param nID_Protected -- string ID of event
     * @return the object (if nID is correct and record exists)
     * otherwise return 403. CRC Error (wrong nID_Protected) or 403. "Record not found"
     */
    @RequestMapping(value = "/getHistoryEvent_Service", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity<HistoryEvent_Service> getHistoryEvent_Service(
            @RequestParam(value = "nID_Protected") Long nID_Protected) throws ActivitiRestException {

        HistoryEvent_Service event_service = null;
        ResponseEntity<HistoryEvent_Service> result;
        try {
            event_service = historyEventServiceDao.getHistoryEvent_ServiceByID_Protected(nID_Protected);
            result = JsonRestUtils.toJsonResponse(event_service);
        } catch (EntityNotFoundException e) {
            ActivitiRestException newErr = new ActivitiRestException("BUSINESS_ERR","Record not found", e);
            newErr.setHttpStatus(HttpStatus.FORBIDDEN);
            throw newErr;
        } catch (IllegalArgumentException e) {
            ActivitiRestException newErr =new ActivitiRestException("BUSINESS_ERR", e.getMessage(), e);
            newErr.setHttpStatus(HttpStatus.FORBIDDEN);
            throw newErr;
        } catch (RuntimeException e) {
            throw new ActivitiRestException(e.getMessage(), e);
//            HttpHeaders headers = new HttpHeaders();
//            MediaType mediaType = new MediaType("application", "json", Charset.forName("UTF-8"));
//            headers.setContentType(mediaType);
//            headers.set("Reason", e.getMessage());
//            result = new ResponseEntity<>(headers, HttpStatus.valueOf(403));
        }
        return result;
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
    		@RequestParam(value = "nID", required = false) Long nID_Task,
            //@RequestParam(value = "nID_Protected", required = false) Long nID_Protected, 
            @RequestParam(value = "sStatus") String sStatus,
            @RequestParam(value = "sID_Status", required = false) String sID_Status,
            HttpServletResponse response) {

        try {
            historyEventServiceDao.updateHistoryEvent_Service(nID_Task,
            		//nID_Protected
            		sStatus, sID_Status);
        } catch (RuntimeException e) {
            response.setStatus(403);
            response.setHeader("Reason", e.getMessage());
        }
    }




}
