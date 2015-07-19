package org.activiti.rest.controller;

import java.text.SimpleDateFormat;

import org.activiti.engine.task.Task;

public enum ReportField {

	REQUEST_NUMBER("1", "${nID_Task}") {
		@Override
		public String replaceValue(String currentRow, Task curTask, SimpleDateFormat sDateFormat) {
			return currentRow.replace(this.getPattern(), curTask.getId());
		}
	},
	CREATE_DATE("2", "${sDateCreate}") {
		@Override
		public String replaceValue(String currentRow, Task curTask, SimpleDateFormat sDateFormat) {
			return currentRow.replace(this.getPattern(), sDateFormat.format(curTask.getCreateTime()));
		}
	};
	
	private String id;
	private String pattern;
	
	private ReportField(String id, String pattern) {
		this.id = id;
		this.pattern = pattern;
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

	public abstract String replaceValue(String currentRow, Task curTask, SimpleDateFormat sDateFormat);
	
}
