package org.wf.dp.dniprorada.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * User: goodg_000 Date: 03.05.2015 Time: 15:34
 */
public class Region extends Entity {

	@JsonProperty(value = "sName")
	private String name;
	
	@JsonProperty(value = "aCity")
	private List<City> cities;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<City> getCities() {
		return cities;
	}

	public void setCities(List<City> cities) {
		this.cities = cities;
	}
}
