package org.wf.dp.dniprorada.engine.task;

import java.util.List;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wf.dp.dniprorada.util.GeneralConfig;

/**
 * @author a.skosyr
 */
@Component("SendAttachToDocuments")
public class SendAttachToDocuments implements JavaDelegate {

    private final static Logger log = LoggerFactory.getLogger(SendAttachToDocuments.class);

    @Autowired
    private RuntimeService runtimeService;
    
    @Autowired
    private TaskService taskService;
    
    public Expression nID_Attach;
    public Expression sName;
    public Expression nID_DocumentType;
    
    @Autowired
    private GeneralConfig generalConfig;

    @Override
    public void execute(DelegateExecution oExecution) throws Exception {
        
    	log.info(String.format("Processing SendAttachToDocuments for the process ID %s", oExecution.getProcessInstanceId()));
    	
    	Object oIDSubject = runtimeService.getVariable(oExecution.getProcessInstanceId(), "nID_Subject");
    	String nID_Attach = getStringFromFieldExpression(this.nID_Attach, oExecution);
        String sName = getStringFromFieldExpression(this.sName, oExecution);
        String nID_DocumentType = getStringFromFieldExpression(this.nID_DocumentType, oExecution);
    	
		log.info(String.format("Retrieved next values from the parameters of system task %s %s %s %s",
						oIDSubject, nID_Attach, sName, nID_DocumentType));
        
		if (nID_Attach != null){
			Attachment oAttachment = taskService.getAttachment(nID_Attach);
			String sDocumentContentType = oAttachment.getType();
			String sFileExtension = StringUtils.substringAfterLast(oAttachment.getName(), ".");
			String sFile = StringUtils.substringBeforeLast(oAttachment.getName(), ".");
			String sID_Subject_Upload = "1";
			String sSubjectName_Upload = "";
			
			String processInstanceId = oExecution.getProcessInstanceId();
			
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).active().list();
			if (tasks != null && !tasks.isEmpty()){
				log.info(String.format("Found %s active tasks for the process instance %s", tasks.size(), processInstanceId));
				sSubjectName_Upload = tasks.get(0).getAssignee();
			} else {
				log.info(String.format("There are no active tasks for the process instance %s", processInstanceId));				
			}
		} else {
	    	log.warn("nID_Attach is empty. Breaking execution of the task");
		}
    }

    protected String getStringFromFieldExpression(Expression expression,
            DelegateExecution execution) {
        if (expression != null) {
            Object value = expression.getValue(execution);
            if (value != null) {
                return value.toString();
            }
        }
        return null;
    }

    
}
