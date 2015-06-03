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

import net.sf.brunneng.jom.annotations.Identifier;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@javax.persistence.Entity
public class SubjectOrgan {
	
	@JsonProperty(value = "nID")
	@Id
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="subjectOrgan_id_seq")
    //@SequenceGenerator(name="subjectOrgan_id_seq", sequenceName="subjectOrgan_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "nID")
	private Long nID; 
	 
	@JsonProperty(value = "oSubject")
	@OneToOne
	@Cascade({ CascadeType.SAVE_UPDATE })
	@JoinColumn(name = "nID_Subject", nullable = false)
	private Subject oSubject;
	
	@JsonProperty(value = "sOKPO")
	@Column(name = "sOKPO", nullable = false)
	private String sOKPO;
	
	@JsonProperty(value = "sFormPrivacy")
	@Column(name = "sFormPrivacy", nullable = true)
	private String sFormPrivacy;
	
	@JsonProperty(value = "sName")
	@Column(name = "sName", nullable = true)
	private String sName;
	
	@JsonProperty(value = "sNameFull")
	@Column(name = "sNameFull", nullable = true)
	private String sNameFull;

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

	public String getsOKPO() {
		return sOKPO;
	}

	public void setsOKPO(String sOKPO) {
		this.sOKPO = sOKPO;
	}

	public String getsFormPrivacy() {
		return sFormPrivacy;
	}

	public void setsFormPrivacy(String sFormPrivacy) {
		this.sFormPrivacy = sFormPrivacy;
	}

	public String getsName() {
		return sName;
	}

	public void setsName(String sName) {
		this.sName = sName;
	}

	public String getsNameFull() {
		return sNameFull;
	}

	public void setsNameFull(String sNameFull) {
		this.sNameFull = sNameFull;
	}

}
