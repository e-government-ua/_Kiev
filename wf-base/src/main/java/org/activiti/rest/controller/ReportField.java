package org.activiti.rest.controller;

public enum ReportField {

	REQUEST_NUMBER("1", "nID_Task", "${nID_Task}"),
	CREATE_DATE("2", "sDateCreate", "${sDateCreate}"),
	AREA_ID("3", "area", "${area}"),
	INN("4", "INN", "${INN}"),
	PASSPORT("5","Passport", "${Passport}"),
	REQUEST_ID("6", "", "${}"),
	FIO("7","bankIdlastName bankIdfirstName bankIdmiddleName", "${bankIdlastName} ${bankIdfirstName} ${bankIdmiddleName}"),
	GOAL_OF_REQUEST("8", "goal of request", "", "1"),
	AIM("9", "", ""),
	DATE_START("10", "date_start", "${sDateStart}"),
	DATE_STOP("11", "date_stop", "${sDateStop}"),
	PLACE_LIVING("12", "place_living", "${place_living}"),
	BANK_ID_PASSPORT("13", "bankIdPassport", "${bankIdPassport}"),
	REQUEST_STATE("14", "", "1"),
	PHONE("15", "phone", "${phone}"),
	EMAIL("16", "email", "${email}");
	
	private String id;
	private String name;
	private String pattern;
	private String defaultValue;
	
	private ReportField(String id, String name, String pattern) {
		this.id = id;
		this.name = name;
		this.pattern = pattern;
		this.defaultValue = null;
	}
	
	private ReportField(String id, String name, String pattern, String defaultValue) {
		this.id = id;
		this.name = name;
		this.pattern = pattern;
		this.defaultValue = defaultValue;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public String getPattern() {
		return pattern;
	}

	public static ReportField getReportFieldForId(String id){
		for (ReportField curr : ReportField.values()){
			if (curr.getId().equals(id)){
				return curr;
			}
		}
		return null;
	}
	
}
