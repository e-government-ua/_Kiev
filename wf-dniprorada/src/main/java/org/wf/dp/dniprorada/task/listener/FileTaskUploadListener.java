package org.wf.dp.dniprorada.task.listener;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.Attachment;
import org.activiti.redis.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wf.dp.dniprorada.model.MimiTypeModel;
import org.activiti.engine.delegate.Expression;

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
	
	private Expression assignee;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateTask task) {
		DelegateExecution execution = task.getExecution();
	//	 List<User> user = execution.getEngineServices().getIdentityService().createUserQuery().memberOfGroup("management_clerk_dmr").list();
		 

		byte[] contentbyte = getRedisService().getAttachments(
				execution.getVariable("attachedId").toString());
		if (contentbyte != null) {

			InputStream content = new ByteArrayInputStream(contentbyte);
			MimiTypeModel mimiType = getMimiType(contentbyte);
			execution
					.getEngineServices()
					.getTaskService()
					.createAttachment(mimiType.getMimiType(), task.getId(),
							execution.getProcessInstanceId(),
							"Копiя паспорта", "attached", content);
		}
	}

	public RedisService getRedisService() {
		return redisService;
	}

	public MimiTypeModel getMimiType(byte[] dataFile) {
		MimiTypeModel mimiTypeModel = new MimiTypeModel();
		try {
			String mimeType = Magic.getMagicMatch(dataFile).getMimeType();
			if (mimeType != null && !mimeType.isEmpty()) {
				mimiTypeModel.setMimiType("application/" + mimeType);
			} else {
				mimiTypeModel.setMimiType("application/octet-stream");
			}

		} catch (MagicParseException e) {
			LOG.info("MagicParseException" + e.getMessage());
		} catch (MagicMatchNotFoundException e) {
			LOG.info("MagicMatchNotFoundException" + e.getMessage());
		} catch (MagicException e) {
			LOG.info("MagicException" + e.getMessage());
		}
		return mimiTypeModel;
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