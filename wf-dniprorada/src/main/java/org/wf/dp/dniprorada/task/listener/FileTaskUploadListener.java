package org.wf.dp.dniprorada.task.listener;

import java.io.InputStream;
import java.util.List;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.identity.User;
import org.activiti.redis.model.ByteArrayMultipartFile;
import org.activiti.redis.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wf.dp.dniprorada.base.model.AbstractModelTask;

/**
 * 
 * @author inna
 * 
 */
@Component("fileTaskUploadListener")
public class FileTaskUploadListener extends AbstractModelTask implements
		TaskListener {
	static final transient Logger LOG = LoggerFactory
			.getLogger(FileTaskUploadListener.class);
	@Autowired
	RedisService redisService;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateTask task) {
		DelegateExecution execution = task.getExecution();
		List<User> user = execution.getEngineServices().getIdentityService()
				.createUserQuery().memberOfGroup("management_clerk_dmr").list();
		execution.getEngineServices().getIdentityService()
				.setAuthenticatedUserId(user.get(0).getId());
		StartFormData startformData = execution.getEngineServices()
				.getFormService()
				.getStartFormData(execution.getProcessDefinitionId());
		List<String> filedTypeFile = getListFieldCastomTypeFile(startformData);
		List<String> listValueKeys = getValueFieldWithCastomTypeFile(execution,
				filedTypeFile);
		if (!listValueKeys.isEmpty()) {
			for (String keyRedis : listValueKeys) {
				if (keyRedis != null && !keyRedis.isEmpty()) {
					ByteArrayMultipartFile contentMultipartFile = getRedisService()
							.getAttachObjFromRedis(keyRedis);
					InputStream is = null;
					try {
						is = contentMultipartFile.getInputStream();
					} catch (Exception e) {
						throw new ActivitiException(e.getMessage(), e);
					}
					if (contentMultipartFile != null) {
						execution
								.getEngineServices()
								.getTaskService()
								.createAttachment(
										contentMultipartFile.getContentType()
												+ ";"
												+ contentMultipartFile.getExp(),
										task.getId(),
										execution.getProcessInstanceId(),
										contentMultipartFile
												.getOriginalFilename(),
										contentMultipartFile.getName(), is);
					}
				}
			}
		}

	}

	public RedisService getRedisService() {
		return redisService;
	}

}