package org.activiti.rest.controller;

import com.mongodb.util.JSON;
import org.activiti.engine.TaskService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

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

	@RequestMapping(value = "/setPaymentStatus_TaskActiviti", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public @ResponseBody String setPaymentStatus_TaskActiviti(
			@RequestParam String sID_Order,
			@RequestParam String sData,
			@RequestParam String sID_PaymentSystem){


        //setPaymentStatus(sID_Order, sData, sID_PaymentSystem);

		return "/";
	}

    private void setPaymentStatus(String sID_Order, String sData, String sID_PaymentSystem) {
        if (!LIQPAY_PAYMENT_SYSTEM.equals(sID_PaymentSystem))
            return;
        //parse sData
        Map<String, Object> json = (Map<String, Object>) JSON.parse(sData);
        String transaction_id = (String) json.get(LIQPAY_FIELD_TRANSACTION_ID);
        String sStatus_Payment = (String) json.get(LIQPAY_FIELD_PAYMENT_STATUS);
        Long nID_Task = null;
        if(sID_Order.contains(TASK_MARK)){
            nID_Task = Long.decode(sID_Order.replace(TASK_MARK,""));
        }
        //check variables
        if (transaction_id == null || nID_Task == null
                || !PAYMENT_SUCCESS.equals(sStatus_Payment))
            //throw new IllegalArgumentException("incorrect input data");
            return;
        //save info to task
        Map<String, Object> variables = new HashMap<>();
        variables.put("sID_Payment", transaction_id);
        taskService.setVariables(""+nID_Task,variables );

    }
}
