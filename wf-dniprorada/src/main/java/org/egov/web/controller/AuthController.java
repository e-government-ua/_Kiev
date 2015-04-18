package org.egov.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wf.dp.dniprorada.service.ProcessingUser;

@Controller
@RequestMapping(value = "/auth")
public class AuthController {
	@Autowired
	ProcessingUser procUser;

	@RequestMapping(value = "/params/{sLogin}/{sPassword}", method = RequestMethod.GET)
	public @ResponseBody boolean getUserNameAndPassword(@PathVariable String sLogin,
			@PathVariable String sPassword) {

		if (procUser.validateUser(sLogin, sPassword)) {
			//create and return session
			return true;
		} else {
			//
			return false;
		}
	}
}
