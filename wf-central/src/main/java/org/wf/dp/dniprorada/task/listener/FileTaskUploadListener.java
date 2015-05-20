package org.wf.dp.dniprorada.task.listener;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.redis.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wf.dp.dniprorada.model.MimiTypeModel;

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

		byte[] contentbyte = getRedisService().getAttachments(
				execution.getVariable("attachedId").toString());
		if (contentbyte != null) {

			InputStream content = new ByteArrayInputStream(contentbyte);
			MimiTypeModel mimiType = getMimiType(contentbyte);
			execution
					.getEngineServices()
					.getTaskService()
					.createAttachment(mimiType.getExtension(), task.getId(),
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
			String exe = Magic.getMagicMatch(dataFile).getExtension();
			if (exe != null && !exe.isEmpty()) {
				mimiTypeModel.setExtension("application/" + exe);
			} else {
				mimiTypeModel.setExtension("application/octet-stream");
			}

		} catch (MagicParseException e) {
			LOG.warn("MagicParseException", e);
		} catch (MagicMatchNotFoundException e) {
			LOG.warn("MagicMatchNotFoundException", e);
		} catch (MagicException e) {
			LOG.warn("MagicException", e);
		}
		return mimiTypeModel;
	}

}