package org.activiti.rest.controller;

import java.text.SimpleDateFormat;
import java.util.List;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.task.Task;

public enum ReportField {

	REQUEST_NUMBER("1", "nID_Task", "${nID_Task}") {
		@Override
		public String replaceValue(String currentRow, Task curTask, SimpleDateFormat sDateFormat, List<FormProperty> formProperties) {
			return currentRow.replace(this.getPattern(), curTask.getId());
		}
	},
	CREATE_DATE("2", "sDateCreate", "${sDateCreate}") {
		@Override
		public String replaceValue(String currentRow, Task curTask,
				SimpleDateFormat sDateFormat, List<FormProperty> formProperties) {
			return currentRow.replace(this.getPattern(), sDateFormat.format(curTask.getCreateTime()));
		}
	},
	AREA_ID("3", "area", "${area}") {
		@Override
		public String replaceValue(String currentRow, Task curTask,
				SimpleDateFormat sDateFormat, List<FormProperty> formProperties) {
			
			for (FormProperty currProp : formProperties){
				if (currProp.getId().equals("area")){
					String enumName = currProp.getValue();
					if(enumName.contains(";")){
			            String[] names = enumName.split(";");
			            return names[names.length - 1];
			        } else {
			            return enumName;
			        }

				}
			}
			
			return currentRow;
		}
	},
	INN("4", "INN", "${INN}", ""),
	PASSPORT("5","passport", "${passport}", ""),
	REQUEST_ID("6", "", "${requestID}", "0"),
	FIO("7","bankIdlastName bankIdfirstName bankIdmiddleName", "${bankIdlastName} ${bankIdfirstName} ${bankIdmiddleName}") {
		@Override
		public String replaceValue(String currentRow, Task curTask,
				SimpleDateFormat sDateFormat, List<FormProperty> formProperties) {
			return currentRow;
		}
	},
	GOAL_OF_REQUEST("8", "goal of request", "${goalRequest}", "4"),
	AIM("9", "aim", "${aim}") {
		@Override
		public String replaceValue(String currentRow, Task curTask,
				SimpleDateFormat sDateFormat, List<FormProperty> formProperties) {
			return currentRow.replace(this.getPattern(), this.getDefaultValue());
		}
	},
	DATE_START("10", "date_start", "${date_start}") {
		@Override
		public String replaceValue(String currentRow, Task curTask,
				SimpleDateFormat sDateFormat, List<FormProperty> formProperties) {
			for (FormProperty currProp : formProperties){
				if (currProp.getId().equals(this.getName())){
					return currentRow.replaceAll(this.getPattern(), currProp.getValue());
				}
			}
			return currentRow;
		}
	},
	DATE_STOP("11", "date_stop", "${sDateStop}") {
		@Override
		public String replaceValue(String currentRow, Task curTask,
				SimpleDateFormat sDateFormat, List<FormProperty> formProperties) {
				for (FormProperty currProp : formProperties){
					if (currProp.getId().equals(this.getName())){
						return currentRow.replaceAll(this.getPattern(), currProp.getValue());
					}
				}
				return currentRow;
			}
	},
	PLACE_LIVING("12", "place_living", "${place_living}") {
		@Override
		public String replaceValue(String currentRow, Task curTask,
				SimpleDateFormat sDateFormat, List<FormProperty> formProperties) {
			for (FormProperty currProp : formProperties){
				if (currProp.getId().equals(this.getName())){
					return currentRow.replaceAll(this.getPattern(), currProp.getValue());
				}
			}
			return currentRow;
		}
	},
	BANK_ID_PASSPORT("13", "bankIdPassport", "${bankIdPassport}") {
		@Override
		public String replaceValue(String currentRow, Task curTask,
				SimpleDateFormat sDateFormat, List<FormProperty> formProperties) {
			for (FormProperty currProp : formProperties){
				if (currProp.getId().equals(this.getName())){
					return currentRow.replaceAll(this.getPattern(), currProp.getValue());
				}
			}
			return currentRow;
		}
	},
	REQUEST_STATUS("14", "request Status", "${requestStatus}", "1") {
		@Override
		public String replaceValue(String currentRow, Task curTask,
				SimpleDateFormat sDateFormat, List<FormProperty> formProperties) {
			for (FormProperty currProp : formProperties){
				if (currProp.getId().equals(this.getName())){
					return currentRow.replaceAll(this.getPattern(), currProp.getValue());
				}
			}
			return currentRow;
		}
	},
	PHONE("15", "phone", "${phone}") {
		@Override
		public String replaceValue(String currentRow, Task curTask,
				SimpleDateFormat sDateFormat, List<FormProperty> formProperties) {
			for (FormProperty currProp : formProperties){
				if (currProp.getId().equals(this.getName())){
					return currentRow.replaceAll(this.getPattern(), currProp.getValue());
				}
			}
			return currentRow;
		}
	},
	EMAIL("16", "email", "${email}") {
		@Override
		public String replaceValue(String currentRow, Task curTask,
				SimpleDateFormat sDateFormat, List<FormProperty> formProperties) {
			for (FormProperty currProp : formProperties){
				if (currProp.getId().equals(this.getName())){
					return currentRow.replaceAll(this.getPattern(), currProp.getValue());
				}
			}
			return currentRow;
		}
	};
	
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

	public String replaceValue(String currentRow, Task curTask,
			SimpleDateFormat sDateFormat, List<FormProperty> formProperties) {
		return currentRow.replace(this.getPattern(), this.getDefaultValue());
	}
	
}
