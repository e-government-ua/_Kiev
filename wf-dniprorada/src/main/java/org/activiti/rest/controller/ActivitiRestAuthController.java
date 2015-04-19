package org.activiti.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.activiti.rest.service.api.ProcessingUser;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/auth")
public class ActivitiRestAuthController {

    @Autowired
	private ProcessingUser procUser;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
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
