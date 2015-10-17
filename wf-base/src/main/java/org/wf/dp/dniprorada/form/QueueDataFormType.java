package org.wf.dp.dniprorada.form;

import com.google.gson.Gson;
import org.activiti.engine.form.AbstractFormType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author inna
 */
public class QueueDataFormType extends AbstractFormType {

    public static final String TYPE_NAME = "queueData";
    public static final String sDate = "sDate";
    public static final String nID_FlowSlotTicket = "nID_FlowSlotTicket";
    private static final long serialVersionUID = 1L;

    public static Map<String, Object> parseQueueData(String queueData) {
        return new Gson().fromJson(queueData, HashMap.class);
    }

    public static long get_nID_FlowSlotTicket(Map<String, Object> queueDataMap) {
        return ((Number) queueDataMap.get(QueueDataFormType.nID_FlowSlotTicket)).longValue();
    }

    public String getName() {
        return TYPE_NAME;
    }

    @Override
    public Object convertFormValueToModelValue(String propertyValue) {
        return propertyValue;
    }

    @Override
    public String convertModelValueToFormValue(Object modelValue) {
        return (String) modelValue;
    }

}