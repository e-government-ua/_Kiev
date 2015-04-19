package org.activiti.rest.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;
import org.activiti.rest.controller.adapter.ProcDefinitionAdapter;
import org.activiti.rest.controller.adapter.TaskAssigneeAdapter;
import org.activiti.rest.controller.entity.ProcDefinitionI;
import org.activiti.rest.controller.entity.Process;
import org.activiti.rest.controller.entity.ProcessI;
import org.activiti.rest.controller.entity.TaskAssigneeI;
import org.activiti.rest.service.api.runtime.process.ExecutionBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.wf.dp.dniprorada.model.ByteArrayMultipartFile;

/**
 * ...wf-dniprorada/service/...
 * Example:
 * .../wf-dniprorada/service/rest/startProcessByKey/citizensRequest
 *
 * @author Tereshchenko
 */
@Controller
@RequestMapping(value = "/rest")
public class ActivitiRestApiController extends ExecutionBaseResource {

    @SuppressWarnings("unused")
    private final Logger log = LoggerFactory.getLogger(ActivitiRestApiController.class);
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RedisTemplate<String, byte[]> template;

    @RequestMapping(value = "/start-process/{key}", method = RequestMethod.GET)
    @Transactional
    public
    @ResponseBody
    ProcessI startProcessByKey(@PathVariable("key") String key) {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(key);
        if (pi == null || pi.getId() == null) {
            throw new IllegalArgumentException(String.format(
                    "process did not started by key:{%s}", key));
        }
        return new Process(pi.getProcessInstanceId());
    }

    @RequestMapping(value = "/tasks/{assignee}", method = RequestMethod.GET)
    @Transactional
    public
    @ResponseBody
    List<TaskAssigneeI> getTasksByAssignee(@PathVariable("assignee") String assignee) {
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
    public
    @ResponseBody
    List<ProcDefinitionI> getProcessDefinitions() {
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().latestVersion().list();
        List<ProcDefinitionI> procDefinitions = new ArrayList<>();
        ProcDefinitionAdapter adapter = new ProcDefinitionAdapter();
        for (ProcessDefinition processDefinition : processDefinitions) {
            procDefinitions.add(adapter.apply(processDefinition));
        }
        return procDefinitions;
    }

    @RequestMapping(value = "/file/upload", method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    String putAttachments(@RequestParam("file") MultipartFile file) throws IOException {
        String key = UUID.randomUUID().toString();
        while (template.hasKey(key)) {
            key = UUID.randomUUID().toString();
        }
        template.boundValueOps(key).set(file.getBytes());
        return key;
    }

    
    @RequestMapping(value = "/file/download", method = RequestMethod.GET)
    @Transactional
    public @ResponseBody
    byte[] getAttachment(@RequestParam("taskId") String taskProcessInstanceId, @RequestParam("attachmentId") String attachmentId,
    		HttpServletRequest request, HttpServletResponse httpResponse) throws IOException {
        
        //Выбираем по задаче прикрепленные файлы и ищем запрашиваемый
        List<Attachment> attachments = taskService.getProcessInstanceAttachments(taskProcessInstanceId);
        Attachment attachmentRequested = null;
        for (Attachment attachment : attachments) {
            if (attachment.getId().equalsIgnoreCase(attachmentId)) {
                attachmentRequested = attachment;
            }
        }

        if (attachmentRequested == null) {
            throw new ActivitiObjectNotFoundException(
                    "Attachment with id '" + attachmentId + "' not found.",
                    Attachment.class);
        }

        InputStream attachmentStream = taskService.getAttachmentContent(attachmentRequested.getId());
        if (attachmentStream == null) {
            throw new ActivitiObjectNotFoundException(
                    "Attachment with id '" + attachmentId
                    + "' doesn't have content associated with it.",
                    Attachment.class);
        }

        //Вычитывем из потока массив байтов контента и помещаем параметры файла в header 
        ByteArrayMultipartFile multipartFile = new ByteArrayMultipartFile(attachmentStream, 
        	     attachmentRequested.getName(), attachmentRequested.getName(), attachmentRequested.getType()); 
        httpResponse.setHeader("Content-disposition", "attachment");
        httpResponse.setHeader("filename", multipartFile.getName());
        httpResponse.setHeader("exp", multipartFile.getExp());
        httpResponse.setContentType("." + multipartFile.getContentType());
        httpResponse.setContentLength(multipartFile.getBytes().length);
      
        return multipartFile.getBytes();

    }
}
