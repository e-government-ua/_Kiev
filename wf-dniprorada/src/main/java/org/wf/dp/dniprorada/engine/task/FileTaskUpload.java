package org.wf.dp.dniprorada.engine.task;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.form.StartFormData;
import org.activiti.redis.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wf.dp.dniprorada.base.model.AbstractModelTask;
import org.wf.dp.dniprorada.model.BuilderAtachModel;
import org.wf.dp.dniprorada.model.MimiTypeModel;

/**
 * 
 * @author inna
 * 
 */
@Component("fileTaskUpload")
public class FileTaskUpload extends AbstractModelTask implements JavaDelegate {
	static final transient Logger LOG = LoggerFactory
			.getLogger(FileTaskUpload.class);

	public static final String BUILDER_ATACH_MODEL_LIST = "builderAtachModel";

	@Autowired
	RedisService redisService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		StartFormData startformData = execution.getEngineServices().getFormService().getStartFormData(execution.getProcessDefinitionId());
		List<String> filedTypeFile = getListFieldCastomTypeFile(startformData);
		List<String> listValueKeys = getValueFieldWithCastomTypeFile(execution,
				filedTypeFile);
		List<BuilderAtachModel> listModel = new ArrayList<BuilderAtachModel>();
		if (!listValueKeys.isEmpty()) {
			for (String keyRedis : listValueKeys) {
				 byte[] contentbyte = getRedisService().getAttachments(keyRedis);
				if (contentbyte != null) {
					String byteToStringContent = contentByteToString(contentbyte);
					MimiTypeModel mimiType = getMimiType(contentbyte);

					BuilderAtachModel builderAtachModel = new BuilderAtachModel();
					builderAtachModel
							.setByteToStringContent(byteToStringContent);
					builderAtachModel.setContentType(mimiType.getMimiType());
					builderAtachModel.setExp(mimiType.getExtension());
					builderAtachModel.setOriginalFilename(keyRedis);

					listModel.add(builderAtachModel);
				}
			}
		}

		if (!listModel.isEmpty()) {
			execution.setVariable(BUILDER_ATACH_MODEL_LIST,
					listModel);
		}
		
	}


	public RedisService getRedisService() {
		return redisService;
	}

}