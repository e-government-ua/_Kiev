package org.activiti.rest.controller;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.rest.controller.adapter.ProcDefinitionAdapter;
import org.activiti.rest.controller.adapter.TaskAssigneeAdapter;
import org.activiti.rest.controller.entity.*;
import org.activiti.rest.controller.entity.Process;
import org.activiti.rest.service.api.runtime.process.ExecutionBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

	@RequestMapping(value = "/start-process/{key}", method = RequestMethod.GET)
	@Transactional
	public @ResponseBody ProcessI startProcessByKey(@PathVariable("key") String key) {
		ProcessInstance pi = runtimeService.startProcessInstanceByKey(key);
		if (pi == null || pi.getId() == null) {
			throw new IllegalArgumentException(String.format(
					"process did not started by key:{%s}", key));
		}
		return new Process(pi.getProcessInstanceId());
	}

	@RequestMapping(value = "/tasks/{assignee}", method = RequestMethod.GET)
	@Transactional
	public @ResponseBody List<TaskAssigneeI> getTasksByAssignee(@PathVariable("assignee") String assignee) {
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(assignee).list();
        List<TaskAssigneeI> facadeTasks = new ArrayList<>();
        TaskAssigneeAdapter adapter = new TaskAssigneeAdapter();
        for (Task task : tasks) {
            facadeTasks.add(adapter.apply(task));
        }
        return facadeTasks;
    }

	
	@RequestMapping(value = "/process-definitions", method = RequestMethod.GET)
	@Transactional
	public @ResponseBody List<ProcDefinitionI> getProcessDefinitions() {
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().latestVersion().list();
        List<ProcDefinitionI> procDefinitions = new ArrayList<>();
        ProcDefinitionAdapter adapter = new ProcDefinitionAdapter();
        for (ProcessDefinition processDefinition : processDefinitions) {
            procDefinitions.add(adapter.apply(processDefinition));
        }
        return procDefinitions;
    }
}
