package org.activiti.rest.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.wf.dp.dniprorada.base.dao.EscalationRuleFunctionDao;
import org.wf.dp.dniprorada.base.model.EscalationRuleFunction;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Controller
@RequestMapping(value = "/escalation")
public class ActivitiRestEscalationController {

    private static final Logger log = Logger.getLogger(ActivitiRestEscalationController.class);
    @Autowired
    private EscalationRuleFunctionDao escalationRuleFunctionDao;

    //util??
    private void throwActivitiEx(String errorCode, String message, Throwable e)
            throws ActivitiRestException {
        if (isEmpty(message)) {
            message = "error!";
        }
        ActivitiRestException newErr =
                e == null ? new ActivitiRestException(errorCode, message)
                        : new ActivitiRestException(errorCode, message, e);
        newErr.setHttpStatus(HttpStatus.FORBIDDEN);//??
        throw newErr;
    }

    private boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    private void throwActivitiEx(String message, Throwable e)
            throws ActivitiRestException {
        throwActivitiEx("BUSINESS_ERR", message, e);
    }

    private void throwActivitiEx(String message)
            throws ActivitiRestException {
        throwActivitiEx("BUSINESS_ERR", message, null);
    }

    //---------------

    private void throwActivitiEx(String errorCode, String message)
            throws ActivitiRestException {
        throwActivitiEx(errorCode, message, null);
    }

    @RequestMapping(value = "/setEscalationRuleFunction", method = RequestMethod.GET)
    public EscalationRuleFunction setEscalationRuleFunction(
            @RequestParam(value = "nID", required = false) Long nID ,
            @RequestParam(value = "sName") String sName ,
            @RequestParam(value = "sBeanHandler", required = false) String sBeanHandler)
            throws ActivitiRestException {

        try {
            return escalationRuleFunctionDao.saveOrUpdate(nID, sName, sBeanHandler);
        } catch (Exception e){
            log.error("ex in controller!", e);
            throw new ActivitiRestException("ex in controller!", e);
        }

    }

    @RequestMapping(value = "/getEscalationRuleFunction", method = RequestMethod.GET)
    public EscalationRuleFunction getEscalationRuleFunction(
            @RequestParam(value = "nID") Long nID) throws ActivitiRestException {

        try {
            EscalationRuleFunction ruleFunction = escalationRuleFunctionDao.getById(nID);
            if (ruleFunction == null) {
                throwActivitiEx("Record not found. No such ruleFunction with nID=" + nID);
            }
            return ruleFunction;
        } catch (Exception e){
            log.error("ex in controller!", e);
            throw new ActivitiRestException("ex in controller!", e);
        }
    }


    @RequestMapping(value = "/getEscalationRuleFunctions", method = RequestMethod.GET)
    public List<EscalationRuleFunction> getEscalationRuleFunctions()
            throws ActivitiRestException {

        try {
            return escalationRuleFunctionDao.getAll();
        } catch (Exception e){
            throw new ActivitiRestException("ex in controller!", e);
        }
    }

    @RequestMapping(value = "/removeEscalationRuleFunction", method = RequestMethod.GET)
    public void removeEscalationRuleFunction(
            @RequestParam(value = "nID") Long nID) throws ActivitiRestException {

        try {
            escalationRuleFunctionDao.delete(nID);
        } catch (EntityNotFoundException e) {
            throwActivitiEx(e.getMessage(), e);
        } catch (Exception e) {
            throw new ActivitiRestException("ex in controller!", e);
        }
    }

}
