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
import org.wf.dp.dniprorada.util.EscalationUtil;

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

    //----------Escalation handlers-----------------

    //----EscalationHandler_SendMailAlert
    @RequestMapping(value = "/sendMailAlertByEscalationHandler", method = RequestMethod.GET)
    public
    @ResponseBody
    void sendMailAlertByEscalationHandler(//??
                                          @RequestParam(value = "nID_Task_Activiti", required = false) Long nID_Task_Activiti,//temp!!!
                                          @RequestParam(value = "sCondition") String sCondition,
                                          @RequestParam(value = "soData") String soData,
                                          @RequestParam(value = "sPatternFile", required = false) String sPatternFile)//temp!!!
            throws ActivitiRestException {

        try {
            new EscalationUtil().sendMailAlert(nID_Task_Activiti, sCondition, soData, sPatternFile);
        } catch (Exception e) {
            throw new ActivitiRestException("ex during sending mail alert in escalationController!", e);
        }

    }
/* 3.2) Помещать параметры из "soData" в мапу мап(распарсить как JSON-объект), ожидая строку вида:
{"sParam1":"a@a.a","nParam2":456,"bParam3":true,"asMailRecipient":["b@b.b","c@c.c"]}
при том, если первые символы переменных первого уровня маленькие(при этом следующий символ большой):
s - кастить в String
n - кастить в Long
b - кастить в Boolean
as - кастить в массив/лист String
an - кастить в массив/лист Long
в остальных случаях кастить в стринг

 3.3) Добавлять в мапу п.п.3.2 параметры из полученной задачи, по ее ИД (параметр nID_Task_Activiti), с соблюдением тех-же правил определения типа (кастинг) а п.3.2.

 3.4) По выражению в "sCondition" ,с учетом параметров, полученных в п.п. 3.3. - произвести проверку, при которой если получится True - выполнять дальнейшие действия в п.п.3.5.

 3.5) Выполнить отсылку на электронную почту:

 3.5.1) по адресам, распарсеного подпараметра "asMailRecipient" (из soData)
 3.5.2) По шаблону тела, взятому с параметра "sPatternFile" (в коде искать метод getPatternFile(String sPathFile))
 3.5.3) С заголовком, распарсеного подпараметра "sHead" (из soData)
 3.5.4) Заменой всех найденых вхождений в шаблоне тела, типа [sParam0], где "sParam0" - название параметра (положенного в мапу по п.п. 3.2 и п.п. 3.3):
* */


    public static void main(String[] args) throws Exception {

        new EscalationUtil().sendMailAlert
                (null,
                        "   sUserTask=='1' && (new Date()-new Date(sDateEdit))/1000/60/60/24 > nDays",
                        "{sUserTask:'1', sDateEdit:'01-01-2015', nDays:0, asList1:['2'], anList2:[10], bBool:true}",
                        "");

    }
}
