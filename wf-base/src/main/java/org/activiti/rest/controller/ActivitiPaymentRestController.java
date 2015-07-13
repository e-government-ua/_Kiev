package org.activiti.rest.controller;

import com.google.gwt.user.server.Base64Utils;
import com.mongodb.util.JSON;
import org.activiti.engine.TaskService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

@Controller
public class ActivitiPaymentRestController {

    private static final Logger log = Logger.getLogger(ActivitiPaymentRestController.class);

    public static final String LIQPAY_PAYMENT_SYSTEM = "Liqpay";
    public static final String LIQPAY_FIELD_TRANSACTION_ID = "transaction_id";
    public static final String LIQPAY_FIELD_PAYMENT_STATUS = "status";
    public static final String TASK_MARK = "TaskActiviti_";
    public static final String PAYMENT_SUCCESS = "success";

    @Autowired
    TaskService taskService;
//    @Autowired
//    RuntimeService runtimeService;
//    @Autowired
//    private HistoryService historyService;

    @RequestMapping(value = "/setPaymentStatus_TaskActiviti", method = RequestMethod.POST, headers = { "Accept=application/json" })
	public @ResponseBody String setPaymentStatus_TaskActiviti(
			@RequestParam byte[] data,
			@RequestParam byte[] signature,
			@RequestParam String sID_Order,
			@RequestParam String sID_PaymentSystem,
			HttpServletResponse response){

    	String sData = new String(Base64Utils.toBase64(data));
        setPaymentStatus(sID_Order, sData, sID_PaymentSystem);

		return sData;
	}

    private void setPaymentStatus(String sID_Order, String sData, String sID_PaymentSystem) {
        if (!LIQPAY_PAYMENT_SYSTEM.equals(sID_PaymentSystem)) {
            log.info("not liqpay system");
            return;
        }
        //parse sData
        String transaction_id = null;
        String sStatus_Payment = null;
        try {
            Map<String, Object> json = (Map<String, Object>) JSON.parse(sData);
            transaction_id = (String) json.get(LIQPAY_FIELD_TRANSACTION_ID);
            sStatus_Payment = (String) json.get(LIQPAY_FIELD_PAYMENT_STATUS);
        } catch (Exception e) {
            log.error("can't parse json! reason:" + e.getMessage());
        }
        Long nID_Task = null;
        try {
            if (sID_Order.contains(TASK_MARK)) {
                nID_Task = Long.decode(sID_Order.replace(TASK_MARK, ""));
            }
        } catch (NumberFormatException e) {
            log.error("incorrect sID_Order! can't invoke task_id: " + sID_Order);
        }

        //check variables
        if (transaction_id == null || nID_Task == null
                || !PAYMENT_SUCCESS.equals(sStatus_Payment)) {
            log.warn("incorrect input data: " +
                    "tr_id=" + transaction_id + ", task_id=" + nID_Task + ", pay_status=" + sStatus_Payment);
            return;
        }

        //save info to task
//        Map<String, Object> variables = new HashMap<>();
//        variables.put("sID_Payment", transaction_id);
//        taskService.setVariables(""+nID_Task,variables );
        try {
            taskService.setVariable("" + nID_Task, "sID_Payment", transaction_id);
        } catch (Exception e){
            log.error("error during changing task " + nID_Task + ", field sID_Payment=" + transaction_id, e);
        }
//        HistoricTaskInstance historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
//                .taskId("" + nID_Task).singleResult();
//        historicTaskInstanceQuery.
    }
/*
    public static void main(String[] args) {
        Map values = new HashMap<String, String>();
        values.put(LIQPAY_FIELD_TRANSACTION_ID, "123");
        values.put(LIQPAY_FIELD_PAYMENT_STATUS, "success");
        ActivitiPaymentRestController controller = new ActivitiPaymentRestController();
        controller.setPaymentStatus("TaskActiviti_12345", new JSONObject(values).toString(), LIQPAY_PAYMENT_SYSTEM);
    }*/
}
