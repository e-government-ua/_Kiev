package org.activiti.rest.controller;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.rest.controller.entity.InitRequestI;
import org.activiti.rest.controller.entity.InitResponse;
import org.activiti.rest.controller.entity.InitResponseI;
import org.activiti.rest.service.api.runtime.process.ExecutionBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ...wf-dniprorada/service/...
 * Example:
 * .../wf-dniprorada/service/rest/startProcessByKey/citizensRequest
 * 
 * @author Tereshchenko
 *
 */
@Controller
@RequestMapping(value="/rest")
public class ActivitiRestApiController extends ExecutionBaseResource{
	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(ActivitiRestApiController.class);
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private RepositoryService repositoryService;

    @RequestMapping(value = "/init", method = RequestMethod.POST)
    public @ResponseBody InitResponseI init(@RequestBody InitRequestI initRequest) throws ActivitiRestException {

        return new InitResponse(null);
    }
	
	@RequestMapping(value = "/startProcessByKey/{key}", method = RequestMethod.GET)
	@Transactional
	public @ResponseBody String startProcess(@PathVariable("key") String key) {
		ProcessInstance pi = runtimeService.startProcessInstanceByKey(key);
		if (pi == null || pi.getId() == null) {
			throw new IllegalArgumentException(String.format(
					"process did not started by key:{%s}", key));
		}
		return pi.getProcessInstanceId();
	}

	@RequestMapping(value = "/getTasks/{assignee}", method = RequestMethod.GET)
	@Transactional
	public List<Task> getTasks(@PathVariable("assignee") String assignee) {
		return taskService.createTaskQuery().taskAssignee(assignee).list();
	}

	
	@RequestMapping(value = "/getProcessDefinitions", method = RequestMethod.GET)
	@Transactional
	public List<ProcessDefinition> getProcessDefinitions() {
		return repositoryService.createProcessDefinitionQuery().latestVersion().list();
    }
}
