package org.wf.dp.dniprorada.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import net.sf.brunneng.jom.annotations.Identifier;

import com.fasterxml.jackson.annotation.JsonProperty;

@javax.persistence.Entity
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Subject {
	
	@JsonProperty(value = "nID")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="subject_id_seq")
    @SequenceGenerator(name="subject_id_seq", sequenceName="subject_id_seq", allocationSize=1)
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nID")
	
    
	private Long nID;
	
	@JsonProperty(value = "sID")
	@Column(name = "sID", nullable = true)
	private String sID;
	
	@JsonProperty(value = "sLabel")
	@Column(name = "sLabel", nullable = true)
	private String sLabel;
	
	@JsonProperty(value = "sLabelShort")
	@Column(name = "sLabelShort", nullable = true)
	private String sLabelShort;
	
	
	@Identifier
	public Long getnID() {
		return nID;
	}
	
	public void setnID(Long nID) {
		this.nID = nID;
	}

	public String getsID() {
		return sID;
	}

	public void setsID(String sID) {
		this.sID = sID;
	}

	public String getsLabel() {
		return sLabel;
	}

	public void setsLabel(String sLabel) {
		this.sLabel = sLabel;
	}

	public String getsLabelShort() {
		return sLabelShort;
	}

	public void setsLabelShort(String sLabelShort) {
		this.sLabelShort = sLabelShort;
	}

}
