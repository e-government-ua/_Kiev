package org.wf.dp.dniprorada.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@Entity
@Table(name="DocumentAccess")
public class DocumentAccess {
	@JsonProperty(value = "nID")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="nID")
	private Long nID;
	
	@JsonProperty(value = "nID_Document")
	@Column(name="nID_Document")
	private Long nID_Document;
	
	@JsonProperty(value = "sDateCreate")
	@Column(name="sDateCreate")
	private String sDateCreate;
	
	@JsonProperty(value = "nMS")
	@Column(name="nMS")
	private Long nMS;
	
	@JsonProperty(value = "sFIO")
	@Column(name="sFIO")
	private String sFIO;
	
	@JsonProperty(value = "sTarget")
	@Column(name="sTarget")
	private String sTarget;
	
	@JsonProperty(value = "sTelephone")
	@Column(name="sTelephone")
	private String sTelephone;
	
	@JsonProperty(value = "sMail")
	@Column(name="sMail")
	private String sMail;
	
	@JsonProperty(value = "sSecret")
	@Column(name="sSecret")
	private String sSecret;
	
	@JsonProperty(value = "sAnswer")
	@Column(name="sAnswer")
	private String sAnswer;
	
	@JsonProperty(value = "sDateAnswerExpire")
	@Column(name="sDateAnswerExpire")
	private String sDateAnswerExpire;
        

	public Long getID_Document() {
		return nID_Document;
	}

	public void setID_Document(Long nID_Document) {
		this.nID_Document = nID_Document;
	}

	public String getDateCreate() {
		return sDateCreate;
	}

	public void setDateCreate(String sDateCreate) {
		this.sDateCreate = sDateCreate;
	}

	public Long getMS() {
		return nMS;
	}

	public void setMS(Long n) {
		this.nMS = n;
	}

	public String getFIO() {
		return sFIO;
	}

	public void setFIO(String sFIO) {
		this.sFIO = sFIO;
	}

	public String getTarget() {
		return sTarget;
	}

	public void setTarget(String sTarget) {
		this.sTarget = sTarget;
	}

	public String getTelephone() {
		return sTelephone;
	}

	public void setTelephone(String sTelephone) {
		this.sTelephone = sTelephone;
	}

	public String getMail() {
		return sMail;
	}

	public void setMail(String sMail) {
		this.sMail = sMail;
	}

	public String getSecret() {
		return sSecret;
	}

	public void setSecret(String sSecret) {
		this.sSecret = sSecret;
	}

	public String getAnswer() {
		return sAnswer;
	}

	public void setAnswer(String s) {
		this.sAnswer = s;
	}
	

	public String getDateAnswerExpire() {
		return sDateAnswerExpire;
	}

	public void setDateAnswerExpire(Date o) {
		this.sDateAnswerExpire = o.toString();
	}
        
	public Long getID() {
		return nID;
	}

	public void setID(Long n) {
		this.nID = n;
	}
	@Override
	public String toString() {
		return nID+nID_Document+sDateCreate+nMS+sFIO+sTarget+sTelephone+sMail+sSecret+sAnswer+sDateAnswerExpire;
	}
}