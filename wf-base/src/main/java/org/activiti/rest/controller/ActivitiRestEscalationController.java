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
import org.wf.dp.dniprorada.base.service.escalation.EscalationService;
import org.wf.dp.dniprorada.base.service.escalation.EscalationHelper;
import org.wf.dp.dniprorada.util.GeneralConfig;

import javax.persistence.EntityNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/escalation")
public class ActivitiRestEscalationController {

    private static final Logger log = Logger.getLogger(ActivitiRestEscalationController.class);

    @Autowired
    private EscalationRuleFunctionDao escalationRuleFunctionDao;

    @Autowired
    private EscalationRuleDao escalationRuleDao;

    @Autowired
    private EscalationService escalationService;

    @Autowired
    private EscalationHelper escalationHelper;
    
	@Autowired		
	GeneralConfig generalConfig;

    @RequestMapping(value = "/runEscalationRule", method = RequestMethod.GET)
    public
    @ResponseBody
    void runEscalationRule(
        @RequestParam(value = "nID") Long nID)
            throws ActivitiRestException {

        escalationService.runEscalationRule(nID, generalConfig.sHost());
    }

    @RequestMapping(value = "/runEscalation", method = RequestMethod.GET)
    public
    @ResponseBody
    void runEscalationAll() throws ActivitiRestException {
        //@RequestParam(value = "nID", required = false) Long nID ,
        //@RequestParam(value = "sName") String sName ,
        //@RequestParam(value = "sBeanHandler", required = false) String sBeanHandler
        escalationService.runEscalationAll();
    }
    @RequestMapping(value = "/sendEmail", method = RequestMethod.GET)
    public
    @ResponseBody
    void sendEmail(
            @RequestParam(value = "sCondition", required = false) String sCondition)
            throws ActivitiRestException {

        Map<String, Object> taskParam = new HashMap<>();
        //[Surname],[Name],[Middlename]
        taskParam.put("Surname", "Petrenko");
        taskParam.put("Name", "Petro");
        taskParam.put("Middlename", "Petrovych");
        taskParam.put("years", 40L);

        String json = "{sUserTask:'1', sDateEdit:'01-01-2015', " +
                "nDays:10, asRecipientMail:['olga2012olga@gmail.com'], " +
                "anList2:[10], bBool:true}";
        String file = "print/kiev_dms_print1.html";

        sCondition = sCondition == null ? "nDays == 10": sCondition;// "   sUserTask=='1' && (new Date()-new Date(sDateEdit))/1000/60/60/24 > nDays";

        escalationHelper.checkTaskOnEscalation
                (taskParam, sCondition, json, file, "escalationHandler_SendMailAlert");
    }
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

        EscalationRuleFunction ruleFunction = escalationRuleFunctionDao.findById(nID).orNull();
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
            return escalationRuleFunctionDao.findAll();
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
            EscalationRuleFunction ruleFunction = null;
            if (nID_EscalationRuleFunction != null){
                ruleFunction = escalationRuleFunctionDao.findById(nID_EscalationRuleFunction).orNull();
            }
            return escalationRuleDao.saveOrUpdate(nID, sID_BP, sID_UserTask,
                    sCondition, soData, sPatternFile, ruleFunction);
        } catch (Exception e) {
            throw new ActivitiRestException("ex in controller!", e);
        }

    }

    @RequestMapping(value = "/getEscalationRule", method = RequestMethod.GET)
    public
    @ResponseBody
    EscalationRule getEscalationRule(
            @RequestParam(value = "nID") Long nID) throws ActivitiRestException {

        EscalationRule rule = escalationRuleDao.findById(nID).orNull();
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
            return escalationRuleDao.findAll();
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

    //----------Escalation handlers-----------------

    //----EscalationHandler_SendMailAlert
//    @RequestMapping(value = "/sendMailAlertByEscalationHandler", method = RequestMethod.GET)
//    public
//    @ResponseBody
//    void sendMailAlertByEscalationHandler(//??
//                                          @RequestParam(value = "nID_Task_Activiti", required = false) Long nID_Task_Activiti,//temp!!!
//                                          @RequestParam(value = "sCondition") String sCondition,
//                                          @RequestParam(value = "soData") String soData,
//                                          @RequestParam(value = "sPatternFile", required = false) String sPatternFile)//temp!!!
//            throws ActivitiRestException {
//
//        try {
//            new EscalationUtil().sendMailAlert(nID_Task_Activiti, sCondition, soData, sPatternFile);
//        } catch (Exception e) {
//            throw new ActivitiRestException("ex during sending mail alert in escalationController!", e);
//        }
//
//    }
}
