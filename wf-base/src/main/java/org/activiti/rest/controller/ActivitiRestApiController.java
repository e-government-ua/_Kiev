package org.activiti.rest.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;
import org.activiti.redis.exception.RedisException;
import org.activiti.redis.service.RedisService;
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
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.wf.dp.dniprorada.base.model.AbstractModelTask;
import org.wf.dp.dniprorada.engine.task.FileTaskUpload;
import org.wf.dp.dniprorada.model.BuilderAtachModel;
import org.wf.dp.dniprorada.model.ByteArrayMultipartFileOld;

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
    private RedisService redisService;
    @Autowired
    private HistoryService historyService;

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


    /**
     * Укладываем в редис multipartFileToByteArray 
     * @param file
     * @return
     * @throws org.activiti.rest.controller.ActivitiIOException
     */
    @RequestMapping(value = "/file/upload_file_to_redis", method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    String putAttachmentsToRedis(@RequestParam("file") MultipartFile file) throws ActivitiIOException, Exception  {
    	String atachId = null;
		try {
			atachId = redisService.putAttachments(AbstractModelTask.multipartFileToByteArray(file).toByteArray());
		//}catch (RedisException e) {
		}catch (Exception e) {
			 //throw new ActivitiIOException(ActivitiIOException.Error.REDIS_ERROR,e.getMessage());
			 throw e;
		/*} catch (IOException e) {
			throw new ActivitiIOException(ActivitiIOException.Error.REDIS_ERROR,e.getMessage());*/
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


    /**
     * Получение Attachment средствами активити из таблицы ACT_HI_ATTACHMENT
     * @param taskId
     * @param attachmentId
     * @param nFile
     * @param request
     * @param httpResponse
     * @return
     * @throws java.io.IOException
     */
    @RequestMapping(value = "/file/download_file_from_db", method = RequestMethod.GET)
    @Transactional
    public @ResponseBody
    byte[] getAttachmentFromDb(@RequestParam(value = "taskId") String taskId,
                               @RequestParam(required = false, value = "attachmentId") String attachmentId,
                               @RequestParam(required = false, value = "nFile") Integer nFile,
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
        for (int i = 0; i < attachments.size(); i++) {
            if (attachments.get(i).getId().equalsIgnoreCase(attachmentId)) {
                attachmentRequested = attachments.get(i);
                break;
            }
            if (null != nFile && nFile.equals(i+1)) {
                attachmentRequested = attachments.get(i);
                break;
            }
        }

        if (attachmentRequested == null && !attachments.isEmpty()) {
            attachmentRequested = attachments.get(0);
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
		ByteArrayMultipartFileOld multipartFile = new ByteArrayMultipartFileOld(
				attachmentStream, attachmentRequested.getDescription(),
				attachmentRequested.getName(), attachmentRequested.getType());

        //httpResponse.setHeader("Content-disposition", "attachment; filename=" + composeFileName(multipartFile));
        //httpResponse.setHeader("Content-Type", multipartFile.getContentType() + ";charset=UTF-8");
        httpResponse.setHeader("Content-disposition", "attachment; filename=" + attachmentRequested.getName());
        httpResponse.setHeader("Content-Type","application/octet-stream");
        
        httpResponse.setContentLength(multipartFile.getBytes().length);
      
        return multipartFile.getBytes();
    }
    
    private String composeFileName(ByteArrayMultipartFileOld multipartFile){
    	return multipartFile.getName() + (multipartFile.getExp() != null 
    				? "." + multipartFile.getExp() 
    				: "");
    }

    
    /**
     * Сервис для получения Attachment из execution
     * @param taskId
     * @param request
     * @param httpResponse
     * @return
     * @throws java.io.IOException
     */
    
    @RequestMapping(value = "/file/download_file_from_db_execution", method = RequestMethod.GET)
    @Transactional
    public @ResponseBody
    byte[] getAttachmentFromDbExecution(@RequestParam("taskId") String taskId, 
    		             HttpServletRequest request, HttpServletResponse httpResponse) throws IOException {
    	
    	//получаем по задаче ид процесса
    	HistoricTaskInstance historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
        		.taskId(taskId).singleResult(); 
    	String processInstanceId = historicTaskInstanceQuery.getProcessInstanceId();
    	if (processInstanceId == null) {
            throw new ActivitiObjectNotFoundException(String.format(
        			"ProcessInstanceId for taskId '{%s}' not found.", taskId),
                    Attachment.class);
        }
    	
    	//получаем по ид процесса сам процесс
    	HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery()
    	    	.processInstanceId(processInstanceId)
    	    	.includeProcessVariables()
    	    	.singleResult();
    	if (processInstance == null) {
    		throw new ActivitiObjectNotFoundException(String.format(
    			"ProcessInstance for processInstanceId '{%s}' not found.", processInstanceId),
                Attachment.class);
    	}
    	
    	//получаем коллекцию переменных процеса 
    	Map<String, Object> processVariables = processInstance.getProcessVariables();
    	if (processVariables == null || processVariables.get(FileTaskUpload.BUILDER_ATACH_MODEL_LIST) == null
    			|| ((List<BuilderAtachModel>)processVariables.get(FileTaskUpload.BUILDER_ATACH_MODEL_LIST)).get(0) == null) {
			throw new ActivitiObjectNotFoundException(String.format(
					"ProcessVariable '{%s}' for processInstanceId '{%s}' not found.", 
					FileTaskUpload.BUILDER_ATACH_MODEL_LIST, processInstanceId));
		} 
    	
    	//получаем прикрепленный файл
    	BuilderAtachModel atachModel = 
    			((List<BuilderAtachModel>) processVariables.get(FileTaskUpload.BUILDER_ATACH_MODEL_LIST)).get(0);

        //Помещаем параметры контента в header 
        /*httpResponse.setHeader("Content-disposition", 
        		"attachment; filename=" + atachModel.getOriginalFilename() + "." + atachModel.getExp());*/
        httpResponse.setHeader("Content-disposition", 
        		"attachment; filename=" + atachModel.getOriginalFilename());
        httpResponse.setHeader("Content-Type", atachModel.getContentType() + ";charset=UTF-8");
        httpResponse.setContentLength(atachModel.getByteToStringContent().getBytes().length);
        
        return AbstractModelTask.contentStringToByte(atachModel.getByteToStringContent());
    }
    
    
}
