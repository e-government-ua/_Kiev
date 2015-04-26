package org.wf.dp.dniprorada.task.listener;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.redis.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wf.dp.dniprorada.base.model.AbstractModelTask;
import org.wf.dp.dniprorada.model.MimiTypeModel;

/**
 * 
 * @author inna
 * 
 */
@Component("fileTaskUploadListener")
public class FileTaskUploadListener extends AbstractModelTask implements TaskListener {
	static final transient Logger LOG = LoggerFactory
			.getLogger(FileTaskUploadListener.class);
	@Autowired
	RedisService redisService;
	
	private Expression assignee;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateTask task) {
		DelegateExecution execution = task.getExecution();
	//	 List<User> user = execution.getEngineServices().getIdentityService().createUserQuery().memberOfGroup("management_clerk_dmr").list();
		task.setAssignee(getStringFromFieldExpression(this.assignee, execution));

		//String keys = getStringFromFieldExpression(this.attachedId, execution);
		List<String> listKeys = getListKeysRedis(execution.getVariable("attachedId").toString());
		if (!listKeys.isEmpty()) {
			for (String keyRedis : listKeys) {
				 byte[] contentbyte = getRedisService().getAttachments(keyRedis);
				if (contentbyte != null) {

			InputStream content = new ByteArrayInputStream(contentbyte);
			MimiTypeModel mimiType = getMimiType(contentbyte);
			execution
					.getEngineServices()
					.getTaskService()
					.createAttachment(mimiType.getMimiType(), task.getId(),
							execution.getProcessInstanceId(),
							keyRedis, "attached", content);
		}
			}
		}
	}

	public RedisService getRedisService() {
		return redisService;
	}

	
	
}