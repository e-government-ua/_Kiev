package org.egov.util;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class QuartzUtil {

    private static final String QUARTZ_FORMAT = "{\"0 [START_MINUTE]/[INTERVAL] [HOURS_PERIOD] ? * [DAYS]\":\"PT[INTERVAL]M\"}";

    public static String getQuartzFormulaByParameters(String sRegionTime, String saRegionWeekDay, Integer nLen) {
        // sRegionTime is in format HH:MM-HH:MM
        // saRegionWeekDay is in format "mo,tu,we,th,fr,sa". Need to convert to MON, TUE, WED, THU, FRI, SAT, SUN
        String startTime = StringUtils.substringBefore(sRegionTime, "-");
        String endTime = StringUtils.substringAfter(sRegionTime, "-");
        String startHour = StringUtils.substringBefore(startTime, ":");
        String startMinute = StringUtils.substringAfter(startTime, ":");
        String endHour = StringUtils.substringBefore(endTime, ":");

        for (DAY_OF_WEEK b : DAY_OF_WEEK.values()) {
            saRegionWeekDay = saRegionWeekDay.replace(b.getInternalStr(), b.getQuartzStr());
        }

        String res = StringUtils.replace(QUARTZ_FORMAT, "[INTERVAL]", nLen.toString());
        res = StringUtils.replace(res, "[START_MINUTE]", startMinute);
        res = StringUtils.replace(res, "[HOURS_PERIOD]", startHour + "-" + endHour);
        res = StringUtils.replace(res, "[DAYS]", saRegionWeekDay);

        return res;
    }

    public static String getQuartzParametersByFormula(String quartzExpr) {
        Map<String, String> resMap = new HashMap<String, String>();

        String arrayStrings[] = quartzExpr.split("\\s+");
        if (arrayStrings.length == 6) {
            String hours = arrayStrings[2];
            String interval = arrayStrings[1];

            String startHour = StringUtils.substringBefore(hours, "-");
            String endHour = StringUtils.substringAfter(hours, "-");
            String startMinute = StringUtils.substringBefore(interval, "/");
            String endMinute = "00";
            StringBuilder sb = new StringBuilder();
            sb.append(startHour).append(":").append(startMinute).append("-").append(endHour).append(":")
                    .append(endMinute);
            resMap.put("sRegionTime", sb.toString());

            String weekDays = arrayStrings[5].indexOf("\"") > 0 ?
                    StringUtils.substringBefore(arrayStrings[5], "\"") :
                    arrayStrings[5];
            for (DAY_OF_WEEK b : DAY_OF_WEEK.values()) {
                weekDays = StringUtils.replace(weekDays, b.getQuartzStr(), b.getInternalStr());
            }

            resMap.put("saRegionWeekDay", weekDays);

            resMap.put("nLen", StringUtils.substringAfter(interval, "/"));
        }

        JSONObject res = new JSONObject(resMap);

        return res.toString();
    }

    private enum DAY_OF_WEEK {
        SUN("SUN", "su"),
        MON("MON", "mo"),
        TUE("TUE", "tu"),
        WED("WED", "we"),
        THU("THU", "th"),
        FRI("FRI", "fr"),
        SAT("SAT", "sa");

        private String quartzStr;
        private String internalStr;

        DAY_OF_WEEK(String quartzStr, String internalStr) {
            this.quartzStr = quartzStr;
            this.internalStr = internalStr;
        }

        public String getQuartzStr() {
            return this.quartzStr;
        }

        public String getInternalStr() {
            return internalStr;
        }

    }

}
