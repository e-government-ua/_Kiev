package org.wf.dp.dniprorada.task.listener;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.form.FormData;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.IdentityLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.wf.dp.dniprorada.base.model.AbstractModelTask;

import java.util.List;
import java.util.Set;

/**
 * This is twin of fileTaskUploadListener (addAttachmentFromStartFormListener)
 * This listener takes previous task form. DID NOT FINISHED YET.
 *
 * @author Dmytro Tsapko
 */
@Component("addAttachmentFromCurrentTaskListener")
public class addAttachmentFromCurrentTaskListener extends AbstractModelTask implements TaskListener {

    static final transient Logger LOG = LoggerFactory.getLogger(addAttachmentFromCurrentTaskListener.class);

    private static final long serialVersionUID = 1L;

    @Override
    public void notify(DelegateTask task) {
        DelegateExecution execution = task.getExecution();
        // получить группу бп
        Set<IdentityLink> identityLink = task.getCandidates();
        // получить User группы
        List<User> users = execution.getEngineServices().getIdentityService()
                .createUserQuery()
                .memberOfGroup(identityLink.iterator().next().getGroupId())
                .list();

        if (users == null || users.size() == 0 || users.get(0) == null || users.get(0).getId() == null) {
            //TODO  what to do if no user?
        } else {
            // setAuthenticatedUserId первого попавщегося
            //TODO Shall we implement some logic for user selection.
            execution.getEngineServices().getIdentityService()
                    .setAuthenticatedUserId(users.get(0).getId());

            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            HistoryService historyService = processEngine.getHistoryService();
            List<HistoricTaskInstance> taskInstanceList = historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId())
                    .finished()
                    .orderByHistoricTaskInstanceEndTime().desc()
                    .list();
            System.out.println("taskInstanceList: " + taskInstanceList);
            //			System.out.println("taskInstanceList.get(0).getName(): " + taskInstanceList.get(0).getName());

            FormData formData = null;
            if (taskInstanceList != null && !taskInstanceList.isEmpty()) {
                formData = processEngine.getFormService().getTaskFormData(taskInstanceList.get(0).getId());
            }

            FormData startformData = execution.getEngineServices()
                    .getFormService()
                    .getStartFormData(execution.getProcessDefinitionId());
            System.out.println("formData: " + formData);
            addAttachmentsToTask(startformData, task);
        }
    }
}
