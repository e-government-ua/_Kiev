package org.wf.dp.dniprorada.task.listener;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.redis.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * @author inna
 * 
 */
@Component("fileTaskUploadListener")
public class FileTaskUploadListener implements TaskListener {
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
		
		byte[] contentbyte=null;
			contentbyte = getRedisService().getAttachments(
					execution.getVariable("attachedId").toString());
			LOG.info("contentbyte"+ contentbyte);
		
		if(contentbyte!=null){
			
		InputStream content = new ByteArrayInputStream(contentbyte);
		LOG.info("InputStream"+ content);
		execution
				.getEngineServices()
				.getTaskService()
				.createAttachment("application/properties", task.getId(),
						execution.getProcessInstanceId(), "attached",
						"attached", content);
		}
	}

	public RedisService getRedisService() {
		return redisService;
	}

}