package org.egov.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MVSDepartmentsTagUtil {

	private Logger LOG = LoggerFactory.getLogger(MVSDepartmentsTagUtil.class);
	private static final Map<String,String> VALUES = new HashMap<String,String>();
	
	static {
		BufferedReader br = null;
		try {
			String line = null;
			br = new BufferedReader(new InputStreamReader(Thread
					.currentThread().getContextClassLoader()
					.getResourceAsStream("patterns/dictionary/MVD_Department.csv"), "UTF-8"));
			while ((line = br.readLine()) != null) {
				String[] valuesArr = line.split(";");
				if (valuesArr.length == 3)
					VALUES.put(valuesArr[0], valuesArr[1] + ";" + valuesArr[2]);
			}
		} catch (IOException e) {
			throw new ExceptionInInitializerError(e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public String replaceMVSTagWithValue(String text){
		if (VALUES.isEmpty()){
			LOG.info("MVD_Department dictionary is empty. Returning initial text");
			return text;
		}
		String res = text;
		if (text.indexOf("[pattern_dictonary:") != -1){
			String pattern = StringUtils.substringBetween(text, "[pattern_dictonary:", "]");
			LOG.info("Found pattern in the text: " + pattern);
			String[] params = pattern.split(":");
			if (params.length > 1){
				LOG.info("Have to replace pattern with ID:" + params[0] + " and column:" + params[1]);
				String patternValue = VALUES.get(params[0]);
				LOG.info("Pattern value for the specified ID: " + patternValue);
				if (patternValue != null){
					String[] patternColumns = patternValue.split(";");
					String valueToReplace = patternColumns[Integer.valueOf(params[1]) + 2];// value in the map starts from second column in csv file
					LOG.info("Repplacing pattern with the value " + valueToReplace);
					res = StringUtils.replace(text, "[pattern_dictonary:" + pattern + "]", valueToReplace);
				}
			}
		}
		return res;
	}

}
