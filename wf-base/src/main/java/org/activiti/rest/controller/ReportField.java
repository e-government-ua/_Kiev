package org.activiti.rest.controller;

import java.text.SimpleDateFormat;
import java.util.List;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.task.Task;

public enum ReportField {

	REQUEST_NUMBER("1", "${nID_Task}") {
		@Override
		public String replaceValue(String currentRow, Task curTask, SimpleDateFormat sDateFormat, List<FormProperty> formProperties) {
			return currentRow.replace(this.getPattern(), curTask.getId());
		}
	},
	CREATE_DATE("2", "${sDateCreate}") {
		@Override
		public String replaceValue(String currentRow, Task curTask,
				SimpleDateFormat sDateFormat, List<FormProperty> formProperties) {
			return currentRow.replace(this.getPattern(), sDateFormat.format(curTask.getCreateTime()));
		}
	},
	AREA_ID("3", "${area}") {
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
	INN("4", "${INN}", ""),
	PASSPORT("5","${passport}", ""),
	REQUEST_ID("6", "${requestID}", "0"),
	FIO("7", "${bankIdlastName} ${bankIdfirstName} ${bankIdmiddleName}") {
		@Override
		public String replaceValue(String currentRow, Task curTask,
				SimpleDateFormat sDateFormat, List<FormProperty> formProperties) {
			return currentRow;
		}
	},
	GOAL_OF_REQUEST("8", "${goalRequest}", "4"),
	AIM("9", "aim", "${aim}") {
		@Override
		public String replaceValue(String currentRow, Task curTask,
				SimpleDateFormat sDateFormat, List<FormProperty> formProperties) {
			for (FormProperty currProp : formProperties){
				if (currProp.getId().equals("aim")){
					return currentRow.replaceAll(this.getPattern(), currProp.getValue());
				}
			}
			return currentRow;
		}
	},
	DATE_START("10", "${date_start}") {
		@Override
		public String replaceValue(String currentRow, Task curTask,
				SimpleDateFormat sDateFormat, List<FormProperty> formProperties) {
			for (FormProperty currProp : formProperties){
				if (currProp.getId().equals("date_start")){
					return currentRow.replaceAll(this.getPattern(), currProp.getValue());
				}
			}
			return currentRow;
		}
	},
	DATE_STOP("11", "${date_stop}") {
		@Override
		public String replaceValue(String currentRow, Task curTask,
				SimpleDateFormat sDateFormat, List<FormProperty> formProperties) {
				for (FormProperty currProp : formProperties){
					if (currProp.getId().equals("date_stop")){
						return currentRow.replaceAll(this.getPattern(), currProp.getValue());
					}
				}
				return currentRow;
			}
	},
	PLACE_LIVING("12", "${place_living}") {
		@Override
		public String replaceValue(String currentRow, Task curTask,
				SimpleDateFormat sDateFormat, List<FormProperty> formProperties) {
			for (FormProperty currProp : formProperties){
				if (currProp.getId().equals("place_living")){
					return currentRow.replaceAll(this.getPattern(), currProp.getValue());
				}
			}
			return currentRow;
		}
	},
	BANK_ID_PASSPORT("13", "${bankIdPassport}") {
		@Override
		public String replaceValue(String currentRow, Task curTask,
				SimpleDateFormat sDateFormat, List<FormProperty> formProperties) {
			for (FormProperty currProp : formProperties){
				if (currProp.getId().equals("bankIdPassport")){
					return currentRow.replaceAll(this.getPattern(), currProp.getValue());
				}
			}
			return currentRow;
		}
	},
	REQUEST_STATUS("14", "${requestStatus}", "1"),
	PHONE("15", "${phone}") {
		@Override
		public String replaceValue(String currentRow, Task curTask,
				SimpleDateFormat sDateFormat, List<FormProperty> formProperties) {
			for (FormProperty currProp : formProperties){
				if (currProp.getId().equals("phone")){
					return currentRow.replaceAll(this.getPattern(), currProp.getValue());
				}
			}
			return currentRow;
		}
	},
	EMAIL("16", "${email}") {
		@Override
		public String replaceValue(String currentRow, Task curTask,
				SimpleDateFormat sDateFormat, List<FormProperty> formProperties) {
			for (FormProperty currProp : formProperties){
				if (currProp.getId().equals("email")){
					return currentRow.replaceAll(this.getPattern(), currProp.getValue());
				}
			}
			return currentRow;
		}
	};
	
	private String id;
	private String pattern;
	private String defaultValue;
	
	private ReportField(String id, String pattern) {
		this.id = id;
		this.pattern = pattern;
		this.defaultValue = null;
	}
	
	private ReportField(String id, String pattern, String defaultValue) {
		this.id = id;
		this.pattern = pattern;
		this.defaultValue = defaultValue;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public String getId() {
		return id;
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
