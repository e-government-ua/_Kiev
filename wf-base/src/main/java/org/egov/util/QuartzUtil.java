package org.egov.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;


public class QuartzUtil {

	private static final String QUARTZ_FORMAT = "{\"0 0/[INTERVAL] [HOURS_PERIOD] ? * [DAYS]\":\"PT[INTERVAL]M\"}";
	
	public static String getQuartzFormulaByParameters(String sRegionTime, String saRegionWeekDay, Integer nLen){
		String res = QUARTZ_FORMAT.replaceFirst("[INTERVAL]", nLen.toString());
		res = res.replaceAll("[HOURS_PERIOD]", sRegionTime);
		res = res.replaceAll("[DAYS]", saRegionWeekDay);
		
		return res;
	}
	
	public static String getQuartzParametersByFormula(String quartzExpr){
		Map<String, String> resMap = new HashMap<String, String>();
		
		String arrayStrings[] = quartzExpr.split("\\s+");
		if (arrayStrings.length == 6){
			resMap.put("sRegionTime", arrayStrings[2]);
			resMap.put("saRegionWeekDay", arrayStrings[5].indexOf("\"") > 0 ? StringUtils.substringBefore(arrayStrings[5], "\"") : arrayStrings[5]);
			
			String interval = arrayStrings[1];
			resMap.put("nLen", StringUtils.substringAfter(interval, "/"));
		}
		
		JSONObject res = new JSONObject(resMap);
			
		return res.toString();
	}
	
}
