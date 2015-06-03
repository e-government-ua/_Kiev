package org.wf.dp.dniprorada.base.model;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.form.FormData;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.task.Attachment;
import org.activiti.redis.model.ByteArrayMultipartFile;
import org.activiti.redis.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.wf.dp.dniprorada.form.FormFileType;
import org.wf.dp.dniprorada.model.MimiTypeModel;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public abstract class AbstractModelTask {
	
	static final transient Logger LOG = LoggerFactory
			.getLogger(AbstractModelTask.class);
	
	public static final String LIST_KEY_PREFIX = "lst";
	public static final String LIST_KEY_DELIM = ":";

	@Autowired
	RedisService redisService;

	/**
	 * Получить список по ключу списка из execution
	 * @param listKey
	 * @param execution
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getListVariable(String listKey, DelegateExecution execution) {
		List<T> result = new ArrayList<T>();
		
		String keyPrefix = LIST_KEY_PREFIX+LIST_KEY_DELIM+listKey;
		
		for (String execVarKey : execution.getVariableNames()) {
			if (execVarKey.startsWith(keyPrefix)) {
				result.add((T)execution.getVariable(execVarKey));
			}
		}
		return result;
	}
	
	/**
	 * Сохранить список обьектов в execution
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
	 * Возвращает сложгый ключ переменной бизнес-процесса 
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
	 * @param keyRedis
	 * @return
	 */
	public static List<String> getListKeysRedis(String keyRedis) {
		List<String> listKeys = new ArrayList<String>();
		if(keyRedis!=null&&!keyRedis.isEmpty()){
		String[] keys = keyRedis.split(";");
		listKeys = Arrays.asList(keys);
		return listKeys;
		}
		return listKeys;
	}
	
	/**
	 * Получить значения полей с кастомным типом file
	 * @param execution
	 * @param filedTypeFile
	 * @return
	 */
	public static List<String> getValueFieldWithCastomTypeFile(DelegateExecution execution, List<String> filedTypeFile) {
		List<String> listValueKeys = new ArrayList<String>();
		if (!filedTypeFile.isEmpty()) {
			Map<String, Object> variables = execution.getEngineServices()
					.getRuntimeService()
					.getVariables(execution.getProcessInstanceId());
			for (Map.Entry<String, Object> entry1 : variables.entrySet()) {
				if (filedTypeFile.contains(entry1.getKey())) {
					listValueKeys.add(String.valueOf(entry1.getValue()));
				}
			}
		}
		return listValueKeys;
	}

	/**
	 * Получить ид поля с кастомным типом file
	 * @param startformData
	 * @return
	 */
	public static List<String> getListFieldCastomTypeFile(FormData startformData) {
		List<String>filedTypeFile = new ArrayList<String>();
		List<FormProperty> startformDataList = startformData.getFormProperties();
		if(!startformDataList.isEmpty()){
		for (FormProperty prop : startformDataList) {
			if(prop.getType() instanceof FormFileType){
				filedTypeFile.add(prop.getId());
			}
		}
		}
		return filedTypeFile;
	}
	
	/**
	 * Получить имя поля 
	 * @param startformData
	 * @return
	 */
	public static List<String> getListCastomFieldName(FormData startformData) {
		List<String>filedName = new ArrayList<String>();
		List<FormProperty> startformDataList = startformData.getFormProperties();
		if(!startformDataList.isEmpty()){
		for (FormProperty prop : startformDataList) {
			if(prop.getType() instanceof FormFileType){
				filedName.add(prop.getName());
			}
		}
		}
		return filedName;
	}
	
	/**
	 * multipartFile To ByteArray
	 * @param file
	 * @return
	 * @throws java.io.IOException
	 */
	public static ByteArrayOutputStream multipartFileToByteArray(MultipartFile file)
			throws IOException {
		ByteArrayMultipartFile byteArrayMultipartFile  
				= new ByteArrayMultipartFile(
						file.getBytes(), file.getName(), file.getOriginalFilename(), file.getContentType());
		 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		 ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);
		 oos.writeObject(byteArrayMultipartFile);
		 oos.flush();
		 oos.close();
		return byteArrayOutputStream;
	}

	/**
	 * ByteArray To multipartFile
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
     * Adds Attachemnts based on formData to task.
     * @param formData FormData from task where we search file fields.
     * @param task where we add Attachments.
     */

    public void addAttachmentsToTask(FormData formData, DelegateTask task) {

        DelegateExecution execution = task.getExecution();
        List<String> filedTypeFile = getListFieldCastomTypeFile(formData);
        LOG.info("11filedTypeFile="+filedTypeFile.toString());
		System.out.println("STEP 1 ___");
		List<String> listValueKeys = getValueFieldWithCastomTypeFile(execution,
                filedTypeFile);
        LOG.info("21listValueKeys="+listValueKeys.toString());

        List<String> filedName = getListCastomFieldName(formData);
        LOG.info("31filedName="+filedName.toString());
		System.out.println("STEP 2 ___");
        if (!listValueKeys.isEmpty()) {
            int n = 0;
            for (String keyRedis : listValueKeys) {
                LOG.info("keyRedis=" + keyRedis);
				System.out.println("STEP 3 ___ keyRedis=" + keyRedis);
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
                        if (!filedName.isEmpty() && n < filedName.size()) {
                            String name = filedName.get((filedName.size() - 1) - n);
                            LOG.info("name=" + name);
							System.out.println("STEP 4 ___" + "name= " + name);
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
                n++;
            }
        }
    }

	public RedisService getRedisService() {
		return redisService;
	}
}
