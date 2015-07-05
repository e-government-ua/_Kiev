package org.wf.dp.dniprorada.model;

import net.sf.brunneng.jom.annotations.Identifier;
import org.wf.dp.dniprorada.base.model.*;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@Table(name="DocumentAccess")
public class DocumentAccess extends org.wf.dp.dniprorada.base.model.Entity {

	@Column(name = "nID_Document")
	private Long nID_Document;

	@Column(name = "sDateCreate")
	private String sDateCreate;

	@Column(name = "nMS")
	private Long nMS;

	@Column(name = "sFIO")
	private String sFIO;

	@Column(name = "sTarget")
	private String sTarget;

	@Column(name = "sTelephone")
	private String sTelephone;

	@Column(name = "sMail")
	private String sMail;

	@Column(name = "sSecret")
	private String sSecret;

	@Column(name = "sAnswer")
	private String sAnswer;

	@Column(name = "sDateAnswerExpire")
	private String sDateAnswerExpire;

	@Column(name = "sCode")
	private String sCode;

	@Column(name = "sCodeType")
	private String sCodeType;

	@Identifier
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

	public String getsCode() {
		return sCode;
	}

	public void setsCode(String sCode) {
		this.sCode = sCode;
	}

	public String getsCodeType() {
		return sCodeType;
	}

	public void setsCodeType(String sCodeType) {
		this.sCodeType = sCodeType;
	}

	@Override
	public String toString() {
		return "{\n" + "nID:" + getId() + "\nnID_Document:" + nID_Document + "sDateCreate:" + sDateCreate +
				"\nnMS:" + nMS + "\nsFIO:" + sFIO + "\nsTarget:" + sTarget + "\nsTelephone:" + sTelephone +
				"\nsMail:" + sMail + "\nsSecret:" + sSecret + "\nsAnswer:" + sAnswer + "" +
				"\nsDateAnswerExpire:" + sDateAnswerExpire +
				"\nsCode:" + sCode + "\nsCodeType:" + sCodeType + "}";
	}
}