package org.activiti.rest.controller;

import com.google.gson.Gson;
import com.google.gwt.user.server.Base64Utils;
import com.mongodb.util.JSON;
import com.sun.mail.util.BASE64DecoderStream;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wf.dp.dniprorada.model.LiqpayCallbackModel;

import java.util.HashMap;
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

//    @Autowired
//    TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private HistoryService historyService;
    private final String sID_PaymentSystem = "Liqpay";
    
    @RequestMapping(value = "/setPaymentStatus_TaskActiviti", method = RequestMethod.POST, headers = { "Accept=application/json" })
	public @ResponseBody String setPaymentStatus_TaskActiviti(
			@RequestParam String sID_Order,
			@RequestParam String sID_PaymentSystem,
			@RequestParam byte[] data,
			@RequestParam byte[] signature
			){

    	String sData = new String(BASE64DecoderStream.decode(data));
        setPaymentStatus(sID_Order, sData, sID_PaymentSystem);

		return sData;
	}
    @RequestMapping(value = "/setPaymentStatus_TaskActiviti2/", method = RequestMethod.POST, headers = { "Accept=application/json" })
	public @ResponseBody String setPaymentStatusNew_TaskActiviti(
			@RequestParam byte[] data,
			@RequestParam byte[] signature
			){
    	
    	String sFullData = new String(BASE64DecoderStream.decode(data));
    	Gson gson = new Gson();
    	LiqpayCallbackModel liqpayCallback = gson.fromJson(sFullData, LiqpayCallbackModel.class);
        setPaymentStatus(liqpayCallback.getOrder_id(), sFullData, sID_PaymentSystem);

		return sFullData;
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

        //save info to process
        try {
            log.info("try to get task. task_id=" + nID_Task);
            HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery()
                    .taskId("" + nID_Task).singleResult();
            log.info("try to set sID_Payment to processInstance of task, getProcessInstanceId="
                    +  task.getProcessInstanceId());
            runtimeService.setVariable(
                    task.getProcessInstanceId(),
                    "sID_Payment", transaction_id);
            runtimeService.setVariable("" + nID_Task, "sID_Payment", transaction_id);
            if (task.getProcessVariables().get("sID_Payment") == transaction_id) {
                log.info("success");
            }
        } catch (Exception e){
            log.error("error during changing task " + nID_Task + ", field sID_Payment=" + transaction_id, e);
        }


    }



    private Object getProccessVariableValue(String processInstance_ID,
                                            String variableName) {

        HistoricVariableInstance historicVariableInstance = historyService
                .createHistoricVariableInstanceQuery()
                .processInstanceId(processInstance_ID)
                .variableName(variableName).singleResult();
        return (Object) historicVariableInstance.getValue();
    }

    public static void main(String[] args) {
        Map values = new HashMap<String, String>();
        values.put(LIQPAY_FIELD_TRANSACTION_ID, "123");
        values.put(LIQPAY_FIELD_PAYMENT_STATUS, "success");
        ActivitiPaymentRestController controller = new ActivitiPaymentRestController();
        controller.setPaymentStatus("TaskActiviti_12345", new JSONObject(values).toString(), LIQPAY_PAYMENT_SYSTEM);
    }
}
