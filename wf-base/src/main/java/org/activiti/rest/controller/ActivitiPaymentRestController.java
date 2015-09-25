package org.activiti.rest.controller;			
			
import com.google.gson.Gson;			
import com.sun.mail.util.BASE64DecoderStream;			
import org.activiti.engine.HistoryService;			
import org.activiti.engine.RuntimeService;			
import org.activiti.engine.TaskService;			
import org.activiti.engine.history.HistoricVariableInstance;			
import org.apache.log4j.Logger;			
import org.springframework.beans.factory.annotation.Autowired;			
import org.springframework.stereotype.Controller;			
import org.springframework.web.bind.annotation.RequestMapping;			
import org.springframework.web.bind.annotation.RequestMethod;			
import org.springframework.web.bind.annotation.RequestParam;			
import org.springframework.web.bind.annotation.ResponseBody;			
import org.wf.dp.dniprorada.base.dao.AccessDataDao;			
import org.wf.dp.dniprorada.model.LiqpayCallbackModel;			
import org.wf.dp.dniprorada.util.GeneralConfig;			
import org.wf.dp.dniprorada.util.Mail;			
			
import javax.servlet.http.HttpServletRequest;			
			
@Controller			
public class ActivitiPaymentRestController {			
			
    public static final String LIQPAY_PAYMENT_SYSTEM = "Liqpay";			
    public static final String LIQPAY_FIELD_TRANSACTION_ID = "transaction_id";			
    public static final String LIQPAY_FIELD_PAYMENT_STATUS = "status";			
    public static final String TASK_MARK = "TaskActiviti_";			
    public static final String PAYMENT_SUCCESS = "success";			
    public static final String PAYMENT_SUCCESS_TEST = "sandbox";			
    private static final Logger log = Logger.getLogger(ActivitiPaymentRestController.class);			
    private final String sID_PaymentSystem = "Liqpay";			
//    @Autowired			
//    TaskService taskService;			
    @Autowired			
    private TaskService taskService;			
    @Autowired			
    private RuntimeService runtimeService;			
    @Autowired			
    private HistoryService historyService;			
			
	@Autowired		
	GeneralConfig generalConfig;		
			
    @Autowired			
    Mail oMail;			
    			
    @Autowired			
    AccessDataDao accessDataDao;			
    			
