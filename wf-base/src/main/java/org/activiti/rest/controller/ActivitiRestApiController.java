package org.activiti.rest.controller;

import com.google.common.base.Charsets;

import liquibase.util.csv.CSVWriter;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.*;
import org.activiti.engine.form.FormData;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.form.FormPropertyImpl;
import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.impl.util.json.JSONObject;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.*;
import org.activiti.redis.exception.RedisException;
import org.activiti.redis.service.RedisService;
import org.activiti.rest.controller.adapter.AttachmentEntityAdapter;
import org.activiti.rest.controller.adapter.ProcDefinitionAdapter;
import org.activiti.rest.controller.adapter.TaskAssigneeAdapter;
import org.activiti.rest.controller.entity.*;
import org.activiti.rest.controller.entity.Process;
import org.activiti.rest.service.api.runtime.process.ExecutionBaseResource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.ByteArrayDataSource;
import org.apache.commons.mail.EmailException;
import org.joda.time.DateTime;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.wf.dp.dniprorada.base.dao.AccessDataDao;
import org.wf.dp.dniprorada.base.dao.FlowSlotTicketDao;
import org.wf.dp.dniprorada.base.model.AbstractModelTask;
import org.wf.dp.dniprorada.engine.task.FileTaskUpload;
import org.wf.dp.dniprorada.form.QueueDataFormType;
import org.wf.dp.dniprorada.model.BuilderAtachModel;
import org.wf.dp.dniprorada.model.ByteArrayMultipartFileOld;
import org.wf.dp.dniprorada.rest.HttpRequester;
import org.wf.dp.dniprorada.util.GeneralConfig;
import org.wf.dp.dniprorada.util.Mail;
import org.wf.dp.dniprorada.util.Util;
import org.wf.dp.dniprorada.util.luna.AlgorithmLuna;
import org.wf.dp.dniprorada.util.luna.CRCInvalidException;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * ...wf/service/... Example:
 * .../wf/service/rest/startProcessByKey/citizensRequest
 *
 * @author BW
 */
@Controller
@RequestMapping(value = "/rest")
public class ActivitiRestApiController extends ExecutionBaseResource {

    private static final int DEFAULT_REPORT_FIELD_SPLITTER = 59;
    private static final Logger log = LoggerFactory.getLogger(ActivitiRestApiController.class);
    public static final String CANCEL_INFO_FIELD = "sCancelInfo";
    @Autowired
    AccessDataDao accessDataDao;
    @Autowired
    private FlowSlotTicketDao flowSlotTicketDao;
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
    @Autowired
    private FormService formService;
    @Autowired
    private Mail oMail;
    @Autowired
    private GeneralConfig generalConfig;
    @Autowired
    private HttpRequester httpRequester;

    @RequestMapping(value = "/start-process/{key}", method = RequestMethod.GET)
    @Transactional
    public @ResponseBody
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
    public @ResponseBody
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
    public @ResponseBody
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
     * @param sData
     * @return
     * @throws org.activiti.rest.controller.ActivitiIOException
     */
    @RequestMapping(value = "/setAccessData", method = RequestMethod.GET)
    @Transactional
    public @ResponseBody
    String setAccessData(
            @RequestParam("sData") String sData) throws ActivitiIOException, Exception {

        String sKey = accessDataDao.setAccessData(sData);
        return sKey;
    }

    /**
     * Укладываем в редис multipartFileToByteArray
     *
     * @param file
     * @return
     * @throws org.activiti.rest.controller.ActivitiIOException
     */
    @RequestMapping(value = "/file/upload_file_to_redis", method = RequestMethod.POST)
    @Transactional
    public @ResponseBody
    String putAttachmentsToRedis(@RequestParam(required = true, value = "file") MultipartFile file) throws ActivitiIOException, Exception {
        String atachId = null;
        try {
            atachId = redisService.putAttachments(AbstractModelTask.multipartFileToByteArray(file,
                    file.getOriginalFilename()).toByteArray());

        } catch (Exception e) {
            throw e;
        }
        return atachId;
    }

    @RequestMapping(value = "/file/download_file_from_redis", method = RequestMethod.GET)
    @Transactional
    public @ResponseBody
    byte[] getAttachmentsFromRedis(@RequestParam("key") String key) throws ActivitiIOException {
        byte[] upload = null;
        try {
            upload = redisService.getAttachments(key);
        } catch (RedisException e) {
            throw new ActivitiIOException(ActivitiIOException.Error.REDIS_ERROR, e.getMessage());
        }
        return upload;
    }

