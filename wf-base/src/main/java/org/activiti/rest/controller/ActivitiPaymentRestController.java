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
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

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
    private TaskService taskService;
    
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private HistoryService historyService;
    private final String sID_PaymentSystem = "Liqpay";

    @RequestMapping(value = "/setPaymentStatus_TaskActiviti0", method = RequestMethod.POST, headers = { "Accept=application/json" })
	public @ResponseBody String setPaymentStatus_TaskActiviti(
			@RequestParam String sID_Order,
			@RequestParam String sID_PaymentSystem,
			@RequestParam String sData
			){

            log.info("sID_Order="+sID_Order);
            log.info("sID_PaymentSystem="+sID_PaymentSystem);
            log.info("sData="+sData);
            setPaymentStatus(sID_Order, null, sID_PaymentSystem);
            return sData;
	}
    
    @RequestMapping(value = "/setPaymentStatus_TaskActiviti", method = RequestMethod.POST, headers = { "Accept=application/json" })
	public @ResponseBody String setPaymentStatus_TaskActiviti(
			@RequestParam String sID_Order,
			@RequestParam String sID_PaymentSystem,
			@RequestParam String sData,
			@RequestParam byte[] data,
			@RequestParam byte[] signature
			){

            log.info("sID_Order="+sID_Order);
            log.info("sID_PaymentSystem="+sID_PaymentSystem);
            log.info("sData="+sData);
            log.info("data="+data);
            log.info("signature="+signature);
            String sDataDecoded = new String(BASE64DecoderStream.decode(data));
            log.info("sDataDecoded="+sDataDecoded);
            setPaymentStatus(sID_Order, sDataDecoded, sID_PaymentSystem);
            return sData;
	}
    @RequestMapping(value = "/setPaymentStatus_TaskActiviti2/", method = RequestMethod.POST, headers = { "Accept=application/json" })
	public @ResponseBody String setPaymentStatusNew_TaskActiviti(
			@RequestParam byte[] data,
			@RequestParam byte[] signature
			){

            log.info("data="+data);
            log.info("signature="+signature);

            String sFullData = new String(BASE64DecoderStream.decode(data));
            log.info("sFullData="+sFullData);
            Gson gson = new Gson();
            LiqpayCallbackModel liqpayCallback = gson.fromJson(sFullData, LiqpayCallbackModel.class);
            //log.info("sID_PaymentSystem="+sID_PaymentSystem);
            log.info("liqpayCallback.getOrder_id()="+liqpayCallback.getOrder_id());
            setPaymentStatus(liqpayCallback.getOrder_id(), sFullData, sID_PaymentSystem);
            return sFullData;
	}

    private void setPaymentStatus(String sID_Order, String sData, String sID_PaymentSystem) {
        if (!LIQPAY_PAYMENT_SYSTEM.equals(sID_PaymentSystem)) {
            log.info("not liqpay system");
            return;
        }
        
        Long nID_Task = null;
        try {
            if (sID_Order.contains(TASK_MARK)) {
                nID_Task = Long.decode(sID_Order.replace(TASK_MARK, ""));
            }
        } catch (NumberFormatException e) {
            log.error("incorrect sID_Order! can't invoke task_id: " + sID_Order);
        }
        String snID_Task = ""+nID_Task;
        
        //parse sData
        String sID_Transaction = "Pay_"+snID_Task;
        String sStatus_Payment = null;
        if(sData != null){
            try {
                Map<String, Object> json = (Map<String, Object>) JSON.parse(sData);
                sID_Transaction = (String) json.get(LIQPAY_FIELD_TRANSACTION_ID);
                sStatus_Payment = (String) json.get(LIQPAY_FIELD_PAYMENT_STATUS);
            } catch (Exception e) {
                log.error("can't parse json! reason:" + e.getMessage());
            }
        }

        //check variables
        if (sData != null && (sID_Transaction == null || nID_Task == null || !PAYMENT_SUCCESS.equals(sStatus_Payment))) {
            log.warn("incorrect input data: " + "tr_id=" + sID_Transaction + ", task_id=" + nID_Task + ", pay_status=" + sStatus_Payment);
            return;
        }

        //save info to process
        try {
            log.info("try to get task. snID_Task=" + snID_Task);
            /*
            HistoricTaskInstance oTask = historyService.createHistoricTaskInstanceQuery().taskId("" + nID_Task).singleResult();
            //HistoricTaskInstance oTask = runtimeService.createExecutionQuery(). createExecutionQuery().taskId("" + nID_Task).singleResult();
            
            log.info("try to set sID_Payment to processInstance of task, getProcessInstanceId=" + oTask.getProcessInstanceId());
            runtimeService.setVariable(oTask.getProcessInstanceId(), "sID_Payment", sID_Transaction);
            runtimeService.setVariable("" + nID_Task, "sID_Payment", sID_Transaction);
            if (oTask.getProcessVariables().get("sID_Payment") == sID_Transaction) {
                log.info("success");
            }*/

//=            Task oTask = taskService.createTaskQuery().taskId(""+nID_Task).singleResult();
//=            String snID_Process = oTask.getProcessInstanceId();
            
//TODO разобраться почему приходит ИД процесса а не таски
            String snID_Process = snID_Task;
            log.info("try to set sID_Payment to processInstance of task, snID_Process=" + snID_Process);
            runtimeService.setVariable(snID_Process, "sID_Payment", sID_Transaction);
        } catch (Exception e){
            log.error("error during changing: nID_Task=" + nID_Task + ", sID_Transaction=" + sID_Transaction, e);
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
