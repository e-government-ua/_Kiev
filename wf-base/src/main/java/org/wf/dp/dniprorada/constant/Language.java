package org.wf.dp.dniprorada.constant;

public enum Language {
	
	RUSSIAN("ru"), 
	ENGLISH("en");
	
	private String shortName;
	private Language(String shortName){
		this.shortName = shortName;
	}
	
	public String getShortName(){
		return this.shortName;
	}
}
