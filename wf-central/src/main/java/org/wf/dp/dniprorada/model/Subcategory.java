package org.wf.dp.dniprorada.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * User: goodg_000 Date: 04.05.2015 Time: 22:24
 */
public class Subcategory extends Entity {

	@JsonProperty(value = "sName")
	private String name;
	
	@JsonProperty(value = "sID")
	private String code;
	
	@JsonProperty(value = "nID_Category")
	private Category category;
	
	@JsonProperty(value = "nOrder")
	private Integer order;
	
	@JsonProperty(value="aService")
	private List<Service> services;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public List<Service> getServices() {
		return services;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}
}
