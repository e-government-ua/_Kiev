package org.wf.dp.dniprorada.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Column;

@javax.persistence.Entity
public class DocumentType extends Entity{
	
	@JsonProperty(value = "sName")
	@Column(name = "sName", nullable = false)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
