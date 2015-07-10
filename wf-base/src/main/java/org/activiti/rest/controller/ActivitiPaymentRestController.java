package org.activiti.rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ActivitiPaymentRestController {
	@RequestMapping(value = "/setPaymentStatus_TaskActiviti", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public @ResponseBody String setPaymentStatus_TaskActiviti(
			@RequestParam String sID_Order,
			@RequestParam String sData,
			@RequestParam String sID_PaymentSystem){
		return "/";
	}
}
