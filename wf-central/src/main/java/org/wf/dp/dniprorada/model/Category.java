package org.wf.dp.dniprorada.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * User: goodg_000 Date: 04.05.2015 Time: 22:10
 */
public class Category extends Entity {

	@JsonProperty(value = "sID")
	private String code;
	@JsonProperty(value = "sName")
	private String name;
	@JsonProperty(value = "nOrder")
	private Integer order;
	@JsonProperty(value = "aSubcategory")
	private List<Subcategory> subcategoryList;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public List<Subcategory> getSubcategoryList() {
		return subcategoryList;
	}

	public void setSubcategoryList(List<Subcategory> subcategoryList) {
		this.subcategoryList = subcategoryList;
	}
}
