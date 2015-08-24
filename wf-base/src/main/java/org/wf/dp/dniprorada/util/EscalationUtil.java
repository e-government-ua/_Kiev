package org.wf.dp.dniprorada.util;

import com.google.gson.Gson;
import com.mongodb.util.JSON;
import org.apache.log4j.Logger;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.HashMap;
import org.activiti.engine.FormService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.FormType;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.rest.controller.ActivitiRestException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.wf.dp.dniprorada.base.dao.EscalationRuleDao;
import org.wf.dp.dniprorada.base.dao.EscalationRuleFunctionDao;
import org.wf.dp.dniprorada.base.model.EscalationRule;
import org.wf.dp.dniprorada.base.model.EscalationRuleFunction;


public class EscalationUtil {
    private static final Logger log = Logger.getLogger(EscalationUtil.class);

    @Autowired
    private EscalationRuleFunctionDao escalationRuleFunctionDao;

    @Autowired
    private TaskService taskService;
    
    @Autowired
    private FormService formService;
    
    @Autowired
    private EscalationRuleDao escalationRuleDao;
    
    
    class Parameter {
        String name;
        String className;
        Object value;
        Object castValue;

        public Parameter(String name, Object value) {
            this.name = name;
            this.value = value;
        }
    }

    public static void main(String[] args) throws Exception {

        new EscalationUtil().sendMailAlert
                (null,
                        "   sUserTask=='1' && (new Date()-new Date(sDateEdit))/1000/60/60/24 > nDays",
                        "{sUserTask:'1', sDateEdit:'01-01-2015', nDays:0, asList1:['2'], anList2:[10], bBool:true}",
                        "");

    }

    
    public void runEscalationAll() throws ActivitiRestException {
        //@RequestParam(value = "nID", required = false) Long nID ,
        //@RequestParam(value = "sName") String sName ,
        //@RequestParam(value = "sBeanHandler", required = false) String sBeanHandler
            
        try {
            List<EscalationRule> aEscalationRule = escalationRuleDao.getAll();    
            for(EscalationRule oEscalationRule:aEscalationRule){
                EscalationRuleFunction oEscalationRuleFunction = oEscalationRule.getoEscalationRuleFunction();
                
                String sID_BP=oEscalationRule.getsID_BP();
                log.info("[getTaskData]:sID_BP=" + sID_BP);
                TaskQuery oTaskQuery = taskService.createTaskQuery().processDefinitionKey(sID_BP);//.taskCreatedAfter(dateAt).taskCreatedBefore(dateTo)

                String sID_State_BP = oEscalationRule.getsID_UserTask();
                log.info("[getTaskData]:sID_State_BP=" + sID_State_BP);
                if(sID_State_BP != null && !"*".equals(sID_State_BP)){
                    oTaskQuery = oTaskQuery.taskDefinitionKey(sID_State_BP);
                }
                
                Integer nRowStart=0;
                Integer nRowsMax=1000;
                List<Task> aTask = oTaskQuery.listPage(nRowStart, nRowsMax);
                
                for(Task oTask:aTask){
                    //long nID_task_activiti = Long.valueOf(oTask.getId());
                    //Map<String, Object> mTaskParam = getTaskData(nID_task_activiti);//new HashMap()
                    Map<String, Object> mTaskParam = getTaskData(oTask);
                    
                    log.info("[getTaskData]:checkTaskOnEscalation mTaskParam=" + mTaskParam);
                    new EscalationUtil().checkTaskOnEscalation(mTaskParam
                            , oEscalationRule.getsCondition()
                            , oEscalationRule.getSoData()
                            , oEscalationRule.getsPatternFile()
                            , oEscalationRuleFunction.getsBeanHandler()
                    );
                }
                
            }
            //return escalationRuleFunctionDao.saveOrUpdate(nID, sName, sBeanHandler);
        } catch (Exception oException){
            log.error("[getTaskData]:" + oException);
            throw new ActivitiRestException("ex in controller!", oException);
        }

    }
    
    private Map<String, Object> getTaskData(Task oTask) {//Long nID_task_activiti
        long nID_task_activiti = Long.valueOf(oTask.getId());
        log.info("[getTaskData]:nID_task_activiti=" + nID_task_activiti);
        log.info("[getTaskData]:oTask.getCreateTime().toString()=" + oTask.getCreateTime());
        log.info("[getTaskData]:oTask.getDueDate().toString()=" + oTask.getDueDate());

        Map<String, Object> m=new HashMap();
        
        //Date = Date
        long nDiffMS=0;
        if(oTask.getDueDate()!=null){
            nDiffMS=oTask.getDueDate().getTime() - oTask.getCreateTime().getTime();
        }
        log.info("[getTaskData]:nDiffMS=" + nDiffMS);
        long nElapsedHours=nDiffMS/1000/60/60;
        
        log.info("[getTaskData]:nElapsedHours=" + nElapsedHours);
        m.put("nElapsedHours", nElapsedHours);
        
        TaskFormData oTaskFormData = formService.getTaskFormData(oTask.getId());
        for (FormProperty oFormProperty : oTaskFormData.getFormProperties()) {
                log.info(String.format("[getTaskData]Matching property %s:%s:%s with fieldNames", oFormProperty.getId(), oFormProperty.getName(), oFormProperty.getType().getName()));
                if("long".equalsIgnoreCase(oFormProperty.getType().getName())){
                    m.put(oFormProperty.getId(), Long.valueOf(oFormProperty.getValue()));
                }else{
                    m.put(oFormProperty.getId(), oFormProperty.getValue());
                }
        }
        
        return m;
    }
    
    
    public void checkTaskOnEscalation
            (Map<String, Object> mTaskParam,
             String sCondition, String soData,
             String sPatternFile, String sBeanHandler) {

    }

