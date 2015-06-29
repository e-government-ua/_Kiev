package org.wf.dp.dniprorada.engine.task;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.form.StartFormData;
import org.activiti.redis.model.ByteArrayMultipartFile;
import org.activiti.redis.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wf.dp.dniprorada.base.model.AbstractModelTask;
import org.wf.dp.dniprorada.model.BuilderAtachModel;

/**
 * 
 * @author inna & BW
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

		StartFormData startformData = execution.getEngineServices()
				.getFormService()
				.getStartFormData(execution.getProcessDefinitionId());
		List<String> asFiledTypeFile = getListFieldCastomTypeFile(startformData);
                LOG.info("asFiledTypeFile="+asFiledTypeFile);
		List<String> asValueKey = getValueFieldWithCastomTypeFile(execution, asFiledTypeFile);
                LOG.info("asValueKey="+asValueKey);
		List<BuilderAtachModel> listModel = new ArrayList<BuilderAtachModel>();
		if (!asValueKey.isEmpty()) {
			for (String sKeyRedis : asValueKey) {
                                LOG.info("_sKeyRedis="+sKeyRedis);
				if (sKeyRedis != null && !sKeyRedis.isEmpty() && !"".equals(sKeyRedis.trim()) && !"null".equals(sKeyRedis.trim())) {
					byte[] byteFile = getRedisService().getAttachments(sKeyRedis);
					ByteArrayMultipartFile contentMultipartFile = getByteArrayMultipartFileFromRedis(byteFile);
					if (contentMultipartFile != null) {
						String outFilename = null;
				        try {
				            outFilename = new String(contentMultipartFile
									.getOriginalFilename().getBytes("ISO-8859-1"), "UTF-8");
				        } catch (java.io.UnsupportedEncodingException e) {
				        	throw new ActivitiException(e.getMessage(), e);
				        }
						BuilderAtachModel builderAtachModel = new BuilderAtachModel();
						builderAtachModel
								.setByteToStringContent(contentByteToString(contentMultipartFile
										.getBytes()));
						builderAtachModel.setContentType(contentMultipartFile
								.getContentType());
						builderAtachModel.setExp(contentMultipartFile.getExp());
						builderAtachModel
								.setOriginalFilename(outFilename);
						builderAtachModel.setName(contentMultipartFile
								.getName());
						listModel.add(builderAtachModel);

					}
				}
			}
		}

		if (!listModel.isEmpty()) {
			execution.setVariable(BUILDER_ATACH_MODEL_LIST, listModel);
		}

	}

	

	public RedisService getRedisService() {
		return redisService;
	}

}