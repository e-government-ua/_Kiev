package org.wf.dp.dniprorada.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import net.sf.brunneng.jom.annotations.Identifier;

@javax.persistence.Entity
public class DocumentContentType {

	@JsonProperty(value = "nID")
	@Id
	@GeneratedValue
	@Column(name = "nID")
	private Integer id;

	@JsonProperty(value = "sName")
	@Column(name = "sName", nullable = false)
	private String name;

	@Identifier
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
