package org.wf.dp.dniprorada.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.wf.dp.dniprorada.base.model.Entity;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * User: goodg_000 Date: 04.05.2015 Time: 23:52
 */
@javax.persistence.Entity
public class ServiceData extends Entity {

	@JsonProperty(value = "nID_Service")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "nID_Service", nullable = false, insertable = false, updatable = false)
	private Service service;
	
	@JsonProperty(value = "nID_City")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "nID_City")
	private City city;
	
	@JsonProperty(value = "nID_Region")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "nID_Region")
	private Region region;
	
	@JsonProperty(value = "nID_ServiceType")
	@ManyToOne(fetch = FetchType.EAGER)
	@Cascade({CascadeType.SAVE_UPDATE})
	@JoinColumn(name = "nID_ServiceType", nullable = false)
	private ServiceType serviceType;
	
	@JsonProperty(value = "oSubject_Operator")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "nID_Subject_Operator", nullable = true)
	private SubjectOrgan subject_Operator;
	
	@JsonProperty(value = "oData")
	@Column(name = "oData", nullable = false)
	private String data;
	
	@JsonProperty(value = "sURL")
	@Column(name = "sURL", nullable = false)
	private String url;

	@JsonProperty(value = "bHidden")
	@Column(name = "bHidden", nullable = false)
	private boolean hidden;
	
        
	@JsonProperty(value = "bTest")
	@Column(name = "bTest", nullable = false)
	private boolean bTest;
	
        
	@JsonProperty(value = "sNote")
	@Column(name = "sNote", nullable = false)
	private String sNote;

        
        
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
        
	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean b) {
		this.hidden = b;
	}


	public boolean isTest() {
		return bTest;
	}

	public void setTest(boolean b) {
		this.bTest = b;
	}
        

	public String getNote() {
		return sNote;
	}

	public void setNote(String s) {
		this.sNote = s;
	}
        
        /*@Override
        public boolean equals(Object o) {
            return super.equals(o); //To change body of generated methods, choose Tools | Templates.
        }*/


}
