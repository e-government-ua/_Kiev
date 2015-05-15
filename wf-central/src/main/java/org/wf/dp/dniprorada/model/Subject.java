package org.wf.dp.dniprorada.model;

public class Subject {
	private int nID;
	private int sID;
	private String sINN;
	private String sPassport;
	private String sSB;
	public int getnID() {
		return nID;
	}
	public void setnID(int nID) {
		this.nID = nID;
	}
	public int getsID() {
		return sID;
	}
	public void setsID(int sID) {
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
}
