package org.wf.dp.dniprorada.task.listener;

import java.util.List;
import java.util.Set;

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

/**
 * 
 * @author inna
 * 
 */
@Component("fileTaskUploadListener")
public class FileTaskUploadListener extends AbstractModelTask implements TaskListener {

	static final transient Logger LOG = LoggerFactory.getLogger(FileTaskUploadListener.class);

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

		//System.out.println("Did we found any user?  " + users);
                LOG.info("Did we found any user?  " + users);
                
		if(users == null || users.size()==0|| users.get(0)==null || users.get(0).getId() == null){
			//TODO  what to do if no user?
		} else {
		// setAuthenticatedUserId первого попавщегося
        //TODO Shall we implement some logic for user selection.
		execution.getEngineServices().getIdentityService()
				.setAuthenticatedUserId(users.get(0).getId());

		// получить информацию по стартовой форме бп
		FormData startformData = execution.getEngineServices()
				.getFormService()
				.getStartFormData(execution.getProcessDefinitionId());
			//System.out.println("beginning of addAttachmentsToTask(startformData, task)");
                        LOG.info("beginning of addAttachmentsToTask(startformData, task):execution.getProcessDefinitionId()="+execution.getProcessDefinitionId());
			addAttachmentsToTask(startformData, task);
		}
	}
}
