package org.wf.dp.dniprorada.base.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wf.dp.dniprorada.model.MimiTypeModel;

import sun.misc.BASE64Encoder;

public abstract class AbstractModelTask {
	
	static final transient Logger LOG = LoggerFactory
			.getLogger(AbstractModelTask.class);
	
	public static final String LIST_KEY_PREFIX = "lst";
	public static final String LIST_KEY_DELIM = ":";
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
				mimiTypeModel.setExtension("application/" + exe);
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
			LOG.info("MagicParseException" + e.getMessage());
		} catch (MagicMatchNotFoundException e) {
			LOG.info("MagicMatchNotFoundException" + e.getMessage());
		} catch (MagicException e) {
			LOG.info("MagicException" + e.getMessage());
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
	
	public static List<String> getListKeysRedis(String keyRedis) {
		List<String> listKeys = new ArrayList<String>();
		if(keyRedis!=null&&!keyRedis.isEmpty()){
		String[] keys = keyRedis.split(";");
		listKeys = Arrays.asList(keys);
		return listKeys;
		}
		return listKeys;
	}
	
}