    @RequestMapping(value = "/setPaymentStatus_TaskActiviti", method = RequestMethod.POST, headers = { "Accept=application/json" })			
	public @ResponseBody String setPaymentStatus_TaskActiviti(		
			@RequestParam String sID_Order,
			@RequestParam String sID_PaymentSystem,
			@RequestParam String sData,
			@RequestParam(value = "sPrefix", required = false) String sPrefix,
			@RequestParam(value = "data", required = false) String data,
			@RequestParam(value = "signature", required = false) String signature,
			HttpServletRequest request
			) throws Exception{
            			
            if(sPrefix==null){			
                sPrefix="";			
            }			
            			
            String URI = request.getRequestURI() + "?" + request.getQueryString();			
            log.info("/setPaymentStatus_TaskActiviti");			
            			
            log.info("sID_Order="+sID_Order);			
            log.info("sID_PaymentSystem="+sID_PaymentSystem);			
            log.info("sData="+sData);			
            log.info("sPrefix="+sPrefix);			
            			
            log.info("data="+data);			
            log.info("signature="+signature);			
            log.info("URI="+URI);			
            String sDataDecoded = null;			
            			
            try{			
                if(data != null){			
                    sDataDecoded = new String(BASE64DecoderStream.decode(data.getBytes()));			
                    log.info("sDataDecoded="+sDataDecoded);			
                }			
                setPaymentStatus(sID_Order, sDataDecoded, sID_PaymentSystem, sPrefix);			
                //setPaymentStatus(sID_Order, null, sID_PaymentSystem);                			
            }catch(Exception oException){			
                log.error("/setPaymentStatus_TaskActiviti", oException);			
                String snID_Subject="0";			
                String sAccessKey=null;			
                try{			
                    //sAccessKey = accessDataDao.setAccessData(snID_Subject);			
                    sAccessKey = accessDataDao.setAccessData(URI);			
                }catch(Exception oException1){			
                    log.error("/setPaymentStatus_TaskActiviti:sAccessKey=", oException1);			
                }			
			
                //generalConfig.sHost() + "/wf/service/setPaymentStatus_TaskActiviti_Direct?sID_Order="+sID_Order+"&sID_PaymentSystem="+sID_PaymentSystem+"&sData=&sID_Transaction=&sStatus_Payment="            			
                String sURL = new StringBuilder(generalConfig.sHost())			
                        .append("/wf/service/setPaymentStatus_TaskActiviti_Direct?")			
                        .append("sID_Order=").append(sID_Order)			
                        .append("&sID_PaymentSystem=").append(sID_PaymentSystem)			
                        .append("&sData=").append("")			
                        .append("sAccessContract=").append("Request")			
                        .append("nID_Subject=").append(snID_Subject)			
                        .append("&sAccessKey=").append(sAccessKey)			
                        .toString();			
			
                    String sFormHTML = new StringBuilder()			
                            .append("<form method=\"GET\" action=\"")//POST			
                            .append(sURL)			
                            .append("\" ")			
                            .append("accept-charset=\"utf-8\">")			
                            .append("<input type=\"text\" name=\"sID_Transaction\" value=\"\"/>")			
                            .append("<input type=\"text\" name=\"sStatus_Payment\" value=\"\"/>")			
                            .append("<input type=\"submit\" value=\"��������� ������ ����������!\"/>")			
                            .append("</form>").toString();			
			
			
                    String saToMail = "bvv4ik@gmail.com,dmitrij.zabrudskij@privatbank.ua";			
                    String sHead = (generalConfig.bTest()?"(test)":"(PROD)")+"/setPaymentStatus_TaskActiviti:������ ��� ������� �������� ��������� ���������� � ������� � ��������-������!";			
                    String sBody = "oException.getMessage()="+oException.getMessage()+"<br>" +			
                                    "<br>" +			
			
                                    "sID_Order="+sID_Order+"<br>"+			
                                    "sID_PaymentSystem="+sID_PaymentSystem+"<br>"+			
                                    "sData="+sData+"<br>"+			
                                    "data="+data+"<br>"+			
                                    "signature="+signature+"<br>"+			
                                    "<br>" +			
                                    "���� ������� �������� ��� �������� �������:<br>" +			
                                    sFormHTML + "<br>" +			
                                    "<br>" +			
                                    "���� �� ����������, ��������� �� <a href=\"" + sURL + "&sID_Transaction=&sStatus_Payment=" + "\" target=\"_top\">���� ������</a>, � ���������� ������� �� ����������(sID_Transaction) � ������(sStatus_Payment). �����, ��� ������������� ��������������� ����� � ������ (����� �������� � ������)<br>" +			
                                    "(" + sURL + "&sID_Transaction=&sStatus_Payment=" + ")<br>" +			
                                    "<br>"			
                                    ;			
                    oMail			
                    .reset();			
                    oMail			
                    //._From(mailAddressNoreplay)			
                    ._To(saToMail)			
                    ._Head(sHead)			
                    ._Body(sBody)			
                    ;                			
                    oMail.send();			
                    throw oException;			
                }			
            return sData;			
	}		
			
    @RequestMapping(value = "/setPaymentStatus_TaskActiviti_Direct", method = RequestMethod.GET, headers = { "Accept=application/json" })			
	public @ResponseBody String setPaymentStatus_TaskActiviti_Direct(		
			@RequestParam String sID_Order,
			@RequestParam String sID_PaymentSystem,
			@RequestParam String sData,
			@RequestParam(value = "sPrefix", required = false) String sPrefix,
                        			
			//@RequestParam String snID_Task,
			@RequestParam String sID_Transaction,
			@RequestParam String sStatus_Payment
                        			
			) throws Exception{
			
            if(sPrefix==null){			
                sPrefix="";			
            }			
            			
            log.info("/setPaymentStatus_TaskActiviti_Direct");			
            log.info("sID_Order="+sID_Order);			
            log.info("sID_PaymentSystem="+sID_PaymentSystem);			
            log.info("sData="+sData);			
            log.info("sPrefix="+sPrefix);			
			
            log.info("sID_Transaction="+sID_Transaction);			
            log.info("sStatus_Payment="+sStatus_Payment);			
			
            //String snID_Task=sID_Order;			
            			
            Long nID_Task = null;			
            try {			
                if (sID_Order.contains(TASK_MARK)) {			
                    nID_Task = Long.decode(sID_Order.replace(TASK_MARK, ""));			
                }			
            } catch (NumberFormatException e) {			
                log.error("incorrect sID_Order! can't invoke task_id: " + sID_Order);			
            }			
            String snID_Task = ""+nID_Task;			
            log.info("snID_Task="+snID_Task);			
			
            if("Liqpay".equals(sID_PaymentSystem)){			
                setPaymentTransaction_ToActiviti(snID_Task, sID_Transaction, sStatus_Payment, sPrefix);			
                sData="Ok";			
            }else{			
                sData="Fail";			
            }			
            //sID_Order=TaskActiviti_105123&sID_PaymentSystem=Liqpay&sData=&nID_Subject=25447			
            return sData;			
	}                		
        			
        			
    /*@RequestMapping(value = "/setPaymentStatus_TaskActiviti_", method = RequestMethod.POST, headers = { "Accept=application/json" })			
	public @ResponseBody String setPaymentStatus_TaskActiviti(		
			@RequestParam String sID_Order,
			@RequestParam String sID_PaymentSystem,
			@RequestParam String sData,
			@RequestParam(value = "sPrefix", required = false) String sPrefix,
                        			
			@RequestParam byte[] data,
			@RequestParam byte[] signature
			) throws Exception{
			
            log.info("sID_Order="+sID_Order);			
            log.info("sID_PaymentSystem="+sID_PaymentSystem);			
            log.info("sData="+sData);			
            log.info("data="+data);			
            log.info("signature="+signature);			
            String sDataDecoded = new String(BASE64DecoderStream.decode(data));			
            log.info("sDataDecoded="+sDataDecoded);			
            setPaymentStatus(sID_Order, sDataDecoded, sID_PaymentSystem, sPrefix);			
            return sData;			
	}		
			
    @RequestMapping(value = "/setPaymentStatus_TaskActiviti2/", method = RequestMethod.POST, headers = { "Accept=application/json" })			
	public @ResponseBody String setPaymentStatusNew_TaskActiviti(		
			@RequestParam byte[] data,
			@RequestParam byte[] signature
			) throws Exception{
			
            log.info("data="+data);			
            log.info("signature="+signature);			
			
            String sFullData = new String(BASE64DecoderStream.decode(data));			
            log.info("sFullData="+sFullData);			
            Gson gson = new Gson();			
            LiqpayCallbackModel liqpayCallback = gson.fromJson(sFullData, LiqpayCallbackModel.class);			
            //log.info("sID_PaymentSystem="+sID_PaymentSystem);			
            log.info("liqpayCallback.getOrder_id()="+liqpayCallback.getOrder_id());			
            setPaymentStatus(liqpayCallback.getOrder_id(), sFullData, sID_PaymentSystem, sPrefix);			
            return sFullData;			
	}*/		
			
