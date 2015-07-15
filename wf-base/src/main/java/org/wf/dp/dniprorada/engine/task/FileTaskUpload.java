package org.wf.dp.dniprorada.engine.task;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.form.FormData;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.redis.model.ByteArrayMultipartFile;
import org.activiti.redis.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wf.dp.dniprorada.base.dao.BaseEntityDao;
import org.wf.dp.dniprorada.base.dao.FlowSlotDao;
import org.wf.dp.dniprorada.base.dao.FlowSlotTicketDao;
import org.wf.dp.dniprorada.base.model.AbstractModelTask;
import static org.wf.dp.dniprorada.base.model.AbstractModelTask.getListCastomFieldName;
import static org.wf.dp.dniprorada.base.model.AbstractModelTask.getValueFieldWithCastomTypeFile;
import org.wf.dp.dniprorada.base.model.FlowSlotTicket;
import org.wf.dp.dniprorada.base.util.JsonRestUtils;
import org.wf.dp.dniprorada.base.viewobject.flow.SaveFlowSlotTicketResponse;
import org.wf.dp.dniprorada.form.QueueDataFormType;
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

        @Autowired
        private BaseEntityDao baseEntityDao;

   @Autowired
   private FlowSlotDao flowSlotDao;

   @Autowired
   private FlowSlotTicketDao oFlowSlotTicketDao;

        
	/**
	 * РџРѕР»СѓС‡РёС‚СЊ РёРґ РїРѕР»СЏ СЃ РєР°СЃС‚РѕРјРЅС‹Рј С‚РёРїРѕРј file
	 * @param oFormData
	 * @return
	 */
	public static List<String> getListField_QueueDataFormType(FormData oFormData) {
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
	}
        
        
	@Override
	public void execute(DelegateExecution execution) throws Exception {

		StartFormData startformData = execution.getEngineServices()
				.getFormService()
				.getStartFormData(execution.getProcessDefinitionId());
                

                LOG.info("SCAN:file");
                
		List<String> asFieldID = getListFieldCastomTypeFile(startformData);
                LOG.info("asFieldID="+asFieldID);
		List<String> asFieldValue = getValueFieldWithCastomTypeFile(execution, asFieldID);
                LOG.info("asFieldValue="+asFieldValue);
                
                List<String> asFieldName = getListCastomFieldName(startformData);
                LOG.info("asFieldName="+asFieldName.toString());
                
                
		List<BuilderAtachModel> listModel = new ArrayList<BuilderAtachModel>();
		if (!asFieldValue.isEmpty()) {
			for (String sKeyRedis : asFieldValue) {
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
                        /* TODO происходит ошибка всегда после стартовой формы! А зачем атачить еще раз, когда выше уже атач делается?  Тут нужно только сами поля обновить (засетить ИД атачментов)
			for (BuilderAtachModel builder : listModel){
				execution.getEngineServices().getTaskService().createAttachment(builder.getContentType(), 
						execution.getId(), execution.getProcessInstanceId(), builder.getName(), builder.getOriginalFilename(), builder.getByteToStringContent());
			}
                        */
		}
                
                
                
                LOG.info("SCAN:queueData");
                asFieldID = getListField_QueueDataFormType(startformData);
                LOG.info("asFieldID="+asFieldID.toString());
                asFieldValue = getValueFieldWithCastomTypeFile(execution, asFieldID);
                LOG.info("asFieldValue="+asFieldValue.toString());
                //asFieldName = getListCastomFieldName(startformData);
                //LOG.info("asFieldName="+asFieldName.toString());
                if (!asFieldValue.isEmpty()) {
                    String sValue = asFieldValue.get(0);
                    LOG.info("sValue=" + sValue);
                    long nID_FlowSlotTicket=0;
                    int nAt=sValue.indexOf(":");
                    int nTo=sValue.indexOf(",");
                    String s=sValue.substring(nAt+1,nTo);
                    LOG.info("s=" + s);
                    try{
                        nID_FlowSlotTicket = Long.valueOf(s);
                        LOG.info("nID_FlowSlotTicket:Ok!");
                    }catch(Exception oException){
                        LOG.error(oException.getMessage());
                        nID_FlowSlotTicket=1;
                    }
                    LOG.info("nID_FlowSlotTicket=" + nID_FlowSlotTicket);
                    try{
                        
                        
                        FlowSlotTicket oFlowSlotTicket = baseEntityDao.getById(FlowSlotTicket.class, nID_FlowSlotTicket);
                        if (oFlowSlotTicket == null) {
                            LOG.error("FlowSlotTicket with id=" + nID_FlowSlotTicket + " is not found!");
                        }else{
                            long nID_FlowSlot=oFlowSlotTicket.getoFlowSlot().getId();
                            LOG.error("nID_FlowSlot="+nID_FlowSlot);
                            long nID_Subject = oFlowSlotTicket.getnID_Subject();
                            LOG.error("nID_Subject="+nID_Subject);
                            long nID_Task_Activiti = 1; //TODO set real ID!!!
                            try{
                                /*
                                LOG.info("execution.getBusinessKey()="+execution.getBusinessKey());
                                LOG.info("execution.getCurrentActivityId()="+execution.getCurrentActivityId());
                                LOG.info("execution.getCurrentActivityName()="+execution.getCurrentActivityName());
                                LOG.info("execution.getEventName()="+execution.getEventName());
                                LOG.info("execution.getId()="+execution.getId());
                                LOG.info("execution.getParentId()="+execution.getParentId());
                                LOG.info("execution.getProcessBusinessKey()="+execution.getProcessBusinessKey());
                                LOG.info("execution.getProcessDefinitionId()="+execution.getProcessDefinitionId());
                                LOG.info("execution.getProcessInstanceId()="+execution.getProcessInstanceId());//THIS!!!
                                LOG.info("execution.getTenantId()="+execution.getTenantId());
                                */
/*                                
2015-07-05_15:33:11.144 | INFO | org.wf.dp.dniprorada.engine.task.FileTaskUpload- execution.getBusinessKey()=null
2015-07-05_15:33:11.144 | INFO | org.wf.dp.dniprorada.engine.task.FileTaskUpload- execution.getCurrentActivityId()=servicetask1
2015-07-05_15:33:11.144 | INFO | org.wf.dp.dniprorada.engine.task.FileTaskUpload- execution.getCurrentActivityName()=ПіпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ
2015-07-05_15:33:11.144 | INFO | org.wf.dp.dniprorada.engine.task.FileTaskUpload- execution.getEventName()=null
2015-07-05_15:33:11.144 | INFO | org.wf.dp.dniprorada.engine.task.FileTaskUpload- execution.getId()=955057
2015-07-05_15:33:11.144 | INFO | org.wf.dp.dniprorada.engine.task.FileTaskUpload- execution.getParentId()=955001
2015-07-05_15:33:11.144 | INFO | org.wf.dp.dniprorada.engine.task.FileTaskUpload- execution.getProcessBusinessKey()=key
2015-07-05_15:33:11.144 | INFO | org.wf.dp.dniprorada.engine.task.FileTaskUpload- execution.getProcessDefinitionId()=kiev_mreo_1:126:942617
2015-07-05_15:33:11.144 | INFO | org.wf.dp.dniprorada.engine.task.FileTaskUpload- execution.getProcessInstanceId()=955001
2015-07-05_15:33:11.144 | INFO | org.wf.dp.dniprorada.engine.task.FileTaskUpload- execution.getTenantId()=
*/                                
                                try{
                                    nID_Task_Activiti = Long.valueOf(execution.getProcessInstanceId());
                                    LOG.info("nID_Task_Activiti:Ok!");
                                }catch(Exception oException){
                                    LOG.error(oException.getMessage());
                                }
                                //execution.getCurrentActivityId()
                                //nID_Task_Activiti
                            }catch(Exception oException){
                                LOG.error(oException.getMessage());
                            }
                            LOG.error("nID_Task_Activiti="+nID_Task_Activiti);
                            oFlowSlotTicket.setnID_Task_Activiti(nID_Task_Activiti);
                            baseEntityDao.saveOrUpdate(oFlowSlotTicket);
                            LOG.info("JSON:" + JsonRestUtils.toJsonResponse(new SaveFlowSlotTicketResponse(oFlowSlotTicket.getId())));
                        }
                        
                        
                    }catch(Exception oException){
                        LOG.error(oException.getMessage());
                    }

                }                
                
                

	}

	

	public RedisService getRedisService() {
		return redisService;
	}

}