package org.activiti.rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/setPaymentStatus_TaskActiviti", method = RequestMethod.GET)
public class ActivitiPaymentRestController {
	public @ResponseBody String setPaymentStatus_TaskActiviti(
			@RequestParam String sID_Order,
			@RequestParam String sData,
			@RequestParam String sID_PaymentSystem){
		return "/";
	}
}
