package org.activiti.rest.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;
import org.activiti.redis.exception.RedisException;
import org.activiti.redis.service.RedisService;
import org.activiti.rest.controller.adapter.AttachmentEntityAdapter;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.wf.dp.dniprorada.model.ByteArrayMultipartFile;

/**
 * ...wf-region/service/...
 * Example:
 * .../wf-region/service/rest/startProcessByKey/citizensRequest
 *
 * @author Tereshchenko
 */
@Controller
@RequestMapping(value = "/restOldVersion")
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
    private RedisService redisService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private IdentityService identityService;

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

    @RequestMapping(value = "/file/upload_file_to_redis", method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    String putAttachmentsToRedis(@RequestParam("file") MultipartFile file) throws ActivitiIOException  {
    	String atachId = null;
		try {
			atachId = redisService.putAttachments(file.getBytes());
		}catch (RedisException e) {
			 throw new ActivitiIOException(ActivitiIOException.Error.REDIS_ERROR,e.getMessage());
		} catch (IOException e) {
			throw new ActivitiIOException(ActivitiIOException.Error.REDIS_ERROR,e.getMessage());
		}
		return atachId;
    }
    
    @RequestMapping(value = "/file/download_file_from_redis", method = RequestMethod.GET)
    @Transactional
    public
    @ResponseBody
    byte[] getAttachmentsFromRedis(@RequestParam("key") String key) throws ActivitiIOException  {
    	byte[] upload =null;
    	try {
    		upload =  redisService.getAttachments(key);
		} catch (RedisException e) {
			throw new ActivitiIOException(ActivitiIOException.Error.REDIS_ERROR,e.getMessage());
		}
		return upload;
    }

    
    @RequestMapping(value = "/file/download_file_from_db", method = RequestMethod.GET)
    @Transactional
    public @ResponseBody
    byte[] getAttachmentFromDb(@RequestParam("taskId") String taskId, 
    		             //@RequestParam("attachmentId") String attachmentId,
    		             HttpServletRequest request, HttpServletResponse httpResponse) throws IOException {
    	
    	//Получаем по задаче ид процесса
    	HistoricTaskInstance historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
        		.taskId(taskId).singleResult(); 
    	String processInstanceId = historicTaskInstanceQuery.getProcessInstanceId();
    	if (processInstanceId == null) {
            throw new ActivitiObjectNotFoundException(
                    "ProcessInstanceId for taskId '" + taskId + "' not found.",
                    Attachment.class);
        }
    	
        //Выбираем по процессу прикрепленные файлы
        List<Attachment> attachments = taskService.getProcessInstanceAttachments(processInstanceId);
        Attachment attachmentRequested = null;
        for (Attachment attachment : attachments) {
            //if (attachment.getId().equalsIgnoreCase(attachmentId)) {
                attachmentRequested = attachment;
            //}
        }

        if (attachmentRequested == null) {
            throw new ActivitiObjectNotFoundException(
                    "Attachment for taskId '" + taskId + "' not found.",
                    Attachment.class);
        }

        InputStream attachmentStream = taskService.getAttachmentContent(attachmentRequested.getId());
        if (attachmentStream == null) {
            throw new ActivitiObjectNotFoundException(
                    "Attachment for taskId '" + taskId + "' doesn't have content associated with it.",
                    Attachment.class);
        }

        //Вычитывем из потока массив байтов контента и помещаем параметры контента в header 
        ByteArrayMultipartFile multipartFile = new ByteArrayMultipartFile(attachmentStream, 
        	     attachmentRequested.getName(), attachmentRequested.getName(), attachmentRequested.getType()); 
        httpResponse.setHeader("Content-disposition", "attachment; filename=" + multipartFile.getName() + "." + multipartFile.getExp());
        httpResponse.setHeader("Content-Type", multipartFile.getContentType() + ";charset=UTF-8");
        httpResponse.setContentLength(multipartFile.getBytes().length);
      
        return multipartFile.getBytes();

    }

    /**
     * прикрепляем к таске Attachment.
     * @param file
     * @return
     * @throws org.activiti.rest.controller.ActivitiIOException
     */
    @RequestMapping(value = "/file/upload_file_as_attachment", method = RequestMethod.POST, produces = "application/json")
    @Transactional
    public
    @ResponseBody
    AttachmentEntityI putAttachmentsToExecution(@RequestParam(value = "taskId") String taskId,
                                                @RequestParam("file") MultipartFile file,
                                                @RequestParam(value = "description") String description) throws ActivitiIOException, Exception {

        String processInstanceId = null;
        String assignee = null;

        List<Task> tasks = taskService.createTaskQuery().taskId(taskId).list();
        if (tasks != null && !tasks.isEmpty()) {
            Task task = tasks.iterator().next();
            processInstanceId = task.getProcessInstanceId();
            assignee = task.getAssignee() != null ? task.getAssignee() : "kermit";
            System.out.println("processInstanceId: " + processInstanceId + " taskId: " + taskId + "assignee: " + assignee);
        } else {
            System.out.println("There is no tasks at all!");
        }

        identityService.setAuthenticatedUserId(assignee);

        System.out.println("FileExtention: " + getFileExtention(file) + " fileContentType: " + file.getContentType() + "fileName: " + file.getOriginalFilename());
        System.out.println("description: " + description);

        Attachment attachment = taskService.createAttachment(file.getContentType()
                        + ";"
                        + getFileExtention(file),
                taskId,
                processInstanceId,
                file.getOriginalFilename(),
                description, file.getInputStream());

        AttachmentEntityAdapter adapter = new AttachmentEntityAdapter();

        return adapter.apply(attachment);
    }

    private String getFileExtention(MultipartFile file) {

        String[] parts = file.getOriginalFilename().split("\\.");
        if (parts.length != 0) {
            return parts[parts.length - 1];
        }

        return "";
    }
}
