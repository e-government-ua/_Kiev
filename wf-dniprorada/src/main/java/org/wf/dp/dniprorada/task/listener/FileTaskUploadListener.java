package org.wf.dp.dniprorada.task.listener;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.IdentityLink;
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
	static final transient Logger LOG = LoggerFactory.getLogger(FileTaskUploadListener.class);
        //private final Logger log = LoggerFactory.getLogger(FileTaskUploadListener.class);
                        //log.error("inn(0):"+inn);

	@Autowired
	RedisService redisService;

	/**
	 * 
	 */
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

		
		if(users == null || users.size()==0|| users.get(0)==null || users.get(0).getId() == null){
			//TODO  what to do if no user?
		} else {
		// setAuthenticatedUserId первого попавщегося
		execution.getEngineServices().getIdentityService()
				.setAuthenticatedUserId(users.get(0).getId());

		// получить информацию по стартовой форме бп
		StartFormData startformData = execution.getEngineServices()
				.getFormService()
				.getStartFormData(execution.getProcessDefinitionId());

		List<String> filedTypeFile = getListFieldCastomTypeFile(startformData);
                //LOG.info("filedTypeFile="+filedTypeFile.toString());
                System.out.println("filedTypeFile="+filedTypeFile.toString());
		List<String> listValueKeys = getValueFieldWithCastomTypeFile(execution,
				filedTypeFile);
                //LOG.info("listValueKeys="+filedTypeFile.toString());
                System.out.println("listValueKeys="+filedTypeFile.toString());
		List<String> filedName = getListCastomFieldName(startformData);
                //LOG.info("filedName="+filedTypeFile.toString());
                System.out.println("filedName="+filedTypeFile.toString());
		
		if (!listValueKeys.isEmpty()) {
			for (String keyRedis : listValueKeys) {
				if (keyRedis != null && !keyRedis.isEmpty()) {
					byte[] byteFile = getRedisService().getAttachments(keyRedis);
					ByteArrayMultipartFile contentMultipartFile = null;
					try {
						contentMultipartFile = getByteArrayMultipartFileFromRedis(byteFile);
					} catch (ClassNotFoundException | IOException e1) {
						throw new ActivitiException(e1.getMessage(), e1);
					}
					InputStream is = null;
					try {
						is = contentMultipartFile.getInputStream();
					} catch (Exception e) {
						throw new ActivitiException(e.getMessage(), e);
					}
					if (contentMultipartFile != null) {
						String outFilename = null;
						try {
							outFilename = new String(contentMultipartFile
									.getOriginalFilename().getBytes(
											"ISO-8859-1"), "UTF-8");
						} catch (java.io.UnsupportedEncodingException e) {
							throw new ActivitiException(e.getMessage(), e);
						}
						if (!filedName.isEmpty()) {
							for (String name : filedName) {
						execution
								.getEngineServices()
								.getTaskService()
								.createAttachment(
										contentMultipartFile.getContentType()
												+ ";"
												+ contentMultipartFile.getExp(),
										task.getId(),
										execution.getProcessInstanceId(),
										outFilename,
										name, is);
							}
						}
					}
				}
			}
		}
		}
	}

	public RedisService getRedisService() {
		return redisService;
	}

}