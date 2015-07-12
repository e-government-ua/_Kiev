package org.activiti.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wf.dp.dniprorada.base.util.JsonRestUtils;
import org.wf.dp.dniprorada.dao.SubjectMessagesDao;
import org.wf.dp.dniprorada.model.SubjectMessage;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/messages")
public class ActivitiRestSubjectMessageController {

    @Autowired
    private SubjectMessagesDao subjectMessagesDao;

    @RequestMapping(value = "/setMessage", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity setMessage(@RequestParam(value = "sHead") String sHead,
                          @RequestParam(value = "sBody") String sBody,
                          @RequestParam(value = "nID_Subject", required = false) Long nID_Subject,
                          @RequestParam(value = "sMail", required = false) String sMail,
                          @RequestParam(value = "sContacts", required = false) String sContacts,
                          @RequestParam(value = "sData", required = false) String sData) {
        SubjectMessage message = createSubjectMessage(sHead, sBody, nID_Subject, sMail, sContacts, sData);
        subjectMessagesDao.setMessage(message);
        message = subjectMessagesDao.getMessage(message.getId());
        return JsonRestUtils.toJsonResponse(message);
    }

    @RequestMapping(value = "/getMessages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, headers = {"Accept=application/json"})
    public
    @ResponseBody
    ResponseEntity getMessages() {
        List<SubjectMessage> messages = subjectMessagesDao.getMessages();
        return JsonRestUtils.toJsonResponse(messages);
    }

    @RequestMapping(value = "/getMessage", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, headers = {"Accept=application/json"})
    public
    @ResponseBody
    ResponseEntity getMessage(@RequestParam(value = "nID") Long nID) {
        SubjectMessage message = subjectMessagesDao.getMessage(nID);
        return JsonRestUtils.toJsonResponse(message);
    }

    private SubjectMessage createSubjectMessage(String sHead, String sBody, Long nID_subject, String sMail, String sContacts, String sData) {
        SubjectMessage message = new SubjectMessage();
        message.setHead(sHead);
        message.setBody(sBody);
        message.setId_subject((nID_subject == null) ? 0 : nID_subject);
        message.setMail((sMail == null) ? "" : sMail);
        message.setContacts((sContacts == null) ? "" : sContacts);
        message.setData((sData == null) ? "" : sData);
        message.setDate(new Timestamp(new Date().getTime()));
        return message;
    }
}