    //String -- temp!!!! must be void and not here)
    private String sendMailAlert(Long nID_task_activiti, String sCondition, String soData, String sPatternFile)
            throws NoSuchMethodException, ScriptException, ClassNotFoundException {

        Map<String, Object> taskData = getTaskData(nID_task_activiti);//from task
        Map<String, Object> jsonData = parseJsonData(soData);//from json
        taskData.putAll(jsonData); //concat

        return "" + getResultOfCondition(taskData, sCondition);
    }



    private Map<String, Object> parseJsonData(String soData) {
        Map<String, Object> json = (Map<String, Object>) JSON.parse(soData);
        Map<String, Object> json_ = new Gson().fromJson(soData, HashMap.class);
        return json;
    }

    private boolean getResultOfCondition(Map<String, Object> data, String sCondition)
            throws ClassNotFoundException, ScriptException, NoSuchMethodException {

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        //----get parameters---
        for (String key : data.keySet()) {
            //chaeck are present in sCondition??
            Parameter parameter = new Parameter(key, data.get(key));
            castValue(parameter);
//            engine.put(key, Class.forName(getClassName(key)).cast(jsonData.get(key)));
            engine.put(parameter.name, parameter.castValue);
        }
        ///---eval script and invoke result----
        String script = getJavaScriptStr(sCondition);
        log.info(">>>>------SCRIPT:");
        log.info(script);
        engine.eval(script);
        Invocable inv = (Invocable) engine;
        Boolean result = (Boolean) inv.invokeFunction("getResult");
        log.info(">>>>------SCRIPT RESULT=" + result);
        return result;
    }


    private String getJavaScriptStr(String sCondition) {
        return "function getResult() { " +
                "   return "
                + sCondition //  "   sUserTask=='1' && (new Date()-new Date(sDateEdit))/1000/60/60/24 > nDays"
                + ";}";
    }

/*при том, если первые символы переменных первого уровня маленькие(при этом следующий символ большой):
s - кастить в String
n - кастить в Long
b - кастить в Boolean
as - кастить в массив/лист String
an - кастить в массив/лист Long
в остальных случаях кастить в стринг*/

    /*private String getClassName (String fieldName){
        if (fieldName == null || fieldName.length() < 1)
            throw new IllegalArgumentException("incorrect fieldName (empty)!");
        //get mark
        String mark = fieldName.substring(0, 1);
        if (mark.toLowerCase().equals(mark) && fieldName.length() > 1){
            String mark_2 = fieldName.substring(0,2);
            if (mark_2.toLowerCase().equals(mark_2)){
                mark = mark_2;
            }
        }
        switch (mark){
            case "n":   return "java.lang.Long";
            case "b":   return "java.lang.Boolean";
            case "as":  return "[S";
            case "an":  return "[L";
            case "s":
            default:    return "java.lang.String";
        }
    }*/

    private void castValue(Parameter parameter) {
        String fieldName = parameter.name;
        if (fieldName == null || fieldName.length() < 1)
            throw new IllegalArgumentException("incorrect fieldName (empty)!");
        //get mark
        String mark = fieldName.substring(0, 1);
        if (mark.toLowerCase().equals(mark) && fieldName.length() > 1) {
            String mark_2 = fieldName.substring(0, 2);
            if (mark_2.toLowerCase().equals(mark_2)) {
                mark = mark_2;
            }
        }
        switch (mark) {
            case "n":
                parameter.className = "Long";
                parameter.castValue = new Long(parameter.value.toString());
                break;
            case "b":
                parameter.className = "Boolean";
                parameter.castValue = new Boolean(parameter.value.toString());
                break;
            case "as":
                parameter.className = "String[]";//"[S"
                String[] strings = new String[((List) parameter.value).size()];
                int i = 0;
                for (Object value : ((List) parameter.value)) {
                    strings[i++] = value.toString();
                }
                parameter.castValue = strings;
                break;
            case "an":
                parameter.className = "String[]";//"[L"
                Long[] longs = new Long[((List) parameter.value).size()];
                int j = 0;
                for (Object value : ((List) parameter.value)) {
                    longs[j++] = new Long(value.toString());
                }
                parameter.castValue = longs;
            case "s":
            default:
                parameter.className = "String";//"[S"
                parameter.castValue = parameter.value.toString();
                break;
        }
    }
}
