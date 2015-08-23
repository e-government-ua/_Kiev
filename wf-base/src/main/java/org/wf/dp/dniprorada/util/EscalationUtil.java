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

    //String -- temp!!!! must be void and not here)
    public String sendMailAlert(Long nID_task_activiti, String sCondition, String soData, String sPatternFile)
            throws NoSuchMethodException, ScriptException, ClassNotFoundException {

        return "" + getResultOfCondition(nID_task_activiti, sCondition, soData);

    }


    private boolean getResultOfCondition(
            Long nID_task_activiti,
            String sCondition,
            String soData) throws ClassNotFoundException, ScriptException, NoSuchMethodException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        //----get parameters---
        Map<String, Object> jsonData = parseJsonData(soData);

        // Добавлять в мапу п.п.3.2 параметры из полученной задачи, по ее ИД (параметр nID_Task_Activiti)
        // todo downloadTasksData
//        engine.put("nID_task_activiti", nID_task_activiti); //??

        for (String key : jsonData.keySet()) {
            //chaeck are present in sCondition??
            Parameter parameter = new Parameter(key, jsonData.get(key));
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

    private Map<String, Object> parseJsonData(String soData) {
        Map<String, Object> json = (Map<String, Object>) JSON.parse(soData);
        Map<String, Object> json_ = new Gson().fromJson(soData, HashMap.class);
        return json;
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
