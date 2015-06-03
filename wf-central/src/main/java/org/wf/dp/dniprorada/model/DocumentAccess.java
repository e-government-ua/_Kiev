package org.wf.dp.dniprorada.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.Date;

@Entity
@Table(name="DocumentAccess")
public class DocumentAccess {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="nID")
	private Long nID;
	@Column(name="nID_Document")
	private Long nID_Document;
	@Column(name="sDateCreate")
	private String sDateCreate;
	@Column(name="nMS")
	private Long nMS;
	@Column(name="sFIO")
	private String sFIO;
	@Column(name="sTarget")
	private String sTarget;
	@Column(name="sTelephone")
	private String sTelephone;
	@Column(name="sMail")
	private String sMail;
	@Column(name="sSecret")
	private String sSecret;
	@Column(name="sAnswer")
	private String sAnswer;
	@Column(name="sDateAnswerExpire")
	private Date sDateAnswerExpire;
        

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
	

	public Date getDateAnswerExpire() {
		return sDateAnswerExpire;
	}

	public void setDateAnswerExpire(Date o) {
		this.sDateAnswerExpire = o;
	}
        
	public Long getID() {
		return nID;
	}

	public void setID(Long n) {
		this.nID = n;
	}
}