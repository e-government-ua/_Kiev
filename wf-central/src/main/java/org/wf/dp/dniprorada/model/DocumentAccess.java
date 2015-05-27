package org.wf.dp.dniprorada.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.Date;

@Entity
@Table(name="DocumentAccess")
public class DocumentAccess {
	@Id
	@GeneratedValue
	@Column(name="nID")
	private Integer nID;
	@Column(name="nID_Document")
	private Integer nID_Document;
	@Column(name="sDateCreate")
	private Date sDateCreate;
	@Column(name="nMS")
	private Long sDays;
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

	public Integer getnID_Document() {
		return nID_Document;
	}

	public void setnID_Document(Integer nID_Document) {
		this.nID_Document = nID_Document;
	}

	public Date getsDateCreate() {
		return sDateCreate;
	}

	public void setsDateCreate(Date sDateCreate) {
		this.sDateCreate = sDateCreate;
	}

	public Long getsDays() {
		return sDays;
	}

	public void setsDays(Long sDays) {
		this.sDays = sDays;
	}

	public String getsFIO() {
		return sFIO;
	}

	public void setsFIO(String sFIO) {
		this.sFIO = sFIO;
	}

	public String getsTarget() {
		return sTarget;
	}

	public void setsTarget(String sTarget) {
		this.sTarget = sTarget;
	}

	public String getsTelephone() {
		return sTelephone;
	}

	public void setsTelephone(String sTelephone) {
		this.sTelephone = sTelephone;
	}

	public String getsMail() {
		return sMail;
	}

	public void setsMail(String sMail) {
		this.sMail = sMail;
	}

	public String getsSecret() {
		return sSecret;
	}

	public void setsSecret(String sSecret) {
		this.sSecret = sSecret;
	}
	
	public Integer getnID() {
		return nID;
	}

	public void setnID(Integer nID) {
		this.nID = nID;
	}
}