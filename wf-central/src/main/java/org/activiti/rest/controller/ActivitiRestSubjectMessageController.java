package org.activiti.rest.controller;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wf.dp.dniprorada.base.dao.EntityDao;
import org.wf.dp.dniprorada.base.dao.EntityNotFoundException;
import org.wf.dp.dniprorada.base.util.JsonRestUtils;
import org.wf.dp.dniprorada.dao.HistoryEvent_ServiceDao;
import org.wf.dp.dniprorada.dao.SubjectMessagesDao;
import org.wf.dp.dniprorada.model.HistoryEvent_Service;
import org.wf.dp.dniprorada.model.SubjectMessage;
import org.wf.dp.dniprorada.model.SubjectMessageType;
import org.wf.dp.dniprorada.util.luna.CRCInvalidException;

import java.util.List;

@Controller
@RequestMapping(value = "/messages")
public class ActivitiRestSubjectMessageController {

    private static final Logger log = LoggerFactory.getLogger(ActivitiRestSubjectMessageController.class);

    @Autowired
    private HistoryEvent_ServiceDao historyEventServiceDao;
    @Autowired
    private SubjectMessagesDao subjectMessagesDao;
    @Autowired
    @Qualifier("subjectMessageTypeDao")
    private EntityDao<SubjectMessageType> subjectMessageTypeDao;

    @RequestMapping(value = "/setMessage", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity setMessage(
            @RequestParam(value = "sHead") String sHead,
            @RequestParam(value = "sBody", required = false) String sBody,
            @RequestParam(value = "nID_Subject", required = false) Long nID_Subject,
            @RequestParam(value = "sMail", required = false) String sMail,
            @RequestParam(value = "sContacts", required = false) String sContacts,
            @RequestParam(value = "sData", required = false) String sData,
            @RequestParam(value = "nID_SubjectMessageType", required = false) Long nID_SubjectMessageType,
            @RequestParam(value = "nID_Protected", required = false) Long nID_Protected,
            @RequestParam(value = "sID_Rate", required = false) String sID_Rate) throws ActivitiRestException {

        SubjectMessage message =
                createSubjectMessage(sHead, sBody, nID_Subject, sMail, sContacts, sData, nID_SubjectMessageType);
        subjectMessagesDao.setMessage(message);
        message = subjectMessagesDao.getMessage(message.getId());
        checkRate(nID_Protected, sID_Rate);
        return JsonRestUtils.toJsonResponse(message);
    }
    
    
    @RequestMapping(value = "/setMessageFeedback", method = RequestMethod.POST)//Feedback
    public
    @ResponseBody
    String setMessageFeedback(
            @RequestParam(value = "sHead") String sHead,
            @RequestParam(value = "sBody", required = false) String sBody,
            @RequestParam(value = "nID_Subject", required = false) Long nID_Subject,
            @RequestParam(value = "sMail", required = false) String sMail,
            @RequestParam(value = "sContacts", required = false) String sContacts,
            @RequestParam(value = "sData", required = false) String sData,
            @RequestParam(value = "nID_SubjectMessageType", required = false) Long nID_SubjectMessageType,
            @RequestParam(value = "nID_Protected", required = false) Long nID_Protected,
            @RequestParam(value = "sID_Rate", required = false) String sID_Rate) throws ActivitiRestException {

        SubjectMessage message =
                createSubjectMessage(sHead, sBody, nID_Subject, sMail, sContacts, sData, nID_SubjectMessageType);
        subjectMessagesDao.setMessage(message);
        message = subjectMessagesDao.getMessage(message.getId());
        checkRate(nID_Protected, sID_Rate);
        //return "Спасибо! Вы успешно отправили отзыв!";
        return "Ok!";
    }

    
    @RequestMapping(value = "/getMessageTest", method = RequestMethod.GET)
    public
    @ResponseBody
    String getMessageTest() {
        return "Test Проверка";
    }
    
    
    
    @RequestMapping(value = "/getMessages", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE, headers = {"Accept=application/json"})
    public
    @ResponseBody
    ResponseEntity getMessages() {

        List<SubjectMessage> messages = subjectMessagesDao.getMessages();
        return JsonRestUtils.toJsonResponse(messages);
    }

    @RequestMapping(value = "/getMessage", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE, headers = {"Accept=application/json"})
    public
    @ResponseBody
    ResponseEntity getMessage(
            @RequestParam(value = "nID") Long nID) {

        SubjectMessage message = subjectMessagesDao.getMessage(nID);
        return JsonRestUtils.toJsonResponse(message);
    }

    private SubjectMessage createSubjectMessage(String sHead, String sBody, Long nID_subject, String sMail,
                                                String sContacts, String sData, Long nID_subjectMessageType) {
        SubjectMessage message = new SubjectMessage();
        message.setHead(sHead);
        message.setBody(sBody == null ? "" : sBody);
        message.setId_subject((nID_subject == null) ? 0 : nID_subject);
        message.setMail((sMail == null) ? "" : sMail);
        message.setContacts((sContacts == null) ? "" : sContacts);
        message.setData((sData == null) ? "" : sData);
        message.setDate(new DateTime());
        if (nID_subjectMessageType != null) {
            SubjectMessageType subjectMessageType = subjectMessageTypeDao.findByIdExpected(nID_subjectMessageType);
            message.setSubjectMessageType(subjectMessageType);
        }
        return message;
    }

    private void checkRate(Long nID_Protected, String sID_Rate) throws ActivitiRestException {
        if (nID_Protected != null && sID_Rate != null) {
            Integer nRate;
            try {
                nRate = new Integer(sID_Rate);
            } catch (NumberFormatException ex) {
                throw new ActivitiRestException(
                        ActivitiExceptionController.BUSINESS_ERROR_CODE,
                        "incorrect param sID_Rate (not a number): " + sID_Rate,
                        HttpStatus.FORBIDDEN);
            }
            if (nRate < 1 || nRate > 5) {
                throw new ActivitiRestException(
                        ActivitiExceptionController.BUSINESS_ERROR_CODE,
                        "incorrect param sID_Rate (not in range[1..5]): " + sID_Rate,
                        HttpStatus.FORBIDDEN);
            }
            try {
                HistoryEvent_Service event_service =
                        historyEventServiceDao.getHistoryEvent_ServiceByID_Protected(nID_Protected);
                log.info("set rate=%s to the task=%s......", nRate, nID_Protected / 10);
                event_service.setnRate(nRate);
                historyEventServiceDao.saveOrUpdate(event_service);
                log.info("set rate=%s to the task=%s...... Success!", nRate, nID_Protected / 10);
            } catch (EntityNotFoundException e) {
                throw new ActivitiRestException(
                        ActivitiExceptionController.BUSINESS_ERROR_CODE,
                        "Record with nID_Protected=" + nID_Protected + " not found!",
                        HttpStatus.FORBIDDEN);
            } catch (CRCInvalidException e) {
                throw new ActivitiRestException(
                        ActivitiExceptionController.BUSINESS_ERROR_CODE,
                        "CRC Error. incorrect param nID_Protected: " + nID_Protected,
                        HttpStatus.FORBIDDEN);
            } catch (RuntimeException e) {
                throw new ActivitiRestException(e.getMessage(), e);
            }
        }
    }
}
