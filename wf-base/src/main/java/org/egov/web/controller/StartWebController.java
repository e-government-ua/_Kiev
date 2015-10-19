package org.egov.web.controller;

import org.activiti.engine.FormService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/web")
public class StartWebController {

    private final Logger log = LoggerFactory
            .getLogger(StartWebController.class);

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private FormService formService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index() {

        ModelAndView modelAndView = new ModelAndView("index");
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().latestVersion()
                .list();
        modelAndView.addObject("processList", processDefinitions);
        return modelAndView;
    }

    @RequestMapping(value = "/startForm/{id}", method = RequestMethod.GET)
    public ModelAndView startForm(@PathVariable("id") String id) {

        StartFormData sfd = formService.getStartFormData(id);

        List<FormProperty> fpList = sfd.getFormProperties();
/*		
        for(FormProperty fp : fpList) {
			System.out.println("fp_id:"+fp.getId());
			System.out.println("fp_name:"+fp.getName());
			System.out.println("fp_value:"+fp.getValue());
			System.out.println("fp_type_name:"+fp.getType().getName());
			System.out.println("fp_type_info:"+fp.getType().getInformation("values"));
	    }
		
*/

        ModelAndView modelAndView = new ModelAndView("startForm");
        modelAndView.addObject("fpList", fpList);
        modelAndView.addObject("id", id);
        return modelAndView;
    }

    @RequestMapping(value = "/startProcess/{id}", method = RequestMethod.POST)
    public ModelAndView startProcess(@PathVariable("id") String id, @RequestParam Map<String, String> params) {
        ProcessInstance pi = formService.submitStartFormData(id, params);

        ModelAndView modelAndView = new ModelAndView("startedProcess");
        modelAndView.addObject("pi", pi.getProcessInstanceId());
        modelAndView.addObject("bk", pi.getBusinessKey());
        return modelAndView;
    }

}
