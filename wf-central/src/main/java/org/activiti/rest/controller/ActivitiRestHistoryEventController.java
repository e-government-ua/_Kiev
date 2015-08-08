package org.activiti.rest.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.wf.dp.dniprorada.constant.HistoryEventMessage;
import org.wf.dp.dniprorada.constant.HistoryEventType;
import org.wf.dp.dniprorada.dao.DocumentDao;
import org.wf.dp.dniprorada.dao.HistoryEventDao;
import org.wf.dp.dniprorada.dao.HistoryEvent_ServiceDao;
import org.wf.dp.dniprorada.model.Document;
import org.wf.dp.dniprorada.model.EntityNotFoundException;
import org.wf.dp.dniprorada.model.HistoryEvent;
import org.wf.dp.dniprorada.model.HistoryEvent_Service;
import org.wf.dp.dniprorada.util.AlgorithmLuna;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/services")
public class ActivitiRestHistoryEventController {

	private static final Logger log = Logger
			.getLogger(ActivitiRestHistoryEventController.class);

	@Autowired
	private HistoryEvent_ServiceDao historyEventServiceDao;

	@Autowired
	private HistoryEventDao historyEventDao;

	@Autowired
	private DocumentDao documentDao;
	
	/**
	 * check the correctness of nID_Protected (by algorithm Luna) and return the
	 * object of HistoryEvent_Service
	 * 
	 * @param nID_Protected
	 *            -- string ID of event
	 * @return the object (if nID is correct and record exists) otherwise return
	 *         403. CRC Error (wrong nID_Protected) or 403. "Record not found"
	 */
	@RequestMapping(value = "/getHistoryEvent_Service", method = RequestMethod.GET)
	public @ResponseBody
	ResponseEntity<String> getHistoryEvent_Service(
			@RequestParam(value = "nID_Protected") Long nID_Protected)
			throws ActivitiRestException {

		HistoryEvent_Service event_service = null;
		ResponseEntity<String> result;
		try {
			event_service = historyEventServiceDao
					.getHistoryEvent_ServiceByID_Protected(nID_Protected);
			result = JsonRestUtils.toJsonResponse(event_service);
		} catch (EntityNotFoundException e) {
			ActivitiRestException newErr = new ActivitiRestException(
					"BUSINESS_ERR", "Record not found", e);
			newErr.setHttpStatus(HttpStatus.FORBIDDEN);
			throw newErr;
		} catch (IllegalArgumentException e) {
			ActivitiRestException newErr = new ActivitiRestException(
					"BUSINESS_ERR", e.getMessage(), e);
			newErr.setHttpStatus(HttpStatus.FORBIDDEN);
			throw newErr;
		} catch (RuntimeException e) {
			throw new ActivitiRestException(e.getMessage(), e);
		}
		return result;
	}


	/**
	 * add the object of HistoryEvent_Service to db
	 * 
	 * @param nID_Task
	 *            -- ID of Task
	 * @param sStatus
	 *            -- string of status
	 * @param nID_Subject
	 *            -- SubjectID (optional)
	 * @param sID_Status
	 *            -- string-status (optional, for algorithm Luna)
	 * @param response
	 * @return the created object
	 */
	@RequestMapping(value = "/addHistoryEvent_Service", method = RequestMethod.GET)
	public @ResponseBody
	ResponseEntity<String> addHistoryEvent_Service(
			@RequestParam(value = "nID_Proccess") Long nID_Proccess,
			@RequestParam(value = "nID_Subject") Long nID_Subject,
			@RequestParam(value = "sID_Status") String sID_Status,
			@RequestParam(value = "sProcessInstanceName") String sProcessInstanceName,
			HttpServletResponse response) {
		
		Map<String, String> mParamMessage = new HashMap<String, String>();
	    mParamMessage.put(HistoryEventMessage.SERVICE_NAME, sProcessInstanceName);
		setHistoryEvent(HistoryEventType.GET_SERVICE, nID_Subject, mParamMessage);

		return JsonRestUtils.toJsonResponse(historyEventServiceDao
				.addHistoryEvent_Service(nID_Proccess, sID_Status, nID_Subject,
						sID_Status));
	}

