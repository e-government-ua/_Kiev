package org.wf.dp.dniprorada.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * User: goodg_000 Date: 04.05.2015 Time: 23:52
 */
public class ServiceData extends Entity {

	@JsonProperty(value = "nID_Service")
	private Service service;
	
	@JsonProperty(value = "nID_City")
	private City city;
	
	@JsonProperty(value = "nID_Region")
	private Region region;
	
	@JsonProperty(value = "nID_ServiceType")
	private ServiceType serviceType;
	
	@JsonProperty(value = "oData")
	private String data;
	
	@JsonProperty(value = "sUrl")
	private String url;

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
