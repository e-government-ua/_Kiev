package org.wf.dp.dniprorada.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.wf.dp.dniprorada.base.model.Entity;
import org.wf.dp.dniprorada.base.util.JsonDateTimeDeserializer;
import org.wf.dp.dniprorada.base.util.JsonDateTimeSerializer;

import javax.persistence.Column;

@javax.persistence.Entity
public class HistoryEvent_Service extends Entity {

    @JsonProperty(value="sID")
    @Column
    private String sID;

//    @JsonProperty(value="nID_Protected")
//    @Column
    private transient Long nID_Protected;

    @JsonProperty(value="nID_Task")
    @Column
    private Long nID_Task;

    @JsonProperty(value="nID_Subject")
    @Column
    private Long nID_Subject;

    @JsonProperty(value="sStatus")
    @Column
    private String sStatus;

    @JsonProperty(value="sID_Status")
    @Column
    private String sID_Status;
    
    @JsonProperty(value="sDate")
	@JsonSerialize(using= JsonDateTimeSerializer.class)
	@JsonDeserialize(using= JsonDateTimeDeserializer.class)
    @Type(type = DATETIME_TYPE)
	@Column(name = "sDate", nullable = true) 
	private DateTime sDate;
    
    @JsonProperty(value="nID_Service")
    @Column(name = "nID_Service", nullable = true)
    private Long nID_Service;
    
    @JsonProperty(value="nID_Region")
    @Column(name = "nID_Region", nullable = true)
    private Long nID_Region;
    
    @JsonProperty(value="sID_UA")
    @Column(name = "sID_UA", nullable = true)
    private String sID_UA;

    @JsonProperty(value = "nRate")
    @Column(name = "nRate", nullable = true)
    private Integer nRate;

    @JsonProperty(value = "soData")
    @Column(name = "soData", nullable = true)
    private String soData;

    @JsonProperty(value = "sToken")
    @Column(name = "sToken", nullable = true)
    private String sToken;

    @JsonProperty(value = "sHead")
    @Column(name = "sHead", nullable = true)
    private String sHead;

    @JsonProperty(value = "sBody")
    @Column(name = "sBody", nullable = true)
    private String sBody;

    public String getsID() {
        return sID;
    }

    public void setsID(String sID) {
        this.sID = sID;
    }

    public Long getnID_Protected() {
        return nID_Protected;
    }

    public void setnID_Protected(Long nID_Protected) {
        this.nID_Protected = nID_Protected;
    }

    public Long getnID_Task() {
        return nID_Task;
    }

    public void setnID_Task(Long nID_Task) {
        this.nID_Task = nID_Task;
    }

    public Long getnID_Subject() {
        return nID_Subject;
    }

    public void setnID_Subject(Long nID_Subject) {
        this.nID_Subject = nID_Subject;
    }

    public String getsStatus() {
        return sStatus;
    }

    public void setsStatus(String sStatus) {
        this.sStatus = sStatus;
    }

    public String getsID_Status() {
        return sID_Status;
    }

    public void setsID_Status(String sID_Status) {
        this.sID_Status = sID_Status;
    }

	public DateTime getsDate() {
		return sDate;
	}

	public void setsDate(DateTime sDate) {
		this.sDate = sDate;
	}

	public Long getnID_Service() {
		return nID_Service;
	}

	public void setnID_Service(Long nID_Service) {
		this.nID_Service = nID_Service;
	}

	public Long getnID_Region() {
		return nID_Region;
	}

	public void setnID_Region(Long nID_Region) {
		this.nID_Region = nID_Region;
	}

	public String getsID_UA() {
		return sID_UA;
	}

	public void setsID_UA(String sID_UA) {
		this.sID_UA = sID_UA;
	}

    public Integer getnRate() {
        return nRate;
    }

    public void setnRate(Integer nRate) {
        this.nRate = nRate;
    }

    public String getSoData() {
        return soData;
    }

    public void setSoData(String soData) {
        this.soData = soData;
    }

    public String getsToken() {
        return sToken;
    }

    public void setsToken(String sToken) {
        this.sToken = sToken;
    }

    public String getsHead() {
        return sHead;
    }

    public void setsHead(String sHead) {
        this.sHead = sHead;
    }

    public String getsBody() {
        return sBody;
    }

    public void setsBody(String sBody) {
        this.sBody = sBody;
    }
}