	/**
	 * check the correctness of nID_Protected (by algorithm Luna) and update the
	 * object of HistoryEvent_Service in db
	 * 
	 * @param nID_Protected
	 *            -- nID_Protected of event_service
	 * @param sStatus
	 *            -- string of status
	 * @param sID_Status
	 *            -- string-status (optional)
	 * @param response
	 *            return 200ok or "Record not found"
	 */
	@RequestMapping(value = "/updateHistoryEvent_Service", method = RequestMethod.GET)
	public @ResponseBody
	HistoryEvent_Service updateHistoryEvent_Service(
			@RequestParam(value = "nID_Proccess", required = false) Long nID_Proccess,
			@RequestParam(value = "sID_Status") String sID_Status,
			@RequestParam(value = "nID_Subject") Long nID_Subject,
			//@RequestParam(value = "nID_Task") Long nID_Task,
			//@RequestParam(value = "sHistoryEventType") HistoryEventType sHistoryEventType,
			HttpServletResponse response) {

		Map<String, String> mParamMessage = new HashMap<String, String>();
	    mParamMessage.put(HistoryEventMessage.SERVICE_STATE, sID_Status);
	    mParamMessage.put(HistoryEventMessage.TASK_NUMBER, String.valueOf(AlgorithmLuna.getProtectedNumber(nID_Proccess)));
		setHistoryEvent(HistoryEventType.ACTIVITY_STATUS_NEW, nID_Subject, mParamMessage);
		
		log.info("updateHistoryEvent_Service!!!!!!!!!!!!!");
		HistoryEvent_Service historyEvent_Service = historyEventServiceDao
				.getHistoryEvent_ServiceBynID_Task(nID_Proccess);
		log.info("updateHistoryEvent_Service!!!!!!!!!!!!!");
		if (historyEvent_Service != null) {
			boolean isChanged = false;
			if (!historyEvent_Service.getsStatus().equals(sID_Status)) {
				historyEvent_Service.setsStatus(sID_Status);
				isChanged = true;
			}
			if (sID_Status != null
					&& !sID_Status.equals(historyEvent_Service.getsID_Status())) {
				historyEvent_Service.setsID_Status(sID_Status);
				isChanged = true;
			}
			if (isChanged) {
				log.info("updateHistoryEvent_Service!!!!!!!!!!!!!");
				historyEventServiceDao
						.updateHistoryEvent_Service(historyEvent_Service);
				log.info("updateHistoryEvent_Service!!!!!!!!!!!!!");
			}
		}
		return historyEvent_Service;
	}

	
	//################ HistoryEvent services ###################

	@RequestMapping(value = "/setHistoryEvent", method = RequestMethod.POST)
	public @ResponseBody
	Long setHistoryEvent(
			@RequestParam(value = "nID_Subject", required = false) long nID_Subject,
			@RequestParam(value = "nID_HistoryEventType", required = false) Long nID_HistoryEventType,
			@RequestParam(value = "sEventName", required = false) String sEventName_Custom,
			@RequestParam(value = "sMessage") String sMessage,

			HttpServletRequest request, HttpServletResponse httpResponse)
			throws IOException {

		return historyEventDao.setHistoryEvent(nID_Subject,
				nID_HistoryEventType, sEventName_Custom, sMessage);

	}
	
	@RequestMapping(value = "/getHistoryEvent", method = RequestMethod.GET)
	public @ResponseBody
	HistoryEvent getHistoryEvent(@RequestParam(value = "nID") Long id) {
		return historyEventDao.getHistoryEvent(id);
	}

	@RequestMapping(value = "/getHistoryEvents", method = RequestMethod.GET)
	public @ResponseBody
	List<HistoryEvent> getHistoryEvents(
			@RequestParam(value = "nID_Subject") long nID_Subject) {
		return historyEventDao.getHistoryEvents(nID_Subject);
	}
	
	private void setHistoryEvent(HistoryEventType eventType,
			Long nID_Subject, Map<String, String> mParamMessage) {
		try {
			String eventMessage = HistoryEventMessage.createJournalMessage(
					eventType, mParamMessage);
			historyEventDao.setHistoryEvent(nID_Subject, eventType.getnID(),
					eventMessage, eventMessage);
		} catch (IOException e) {
			log.error("error during creating HistoryEvent", e);
		}
	}
	 //################      ###################

}
