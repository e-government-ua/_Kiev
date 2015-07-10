package org.activiti.rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wf.dp.dniprorada.util.Util;

@Controller
public class ActivitiPaymentLiqpayController {
	
	private final Logger log = LoggerFactory.getLogger(ActivitiPaymentLiqpayController.class);
	private StringBuffer sb = new StringBuffer();
	
	@RequestMapping(value="/setPaymentNewStatus_Liqpay", method = RequestMethod.GET, headers = {"Accept=application/json"})
	public @ResponseBody String setPaymentNewStatus_Liqpay(
			@RequestParam String sID_Order,
			@RequestParam String sHost) {
		sb.append(sHost);
		String data = "data"; //вместо "data" подставить ответ вызова API liqpay 
		sb.append("sID_Order="+sID_Order);
		sb.append("&sData="+data); 
		sb.append("&sID_PaymentSystem=Liqpay");
		
		if(sID_Order.startsWith("TaskActiviti_")){
			setPaymentStatus_TaskActiviti(sHost, sb.toString(), data);
		}
		return "/";
	}
	
	private String setPaymentStatus_TaskActiviti(String sHost, String url, String sData){
		String answer = null; 
		try {
			answer = Util.httpAnswer(sb.toString(), sData);
		} catch (Exception e) {
			log.error("HttpAnswer error:", e);
		}
		return answer;
	}
}
