package org.egov.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/test")
public class OtherController {
	@RequestMapping(value = { "/home" }, method = RequestMethod.GET)
	public ModelAndView getIndexPage() {
		ModelAndView mav = new ModelAndView("auth");
		System.out.println("test");
		return mav;
	}
}
