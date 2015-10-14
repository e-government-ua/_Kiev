package org.wf.dp.dniprorada.constant;

import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.impl.util.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class HistoryEventMessage {
    public static final String SERVICE_NAME = "%Назва послуги%";
    public static final String SERVICE_STATE = "%статус%";
    public static final String TASK_NUMBER = "%nTask%";
    public static final String FIO = "%Ім’я того, кому надають доступ%";
    public static final String TELEPHONE = "%телефон%";
    public static final String DOCUMENT_TYPE = "%Тип документу%";
    public static final String DOCUMENT_NAME = "%Назва документу%";
    public static final String ORGANIZATION_NAME = "%Назва органу%";
    public static final String EMAIL = "%email%";
    public static final String DAYS = "%кількість днів%";
    public static final String TABLE_BODY = "%tableBody%";
    public static final String S_BODY = "%sBody%";
    private static final Logger log = LoggerFactory.getLogger(HistoryEventMessage.class);

    public static String createJournalMessage(HistoryEventType eventType, Map<String, String> values) {
        String eventMessage = "";
        try {
            eventMessage = eventType.getsTemplate();
            for (String key : values.keySet()) {
                eventMessage = eventMessage.replaceAll(key, values.get(key));
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        return eventMessage;
    }

    public static String createTable(String soData) {
        if (soData == null || "[]".equals(soData) || "".equals(soData)) {
            return "";
        }
        StringBuilder tableStr = new StringBuilder("Поле \t/ Тип \t/ Поточне значення\n");
        JSONObject jsnobject = new JSONObject("{ \"soData\":" + soData + "}");
        JSONArray jsonArray = jsnobject.getJSONArray("soData");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject record = jsonArray.getJSONObject(i);
            tableStr.append(record.opt("id") != null ? record.get("id") : "?")
                    .append(" \t ")
                    .append(record.opt("type")!= null ? record.get("type").toString() : "??")
                    .append(" \t ")
                    .append(record.opt("value")!= null ? record.get("value").toString() : "")
                    .append(" \n");
        }
        return tableStr.toString();
    }

    public static void main(String[] args) {
        System.out.println(createTable("[{'id':'bankIdfirstName','type':'string','value':'3119325858'}]"));
    }
}
