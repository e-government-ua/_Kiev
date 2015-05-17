package org.wf.dp.dniprorada.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;

/**
 * User: goodg_000 Date: 04.05.2015 Time: 22:24
 */
@javax.persistence.Entity
public class Subcategory extends Entity {

	@JsonProperty(value = "sName")
	@Column(name = "sName", nullable = false)
	private String name;
	
	@JsonProperty(value = "sID")
	@Column(name = "sID", nullable = false)
	private String code;
	
	@JsonProperty(value = "nID_Category")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "nID_Category", nullable = false)
	private Category category;
	
	@JsonProperty(value = "nOrder")
	@Column(name = "nOrder", nullable = false)
	private Integer order;
	
	@JsonProperty(value="aService")
	@OneToMany(mappedBy = "subcategory")
	@LazyCollection(LazyCollectionOption.FALSE)
	@OrderBy("order asc")
	private List<Service> services = new ArrayList<>();
	
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
