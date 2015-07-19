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
			@RequestParam String sData,
			@RequestParam(value = "data", required = false) String data,
			@RequestParam(value = "signature", required = false) String signature
			//@RequestParam byte[] data,
			//@RequestParam byte[] signature
			){

            log.info("sID_Order="+sID_Order);
            log.info("sID_PaymentSystem="+sID_PaymentSystem);
            log.info("sData="+sData);
            
            log.info("data="+data);
            log.info("signature="+signature);
            String sDataDecoded = null;
            if(data != null){
                sDataDecoded = new String(BASE64DecoderStream.decode(data.getBytes()));
                log.info("sDataDecoded="+sDataDecoded);
            }
            setPaymentStatus(sID_Order, sDataDecoded, sID_PaymentSystem);
            //setPaymentStatus(sID_Order, null, sID_PaymentSystem);
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

        log.info("sData=" + sData);
        
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
        //https://test.region.igov.org.ua/wf-region/service/setPaymentStatus_TaskActiviti0?sID_Order=TaskActiviti_1485001&sID_PaymentSystem=Liqpay&sData=&nID_Subject=20045&sAccessKey=b32d9855-dce0-44df-bbe0-dd0e41958cde
        //data=eyJwYXltZW50X2lkIjo2MzQ0NDcxOCwidHJhbnNhY3Rpb25faWQiOjYzNDQ0NzE4LCJzdGF0dXMiOiJzYW5kYm94IiwidmVyc2lvbiI6MywidHlwZSI6ImJ1eSIsInB1YmxpY19rZXkiOiJpMTAxNzI5NjgwNzgiLCJhY3FfaWQiOjQxNDk2Mywib3JkZXJfaWQiOiJUYXNrQWN0aXZpdGlfMTQ4NTAwMSIsImxpcXBheV9vcmRlcl9pZCI6IjQwMXUxNDM3MzI1MDIyMTgzMzAzIiwiZGVzY3JpcHRpb24iOiLQotC10YHRgtC+0LLQsNGPINGC0YDQsNC90LfQsNC60YbQuNGPIiwic2VuZGVyX3Bob25lIjoiMzgwOTc5MTM4MDA3IiwiYW1vdW50IjowLjAxLCJjdXJyZW5jeSI6IlVBSCIsInNlbmRlcl9jb21taXNzaW9uIjowLjAsInJlY2VpdmVyX2NvbW1pc3Npb24iOjAuMCwiYWdlbnRfY29tbWlzc2lvbiI6MC4wLCJhbW91bnRfZGViaXQiOjAuMDEsImFtb3VudF9jcmVkaXQiOjAuMDEsImNvbW1pc3Npb25fZGViaXQiOjAuMCwiY29tbWlzc2lvbl9jcmVkaXQiOjAuMCwiY3VycmVuY3lfZGViaXQiOiJVQUgiLCJjdXJyZW5jeV9jcmVkaXQiOiJVQUgiLCJzZW5kZXJfYm9udXMiOjAuMCwiYW1vdW50X2JvbnVzIjowLjB9
        //signature=z77CQeBn3Z75n5UpJqXKG+KjZyI=
        String sID_Transaction = "Pay_"+snID_Task;
        String sStatus_Payment = null;
        if(sData != null){
            try {
                //Map<String, Object> json = (Map<String, Object>) JSON.parse(sData);
                Map<String, Object> json = new Gson().fromJson(sData, HashMap.class);
                sID_Transaction = (String) json.get(LIQPAY_FIELD_TRANSACTION_ID);
                sStatus_Payment = (String) json.get(LIQPAY_FIELD_PAYMENT_STATUS);
            } catch (Exception e) {
                log.error("can't parse json! reason:" + e.getMessage());
                int nAt;
                int nTo;
                
                String sFieldName = LIQPAY_FIELD_TRANSACTION_ID;//"transaction_id"
                log.info("sFieldName="+sFieldName+",sID_Transaction="+sID_Transaction);
                try {
                    nAt=sData.indexOf(sFieldName);
                    if(nAt>-1){
                        nTo=sData.indexOf(",",nAt);
                        String s=sData.substring(nAt+(sFieldName.length())+2,nTo);
                        log.info("s=" + s);
                        //"transaction_id":63444718
                        sID_Transaction=s;
                        log.info("NEW: sID_Transaction="+sID_Transaction);
                    }else{
                        log.error("nAt="+nAt);
                    }
                } catch (Exception e1) {
                    log.error("can't get field json!(sFieldName="+sFieldName + "):" + e.getMessage());
                }
                
                try {
                    sFieldName = LIQPAY_FIELD_PAYMENT_STATUS;//"status"
                    log.info("sFieldName="+sFieldName+",sStatus_Payment="+sStatus_Payment);
                    nAt=sData.indexOf(sFieldName);
                    if(nAt>-1){
                        nTo=sData.indexOf(",",nAt);
                        String s=sData.substring(nAt+(sFieldName.length())+2+1,nTo-1);
                        log.info("s=" + s);
                        //"transaction_id":63444718
                        sStatus_Payment=s;
                        log.info("NEW: sStatus_Payment="+sStatus_Payment);
                    }else{
                        log.error("nAt="+nAt);
                    }
                } catch (Exception e1) {
                    log.error("can't get field json!(sFieldName="+sFieldName + "):" + e.getMessage());
                }
            }
        }else{
            log.warn("incorrect input data: sData != null: " + "sID_Transaction=" + sID_Transaction + ", snID_Task=" + snID_Task + ", sStatus_Payment=" + sStatus_Payment);
        }

        //check variables
        //if (sData != null && (sID_Transaction == null || nID_Task == null || !PAYMENT_SUCCESS.equals(sStatus_Payment))) {
        if (sData != null && (sID_Transaction == null || sStatus_Payment == null)) {
            log.error("incorrect secondary input data: " + "sID_Transaction=" + sID_Transaction + ", nID_Task=" + snID_Task + ", sStatus_Payment=" + sStatus_Payment);
            //return;
        }

        if (nID_Task == null) {
            log.error("incorrect primary input data(BREAKED): " + "sID_Transaction=" + sID_Transaction + ", snID_Task=" + snID_Task + ", sStatus_Payment=" + sStatus_Payment);
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
            log.info("try to set sID_Payment to processInstance of task, snID_Process=" + snID_Process
                    + "sID_Transaction=" + sID_Transaction + ", sStatus_Payment=" + sStatus_Payment);
            runtimeService.setVariable(snID_Process, "sID_Payment", sID_Transaction+"_"+sStatus_Payment);
        } catch (Exception e){
            log.error("error during changing: nID_Task=" + nID_Task
                    + ", sID_Transaction=" + sID_Transaction + ", sStatus_Payment=" + sStatus_Payment, e);
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
