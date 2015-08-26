package org.wf.dp.dniprorada.util;

import com.google.gson.Gson;
import com.mongodb.util.JSON;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.wf.dp.dniprorada.base.service.escalation.handler.EscalationHandler;
import org.wf.dp.dniprorada.base.service.escalation.handler.EscalationHandler_SendMailAlert;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EscalationUtil {
    private static final Logger log = Logger.getLogger(EscalationUtil.class);

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
        Map<String, Object> taskParam = new HashMap<>();
        //[Surname],[Name],[Middlename]
        taskParam.put("Surname", "Petrenko");
        taskParam.put("Name", "Petro");
        taskParam.put("Middlename", "Petrovych");
        taskParam.put("years", 40L);

//        String[] recipients = new String[2];
//        recipients[0] = "olga2012olga@gmail.com";
//        recipients[1] = "olga.prylypko@gmail.com";

        String json = "{sUserTask:'1', sDateEdit:'01-01-2015', " +
                "nDays:10, asRecipientMail:['olga2012olga@gmail.com', 'olga.prylypko@gmail.com'], " +
                "anList2:[10], bBool:true}";
        String file = "print/kiev_dms_print1.html";

        String sCondition ="nDays == 10";// "   sUserTask=='1' && (new Date()-new Date(sDateEdit))/1000/60/60/24 > nDays";

        new EscalationUtil().checkTaskOnEscalation
                (taskParam, sCondition, json, file, "escalationHandler_SendMailAlert");

    }

    public void checkTaskOnEscalation
            (Map<String, Object> mTaskParam,
             String sCondition, String soData,
             String sPatternFile, String sBeanHandler) {

        //1 -- result of condition
        Map<String, Object> jsonData = parseJsonData(soData);//from json
        mTaskParam = mTaskParam != null ? mTaskParam : new HashMap<String, Object>();

        Boolean conditionResult = false;
        try {
            conditionResult = getResultOfCondition(jsonData, mTaskParam, sCondition);
        } catch (ClassNotFoundException e) {
            log.error("wrong parameters!", e);
        } catch (ScriptException e) {
            log.error("wrong sCondition or parameters! condition=" + sCondition + "params_json=" + soData, e);
        } catch (NoSuchMethodException e) {
            log.error("error in script", e);
        }

        mTaskParam.putAll(jsonData); //concat

        //2 - check beanHandler        //sendMailAlert(Map mParam, String[] asRecipientMail, String sPatternFile);
        if (conditionResult) {
            EscalationHandler escalationHandler = getHandlerClass(sBeanHandler);
            if (escalationHandler != null) {
                escalationHandler.execute(mTaskParam, (String[]) mTaskParam.get("asRecipientMail"), sPatternFile);
            }
        }

    }


    private EscalationHandler getHandlerClass(String sBeanHandler) {
    	ApplicationContext appContext = new ClassPathXmlApplicationContext("context.xml");
    	log.info("Retrieved application context : " + appContext);
    	EscalationHandler res = (EscalationHandler) appContext.getBean("EscalationHandler_SendMailAlert"); 
    	log.info("Retrieved component : " + res);
    	return res;
//        try {
//            return (EscalationHandler) Class.forName("org.wf.dp.dniprorada.base.service.escalation.handler.EscalationHandler_SendMailAlert").newInstance();
//        } catch (InstantiationException | IllegalAccessException e) {
//            log.error("ex", e);
//        } catch (ClassNotFoundException e) {
//            log.error("ex", e);
//        }
//        return null;
//        try {
//            log.info("get app-context");
//            ApplicationContext context = new ClassPathXmlApplicationContext("context-services.xml");
//            log.info("get bean " + sBeanHandler);
//            return (EscalationHandler) context.getBean(sBeanHandler);
//        } catch (Exception e) {
//            log.error("Can't find class for handler: " + sBeanHandler, e);
//            return new EscalationHandler_SendMailAlert();
            //throw new RuntimeException();
      //  }
    }
//String -- temp!!!! must be void and not here)

//    private String sendMailAlert(Long nID_task_activiti, String sCondition, String soData, String sPatternFile)
//            throws NoSuchMethodException, ScriptException, ClassNotFoundException {
//
////        Map<String, Object> taskData = getTaskData(nID_task_activiti);//from task
////        Map<String, Object> jsonData = parseJsonData(soData);//from json
////        taskData.putAll(jsonData); //concat
////
////        return "" + getResultOfCondition(taskData, sCondition);
//    }


    private Map<String, Object> getTaskData(Long nID_task_activiti) {
        // Добавлять в мапу п.п.3.2 параметры из полученной задачи, по ее ИД (параметр nID_Task_Activiti)
        // todo downloadTasksData
        return new HashMap<>();
    }

    private Map<String, Object> parseJsonData(String soData) {
        Map<String, Object> json = (Map<String, Object>) JSON.parse(soData);
        Map<String, Object> json_ = new Gson().fromJson(soData, HashMap.class);
        return json;
    }

    private boolean getResultOfCondition(Map<String, Object> jsonData,
                                         Map<String, Object> taskData,
                                         String sCondition)
            throws ClassNotFoundException, ScriptException, NoSuchMethodException {

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        //----put parameters---
        log.info("json parameter:");
        for (String key : jsonData.keySet()) {
            //chaeck are present in sCondition??
            Parameter parameter = new Parameter(key, jsonData.get(key));
            castValue(parameter);
//            engine.put(key, Class.forName(getClassName(key)).cast(jsonData.get(key)));
            log.info(parameter.name + "=" + parameter.castValue);
            engine.put(parameter.name, parameter.castValue);
            jsonData.put(parameter.name, parameter.castValue);
        }
        for (String key : taskData.keySet()) {
            //chaeck are present in sCondition??
//            Parameter parameter = new Parameter(key, jsonData.get(key));
//            castValue(parameter);
//            engine.put(key, Class.forName(getClassName(key)).cast(jsonData.get(key)));
            engine.put(key, taskData.get(key));
//            jsonData.put(parameter.name, parameter.castValue);
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
