package org.egov.web.controller;

import javax.servlet.http.HttpServletRequest;

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
	public @ResponseBody boolean getUserNameAndPassword(@RequestParam(value = "sLogin") String login, @RequestParam(value = "sPassword") String password,HttpServletRequest request) {

		if (procUser.validateUser(login, password)) {
			request.getSession(true);
			return true;
		} else {
			//
			return false;
		}
	}
}
