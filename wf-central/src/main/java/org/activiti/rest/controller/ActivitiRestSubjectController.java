package org.activiti.rest.controller;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.wf.dp.dniprorada.dao.SubjectDao;
import org.wf.dp.dniprorada.dao.SubjectHumanDao;
import org.wf.dp.dniprorada.dao.SubjectOrganDao;
import org.wf.dp.dniprorada.model.Subject;
import org.wf.dp.dniprorada.model.SubjectHuman;
import org.wf.dp.dniprorada.model.SubjectOrgan;
import org.wf.dp.dniprorada.util.Util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/subject")
public class ActivitiRestSubjectController {

    private final Logger log = LoggerFactory.getLogger(Subject.class);

    @Autowired
    private SubjectDao subjectDao;

    @Autowired
    private SubjectHumanDao subjectHumanDao;

    @Autowired
    private SubjectOrganDao subjectOrganDao;

    @RequestMapping(value = "/syncSubject", method = RequestMethod.GET, headers = { "Accept=application/json" })
    public
    @ResponseBody
    Subject syncSubject(@RequestParam(value = "nID", required = false) Long nID,
            @RequestParam(required = false) String sINN,
            @RequestParam(required = false) String sOKPO,
            HttpServletRequest request, HttpServletResponse httpResponse) {
        log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        Subject subject = null;
        if (nID != null) {
            subject = subjectDao.getSubject(nID);
        } else if (Util.isNotEmpty(sINN)) {
            SubjectHuman subjectHuman = subjectHumanDao.getSubjectHuman(sINN);
            if (subjectHuman == null) {
                subjectHuman = subjectHumanDao.setSubjectHuman(sINN);
            }
            subject = subjectHuman.getoSubject();
        } else if (Util.isNotEmpty(sOKPO)) {
            SubjectOrgan subjectOrgan = subjectOrganDao.getSubjectOrgan(sOKPO);
            if (subjectOrgan == null) {
                subjectOrgan = subjectOrganDao.setSubjectOrgan(sOKPO);
            }
            subject = subjectOrgan.getoSubject();
        } else {
            throw new ActivitiObjectNotFoundException(
                    String.format("RequestParam not found! You should add %s or %s or sOKPO param!", nID, sINN, sOKPO),
                    Subject.class);
        }
        if (subject == null) {
            throw new ActivitiObjectNotFoundException(
                    String.format("Subject not found! nID = %s sINN = %s sOKPO = %s", nID, sINN, sOKPO),
                    Subject.class);
        }
        httpResponse.setHeader("Content-Type", "application/json;charset=UTF-8");
        return subject;
    }

    @RequestMapping(value = "/setSubjectHuman", method = RequestMethod.POST, headers = { "Accept=application/json" })
    public
    @ResponseBody
    SubjectHuman setSubject(@RequestBody SubjectHuman subjectHuman) {
        return subjectHumanDao.saveOrUpdateHuman(subjectHuman);
    }

    @RequestMapping(value = "/setSubjectOrgan", method = RequestMethod.POST, headers = { "Accept=application/json" })
    public
    @ResponseBody
    SubjectOrgan setSubject(@RequestBody SubjectOrgan subjectOrgan) {
        return subjectOrganDao.saveOrUpdateSubjectOrgan(subjectOrgan);
    }
}
