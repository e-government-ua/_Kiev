package org.wf.dp.dniprorada.model;

public class Subject {
	private Integer nID;
	private Integer sID;
	private String sINN;
	private String sPassport;
	private String sSB;
	private String sOKPO;
	private String sName;
	public Integer getnID() {
		return nID;
	}
	public void setnID(Integer nID) {
		this.nID = nID;
	}
	public Integer getsID() {
		return sID;
	}
	public void setsID(Integer sID) {
		this.sID = sID;
	}
	public String getsINN() {
		return sINN;
	}
	public void setsINN(String sINN) {
		this.sINN = sINN;
	}
	public String getsSB() {
		return sSB;
	}
	public void setsSB(String sSB) {
		this.sSB = sSB;
	}
	public String getsPassport() {
		return sPassport;
	}
	public void setsPassport(String sPassport) {
		this.sPassport = sPassport;
	}
	public String getsOKPO() {
		return sOKPO;
	}
	public void setsOKPO(String sOKPO) {
		this.sOKPO = sOKPO;
	}
	public String getsName() {
		return sName;
	}
	public void setsName(String sName) {
		this.sName = sName;
	}
}
