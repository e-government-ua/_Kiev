package org.wf.dp.dniprorada.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.web.multipart.MultipartFile;
import org.wf.dp.dniprorada.base.model.NamedEntity;
import org.wf.dp.dniprorada.base.util.JsonDateDeserializer;
import org.wf.dp.dniprorada.base.util.JsonDateSerializer;

import javax.persistence.*;

@javax.persistence.Entity
public class Document extends NamedEntity {

    @Transient
    @JsonIgnore
    private MultipartFile fileBody;

    @JsonProperty(value = "oDocumentType")
    @ManyToOne(fetch = FetchType.EAGER)
    //@Cascade({ CascadeType.SAVE_UPDATE })
    @JoinColumn(name = "nID_DocumentType", nullable = false)
    private DocumentType documentType;

    @JsonProperty(value = "sID_Content")
    @Column(name = "sID_Content", nullable = false)
    private String contentKey;

    @JsonProperty(value = "oDocumentContentType")
    @ManyToOne(fetch = FetchType.EAGER)
    //@Cascade({ CascadeType.SAVE_UPDATE })
    @JoinColumn(name = "nID_ContentType", nullable = false)
    private DocumentContentType documentContentType;

    @JsonProperty(value = "sFile")
    @Column(name = "sFile", nullable = true)
    private String file;

    @JsonProperty(value = "sDate_Upload")
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @Type(type = DATETIME_TYPE)
    @Column(name = "sDate_Upload", nullable = true)
    private DateTime date_Upload;

    @JsonProperty(value = "sContentType")
    @Column(name = "sContentType", nullable = false)
    private String sContentType;

    //Todo: убрать поля, когда клиент отцепится от них
    @JsonProperty(value = "sID_Subject_Upload")
    @Column(name = "sID_Subject_Upload", nullable = false)
    private String sID_subject_Upload;

    @JsonProperty(value = "sSubjectName_Upload")
    @Column(name = "sSubjectName_Upload", nullable = false)
    private String subjectName_Upload;

    @JsonProperty(value = "oSubject_Upload")
    //@ManyToOne(fetch = FetchType.EAGER)
    @ManyToOne(targetEntity = Subject.class, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "nID_Subject_Upload", nullable = true)
    private Subject subject_Upload;

    @JsonProperty(value = "oSubject")
    //@ManyToOne(fetch = FetchType.EAGER)
    @ManyToOne(targetEntity = Subject.class, fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "nID_Subject", nullable = true)
    private Subject subject;

    @JsonProperty(value = "oSignData")
    @Column(name = "oSignData", nullable = false)
    private String oSignData;

    public MultipartFile getFileBody() {
        return fileBody;
    }

    public void setFileBody(MultipartFile fileBody) {
        this.fileBody = fileBody;
    }

    public String getoSignData() {
        return oSignData;
    }

    public void setoSignData(String soSignData) {
        this.oSignData = (soSignData == null || "".equals(soSignData.trim())) ? "{}" : soSignData;
    }

    public String getContentKey() {
        return contentKey;
    }

    public void setContentKey(String contentKey) {
        this.contentKey = contentKey;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public DateTime getDate_Upload() {
        return date_Upload;
    }

    public void setDate_Upload(DateTime date_Upload) {
        this.date_Upload = date_Upload;
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

    public String getsID_subject_Upload() {
        return sID_subject_Upload;
    }

    public void setsID_subject_Upload(String sID_subject_Upload) {
        this.sID_subject_Upload = sID_subject_Upload;
    }

    public Subject getSubject_Upload() {
        return subject_Upload;
    }

    public void setSubject_Upload(Subject subject_Upload) {
        this.subject_Upload = subject_Upload;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getContentType() {
        return sContentType;
    }

    public void setContentType(String s) {
        this.sContentType = s;
    }

}
