package org.wf.dp.dniprorada.engine.task;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.form.StartFormData;
import org.activiti.redis.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wf.dp.dniprorada.base.model.AbstractModelTask;

/**
 * @author inna & BW
 */
@Component("fileTaskUpload")
public class FileTaskUpload extends AbstractModelTask implements JavaDelegate {
    public static final String BUILDER_ATACH_MODEL_LIST = "builderAtachModel";
    static final transient Logger LOG = LoggerFactory
            .getLogger(FileTaskUpload.class);
    @Autowired
    RedisService redisService;

    //@Autowired
    //private BaseEntityDao baseEntityDao;

    //@Autowired
    //private FlowSlotDao flowSlotDao;

    //@Autowired
    //private FlowSlotTicketDao oFlowSlotTicketDao;

    /**
     * Получить ид поля с кастомным типом file
     *
     * @param oFormData
     * @return
     */
    /*public static List<String> getListField_QueueDataFormType(FormData oFormData) {
		List<String>asFieldID = new ArrayList<String>();
		List<FormProperty> aFormProperty = oFormData.getFormProperties();
		if(!aFormProperty.isEmpty()){
                    for (FormProperty oFormProperty : aFormProperty) {
                            if(oFormProperty.getType() instanceof QueueDataFormType){
                                    asFieldID.add(oFormProperty.getId());
                            }
                    }
		}
		return asFieldID;
	}*/
    @Override
    public void execute(DelegateExecution oExecution) throws Exception {
                /*if(1==1){
                    return;
                }*/
        StartFormData oStartformData = oExecution.getEngineServices()
                .getFormService()
                .getStartFormData(oExecution.getProcessDefinitionId());
                
                /*
                LOG.info("SCAN:file");
                
		List<String> asFieldID = getListFieldCastomTypeFile(oStartformData);
                LOG.info("asFieldID="+asFieldID);
		List<String> asFieldValue = getVariableValues(oExecution, asFieldID);
                LOG.info("asFieldValue="+asFieldValue);
                
                List<String> asFieldName = getListCastomFieldName(oStartformData);
                LOG.info("asFieldName="+asFieldName.toString());
                
                
		List<BuilderAtachModel> aBuilderAtachModel = new ArrayList<BuilderAtachModel>();
		if (!asFieldValue.isEmpty()) {
			for (String sKeyRedis : asFieldValue) {
                                LOG.info("_sKeyRedis="+sKeyRedis);
				if (sKeyRedis != null && !sKeyRedis.isEmpty() && !"".equals(sKeyRedis.trim()) && !"null".equals(sKeyRedis.trim())) {
					byte[] byteFile = getRedisService().getAttachments(sKeyRedis);
					ByteArrayMultipartFile oByteArrayMultipartFile = getByteArrayMultipartFileFromRedis(byteFile);
					if (oByteArrayMultipartFile != null) {
                                            
                                            String sFileName = null;
                                            try {
                                                sFileName = new String(oByteArrayMultipartFile.getOriginalFilename().getBytes(), "UTF-8");
                                            } catch (java.io.UnsupportedEncodingException e) {
                                                LOG.error("on getting sFileName", e);
                                                throw new ActivitiException(e.getMessage(), e);
                                            }
                                            LOG.info("sFileName=" + sFileName);
                                            
                                            //===
                                            BuilderAtachModel oBuilderAtachModel = new BuilderAtachModel();
                                            oBuilderAtachModel.setByteToStringContent(contentByteToString(oByteArrayMultipartFile.getBytes()));
                                            oBuilderAtachModel.setContentType(oByteArrayMultipartFile.getContentType());
                                            oBuilderAtachModel.setExp(oByteArrayMultipartFile.getExp());
                                            oBuilderAtachModel.setOriginalFilename(sFileName);
                                            oBuilderAtachModel.setName(oByteArrayMultipartFile.getName());
                                            aBuilderAtachModel.add(oBuilderAtachModel);
                                            //===
					}
				}
			}
		}
                
		if (!aBuilderAtachModel.isEmpty()) {
			oExecution.setVariable(BUILDER_ATACH_MODEL_LIST, aBuilderAtachModel);
		}
                */

        scanExecutionOnQueueTickets(oExecution, oStartformData); //oTask);//startformData
    }

    public RedisService getRedisService() {
        return redisService;
    }

}