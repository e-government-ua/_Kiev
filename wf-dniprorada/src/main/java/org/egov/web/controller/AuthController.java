package org.egov.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wf.dp.dniprorada.service.ProcessingUser;

@Controller
@RequestMapping(value = "/auth")
public class AuthController {
	@Autowired
	ProcessingUser procUser;

	@RequestMapping(value = "/params", method = RequestMethod.POST)
	public @ResponseBody boolean getUserNameAndPassword(@RequestParam(value = "param") String authParam[]) {

		if (procUser.validateUser(authParam[0], authParam[1])) {
			//create and return session
			return true;
		} else {
			//
			return false;
		}
	}
}
