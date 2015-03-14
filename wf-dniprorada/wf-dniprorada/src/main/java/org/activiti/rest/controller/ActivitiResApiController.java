package org.activiti.rest.controller;

import java.util.List;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * comom prefix for rest:
 * ...wf-dniprorada/service/...
 * 
 * @author Tereshchenko
 *
 */
@Controller
@Service
public class ActivitiResApiController {
	private final Logger log = LoggerFactory
			.getLogger(ActivitiResApiController.class);

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@RequestMapping(value = "/startProcessByKey/{key}", method = RequestMethod.GET)
	@Transactional
	public @ResponseBody String startProcess(@PathVariable("key") String key) {
		ProcessInstance pi = runtimeService.startProcessInstanceByKey(key);

		if (pi == null || pi.getId() == null) {
			throw new IllegalArgumentException(String.format(
					"process key:{%s} did not start", key));
		}

		return pi.getProcessInstanceId();

	}

	@RequestMapping(value = "/getTasks/{assignee}", method = RequestMethod.GET)
	@Transactional
	public @ResponseBody List<Task> getTasks(
			@PathVariable("assignee") String assignee) {
		return taskService.createTaskQuery().taskAssignee(assignee).list();
	}

}
