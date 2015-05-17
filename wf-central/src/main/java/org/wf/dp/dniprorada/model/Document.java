package org.wf.dp.dniprorada.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import net.sf.brunneng.jom.annotations.Identifier;

import org.wf.dp.dniprorada.model.DocumentType;
import org.wf.dp.dniprorada.model.DocumentContentType;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@javax.persistence.Entity
public class Document {

	@JsonProperty(value = "nID")
	@Id
	@Column(name = "nID")
	private Long id;

	@JsonProperty(value = "sName")
	@Column(name = "sName", nullable = false)
	private String name;

	@JsonProperty(value = "oDocumentType")
	@ManyToOne(fetch = FetchType.EAGER)
	@Cascade({ CascadeType.SAVE_UPDATE })
	@JoinColumn(name = "nID_DocumentType", nullable = false)
	private DocumentType documentType;

	@JsonProperty(value = "sID_Content")
	@Column(name = "sID_Content", nullable = false)
	private String сontentKey;

	@JsonProperty(value = "oDocumentContentType")
	@ManyToOne(fetch = FetchType.EAGER)
	@Cascade({ CascadeType.SAVE_UPDATE })
	@JoinColumn(name = "nID_ContentType", nullable = false)
	private DocumentContentType documentContentType;

	@JsonProperty(value = "sFile")
	@Column(name = "sFile", nullable = true)
	private String file;

	/*
	 * @JsonProperty(value="sDate_Upload") //подумать о типе поля
	 * 
	 * @Column(name = "sDate_Upload", nullable = false) private String
	 * date_Upload;
	 */

	@JsonProperty(value = "sID_Subject_Upload")
	@Column(name = "sID_Subject_Upload", nullable = false)
	private String subject_Upload;

	@JsonProperty(value = "sSubjectName_Upload")
	@Column(name = "sSubjectName_Upload", nullable = false)
	private String subjectName_Upload;

	@Identifier
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getСontentKey() {
		return сontentKey;
	}

	public void setСontentKey(String сontentKey) {
		this.сontentKey = сontentKey;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getDate_Upload() {
		return "2015-05-05";
		// return date_Upload;
	}

	public void setDate_Upload(String date_Upload) {
		// this.date_Upload = date_Upload;
	}

	public String getSubject_Upload() {
		return subject_Upload;
	}

	public void setSubject_Upload(String subject_Upload) {
		this.subject_Upload = subject_Upload;
	}

	public DocumentType getDocumentType() {
		return documentType;
	}

	public void setDocumentType(DocumentType documentType) {
		this.documentType = documentType;
	}

	public DocumentContentType getDocumentContentType() {
		return documentContentType;
	}

	public void setDocumentContentType(DocumentContentType documentContentType) {
		this.documentContentType = documentContentType;
	}

	public String getSubjectName_Upload() {
		return subjectName_Upload;
	}

	public void setSubjectName_Upload(String subjectName_Upload) {
		this.subjectName_Upload = subjectName_Upload;
	}

	/*
	 * Todo: Later we are going to change the sID_Subject_Upload and
	 * sSubjectName_Upload to oSubject_Upload
	 * 
	 * @JsonProperty(value="oSubject_Upload")
	 * 
	 * @ManyToOne(fetch = FetchType.EAGER)
	 * 
	 * @Cascade({CascadeType.SAVE_UPDATE})
	 * 
	 * @JoinColumn(name = "nID_ContentType", nullable = false) private Subject
	 * subject_Upload;
	 */

}