    /**
     * Получение Attachment средствами активити из
     * таблицы ACT_HI_ATTACHMENT
     *
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
            if (null != nFile && nFile.equals(i + 1)) {
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

        String sFileName = attachmentRequested.getName();
        int nTo=sFileName.lastIndexOf(".");
        if(nTo>=0){
            sFileName="attach_"+attachmentRequested.getId()+"."+sFileName.substring(nTo+1);
        }
        
        //Вычитывем из потока массив байтов контента и помещаем параметры контента в header 
        ByteArrayMultipartFileOld multipartFile = new ByteArrayMultipartFileOld(
                attachmentStream, attachmentRequested.getDescription(),
                sFileName, attachmentRequested.getType());
//                attachmentRequested.getName(), attachmentRequested.getType());

        //httpResponse.setHeader("Content-disposition", "attachment; filename=" + composeFileName(multipartFile));
        //httpResponse.setHeader("Content-Type", multipartFile.getContentType() + ";charset=UTF-8");
//===        httpResponse.setHeader("Content-disposition", "attachment; filename=" + attachmentRequested.getName());
        httpResponse.setHeader("Content-disposition", "attachment; filename=" + sFileName);
        httpResponse.setHeader("Content-Type", "application/octet-stream");

        httpResponse.setContentLength(multipartFile.getBytes().length);

        return multipartFile.getBytes();
    }

    private String composeFileName(ByteArrayMultipartFileOld multipartFile) {
        return multipartFile.getName() + (multipartFile.getExp() != null
                ? "." + multipartFile.getExp()
                : "");
    }

    /**
     * Сервис для получения Attachment из execution
     *
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
                || ((List<BuilderAtachModel>) processVariables.get(FileTaskUpload.BUILDER_ATACH_MODEL_LIST)).get(0) == null) {
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

    /**
     * прикрепляем к таске Attachment.
     *
     * @param file
     * @return
     * @throws org.activiti.rest.controller.ActivitiIOException
     */
    @RequestMapping(value = "/file/upload_file_as_attachment", method = RequestMethod.POST, produces = "application/json")
    @Transactional
    public @ResponseBody
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
            log.debug("processInstanceId: " + processInstanceId + " taskId: " + taskId + "assignee: " + assignee);
        } else {
            log.error("There is no tasks at all!");

        }

        identityService.setAuthenticatedUserId(assignee);

        String sFilename = file.getOriginalFilename();
        log.debug("sFilename=" + file.getOriginalFilename());
        sFilename = Renamer.sRenamed(sFilename);
        log.debug("FileExtention: " + getFileExtention(file) + " fileContentType: " + file.getContentType() + "fileName: " + sFilename);
        log.debug("description: " + description);

        Attachment attachment = taskService.createAttachment(file.getContentType()
                + ";"
                + getFileExtention(file),
                taskId,
                processInstanceId,
                sFilename,//file.getOriginalFilename()
                description, file.getInputStream());

        AttachmentEntityAdapter adapter = new AttachmentEntityAdapter();

        return adapter.apply(attachment);
    }


    @RequestMapping(value = "/file/upload_content_as_attachment", method = RequestMethod.POST, produces = "application/json")
    @Transactional
    public @ResponseBody
    AttachmentEntityI putTextAttachmentsToExecution(
            @RequestParam(value = "nTaskId") String taskId,
            @RequestParam(value = "sContentType", required = false, defaultValue = "text/html") String sContentType,
            @RequestParam(value = "sDescription") String description,
            @RequestParam(value = "sFileName") String sFileName,
            @RequestBody String sData) throws ActivitiIOException, Exception {

        String processInstanceId = null;
        String assignee = null;

        List<Task> tasks = taskService.createTaskQuery().taskId(taskId).list();
        if (tasks != null && !tasks.isEmpty()) {
            Task task = tasks.iterator().next();
            processInstanceId = task.getProcessInstanceId();
            assignee = task.getAssignee() != null ? task.getAssignee() : "kermit";
            log.debug("processInstanceId: " + processInstanceId + " taskId: " + taskId + "assignee: " + assignee);
        } else {
            log.error("There is no tasks at all!");

        }

        identityService.setAuthenticatedUserId(assignee);

        String sFilename = sFileName;
        log.debug("sFilename=" + sFileName);
        sFilename = Renamer.sRenamed(sFilename);
        log.debug("FileExtention: " + getFileExtention(sFileName) + " fileContentType: " + sContentType + "fileName: " + sFilename);
        log.debug("description: " + description);

        Attachment attachment = taskService.createAttachment(sContentType
                        + ";"
                        + getFileExtention(sFileName),
                taskId,
                processInstanceId,
                sFilename,
                description, new ByteArrayInputStream(sData.getBytes(Charsets.UTF_8)));

        AttachmentEntityAdapter adapter = new AttachmentEntityAdapter();

        return adapter.apply(attachment);
    }

    
    /*@RequestMapping(value = "/file/download_bp_timing_old", method = RequestMethod.GET)
    @Transactional
    public void getTimingForBusinessProcess(@RequestParam(value = "sID_BP_Name") String sID_BP_Name,
            @RequestParam(value = "sDateAt") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateAt,
            @RequestParam(value = "sDateTo", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateTo,
            @RequestParam(value = "nRowStart", required = false, defaultValue = "0") Integer nRowStart,
            @RequestParam(value = "nRowsMax", required = false, defaultValue = "1000") Integer nRowsMax,
            HttpServletRequest request, HttpServletResponse httpResponse) throws IOException {

        if (sID_BP_Name == null || sID_BP_Name.isEmpty()) {
            log.error("ID of business process is {}", sID_BP_Name);
            throw new ActivitiObjectNotFoundException(
                    "Statistics for the business process '" + sID_BP_Name + "' not found.",
                    Process.class);
        }

        List<HistoricTaskInstance> foundResults = historyService.createHistoricTaskInstanceQuery()
                .taskCompletedAfter(dateAt)
                .taskCompletedBefore(dateTo)
                .processDefinitionKey(sID_BP_Name)
                .listPage(nRowStart, nRowsMax);

        SimpleDateFormat sdfFileName = new SimpleDateFormat("yyyy-MM-ddHH-mm-ss");
        String fileName = sID_BP_Name + "_" + sdfFileName.format(Calendar.getInstance().getTime()) + ".csv";

        log.info("File name to return statistics : {}", fileName);

        httpResponse.setContentType("text/csv;charset=UTF-8");
        httpResponse.setHeader("Content-disposition", "attachment; filename=" + fileName);

        CSVWriter csvWriter = new CSVWriter(httpResponse.getWriter());

        
        String[] header = {"nID_Process", "sLoginAssignee", "sDateTimeStart", "nDurationMS", "nDurationHour", "sName"};
        csvWriter.writeNext(header);

        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss");
        if (foundResults != null && foundResults.size() > 0) {
            log.info(String.format("Found {%s} completed tasks for business process {%s} for date period {%s} - {%s}", foundResults.size(), sID_BP_Name, sdfDate.format(dateAt),
                    sdfDate.format(dateTo)));
            for (HistoricTaskInstance currTask : foundResults) {
                String[] line = new String[6];
                line[0] = currTask.getProcessInstanceId();
                line[1] = currTask.getAssignee();
                Date startDate = currTask.getStartTime();
                line[2] = sdfDate.format(startDate);
                line[3] = String.valueOf(currTask.getDurationInMillis());
                long durationInHours = currTask.getDurationInMillis() / (1000 * 60 * 60);
                line[4] = String.valueOf(durationInHours);
                line[5] = currTask.getName();
                csvWriter.writeNext(line);
            }
        } else {
            log.info(String.format("No completed tasks found for business process {0} for date period {1} - {2}", sID_BP_Name, sdfDate.format(dateAt),
                    sdfDate.format(dateTo)));
        }

        csvWriter.close();
    }*/

    /**
     * Получение статистики по бизнес
     * процессу за указанные период
     *
     * @param sID_BP_Name - �?Д бизнес процесса
     * @param dateAt - дата начала периода выборки
     * @param dateTo - дата окончания периода выборки
     * @param nRowStart - позиция начальной строки для
     * возврата (0 по умолчанию)
     * @param nRowsMax - количество записей для
     * возврата (1000 по умолчанию)
     * @param request
     * @param httpResponse
     * @return
     * @throws java.io.IOException
     */
    @RequestMapping(value = "/file/download_bp_timing", method = RequestMethod.GET)
    @Transactional
    public void getTimingForBusinessProcessNew(@RequestParam(value = "sID_BP_Name") String sID_BP_Name,
            @RequestParam(value = "sDateAt") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateAt,
            @RequestParam(value = "sDateTo", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateTo,
            @RequestParam(value = "nRowStart", required = false, defaultValue = "0") Integer nRowStart,
            @RequestParam(value = "nRowsMax", required = false, defaultValue = "1000") Integer nRowsMax,
            @RequestParam(value = "bDetail", required = false, defaultValue = "true") Boolean bDetail,
            HttpServletRequest request, HttpServletResponse httpResponse) throws IOException {

        if (sID_BP_Name == null || sID_BP_Name.isEmpty()) {
            log.error(String.format("Statistics for the business process '{%s}' not found.", sID_BP_Name));
            throw new ActivitiObjectNotFoundException(
                    "Statistics for the business process '" + sID_BP_Name + "' not found.",
                    Process.class);
        }

        List<HistoricTaskInstance> foundResults = historyService.createHistoricTaskInstanceQuery()
                .taskCompletedAfter(dateAt)
                .taskCompletedBefore(dateTo)
                .processDefinitionKey(sID_BP_Name)
                .listPage(nRowStart, nRowsMax);


        SimpleDateFormat sdfFileName = new SimpleDateFormat("yyyy-MM-ddHH-mm-ss");
        String fileName = sID_BP_Name + "_" + sdfFileName.format(Calendar.getInstance().getTime()) + ".csv";

        log.debug("File name to return statistics : {%s}", fileName);

        httpResponse.setContentType("text/csv;charset=UTF-8");
        httpResponse.setHeader("Content-disposition", "attachment; filename=" + fileName);

        CSVWriter csvWriter = new CSVWriter(httpResponse.getWriter());

        List<String> headers = new ArrayList<String>();
        String[] headersMainField = {"nID_Process", "sLoginAssignee", "sDateTimeStart", "nDurationMS", "nDurationHour", "sName"};
        Set<String> headersExtra = new TreeSet<String>();
        headers.addAll(Arrays.asList(headersMainField));
        log.debug("headers: " + headers);
        if (bDetail) {
            for (HistoricTaskInstance currTask : foundResults) {
                
            	//List<HistoricDetail> details = historyService.createHistoricDetailQuery().formProperties().taskId(currTask.getId()).list();
                HistoricTaskInstance details = historyService.createHistoricTaskInstanceQuery()
                		.includeProcessVariables().taskId(currTask.getId()).singleResult();
                if (details != null &&  details.getProcessVariables() != null) {
                	log.info(" proccessVariavles: " + details.getProcessVariables());
                	for(String key : details.getProcessVariables().keySet()){
                		if(!key.startsWith("sBody")){
                			headersExtra.add(key);
                		}
                	}
                }
            }
            headers.addAll(headersExtra);
        }

        log.info("headers: " + headers);
        csvWriter.writeNext(headers.toArray(new String[headers.size()]));

        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss");
        if (foundResults != null && foundResults.size() > 0) {
            log.debug(String.format("Found {%s} completed tasks for business process {%s} for date period {%s} - {%s}", foundResults.size(), sID_BP_Name, sdfDate.format(dateAt),
                    sdfDate.format(dateTo)));

            for (HistoricTaskInstance currTask : foundResults) { 
                List<String> line = new ArrayList<String>();
                line.add(currTask.getProcessInstanceId());
                line.add(currTask.getAssignee());
                Date startDate = currTask.getStartTime();
                line.add(sdfDate.format(startDate));
                line.add(String.valueOf(currTask.getDurationInMillis()));
                long durationInHours = currTask.getDurationInMillis() / (1000 * 60 * 60);
                line.add(String.valueOf(durationInHours));
                line.add(currTask.getName());

                if (bDetail) {
                    log.debug("currTask: " + currTask.getId());
                    HistoricTaskInstance details = historyService.createHistoricTaskInstanceQuery().includeProcessVariables()
                    		.taskId(currTask.getId()).singleResult(); 
                    for (String headerExtra : headersExtra) {
                        String propertyValue = "";
                        if (details != null && details.getProcessVariables() != null) {
                        	Object variableValue = details.getProcessVariables().get(headerExtra);
                        	if(variableValue != null){
                        		if(variableValue instanceof String){
                            		propertyValue = (String)variableValue;
                            	} else {
                            		propertyValue = String.valueOf(variableValue);
                            	}
                        	}
                        }
                        line.add(propertyValue);
                    }
                }
                log.info("line: " + line);
                csvWriter.writeNext(line.toArray(new String[line.size()]));
            }
        } else {
            log.debug(String.format("No completed tasks found for business process {%s} for date period {%s} - {%s}", sID_BP_Name, sdfDate.format(dateAt),
                    sdfDate.format(dateTo)));
        }
        csvWriter.close();
    }

    /**
     * Download information about the tasks in csv format
     *
     * @param sID_BP business process name
     * @param sID_State_BP task state id
     * @param saFields field of the tasks to download. Separated by comma
     * @param nASCI_Spliter splitter of the fields
     * @param sID_Codepage encoding for the file
     * @param sDateCreateFormat format for sDateCreate
     * @param dateAt start date for the filter
     * @param dateTo end date for the filter
     * @param nRowStart start row for paging
     * @param nRowsMax maximal amount of row for paging
     * @param httpRequest http request wrapper
     * @param httpResponse http responce wrapper
     * @throws IOException in case of connection aborted with client
     *
     * example:
     * https://test.region.igov.org.ua/wf/service/rest/file/downloadTasksData?sID_BP=kiev_mreo_1&sDateAt=2015-06-28&sDateTo=2015-08-01&nASCI_Spliter=59&sID_Codepage=UTF8&saFields=nID_Task;bankIdPassport;bankIdlastName;bankIdfirstName;bankIdmiddleName;1;sDateCreate
     */
    @RequestMapping(value = "/file/downloadTasksData", method = RequestMethod.GET)
    @Transactional
    public void downloadTasksData(@RequestParam(value = "sID_BP") String sID_BP,
            @RequestParam(value = "sID_State_BP", required = false) String sID_State_BP,
            @RequestParam(value = "saFields") String saFields,
            @RequestParam(value = "nASCI_Spliter", required = false) String nASCI_Spliter,
            @RequestParam(value = "sFileName", required = false) String fileName,
            @RequestParam(value = "sID_Codepage", required = false, defaultValue = "win1251") String sID_Codepage,
            @RequestParam(value = "sDateCreateFormat", required = false, defaultValue = "yyyy-MM-dd HH:mm:ss") String sDateCreateFormat,
            @RequestParam(value = "sDateAt", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateAt,
            @RequestParam(value = "sDateTo", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateTo,
            @RequestParam(value = "nRowStart", required = false, defaultValue = "0") Integer nRowStart,
            @RequestParam(value = "nRowsMax", required = false, defaultValue = "1000") Integer nRowsMax,
            HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
        //1. validation
        if (StringUtils.isBlank(sID_BP)) {
            log.error("Wrong name of business task - {}", sID_BP);
            throw new ActivitiObjectNotFoundException(
                    "Statistics for the business task '" + sID_BP + "' not found. Wrong BP name.",
                    Task.class);
        }

        dateAt = validateDateAt(dateAt);
        dateTo = validateDateTo(dateTo);
        String separator = validateSeparator(sID_BP, nASCI_Spliter);
        Charset charset = validateCharset(sID_Codepage);

        //2. query
        TaskQuery query = taskService.createTaskQuery().processDefinitionKey(sID_BP).taskCreatedAfter(dateAt).taskCreatedBefore(dateTo);

        if (sID_State_BP != null) {
            query = query.taskDefinitionKey(sID_State_BP);
        }
        List<Task> foundResults = query.listPage(nRowStart, nRowsMax);

        //3. response
        SimpleDateFormat sdfFileName = new SimpleDateFormat("yyyy-MM-ddHH-mm-ss");
        fileName = fileName != null ? fileName
                : "data_BP-" + sID_BP + "_" + sdfFileName.format(Calendar.getInstance().getTime()) + ".txt";
        SimpleDateFormat sDateCreateDF = new SimpleDateFormat(sDateCreateFormat);

        log.debug("File name to return statistics : {}", fileName);

        httpResponse.setContentType("text/csv;charset=" + charset.name());
        httpResponse.setHeader("Content-disposition", "attachment; filename=" + fileName);

        PrintWriter printWriter = new PrintWriter(httpResponse.getWriter());

        fillTheFile(sID_BP, dateAt, dateTo, foundResults, sDateCreateDF, printWriter, saFields, separator);

        printWriter.close();
    }

    private void fillTheFile(String sID_BP, Date dateAt, Date dateTo,
            List<Task> foundResults, SimpleDateFormat sDateCreateDF, PrintWriter printWriter, String pattern, String separator) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss");
        if (foundResults != null && foundResults.size() > 0) {
            log.info(String.format("Found %s tasks for business process %s for date period %s - %s",
                    foundResults.size(), sID_BP, sdfDate.format(dateAt), sdfDate.format(dateTo)));

            List<String> fieldNames = Arrays.asList(pattern.split(";"));
            log.info("List of fields to retrieve: " + fieldNames.toString());

            for (Task curTask : foundResults) {

                String currentRow = pattern;
                log.trace("Process task - {}", curTask);
                TaskFormData data = formService.getTaskFormData(curTask.getId());
                for (FormProperty property : data.getFormProperties()) {
                    log.info(String.format(
                            "Matching property %s:%s:%s with fieldNames", property.getId(), property.getName(), property.getType().getName()));
                    if (currentRow.contains("${" + property.getId() + "}")) {
                        log.info(String.format("Found field with id %s in the pattern. Adding value to the result", "${" + property.getId() + "}"));
                        String sValue = "";
                        String sType = property.getType().getName();
                        log.info("sType=" + sType);
                        if ("enum".equalsIgnoreCase(sType)) {
                            sValue = parseEnumProperty(property);
                        } else {
                            sValue = property.getValue();
                        }
                        log.info("sValue=" + sValue);
                        if (sValue != null) {
                            log.info(String.format("Replacing field with the value %s", sValue));
                            currentRow = currentRow.replace("${" + property.getId() + "}", sValue);
                        }

                    }
                }

                for (ReportField field : ReportField.values()) {
                    if (currentRow.contains(field.getPattern())) {
                        currentRow = field.replaceValue(currentRow, curTask, sDateCreateDF);
                    }
                }
                // replacing all the fields which were empty in the form with empty string
                currentRow = currentRow.replaceAll("\\$\\{.*?\\}", "");
                printWriter.println(currentRow.replaceAll(";", separator));
            }
        } else {
            log.debug(String.format("No tasks found for business process %s for date period %s - %s",
                    sID_BP, sdfDate.format(dateAt), sdfDate.format(dateTo)));
        }
    }

    private Date validateDateTo(Date dateTo) {
        if (dateTo == null) {
            dateTo = DateTime.now().toDate();
            log.debug("No dateTo was set, use - {}", dateTo);
        }
        return dateTo;
    }

    private Date validateDateAt(Date dateAt) {
        if (dateAt == null) {
            dateAt = DateTime.now().minusDays(1).toDate();
            log.debug("No dateAt was set, use - {}", dateAt);
        }
        return dateAt;
    }

    private Charset validateCharset(String sID_Codepage) {
        Charset charset;
        try {
            if (sID_Codepage.replaceAll("-", "").equalsIgnoreCase("win1251") || sID_Codepage.replaceAll("-", "").equalsIgnoreCase("CL8MSWIN1251")) {
                sID_Codepage = "CP1251";    //hack for alias
            }
            charset = Charset.forName(sID_Codepage);
            log.debug("use charset - {}", charset);
        } catch (IllegalArgumentException e) {
            log.error("Do not support charset - {}", sID_Codepage, e);
            throw new ActivitiObjectNotFoundException(
                    "Statistics for the business task for chatset '" + sID_Codepage + "' cannot be construct.",
                    Task.class, e);
        }
        return charset;
    }

    private String validateSeparator(String sID_BP, String nASCI_Spliter) {
        if (nASCI_Spliter == null) {
            return String.valueOf(Character.toChars(DEFAULT_REPORT_FIELD_SPLITTER));
        }
        if (!StringUtils.isNumeric(nASCI_Spliter)) {
            log.error("ASCI code is not a number {}", nASCI_Spliter);
            throw new ActivitiObjectNotFoundException(
                    "Statistics for the business task with name '" + sID_BP + "' not found. Wrong splitter.",
                    Task.class);
        }
        return String.valueOf(Character.toChars(Integer.valueOf(nASCI_Spliter)));
    }

    private DelegationState validateDelegationState(String sID_State_BP) {
        DelegationState delegationState = null;
        try {
            if (sID_State_BP != null) {
                delegationState = DelegationState.valueOf(sID_State_BP.toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            log.error("Do not support bussiness status - {}", sID_State_BP, e);
            throw new ActivitiObjectNotFoundException(
                    "Statistics for the business task with state '" + sID_State_BP + "' not found. Wrong state.",
                    Task.class, e);
        }
        return delegationState;
    }

    /**
     * Returns business processes which are belong to a specified user
     *
     * @param sLogin - login of user in user activity
     */
    @RequestMapping(value = "/getLoginBPs", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @Transactional
    public @ResponseBody
    String getBusinessProcessesForUser(@RequestParam(value = "sLogin") String sLogin,
            HttpServletRequest request, HttpServletResponse httpResponse) throws IOException {
        if (sLogin.isEmpty()) {
            log.error("Unable to found business processes for user with empty login");
            throw new ActivitiObjectNotFoundException(
                    "Unable to found business processes for user with empty login",
                    ProcessDefinition.class);
        }

        List<Map<String, String>> res = new LinkedList<Map<String, String>>();

        log.info(String.format("Selecting business processes for the user with login: %s", sLogin));

        List<ProcessDefinition> processDefinitionsList = repositoryService.createProcessDefinitionQuery().active().latestVersion().list();
        if (!processDefinitionsList.isEmpty() && processDefinitionsList.size() > 0) {
            log.info(String.format("Found %d active process definitions", processDefinitionsList.size()));

            for (ProcessDefinition processDef : processDefinitionsList) {
                log.info("process definition id: " + processDef.getId());

                Set<String> candidateCroupsToCheck = new HashSet<String>();
                loadCandidateGroupsFromTasks(processDef, candidateCroupsToCheck);

                loadCandidateStarterGroup(processDef, candidateCroupsToCheck);

                findUsersGroups(sLogin, res, processDef, candidateCroupsToCheck);
            }
        } else {
            log.info("Have not found ative process definitions.");
        }

        String jsonRes = JSONValue.toJSONString(res);
        log.info("Result" + jsonRes);
        return jsonRes;
    }

    protected void findUsersGroups(String sLogin,
            List<Map<String, String>> res, ProcessDefinition processDef, Set<String> candidateCroupsToCheck) {
        for (String currGroup : candidateCroupsToCheck) {
            log.info(String.format("Checking whether user %s belongs to the group %s", sLogin, currGroup));
            User user = identityService.createUserQuery().userId(sLogin).memberOfGroup(currGroup).singleResult();
            if (user != null) {
                Map<String, String> process = new HashMap<String, String>();
                process.put("sID", processDef.getKey());
                process.put("sName", processDef.getName());
                log.info(String.format("Added record to response %s", process.toString()));
                res.add(process);
                break;
            } else {
                log.info(String.format("user %s is not in group %s", sLogin, currGroup));
            }
        }
    }

    protected void loadCandidateStarterGroup(ProcessDefinition processDef, Set<String> candidateCroupsToCheck) {
        List<IdentityLink> identityLinks = repositoryService.getIdentityLinksForProcessDefinition(processDef.getId());
        log.info(String.format("Found %d identity links for the process %s", identityLinks.size(), processDef.getKey()));
        for (IdentityLink identity : identityLinks) {
            if (IdentityLinkType.CANDIDATE.equals(identity.getType())) {
                String groupId = identity.getGroupId();
                candidateCroupsToCheck.add(groupId);
                log.info(String.format("Added candidate starter group %s ", groupId));
            }
        }
    }

    protected void loadCandidateGroupsFromTasks(ProcessDefinition processDef, Set<String> candidateCroupsToCheck) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDef.getId());

        for (FlowElement flowElement : bpmnModel.getMainProcess().getFlowElements()) {
            if (flowElement instanceof UserTask) {
                UserTask userTask = (UserTask) flowElement;
                List<String> candidateGroups = userTask.getCandidateGroups();
                if (candidateGroups != null && !candidateGroups.isEmpty()) {
                    candidateCroupsToCheck.addAll(candidateGroups);
                    log.info(String.format("Added candidate groups %s from user task %s", candidateGroups, userTask.getId()));
                }
            }
        }
    }

    public static String parseEnumProperty(FormProperty property) {
        Object oValues = property.getType().getInformation("values");
        if (oValues instanceof Map) {
            Map<String, String> mValue = (Map) oValues;
            log.info("[parseEnumProperty]:m=" + mValue);
            String sName = property.getValue();
            log.info("[parseEnumProperty]:sName=" + sName);
            String sValue = mValue.get(sName);
            log.info("[parseEnumProperty]:sValue=" + sValue);
            return parseEnumValue(sValue);
        } else {
            log.error("Cannot parse values for property - {}", property);
            return "";
        }
    }

    public static String parseEnumValue(String sEnumName) {
        log.info("[parseEnumValue]:sEnumName=" + sEnumName);
        sEnumName = StringUtils.defaultString(sEnumName);
        log.info("[parseEnumValue]:sEnumName(2)=" + sEnumName);
        if (sEnumName.contains("|")) {
            String[] as = sEnumName.split("\\|");
            log.info("[parseEnumValue]:as.length - 1=" + (as.length - 1));
            log.info("[parseEnumValue]:as=" + as);
            return as[as.length - 1];
        } else {
            return sEnumName;
        }
    }

    private String getFileExtention(MultipartFile file) {

        String[] parts = file.getOriginalFilename().split("\\.");
        if (parts.length != 0) {
            return parts[parts.length - 1];
        }
        return "";
    }

    private String getFileExtention(String fileName) {

        String[] parts = fileName.split("\\.");
        if (parts.length != 0) {
            return parts[parts.length - 1];
        }
        return "";
    }

    @RequestMapping(value = "/test/sendAttachmentsByMail", method = RequestMethod.GET)
    @Transactional
    public void sendAttachmentsByMail(
            @RequestParam(value = "sMailTo", required = false) String sMailTo,
            @RequestParam(value = "nID_Task", required = false) String snID_Task,
            @RequestParam(value = "sBody", required = false) String sBody,
            @RequestParam(value = "bHTML", required = false) boolean bHTML,
            //@RequestParam(value = "nID_Attachment", required = false) String snID_Attachment,
            @RequestParam(value = "naID_Attachment", required = false) String snaID_Attachment,//naID_Attachment=1530717
            HttpServletRequest request, HttpServletResponse httpResponse)
            throws IOException, MessagingException, EmailException {

//            Mail oMail = new Mail();
        oMail._To("bvv4ik@gmail.com");
        //oMail._To(sMailTo==null?"bvv4ik@gmail.com":sMailTo);
        oMail._Body(sBody == null ? "<a href=\"http:\\\\google.com\">Google</a> It's test Проверка ! ��� ��������!" : sBody);


        log.info("oMail.getHead()=" + oMail.getHead());
        log.info("oMail.getBody()=" + oMail.getBody());
        log.info("oMail.getAuthUser()=" + oMail.getAuthUser());
        log.info("oMail.getAuthPassword()=" + oMail.getAuthPassword());
        log.info("oMail.getFrom()=" + oMail.getFrom());
        log.info("oMail.getTo()=" + oMail.getTo());
        log.info("oMail.getHost()=" + oMail.getHost());
        log.info("oMail.getPort()=" + oMail.getPort());


//            oMail.init();
/*            if(bHTML==true){
         log.info("bHTML");
         oMail._BodyAsHTML();
         }else{
         log.info("!bHTML");
         oMail._BodyAsText();
         }
         */


        if (snaID_Attachment != null) {
            String[] ansID_Attachment = snaID_Attachment.split(",");
            for (String snID_Attachment : ansID_Attachment) {
                //anID_Attachment.split()
                //String snID_Attachment;
                Attachment oAttachment = taskService.getAttachment(snID_Attachment);
                String sFileName = oAttachment.getName();
                String sFileExt = oAttachment.getType().split(";")[0];
                String sDescription = oAttachment.getDescription();
                log.info("oAttachment.getId()=" + oAttachment.getId() + ", sFileName=" + sFileName + ", sFileExt=" + sFileExt + ", sDescription=" + sDescription);
                InputStream oInputStream = taskService.getAttachmentContent(oAttachment.getId());
                DataSource oDataSource = new ByteArrayDataSource(oInputStream, sFileExt);

                //oMail._Attach(oDataSource, sFileName + "." + sFileExt, sDescription);
                oMail._Attach(oDataSource, sFileName + "." + sFileExt, sDescription);
            }
        }

        oMail.send();
    }

    @RequestMapping(value = "/getPatternFile", method = RequestMethod.GET)
    public void getPatternFile(
            @RequestParam(value = "sPathFile") String sPathFile,
            @RequestParam(value = "sContentType", required = false) String sContentType,
            HttpServletResponse response) throws ActivitiRestException {

        try {
            String contentType = sContentType == null ? Util.PATTERN_DEFAULT_CONTENT_TYPE : sContentType;
            response.setContentType(contentType);
            response.setCharacterEncoding(Charsets.UTF_8.toString());
            byte[] resultObj = Util.getPatternFile(sPathFile);
            response.getOutputStream().write(resultObj);
        } catch (IllegalArgumentException | IOException e) {
            ActivitiRestException newErr = new ActivitiRestException("BUSINESS_ERR", e.getMessage(), e);
            newErr.setHttpStatus(HttpStatus.FORBIDDEN);
            throw newErr;
        } catch (Exception e) {
            ActivitiRestException newErr = new ActivitiRestException("SYSTEM_ERR", e.getMessage(), e);
            newErr.setHttpStatus(HttpStatus.FORBIDDEN);
            throw newErr;
        }
    }

    @RequestMapping(value = "/tasks/getTasksByOrder", method = RequestMethod.GET)
    public @ResponseBody
    List<String> getTasksByOrder(@RequestParam(value = "nID_Protected") Long nID_Protected) throws ActivitiRestException {
        List<String> res;

        try {
            res = getTaskByOrderInternal(nID_Protected);
        } catch (CRCInvalidException | RecordNotFoundException e) {
            ActivitiRestException newErr = new ActivitiRestException(
                    "BUSINESS_ERR", e.getMessage(), e);
            newErr.setHttpStatus(HttpStatus.FORBIDDEN);
            throw newErr;
        }

        return res;
    }

    @RequestMapping(value = "/tasks/getTasksByText", method = RequestMethod.GET)
    public @ResponseBody
    Set<String> getTasksByText(@RequestParam(value = "sFind") String sFind,
    		@RequestParam(value = "sLogin", required = false) String sLogin,
    		@RequestParam(value = "bAssigned", required = false) String bAssigned) throws ActivitiRestException {
    	Set<String> res = new HashSet<String>();

    	String searchTeam = sFind.toLowerCase();
        TaskQuery taskQuery = buildTaskQuery(sLogin, bAssigned);
    	List<Task> activeTasks = taskQuery.active().list();
    	for (Task currTask : activeTasks){
    		TaskFormData data = formService.getTaskFormData(currTask.getId());
    		if (data != null){
	    		for (FormProperty property : data.getFormProperties()) {
	                
	                String sValue = "";
	                String sType = property.getType().getName();
	                if ("enum".equalsIgnoreCase(sType)) {
	                    sValue = parseEnumProperty(property);
	                } else {
	                    sValue = property.getValue();
	                }
	                log.info("taskId=" + currTask.getId() + "propertyName=" + property.getName() + "sValue=" + sValue);
	                if (sValue != null) {
	                    if (sValue.toLowerCase().indexOf(searchTeam) >= 0){
	                    	res.add(currTask.getId());
	                    }
	                }
	            }
    		} else {
    			log.info("TaskFormData for task " + currTask.getId() + "is null. Skipping from processing.");
    		}
    	}

        return res;
    }

	protected TaskQuery buildTaskQuery(String sLogin, String bAssigned) {
		TaskQuery taskQuery = taskService.createTaskQuery();
        if (bAssigned != null){
        	if (!Boolean.valueOf(bAssigned).booleanValue()){
        		taskQuery.taskUnassigned();
            	if (sLogin != null && !sLogin.isEmpty()){
                	taskQuery.taskCandidateUser(sLogin);
                } 
        	} else if (sLogin != null && !sLogin.isEmpty()){
        		taskQuery.taskAssignee(sLogin);
        	}
        } else {
        	if (sLogin != null && !sLogin.isEmpty()){
            	taskQuery.taskCandidateOrAssigned(sLogin);
            } 
        }
		return taskQuery;
	}
    
    
    private List<String> getTaskByOrderInternal(Long nID_Protected) throws CRCInvalidException, RecordNotFoundException {
        AlgorithmLuna.validateProtectedNumber(nID_Protected);

        String processInstanceID = String.valueOf(AlgorithmLuna.getOriginalNumber(nID_Protected));

        List<Task> aTask = taskService.createTaskQuery().processInstanceId(processInstanceID).list();

        List<String> res = new ArrayList<>();

        if (aTask == null || aTask.isEmpty()) {
            log.error(String.format("Tasks for process instance with id = '%s' not found", processInstanceID));
            throw new RecordNotFoundException();
        }

        for (Task task : aTask) {
            res.add(task.getId());
        }

        return res;
    }

    @RequestMapping(value = "/tasks/cancelTask", method = RequestMethod.POST)
    public @ResponseBody
    //void cancelTask(@RequestParam(value = "nID_Protected") Long nID_Protected,
    String cancelTask(@RequestParam(value = "nID_Protected") Long nID_Protected,
            @RequestParam(value = "sInfo", required = false) String sInfo) throws ActivitiRestException {
        
        String sMessage = "Ваша заявка відмінена. Ви можете подату нову на Порталі державних послуг iGov.org.ua.<\n<br>"
            +"З повагою, команду порталу  iGov.org.ua";
        
        
        try {
            cancelTasksInternal(nID_Protected, sInfo);
            return sMessage;
        } catch (CRCInvalidException | RecordNotFoundException | TaskAlreadyUnboundException e) {
            ActivitiRestException newErr = new ActivitiRestException(
                    "BUSINESS_ERR", e.getMessage(), e);
            newErr.setHttpStatus(HttpStatus.FORBIDDEN);
            sMessage = "Вибачте, виникла помилка при виконанні операції. Спробуйте ще раз, будь ласка";
            return sMessage;
            //throw newErr;
        }
    }

    void cancelTasksInternal(Long nID_Protected, String sInfo) throws ActivitiRestException,
            CRCInvalidException, RecordNotFoundException, TaskAlreadyUnboundException {

        AlgorithmLuna.validateProtectedNumber(nID_Protected, "Неверный id заявки");

        String processInstanceId = "" + AlgorithmLuna.getOriginalNumber(nID_Protected);

        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();

        if (tasks == null || tasks.isEmpty()) {
            log.error(String.format("Tasks for Process Instance [id = '%s'] not found", processInstanceId));
            throw new RecordNotFoundException("Заявка не найдена");
        }

        HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(
                processInstanceId).singleResult();

        FormData formData = formService.getStartFormData(processInstance.getProcessDefinitionId());

        List<String> propertyIds = AbstractModelTask.getListField_QueueDataFormType(formData);
        List<String> queueDataList = AbstractModelTask.getVariableValues(runtimeService, processInstanceId,
                propertyIds);

        if (queueDataList.isEmpty()) {
            log.error(String.format("Queue data list for Process Instance [id = '%s'] not found", processInstanceId));
            throw new RecordNotFoundException("Метаданные электронной очереди не найдены");
        }

        for (String queueData : queueDataList) {
            Map<String, Object> m = QueueDataFormType.parseQueueData(queueData);
            long nID_FlowSlotTicket = QueueDataFormType.get_nID_FlowSlotTicket(m);
            if (!flowSlotTicketDao.unbindFromTask(nID_FlowSlotTicket)) {
                throw new TaskAlreadyUnboundException("Заявка уже отменена");
            }
        }

        runtimeService.setVariable(processInstanceId, CANCEL_INFO_FIELD,
                String.format("[%s] Причина отмены заявки: %s", DateTime.now(), sInfo == null ? "" : sInfo));

    }

    private static class TaskAlreadyUnboundException extends Exception {

        public TaskAlreadyUnboundException(String message) {
            super(message);
        }
    }


    /*issue 808

 3.4) в найденную таску (по nID_Protected) сетить в глобальную переменную
 3.4.1) saFieldQuestion - содержимое saField
 3.4.2) sQuestion - содержимое sBody

    * */

    /**
     * сервис ЗАПРОСА полей, требующих уточнения, c отсылкой уведомления гражданину
     * @param nID_Protected - номер-ИД заявки (защищенный)
     * @param saField -- строка-массива полей (например: "[{'id':'sFamily','type':'string','value':'Белявский'},{'id':'nAge','type':'long'}]")
     * @param sMail -- строка электронного адреса гражданина
     * @param sHead -- строка заголовка письма //опциональный (если не задан, то "Необходимо уточнить данные")
     * @param sBody -- строка тела письма //опциональный (если не задан, то пустота)
     * @throws ActivitiRestException
     */
    //http://localhost:8081/service/rest/setTaskQuestions?nID_Protected=22&saField=[{%27id%27:%27sFamily%27,%27type%27:%27string%27,%27value%27:%27test%27}]&sMail=olga2012olga@gmail.com
    @RequestMapping(value = "/setTaskQuestions", method = RequestMethod.GET)
    public @ResponseBody
    void setTaskQuestions(@RequestParam(value = "nID_Protected") Long nID_Protected,
                    @RequestParam(value = "saField") String saField,
                    @RequestParam(value = "sMail") String sMail,
                    @RequestParam(value = "sHead", required = false) String sHead,
                    @RequestParam(value = "sBody", required = false) String sBody) throws ActivitiRestException {

        sHead = sHead == null ? "Необхідно уточнити дані" : sHead;
        sBody = sBody == null ? "" : sBody;
        String sToken = generateToken();
        try {
            updateHistoryEvent_Service(saField, sHead, sBody, sToken);
        } catch (Exception e) {
            throw new ActivitiRestException(
                    ActivitiExceptionController.BUSINESS_ERROR_CODE,
                   "error during updating historyEvent_service: " + e.getMessage(),e,
                    HttpStatus.FORBIDDEN);
        }
        try {
            sendEmail(sHead, createEmailBody(nID_Protected,saField,sBody, sToken),sMail);
        } catch (EmailException e) {
            throw new ActivitiRestException(
                    ActivitiExceptionController.SYSTEM_ERROR_CODE,
                    "error during sending email: " + e.getMessage(),e,
                    HttpStatus.FORBIDDEN);
        }

    }
/*тсылать письмо
 3.3.1) на sMail
 3.3.2) с заголовком sHead
 3.3.3) и телом sBody
 3.3.4) + перечисление полей из saField в формате таблицы: Поле / Тип / Текущее значение
 3.3.5) И гиперссылкой в конце типа: https://igov.org.ua/order?nID_Protected=12233&sToken=LHLIUH где:
хост должен быть текущий центральный
nID_Protected - получный параметр
sToken - сгенерированный случайно 20-ти символьный код*/
    private String createEmailBody(Long nID_Protected, String soData, String sBody, String sToken) {
        StringBuilder emailBody = new StringBuilder(sBody);
        emailBody.append("\n")
                .append(createTable(soData))
                .append("\n");
        String link = (new StringBuilder("https://")
                .append(generalConfig.sHostCentral())
                .append("/order?nID_Protected=")
                .append(nID_Protected)
                .append("&sToken=")
                .append(sToken))
                    .toString();
        emailBody.append(link)
                .append("\n");
        return emailBody.toString();
    }

    private void sendEmail(String sHead, String sBody, String recipient) throws EmailException {
        oMail.reset();
        oMail._To(recipient)
             ._Head(sHead)
             ._Body(sBody);
        oMail.send();
    }

    private String createTable(String soData) {
        if (soData == null || "[]".equals(soData)){
            return "";
        }
        StringBuilder tableStr = new StringBuilder("Поле \t/ Тип \t/ Поточне значення\n");
        JSONObject jsnobject = new JSONObject("{ soData:" + soData + "}");
        JSONArray jsonArray = jsnobject.getJSONArray("soData");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject record = jsonArray.getJSONObject(i);
            tableStr.append(record.opt("id") != null ? record.get("id") : "?")
                    .append(" (")
                    .append(record.opt("type")!= null ? record.get("type").toString() : "??")
                    .append("): ")
                    .append(record.opt("value")!= null ? record.get("value").toString() : "")
                    .append(" \n");
        }
        return tableStr.toString();
    }

    //steals from DocumentAccessDaoImpl :)
    private String generateToken() {
        // 97-122 small character
        // 65-90 big character
        // 48-57 number
        StringBuilder os = new StringBuilder();
        Random ran = new Random();
        for (int i = 1; i <= 20; i++) {
            int a = ran.nextInt(3) + 1;
            switch (a) {
                case 1:
                    int num = ran.nextInt((57 - 48) + 1) + 48;
                    os.append((char) num);
                    break;
                case 2:
                    int small = ran.nextInt((122 - 97) + 1) + 97;
                    os.append((char) small);
                    break;
                case 3:
                    int big = ran.nextInt((90 - 65) + 1) + 65;
                    os.append((char) big);
                    break;
            }
        }
        return os.toString();
    }

    private String updateHistoryEvent_Service(String saField, String sHead, String sBody, String sToken) throws Exception {
        String URI = "/wf/service/services/updateHistoryEvent_Service";
        Map<String, String> params = new HashMap<>();
        params.put("soData", saField);
        params.put("sHead", sHead);
        params.put("sBody", sBody);
        params.put("sToken", sToken);
        params.put("sID_Status", "setTaskQuestions");
        params.put("sAccessContract", "Request");
        String sAccessKey_HistoryEvent = accessDataDao.setAccessData(httpRequester.getFullURL(URI, params));
        params.put("sAccessKey", sAccessKey_HistoryEvent);
        log.info("sAccessKey=" + sAccessKey_HistoryEvent);
        String soJSON_HistoryEvent = httpRequester.get("https://" + generalConfig.sHostCentral() + URI, params);
        log.info("soJSON_HistoryEvent="+soJSON_HistoryEvent);
        return soJSON_HistoryEvent;
    }
    
    @RequestMapping(value = "/setTaskAnswer", method = RequestMethod.GET)
    public @ResponseBody
    void setTaskAnswer(@RequestParam(value = "nID_Protected") Long nID_Protected,
                    @RequestParam(value = "saField") String saField,
                    @RequestParam(value = "sToken") String sToken,
                    @RequestParam(value = "sHead", required = false) String sHead,
                    @RequestParam(value = "sBody", required = false) String sBody) throws ActivitiRestException {
        try {
        	sHead = sHead == null ? "На заявку " + nID_Protected + " дана відповідь громаданином" : sHead;
        	
        	AlgorithmLuna.validateProtectedNumber(nID_Protected);
        	
            String processInstanceID = String.valueOf(AlgorithmLuna.getOriginalNumber(nID_Protected));
            
            log.info("Found processInstanceID=" + processInstanceID + ". Will get history event service");
        	
        	String historyEventService = getHistoryEvent_Service(nID_Protected.toString());
        	
        	JSONObject fieldsJson = new JSONObject(historyEventService);
        	
        	if (fieldsJson.has("sToken")){
        		String tasksToken = fieldsJson.getString("sToken");
        		if (tasksToken.isEmpty() || !tasksToken.equals(sToken)){
            		throw new ActivitiRestException(
                            ActivitiExceptionController.BUSINESS_ERROR_CODE,
                           "Token is wrong");        			
        		}
        	} else {
        		throw new ActivitiRestException(
                        ActivitiExceptionController.BUSINESS_ERROR_CODE,
                       "Token is absent");
        	}
        	
        	JSONObject jsnobject = new JSONObject("{ soData:" + saField + "}");
            JSONArray jsonArray = jsnobject.getJSONArray("soData");
        	List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceID).list();
        	
        	if (tasks != null){
            	runtimeService.setVariable(processInstanceID, "sAnswer", sBody);
            	log.info("Added variable sAnswer to the process " + processInstanceID);
        		
        		log.info("Found " + tasks.size() + " tasks by nID_Protected...");
        		for (Task task : tasks){
        			log.info("task;" + task.getName() + "|" + task.getDescription() + "|" + task.getId());
        			TaskFormData data = formService.getTaskFormData(task.getId());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject record = jsonArray.getJSONObject(i);
                        String fieldId = (String) record.get("id");
                    for (FormProperty property : data.getFormProperties()) {
                    	if (fieldId.equals(property.getId())){
                    		if (property instanceof FormPropertyImpl){
                        		log.info("Updating property's " + property.getId() + " value from " + 
                        					property.getValue() + " to " + record.get("value"));
                    			((FormPropertyImpl)property).setValue((String) record.get("value"));                     			
                    		}
                    	} else {
                    		log.info("Skipping property " + property.getId() + " as there is no such property in input parameter");
                    	}
                    }
                    }
        		}
        	}
        	
        	updateHistoryEvent_Service(processInstanceID, saField, null);
        } catch (Exception e) {
            throw new ActivitiRestException(
                    ActivitiExceptionController.BUSINESS_ERROR_CODE,
                    e.getMessage(),e,
                    HttpStatus.FORBIDDEN);
        }
    }
    
    private String getHistoryEvent_Service(String nID_Protected) throws Exception {
        String URI = "/wf/service/services/getHistoryEvent_Service";
        Map<String, String> params = new HashMap<>();
        params.put("nID_Protected", nID_Protected);
        log.info("Getting URL with parameters: " + generalConfig.sHostCentral() + URI + params);
        String soJSON_HistoryEvent = httpRequester.get(generalConfig.sHostCentral() + URI, params);
        log.info("soJSON_HistoryEvent="+soJSON_HistoryEvent);
        return soJSON_HistoryEvent;
    }
    
    private String updateHistoryEvent_Service(String sID_Process, String saField, String sToken) throws Exception {
        String URI = "/wf/service/services/updateHistoryEvent_Service";
        Map<String, String> params = new HashMap<>();
        params.put("nID_Process", sID_Process);
        params.put("soData", saField);
        params.put("sToken", sToken);
        params.put("sID_Status", "setTaskAnswer");
        String sAccessKey_HistoryEvent = accessDataDao.setAccessData(httpRequester.getFullURL(URI, params));
        params.put("sAccessKey", sAccessKey_HistoryEvent);
        log.info("sAccessKey=" + sAccessKey_HistoryEvent);
        String soJSON_HistoryEvent = httpRequester.get(generalConfig.sHostCentral() + URI, params);
        log.info("soJSON_HistoryEvent="+soJSON_HistoryEvent);
        return soJSON_HistoryEvent;
    }
}
