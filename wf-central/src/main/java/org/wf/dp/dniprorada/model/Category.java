package org.wf.dp.dniprorada.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;

/**
 * User: goodg_000 Date: 04.05.2015 Time: 22:10
 */
@javax.persistence.Entity
public class Category extends Entity {

	@JsonProperty(value = "sID")
	@Column(name = "sID", nullable = false)
	private String code;
	
	@JsonProperty(value = "sName")
	@Column(name = "sName", nullable = false)
	private String name;
	
	@JsonProperty(value = "nOrder")
	@Column(name = "nOrder", nullable = false)
	private Integer order;
	
	@JsonProperty(value = "aSubcategory")
	@OneToMany(mappedBy = "category",cascade = CascadeType.ALL, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)
	@OrderBy("order asc")
	private List<Subcategory> subcategories;

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

	public List<Subcategory> getSubcategories() {
		return subcategories;
	}

	public void setSubcategories(List<Subcategory> subcategories) {
		this.subcategories = subcategories;
	}
}
