package org.wf.dp.dniprorada.model;

import java.util.Date;

public class DocumentAccess {
	private Integer nID;
	private Integer nID_Document;
	private Date sDateCreate;
	private Long sDays;
	private String sFIO;
	private String sTarget;
	private String sTelephone;
	private String sMail;
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