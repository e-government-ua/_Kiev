package org.wf.dp.dniprorada.task.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.form.FormData;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.IdentityLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.wf.dp.dniprorada.base.model.AbstractModelTask;

import java.util.List;
import java.util.Set;

/**
 * @author inna
 */
@Component("fileTaskUploadListener")
public class FileTaskUploadListener extends AbstractModelTask implements TaskListener {

    static final transient Logger LOG = LoggerFactory.getLogger(FileTaskUploadListener.class);

    private static final long serialVersionUID = 1L;

    @Override
    public void notify(DelegateTask oTask) {
        DelegateExecution oExecution = oTask.getExecution();
        // получить группу бп
        Set<IdentityLink> identityLink = oTask.getCandidates();
        // получить User группы
        List<User> aUser = oExecution.getEngineServices().getIdentityService()
                .createUserQuery()
                .memberOfGroup(identityLink.iterator().next().getGroupId())
                .list();

        LOG.info("Finding any assigned user-member of group. aUser=" + aUser);
        if (aUser == null || aUser.size() == 0 || aUser.get(0) == null || aUser.get(0).getId() == null) {
            //TODO  what to do if no user?
        } else {
            // setAuthenticatedUserId первого попавщегося
            //TODO Shall we implement some logic for user selection.
            oExecution.getEngineServices().getIdentityService().setAuthenticatedUserId(aUser.get(0).getId());
            // получить информацию по стартовой форме бп
            FormData oStartFormData = oExecution.getEngineServices().getFormService()
                    .getStartFormData(oExecution.getProcessDefinitionId());
            LOG.info("beginning of addAttachmentsToTask(startformData, task):execution.getProcessDefinitionId()="
                    + oExecution.getProcessDefinitionId());
            addAttachmentsToTask(oStartFormData, oTask);
        }
    }
}
