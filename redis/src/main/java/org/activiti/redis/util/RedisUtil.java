package org.activiti.redis.util;

import java.util.regex.Matcher;

import org.activiti.redis.constant.RedisPatterns;




/**
 * Класс для вспомагательных методов
 * @author inna
 *
 */
public class RedisUtil {
	
	
	/**
	 * возращает расширение файла
	 * @param nameFile
	 * @return
	 */
	public static String getFileExp(String nameFile) {
		if (nameFile == null || nameFile.trim().isEmpty())
			return null;
		Matcher m = RedisPatterns.fileNamePattern.matcher(nameFile);
		if (m.find()) {
			String exp = null;
			for (String part : m.group(m.groupCount()).split("\\.")) {
				exp = part;
			}
			return exp;
		}
		return null;
	}

}
