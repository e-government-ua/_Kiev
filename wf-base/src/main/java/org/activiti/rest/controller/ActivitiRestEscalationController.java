package org.activiti.rest.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wf.dp.dniprorada.base.dao.EscalationRuleDao;
import org.wf.dp.dniprorada.base.dao.EscalationRuleFunctionDao;
import org.wf.dp.dniprorada.base.model.EscalationRule;
import org.wf.dp.dniprorada.base.model.EscalationRuleFunction;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Controller
@RequestMapping(value = "/escalation")
public class ActivitiRestEscalationController {

    private static final Logger log = Logger.getLogger(ActivitiRestEscalationController.class);

    @Autowired
    private EscalationRuleFunctionDao escalationRuleFunctionDao;

    @Autowired
    private EscalationRuleDao escalationRuleDao;

    //----------EscalationRuleFunction services-----------------

    @RequestMapping(value = "/setEscalationRuleFunction", method = RequestMethod.GET)
    public
    @ResponseBody
    EscalationRuleFunction setEscalationRuleFunction(
            @RequestParam(value = "nID", required = false) Long nID ,
            @RequestParam(value = "sName") String sName ,
            @RequestParam(value = "sBeanHandler", required = false) String sBeanHandler)
            throws ActivitiRestException {

        try {
            return escalationRuleFunctionDao.saveOrUpdate(nID, sName, sBeanHandler);
        } catch (Exception e){
            throw new ActivitiRestException("ex in controller!", e);
        }

    }

    @RequestMapping(value = "/getEscalationRuleFunction", method = RequestMethod.GET)
    public
    @ResponseBody
    EscalationRuleFunction getEscalationRuleFunction(
            @RequestParam(value = "nID") Long nID) throws ActivitiRestException {

        EscalationRuleFunction ruleFunction = escalationRuleFunctionDao.getById(nID);
        if (ruleFunction == null) {
            throw new ActivitiRestException(
                    ActivitiExceptionController.BUSINESS_ERROR_CODE,
                    "Record not found. No such EscalationRuleFunction with nID=" + nID,
                    HttpStatus.FORBIDDEN);
        }
        return ruleFunction;
    }


    @RequestMapping(value = "/getEscalationRuleFunctions", method = RequestMethod.GET)
    public
    @ResponseBody
    List<EscalationRuleFunction> getEscalationRuleFunctions()
            throws ActivitiRestException {

        try {
            return escalationRuleFunctionDao.getAll();
        } catch (Exception e){
            throw new ActivitiRestException("ex in controller!", e);
        }
    }

    @RequestMapping(value = "/removeEscalationRuleFunction", method = RequestMethod.GET)
    public
    @ResponseBody
    void removeEscalationRuleFunction(
            @RequestParam(value = "nID") Long nID) throws ActivitiRestException {

        try {
            escalationRuleFunctionDao.delete(nID);
        } catch (EntityNotFoundException e) {
            throw new ActivitiRestException(
                    ActivitiExceptionController.BUSINESS_ERROR_CODE,
                    e.getMessage(),
                    e, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            throw new ActivitiRestException("ex in controller!", e);
        }
    }

    //----------EscalationRule services-----------------

    @RequestMapping(value = "/setEscalationRule", method = RequestMethod.GET)
    public
    @ResponseBody
    EscalationRule setEscalationRule(
            @RequestParam(value = "nID", required = false) Long nID,
            @RequestParam(value = "sID_BP") String sID_BP,
            @RequestParam(value = "sID_UserTask") String sID_UserTask,
            @RequestParam(value = "sCondition") String sCondition,
            @RequestParam(value = "soData") String soData,
            @RequestParam(value = "sPatternFile") String sPatternFile,
            @RequestParam(value = "nID_EscalationRuleFunction") Long nID_EscalationRuleFunction)
            throws ActivitiRestException {

        try {
            return escalationRuleDao.saveOrUpdate(nID, sID_BP, sID_UserTask,
                    sCondition, soData, sPatternFile, nID_EscalationRuleFunction);
        } catch (Exception e) {
            throw new ActivitiRestException("ex in controller!", e);
        }

    }

    @RequestMapping(value = "/getEscalationRule", method = RequestMethod.GET)
    public
    @ResponseBody
    EscalationRule getEscalationRule(
            @RequestParam(value = "nID") Long nID) throws ActivitiRestException {

        EscalationRule rule = escalationRuleDao.getById(nID);
        if (rule == null) {
            throw new ActivitiRestException(
                    ActivitiExceptionController.BUSINESS_ERROR_CODE,
                    "Record not found. No such EscalationRule with nID=" + nID,
                    HttpStatus.FORBIDDEN);
        }
        return rule;
    }


    @RequestMapping(value = "/getEscalationRules", method = RequestMethod.GET)
    public
    @ResponseBody
    List<EscalationRule> getEscalationRules()
            throws ActivitiRestException {

        try {
            return escalationRuleDao.getAll();
        } catch (Exception e) {
            throw new ActivitiRestException("ex in controller!", e);
        }
    }

    @RequestMapping(value = "/removeEscalationRule", method = RequestMethod.GET)
    public
    @ResponseBody
    void removeEscalationRule(
            @RequestParam(value = "nID") Long nID) throws ActivitiRestException {

        try {
            escalationRuleDao.delete(nID);
        } catch (EntityNotFoundException e) {
            throw new ActivitiRestException(
                    ActivitiExceptionController.BUSINESS_ERROR_CODE,
                    e.getMessage(),
                    e, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            throw new ActivitiRestException("ex in controller!", e);
        }
    }


}
