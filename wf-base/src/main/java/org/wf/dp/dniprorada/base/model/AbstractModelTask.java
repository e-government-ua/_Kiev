package org.wf.dp.dniprorada.base.model;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.form.FormData;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.task.Attachment;
import org.activiti.redis.model.ByteArrayMultipartFile;
import org.activiti.redis.service.RedisService;
import org.activiti.rest.controller.Renamer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.wf.dp.dniprorada.base.dao.FlowSlotDao;
import org.wf.dp.dniprorada.base.dao.FlowSlotTicketDao;
import org.wf.dp.dniprorada.base.util.JsonRestUtils;
import org.wf.dp.dniprorada.base.viewobject.flow.SaveFlowSlotTicketResponse;
import org.wf.dp.dniprorada.form.FormFileType;
import org.wf.dp.dniprorada.form.QueueDataFormType;
import org.wf.dp.dniprorada.model.MimiTypeModel;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class AbstractModelTask {

    public static final String LIST_KEY_PREFIX = "lst";
    public static final String LIST_KEY_DELIM = ":";
    static final transient Logger LOG = LoggerFactory
            .getLogger(AbstractModelTask.class);
    @Autowired
    protected FlowSlotDao flowSlotDao;
    @Autowired
    protected FlowSlotTicketDao oFlowSlotTicketDao;
    @Autowired
    RedisService redisService;

    /**
     * Возвращает сложгый ключ переменной
     * бизнес-процесса
     *
     * @param listKey
     * @param elementKey
     * @return
     */
    private static String getExecutionVarKey(String listKey, String elementKey) {
        return new StringBuilder().append(LIST_KEY_PREFIX)
                .append(LIST_KEY_DELIM)
                .append(listKey)
                .append(LIST_KEY_DELIM)
                .append(elementKey).toString();
    }

    /**
     * Получение ContentType файла
     *
     * @param dataFile
     * @return
     */
    public static MimiTypeModel getMimiType(byte[] dataFile) {
        MimiTypeModel mimiTypeModel = new MimiTypeModel();
        try {
            String exe = Magic.getMagicMatch(dataFile).getExtension();
            if (exe != null && !exe.isEmpty()) {
                mimiTypeModel.setExtension(exe);
            } else {
                mimiTypeModel.setExtension("application/octet-stream");
            }
            String mimeType = Magic.getMagicMatch(dataFile).getMimeType();
            if (mimeType != null && !mimeType.isEmpty()) {
                mimiTypeModel.setMimiType(mimeType);
            } else {
                mimiTypeModel.setMimiType("application/octet-stream");
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

    /**
     * Конверт Byte To String
     *
     * @param contentbyte
     * @return
     */
    public static String contentByteToString(byte[] contentbyte) {
        BASE64Encoder encoder = new BASE64Encoder();
        String byteToStringContent = encoder.encode(contentbyte);
        return byteToStringContent;
    }

    /**
     * Конверт String to Byte
     *
     * @param contentString
     * @return
     * @throws java.io.IOException
     */
    public static byte[] contentStringToByte(String contentString) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] contentbyte = decoder.decodeBuffer(contentString);
        return contentbyte;
    }

    public static String getStringFromFieldExpression(Expression expression,
            DelegateExecution execution) {
        if (expression != null) {
            Object value = expression.getValue(execution);
            if (value != null) {
                return value.toString();
            }
        }
        return null;
    }

    /**
     * Получить
     *
     * @param keyRedis
     * @return
     */
    public static List<String> getListKeysRedis(String keyRedis) {
        List<String> listKeys = new ArrayList<String>();
        if (keyRedis != null && !keyRedis.isEmpty()) {
            String[] keys = keyRedis.split(";");
            listKeys = Arrays.asList(keys);
            return listKeys;
        }
        return listKeys;
    }

    public static List<String> getVariableValues(DelegateExecution execution, List<String> formFieldIds) {
        return getVariableValues(execution.getEngineServices().getRuntimeService(), execution.getProcessInstanceId(),
                formFieldIds);
    }

    public static List<String> getVariableValues(RuntimeService runtimeService, String processInstanceId,
            List<String> formFieldIds) {
        List<String> listValueKeys = new ArrayList<String>();
        if (!formFieldIds.isEmpty()) {
            Map<String, Object> variables = runtimeService.getVariables(
                    processInstanceId);
            for (String fieldId : formFieldIds) {
                if (variables.containsKey(fieldId)) {
                    listValueKeys.add(String.valueOf(variables.get(fieldId)));
                }
            }
        }
        return listValueKeys;
    }

    /**
     * Получить ид поля с кастомным типом file
     *
     * @param oFormData
     * @return
     */
    public static List<String> getListFieldCastomTypeFile(FormData oFormData) {
        List<String> asFieldID = new ArrayList<String>();
        List<FormProperty> aFormProperty = oFormData.getFormProperties();
        if (!aFormProperty.isEmpty()) {
            for (FormProperty oFormProperty : aFormProperty) {
                if (oFormProperty.getType() instanceof FormFileType) {
                    asFieldID.add(oFormProperty.getId());
                }
            }
        }
        return asFieldID;
    }

    /**
     * @param oFormData form data of process
     * @return variable ids with custom property type QueueDataFormType
     */
    public static List<String> getListField_QueueDataFormType(FormData oFormData) {
        List<String> asFieldID = new ArrayList<String>();
        List<FormProperty> aFormProperty = oFormData.getFormProperties();
        if (!aFormProperty.isEmpty()) {
            for (FormProperty oFormProperty : aFormProperty) {
                if (oFormProperty.getType() instanceof QueueDataFormType) {
                    asFieldID.add(oFormProperty.getId());
                }
            }
        }
        return asFieldID;
    }

    /**
     * Получить имя поля
     *
     * @param formData
     * @return
     */
    public static List<String> getListCastomFieldName(FormData formData) {
        List<String> filedName = new ArrayList<String>();
        List<FormProperty> formDataList = formData.getFormProperties();
        if (!formDataList.isEmpty()) {
            for (FormProperty prop : formDataList) {
                if (prop.getType() instanceof FormFileType) {
                    filedName.add(prop.getName());
                }
            }
        }
        return filedName;
    }

    public static String getCastomFieldValue(FormData formData, String fieldName) {
        List<FormProperty> formDataList = formData.getFormProperties();
        if (!formDataList.isEmpty()) {
            for (FormProperty prop : formDataList) {
                if (prop.getType() instanceof FormFileType && prop.getName().equalsIgnoreCase(fieldName)) {
                    return (prop.getValue() != null ? prop.getValue() : "");
                }
            }
        }
        return "";
    }

    public static ByteArrayOutputStream multipartFileToByteArray(MultipartFile file) throws IOException {
        return multipartFileToByteArray(file, null);
    }

    /**
     * multipartFile To ByteArray
     *
     * @param file
     * @return
     * @throws java.io.IOException
     */
    public static ByteArrayOutputStream multipartFileToByteArray(MultipartFile file, String sFileNameReal)
            throws IOException {

        System.out.println("sFileNameReal=" + sFileNameReal);

        String sFilename = new String(file.getOriginalFilename().getBytes(), "Cp1251");//UTF-8
        System.out.println("sFilename=" + sFilename);

        String sFilename1 = new String(file.getOriginalFilename().getBytes(Charset.forName("UTF-8")));//UTF-8
        System.out.println("sFilename1=" + sFilename1);
        String sFilename2 = new String(file.getOriginalFilename().getBytes(), "UTF-8");//UTF-8
        System.out.println("sFilename2=" + sFilename2);
        String sFilename3 = new String(file.getOriginalFilename().getBytes(Charset.forName("Cp1251")));//UTF-8
        System.out.println("sFilename3=" + sFilename3);
        String sFilename4 = new String(file.getOriginalFilename().getBytes());//UTF-8
        System.out.println("sFilename4=" + sFilename4);

        String sFilename0 = file.getOriginalFilename();//UTF-8
        System.out.println("sFilename0=" + sFilename0);

        //sFilename = Renamer.sRenamed(sFilename0);
        String sFilenameNew0 = Renamer.sRenamed(sFilename0);
        System.out.println("sFilenameNew0=" + sFilenameNew0);
        String sFilenameNew1 = Renamer.sRenamed(sFilename1);
        System.out.println("sFilenameNew1=" + sFilenameNew1);
        String sFilenameNew2 = Renamer.sRenamed(sFilename2);
        System.out.println("sFilenameNew2=" + sFilenameNew2);
        String sFilenameNew3 = Renamer.sRenamed(sFilename3);
        System.out.println("sFilenameNew3=" + sFilenameNew3);
        String sFilenameNew4 = Renamer.sRenamed(sFilename4);
        System.out.println("sFilenameNew4=" + sFilenameNew4);

        //sFilename=sFilenameNew;
        System.out.println("sFilename(new)=" + sFilename);

        ByteArrayMultipartFile byteArrayMultipartFile = new ByteArrayMultipartFile(
                file.getBytes(), file.getName(), sFileNameReal == null ? sFilename1 : sFileNameReal,
                file.getContentType());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);
        oos.writeObject(byteArrayMultipartFile);
        oos.flush();
        oos.close();
        return byteArrayOutputStream;
    }

    /**
     * ByteArray To multipartFile
     *
     * @param byteFile
     * @return
     * @throws java.io.IOException
     * @throws ClassNotFoundException
     */
    public static ByteArrayMultipartFile getByteArrayMultipartFileFromRedis(
            byte[] byteFile) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteFile);
        ObjectInputStream ois = new ObjectInputStream(byteArrayInputStream);
        ByteArrayMultipartFile contentMultipartFile = (ByteArrayMultipartFile) ois.readObject();
        ois.close();
        return contentMultipartFile;
    }

    /**
     * Получить список по ключу списка из execution
     *
     * @param listKey
     * @param execution
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getListVariable(String listKey, DelegateExecution execution) {
        List<T> result = new ArrayList<T>();

        String keyPrefix = LIST_KEY_PREFIX + LIST_KEY_DELIM + listKey;

        for (String execVarKey : execution.getVariableNames()) {
            if (execVarKey.startsWith(keyPrefix)) {
                result.add((T) execution.getVariable(execVarKey));
            }
        }
        return result;
    }

    /**
     * Сохранить список обьектов в execution
     *
     * @param listKey
     * @param list
     * @param execution
     */
    public <T extends ListKeyable> void setListVariable(String listKey, List<T> list, DelegateExecution execution) {
        for (ListKeyable listKeyable : list) {
            execution.setVariable(
                    getExecutionVarKey(listKey, listKeyable.getKey()),
                    listKeyable);
        }
    }

    /**
     * Adds Attachemnts based on formData to task.
     *
     * @param oFormData FormData from task where we search file fields.
     * @param oTask     where we add Attachments.
     */
    public void addAttachmentsToTask(FormData oFormData, DelegateTask oTask) {
        DelegateExecution oExecution = oTask.getExecution();

        LOG.info("SCAN:file");
        List<String> asFieldID = getListFieldCastomTypeFile(oFormData);
        LOG.info("[addAttachmentsToTask]");
        LOG.info("asFieldID=" + asFieldID.toString());
        List<String> asFieldValue = getVariableValues(oExecution, asFieldID);
        LOG.info("asFieldValue=" + asFieldValue.toString());
        List<String> asFieldName = getListCastomFieldName(oFormData);
        LOG.info("asFieldName=" + asFieldName.toString());
        if (!asFieldValue.isEmpty()) {
            int n = 0;
            for (String sKeyRedis : asFieldValue) {
                LOG.info("sKeyRedis=" + sKeyRedis);
                if (sKeyRedis != null && !sKeyRedis.isEmpty() && !"".equals(sKeyRedis.trim()) && !"null"
                        .equals(sKeyRedis.trim()) && sKeyRedis.length() > 15) {
                    if (!asFieldName.isEmpty() && n < asFieldName.size()) {
                        //String sDescription = asFieldName.get((asFieldName.size() - 1) - n);
                        String sDescription = asFieldName.get(n);
                        LOG.info("sDescription=" + sDescription);
                        String sID_Field = asFieldID.get(n);
                        LOG.info("sID_Field=" + sID_Field);

                        byte[] aByteFile = getRedisService().getAttachments(sKeyRedis);
                        ByteArrayMultipartFile oByteArrayMultipartFile = null;
                        try {
                            oByteArrayMultipartFile = getByteArrayMultipartFileFromRedis(aByteFile);
                        } catch (ClassNotFoundException | IOException e1) {
                            throw new ActivitiException(e1.getMessage(), e1);
                        }
                        if (oByteArrayMultipartFile != null) {
                            String sFileName = null;
                            try {
                                sFileName = new String(oByteArrayMultipartFile.getOriginalFilename().getBytes(),
                                        "UTF-8");
                            } catch (java.io.UnsupportedEncodingException e) {
                                LOG.error("on getting sFileName", e);
                                throw new ActivitiException(e.getMessage(), e);
                            }
                            LOG.info("sFileName=" + sFileName);

                            //===
                            InputStream oInputStream = null;
                            try {
                                oInputStream = oByteArrayMultipartFile.getInputStream();
                            } catch (Exception e) {
                                throw new ActivitiException(e.getMessage(), e);
                            }
                            Attachment oAttachment = oExecution.getEngineServices().getTaskService().createAttachment(
                                    oByteArrayMultipartFile.getContentType() + ";" + oByteArrayMultipartFile.getExp(),
                                    oTask.getId(), oExecution.getProcessInstanceId(), sFileName, sDescription,
                                    oInputStream);

                            if (oAttachment != null) {
                                String nID_Attachment = oAttachment.getId();
                                //LOG.info("nID_Attachment=" + nID_Attachment);
                                LOG.info("Try set variable(sID_Field) '" + sID_Field
                                        + "' with the value(nID_Attachment) '" + nID_Attachment
                                        + "', for new attachment...");
                                oExecution.getEngineServices().getRuntimeService()
                                        .setVariable(oExecution.getProcessInstanceId(), sID_Field, nID_Attachment);
                                LOG.info("Finished setting new value for variable with attachment(sID_Field) '"
                                        + sID_Field + "'");
                            } else {
                                LOG.error("Can't add attachment to oTask.getId()=" + oTask.getId());
                            }
                            //===

                        } else {
                            LOG.error("oByteArrayMultipartFile==null! aByteFile=" + aByteFile.toString());
                        }
                    } else {
                        LOG.error("asFieldName has nothing! asFieldName=" + asFieldName);
                    }
                } else {
                    LOG.error("Invalid Redis Key!!! sKeyRedis=" + sKeyRedis);
                }
                n++;
            }
        }

        scanExecutionOnQueueTickets(oExecution, oFormData); //, oTask);//startformData

        /*LOG.info("SCAN:queueData");
         asFieldID = getListField_QueueDataFormType(formData);
         LOG.info("asFieldID="+asFieldID.toString());
         asFieldValue = getVariableValues(execution, asFieldID);
         LOG.info("asFieldValue="+asFieldValue.toString());
         //asFieldName = getListCastomFieldName(formData);
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
         LOG.info("Ok!");
         }catch(Exception oException){
         LOG.error(oException.getMessage());
         nID_FlowSlotTicket=1;
         }
         LOG.info("nID_FlowSlotTicket=" + nID_FlowSlotTicket);
            
         try{
                

         //                FlowSlotTicket oFlowSlotTicket = baseEntityDao.getById(FlowSlotTicket.class, nID_FlowSlotTicket);
         //                if (oFlowSlotTicket == null) {
         //                    LOG.error("FlowSlotTicket with id=" + nID_FlowSlotTicket + " is not found!");
         //                }else{
         //                    long nID_FlowSlot=oFlowSlotTicket.getoFlowSlot().getId();
         //                    LOG.error("nID_FlowSlot="+nID_FlowSlot);
         //                    long nID_Subject = oFlowSlotTicket.getnID_Subject();
         //                    LOG.error("nID_Subject="+nID_Subject);
         //                    long nID_Task_Activiti = 1; //TODO set real ID!!!
         //                    oFlowSlotTicket.setnID_Task_Activiti(nID_Task_Activiti);
         //                    baseEntityDao.saveOrUpdate(oFlowSlotTicket);
         //                    LOG.info("JSON:" + JsonRestUtils.toJsonResponse(new SaveFlowSlotTicketResponse(oFlowSlotTicket.getId())));
         //                }
                
         }catch(Exception oException){
         LOG.error(oException.getMessage());
         }
            
         }*/

    }

    public void scanExecutionOnQueueTickets(DelegateExecution oExecution,
            FormData oFormData) { //DelegateTask oTask) {//StartFormData startformData
        LOG.info("SCAN:queueData");
        List<String> asFieldID = getListField_QueueDataFormType(oFormData);//startformData
        LOG.info("asFieldID=" + asFieldID.toString());
        List<String> asFieldValue = getVariableValues(oExecution, asFieldID);
        LOG.info("asFieldValue=" + asFieldValue.toString());
        //asFieldName = getListCastomFieldName(startformData);
        //LOG.info("asFieldName="+asFieldName.toString());
        if (!asFieldValue.isEmpty()) {
            String sValue = asFieldValue.get(0);
            LOG.info("sValue=" + sValue);
            long nID_FlowSlotTicket = 0;

            //sValue={"nID_FlowSlotTicket":20756,"sDate":"2015-08-22 12:00:00.00"}
            Map<String, Object> m = QueueDataFormType.parseQueueData(sValue);
            //String snID_FlowSlotTicket = (String) m.get("nID_FlowSlotTicket");
            //String snID_FlowSlotTicket = m.get("nID_FlowSlotTicket")+"";
            //Double
            nID_FlowSlotTicket = QueueDataFormType.get_nID_FlowSlotTicket(m);
            //LOG.info("snID_FlowSlotTicket=" + snID_FlowSlotTicket);
            //nID_FlowSlotTicket = Long.valueOf(snID_FlowSlotTicket);
            LOG.info("nID_FlowSlotTicket=" + nID_FlowSlotTicket);
            String sDate = (String) m.get(QueueDataFormType.sDate);
            LOG.info("sDate=" + sDate);

            /*int nAt=sValue.indexOf(":");
             int nTo=sValue.indexOf(",");
             String s=sValue.substring(nAt+1,nTo);
             LOG.info("s=" + s);
             try{
             nID_FlowSlotTicket = Long.valueOf(s);
             Long.valueOf(s)
             LOG.info("nID_FlowSlotTicket:Ok!");
             }catch(Exception oException){
             LOG.error(oException.getMessage());
             nID_FlowSlotTicket=1;
             }*/

            try {

                long nID_Task_Activiti = 1; //TODO set real ID!!!
                try {
                    /*
                     LOG.info("oExecution.getBusinessKey()="+oExecution.getBusinessKey());
                     LOG.info("oExecution.getCurrentActivityId()="+oExecution.getCurrentActivityId());
                     LOG.info("oExecution.getCurrentActivityName()="+oExecution.getCurrentActivityName());
                     LOG.info("oExecution.getEventName()="+oExecution.getEventName());
                     LOG.info("oExecution.getId()="+oExecution.getId());
                     LOG.info("oExecution.getParentId()="+oExecution.getParentId());
                     LOG.info("oExecution.getProcessBusinessKey()="+oExecution.getProcessBusinessKey());
                     LOG.info("oExecution.getProcessDefinitionId()="+oExecution.getProcessDefinitionId());
                     LOG.info("oExecution.getProcessInstanceId()="+oExecution.getProcessInstanceId());//THIS!!!
                     LOG.info("oExecution.getTenantId()="+oExecution.getTenantId());
                     */
                    /*                                
                     2015-07-05_15:33:11.144 | INFO | org.wf.dp.dniprorada.engine.task.FileTaskUpload- oExecution.getBusinessKey()=null
                     2015-07-05_15:33:11.144 | INFO | org.wf.dp.dniprorada.engine.task.FileTaskUpload- oExecution.getCurrentActivityId()=servicetask1
                     2015-07-05_15:33:11.144 | INFO | org.wf.dp.dniprorada.engine.task.FileTaskUpload- oExecution.getCurrentActivityName()=ϳ���������� ���������
                     2015-07-05_15:33:11.144 | INFO | org.wf.dp.dniprorada.engine.task.FileTaskUpload- oExecution.getEventName()=null
                     2015-07-05_15:33:11.144 | INFO | org.wf.dp.dniprorada.engine.task.FileTaskUpload- oExecution.getId()=955057
                     2015-07-05_15:33:11.144 | INFO | org.wf.dp.dniprorada.engine.task.FileTaskUpload- oExecution.getParentId()=955001
                     2015-07-05_15:33:11.144 | INFO | org.wf.dp.dniprorada.engine.task.FileTaskUpload- oExecution.getProcessBusinessKey()=key
                     2015-07-05_15:33:11.144 | INFO | org.wf.dp.dniprorada.engine.task.FileTaskUpload- oExecution.getProcessDefinitionId()=kiev_mreo_1:126:942617
                     2015-07-05_15:33:11.144 | INFO | org.wf.dp.dniprorada.engine.task.FileTaskUpload- oExecution.getProcessInstanceId()=955001
                     2015-07-05_15:33:11.144 | INFO | org.wf.dp.dniprorada.engine.task.FileTaskUpload- oExecution.getTenantId()=
                     */
                    try {
                        nID_Task_Activiti = Long.valueOf(oExecution.getProcessInstanceId());
                        LOG.info("nID_Task_Activiti:Ok!");
                    } catch (Exception oException) {
                        LOG.error(oException.getMessage());
                    }
                    //oExecution.getCurrentActivityId()
                    //nID_Task_Activiti
                } catch (Exception oException) {
                    LOG.error(oException.getMessage());
                }
                LOG.info("nID_Task_Activiti=" + nID_Task_Activiti);

                FlowSlotTicket oFlowSlotTicket = oFlowSlotTicketDao.findById(nID_FlowSlotTicket).orNull();
                if (oFlowSlotTicket == null) {
                    String sError = "FlowSlotTicket with id=" + nID_FlowSlotTicket + " is not found!";
                    LOG.error(sError);
                    throw new Exception(sError);
                } else if (oFlowSlotTicket.getnID_Task_Activiti() != null) {
                    if (nID_Task_Activiti == oFlowSlotTicket.getnID_Task_Activiti()) {
                        String sWarn = "FlowSlotTicket with id=" + nID_FlowSlotTicket
                                + " has assigned same getnID_Task_Activiti()=" + oFlowSlotTicket.getnID_Task_Activiti();
                        LOG.warn(sWarn);
                    } else {
                        String sError =
                                "FlowSlotTicket with id=" + nID_FlowSlotTicket + " has assigned getnID_Task_Activiti()="
                                        + oFlowSlotTicket.getnID_Task_Activiti();
                        LOG.error(sError);
                        throw new Exception(sError);
                    }
                } else {
                    long nID_FlowSlot = oFlowSlotTicket.getoFlowSlot().getId();
                    LOG.info("nID_FlowSlot=" + nID_FlowSlot);
                    long nID_Subject = oFlowSlotTicket.getnID_Subject();
                    LOG.info("nID_Subject=" + nID_Subject);

                    oFlowSlotTicket.setnID_Task_Activiti(nID_Task_Activiti);
                    oFlowSlotTicketDao.saveOrUpdate(oFlowSlotTicket);
                    LOG.info("JSON:" + JsonRestUtils
                            .toJsonResponse(new SaveFlowSlotTicketResponse(oFlowSlotTicket.getId())));
                    oExecution.setVariable("date_of_visit", sDate);
                    LOG.info("date_of_visit=" + sDate);
                }
            } catch (Exception oException) {
                LOG.error(oException.getMessage());
            }

        }

    }

    public RedisService getRedisService() {
        return redisService;
    }
}