    private void setPaymentStatus(String sID_Order, String sData, String sID_PaymentSystem, String sPrefix) throws Exception {			
        if (!LIQPAY_PAYMENT_SYSTEM.equals(sID_PaymentSystem)) {			
            log.error("not liqpay system");			
            throw new Exception("not liqpay system");			
            //return;			
        }			
			
        log.info("sData=" + sData);			
			
        Long nID_Task = null;			
        try {			
            if (sID_Order.contains(TASK_MARK)) {			
                log.info("sID_Order(1)=" + sID_Order);			
                String s = sID_Order.replace(TASK_MARK, "");			
                log.info("sID_Order(2)=" + s);			
                if(sPrefix!=null && !"".equals(sPrefix.trim()) && s.endsWith(sPrefix)){			
                    s=s.substring(0, s.length() - sPrefix.length());			
                }			
                log.info("sID_Order(3)=" + s);			
                nID_Task = Long.decode(s);			
                log.info("nID_Task=" + nID_Task);			
                //nID_Task = Long.decode(sID_Order.replace(TASK_MARK, ""));			
            }			
        } catch (NumberFormatException e) {			
            log.error("incorrect sID_Order! can't invoke task_id: " + sID_Order);			
        }			
        String snID_Task = ""+nID_Task;			
			
        //https://test.region.igov.org.ua/wf/service/setPaymentStatus_TaskActiviti0?sID_Order=TaskActiviti_1485001&sID_PaymentSystem=Liqpay&sData=&nID_Subject=20045&sAccessKey=b32d9855-dce0-44df-bbe0-dd0e41958cde			
        //data=eyJwYXltZW50X2lkIjo2MzQ0NDcxOCwidHJhbnNhY3Rpb25faWQiOjYzNDQ0NzE4LCJzdGF0dXMiOiJzYW5kYm94IiwidmVyc2lvbiI6MywidHlwZSI6ImJ1eSIsInB1YmxpY19rZXkiOiJpMTAxNzI5NjgwNzgiLCJhY3FfaWQiOjQxNDk2Mywib3JkZXJfaWQiOiJUYXNrQWN0aXZpdGlfMTQ4NTAwMSIsImxpcXBheV9vcmRlcl9pZCI6IjQwMXUxNDM3MzI1MDIyMTgzMzAzIiwiZGVzY3JpcHRpb24iOiLQotC10YHRgtC+0LLQsNGPINGC0YDQsNC90LfQsNC60YbQuNGPIiwic2VuZGVyX3Bob25lIjoiMzgwOTc5MTM4MDA3IiwiYW1vdW50IjowLjAxLCJjdXJyZW5jeSI6IlVBSCIsInNlbmRlcl9jb21taXNzaW9uIjowLjAsInJlY2VpdmVyX2NvbW1pc3Npb24iOjAuMCwiYWdlbnRfY29tbWlzc2lvbiI6MC4wLCJhbW91bnRfZGViaXQiOjAuMDEsImFtb3VudF9jcmVkaXQiOjAuMDEsImNvbW1pc3Npb25fZGViaXQiOjAuMCwiY29tbWlzc2lvbl9jcmVkaXQiOjAuMCwiY3VycmVuY3lfZGViaXQiOiJVQUgiLCJjdXJyZW5jeV9jcmVkaXQiOiJVQUgiLCJzZW5kZXJfYm9udXMiOjAuMCwiYW1vdW50X2JvbnVzIjowLjB9			
        //signature=z77CQeBn3Z75n5UpJqXKG+KjZyI=			
        String sID_Transaction = "Pay_"+snID_Task;			
        String sStatus_Payment = null;			
        //parse sData			
        if(sData != null){			
            try {			
                /*			
                //Map<String, Object> json = (Map<String, Object>) JSON.parse(sData);			
                Map<String, Object> json = new Gson().fromJson(sData, HashMap.class);			
                sID_Transaction = (String) json.get(LIQPAY_FIELD_TRANSACTION_ID);			
                sStatus_Payment = (String) json.get(LIQPAY_FIELD_PAYMENT_STATUS);			
                */			
                Gson oGson = new Gson();			
                LiqpayCallbackModel oLiqpayCallbackModel = oGson.fromJson(sData, LiqpayCallbackModel.class);			
                //log.info("sID_PaymentSystem="+sID_PaymentSystem);			
                log.info("oLiqpayCallbackModel.getOrder_id()="+oLiqpayCallbackModel.getOrder_id());			
                sID_Transaction = oLiqpayCallbackModel.getTransaction_id();			
                log.info("oLiqpayCallbackModel.getTransaction_id()="+sID_Transaction);			
                sStatus_Payment = oLiqpayCallbackModel.getStatus();			
                log.info("oLiqpayCallbackModel.getStatus()="+sStatus_Payment);			
            } catch (Exception e) {			
                log.error("can't parse json! reason:" + e.getMessage());			
                throw new Exception("can't parse json! reason:" + e.getMessage());			
                /*int nAt;			
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
                }*/			
            }			
        }else{			
            log.warn("incorrect input data: sData == null: " + "snID_Task=" + snID_Task			
                    + ", sID_Transaction=" + sID_Transaction + ", sStatus_Payment=" + sStatus_Payment);			
        }			
			
        //check variables			
        //if (sData != null && (sID_Transaction == null || nID_Task == null || !PAYMENT_SUCCESS.equals(sStatus_Payment))) {			
        if (sData != null && (sID_Transaction == null || sStatus_Payment == null)) {			
            log.error("incorrect secondary input data: " + "nID_Task=" + snID_Task			
                    + ", sID_Transaction=" + sID_Transaction + ", sStatus_Payment=" + sStatus_Payment);			
        }			
			
        if (sData != null && !PAYMENT_SUCCESS.equals(sStatus_Payment) && !PAYMENT_SUCCESS_TEST.equals(sStatus_Payment)) {			
            log.error("incorrect sStatus_Payment: " + "nID_Task=" + snID_Task			
                    + ", sID_Transaction=" + sID_Transaction + ", sStatus_Payment=" + sStatus_Payment);			
        }			
			
        if (nID_Task == null) {			
            log.error("incorrect primary input data(BREAKED): " + "snID_Task=" + snID_Task			
                     + ", sID_Transaction=" + sID_Transaction + ", sStatus_Payment=" + sStatus_Payment);			
            //return;			
            throw new Exception("incorrect primary input data(BREAKED): " + "snID_Task=" + snID_Task			
                     + ", sID_Transaction=" + sID_Transaction + ", sStatus_Payment=" + sStatus_Payment);			
        }			
        			
        			
        setPaymentTransaction_ToActiviti(snID_Task, sID_Transaction, sStatus_Payment, sPrefix);			
    }			
    private void setPaymentTransaction_ToActiviti(String snID_Task, String sID_Transaction, String sStatus_Payment, String sPrefix) throws Exception{			
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
            			
//TODO ����������� ������ �������� �� �������� � �� �����			
            String snID_Process = snID_Task;			
            String sID_Payment = sID_Transaction+"_"+sStatus_Payment;			
            log.info("try to set: sID_Payment="+sID_Payment);			
            runtimeService.setVariable(snID_Process, "sID_Payment"+sPrefix, sID_Payment);			
            log.info("completed set sID_Payment"+sPrefix+"="+sID_Payment+" to: snID_Process=" + snID_Process);			
        } catch (Exception e){			
            log.error("during changing: snID_Task=" + snID_Task			
                    + ", sID_Transaction=" + sID_Transaction + ", sStatus_Payment=" + sStatus_Payment, e);			
            throw e;			
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
}			
