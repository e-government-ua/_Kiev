package org.activiti.rest.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.wf.dp.dniprorada.base.dao.EscalationRuleFunctionDao;
import org.wf.dp.dniprorada.base.model.EscalationRuleFunction;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping(value = "/escalation")
public class ActivitiRestEscalationController {

    private static final Logger log = Logger.getLogger(ActivitiRestEscalationController.class);

    @Autowired
    private static EscalationRuleFunctionDao escalationRuleFunctionDao;

    @RequestMapping(value = "/setEscalationRuleFunction", method = RequestMethod.GET)
    public EscalationRuleFunction setEscalationRuleFunction(
            @RequestParam(value = "nID", required = false) Long nID ,
            @RequestParam(value = "sName") String sName ,
            @RequestParam(value = "sBeanHandler", required = false) String sBeanHandler,
            HttpServletResponse response) throws ActivitiRestException {

        try {
            return escalationRuleFunctionDao.saveOrUpdate(nID, sName, sBeanHandler);
        } catch (Exception e){
            log.error("ex in controller!", e);
            throw new ActivitiRestException("ex in controller!", e);
        }

    }

    @RequestMapping(value = "/getEscalationRuleFunction", method = RequestMethod.GET)
    public EscalationRuleFunction getEscalationRuleFunction(
            @RequestParam(value = "nID") Long nID,
            HttpServletResponse response) throws ActivitiRestException {

        try {
            return escalationRuleFunctionDao.getById(nID);
        } catch (Exception e){
            log.error("ex in controller!", e);
            throw new ActivitiRestException("ex in controller!", e);
        }
    }

    @RequestMapping(value = "/getEscalationRuleFunctions", method = RequestMethod.GET)
     public List<EscalationRuleFunction> getEscalationRuleFunctions(
            HttpServletResponse response) throws ActivitiRestException {

        try {
            return escalationRuleFunctionDao.getAll();
        } catch (Exception e){
            log.error("ex in controller!", e);
            throw new ActivitiRestException("ex in controller!", e);
        }
    }


}
