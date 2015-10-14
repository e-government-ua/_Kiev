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

	private static final Logger LOG = LoggerFactory.getLogger(MVSDepartmentsTagUtil.class);
	public static final Map<String, Map<String,String>> VALUES = new HashMap<String,Map<String,String>>();
	private static final String DEFAULT_ROOT_PATH = "patterns/dictionary/"; 
	
	protected static void loadDictionary(String path) {
		Map<String, String> values = new HashMap<String, String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(Thread
					.currentThread().getContextClassLoader()
					.getResourceAsStream(DEFAULT_ROOT_PATH + path), "UTF-8"));
			LOG.info("Reading dictionary from stream:" + DEFAULT_ROOT_PATH + path);
			String line;

			while ((line = br.readLine()) != null)   {
				String key = StringUtils.substringBefore(line, ";");
		        
		        values.put(key, line);
			}

			VALUES.put(path, values);
			//Close the input stream
			br.close();
		} catch (IOException e) {
			LOG.error("Error during loading csv file" + e.getMessage());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					LOG.error("Erro occured while closing disctionary reader" + e.getMessage());
				}
			}
		}
	}
	
	public String replaceMVSTagWithValue(String text){
		String res = text;
                int n=0;
		while (text.indexOf("[pattern_dictonary:") != -1 && n<20){
                    n++;
			String pattern = StringUtils.substringBetween(text, "[pattern_dictonary:", "]");
			LOG.info("Found pattern in the text: " + pattern);
			String[] params = pattern.split(":");
			if (params.length > 2){
				LOG.info("Have to replace pattern with ID:" + params[1] + " and column:" + params[2]);
				Map<String, String> patternValues = VALUES.get(params[0]);
				if (patternValues == null){
					synchronized (VALUES) {
						loadDictionary(params[0]);		
						patternValues = VALUES.get(params[0]);
					}
				}
				if (patternValues == null){
					LOG.error("Unable to find dictionary value from the path: " + params[0]);
					return res;
				}
				LOG.info("Pattern value for the specified ID: " + patternValues);
				if (!patternValues.isEmpty()){
					String patternValue = patternValues.get(params[1]);
					String[] patternColumns = patternValue.split(";");
					String valueToReplace = patternColumns[Integer.valueOf(params[2]) - 1];// value in the map starts from second column in csv file
					LOG.info("Replacing pattern with the value " + valueToReplace);
					res = StringUtils.replace(text, "[pattern_dictonary:" + pattern + "]", valueToReplace);
				}
			}
                        text = res;
		}
		return res;
	}

}
