package org.wf.dp.dniprorada.model;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import net.sf.brunneng.jom.annotations.Identifier;

import com.fasterxml.jackson.annotation.JsonProperty;

@javax.persistence.Entity
public class SubjectHuman {
	
	@JsonProperty(value = "nID")
	@Id
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="subjectHuman_id_seq")
    //@SequenceGenerator(name="subjectHuman_id_seq", sequenceName="subjectHuman_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "nID")
	private Long nID;
	
	@JsonProperty(value = "oSubject")
	@OneToOne
	@Cascade({ CascadeType.SAVE_UPDATE })
	@JoinColumn(name = "nID_Subject", nullable = false)
	private Subject oSubject;
	
	@JsonProperty(value = "sINN")
	@Column(name = "sINN", nullable = false)
	private String sINN;
	

	@JsonProperty(value = "sSB")
	@Column(name = "sSB", nullable = true)
	private String sSB;
	
	@JsonProperty(value = "sPassportSeria")
	@Column(name = "sPassportSeria", nullable = true)
	private String sPassportSeria;
	
	@JsonProperty(value = "sPassportNumber")
	@Column(name = "sPassportNumber", nullable = true)
	private String sPassportNumber;
	
	@JsonProperty(value = "sFamily")
	@Column(name = "sFamily", nullable = true)
	private String sFamily;
	
	@JsonProperty(value = "sName")
	@Column(name = "sName", nullable = true)
	private String sName;
	
	@JsonProperty(value = "sSurname")
	@Column(name = "sSurname", nullable = true)
	private String sSurname;
	
	

	@Identifier
	public Long getnID() {
		return nID;
	}

	public void setnID(Long nID) {
		this.nID = nID;
	}

	public Subject getoSubject() {
		return oSubject;
	}

	public void setoSubject(Subject oSubject) {
		this.oSubject = oSubject;
	}

	public String getsSB() {
		return sSB;
	}

	public void setsSB(String sSB) {
		this.sSB = sSB;
	}

	public String getsINN() {
		return sINN;
	}

	public void setsINN(String sINN) {
		this.sINN = sINN;
	}

	public String getsPassportSeria() {
		return sPassportSeria;
	}

	public void setsPassportSeria(String sPassportSeria) {
		this.sPassportSeria = sPassportSeria;
	}

	public String getsPassportNumber() {
		return sPassportNumber;
	}

	public void setsPassportNumber(String sPassportNumber) {
		this.sPassportNumber = sPassportNumber;
	}

	public String getsFamily() {
		return sFamily;
	}

	public void setsFamily(String sFamily) {
		this.sFamily = sFamily;
	}

	public String getsName() {
		return sName;
	}

	public void setsName(String sName) {
		this.sName = sName;
	}

	public String getsSurname() {
		return sSurname;
	}

	public void setsSurname(String sSurname) {
		this.sSurname = sSurname;
	}

}
