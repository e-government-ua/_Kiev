package org.wf.dp.dniprorada.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.wf.dp.dniprorada.base.model.Entity;
import org.wf.dp.dniprorada.base.util.JsonDateTimeDeserializer;
import org.wf.dp.dniprorada.base.util.JsonDateTimeSerializer;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@javax.persistence.Entity
public class SubjectMessage extends Entity {

    private static final long serialVersionUID = -5269544412868933212L;

    @JsonProperty(value = "sHead")
    @Column(name = "sHead", length = 200, nullable = false)
    private String head;

    @JsonProperty(value = "sBody")
    @Column(name = "sBody", nullable = false)
    private String body;

    @JsonProperty(value = "sDate")
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    @JsonDeserialize(using = JsonDateTimeDeserializer.class)
    @Type(type = DATETIME_TYPE)
    @Column(name = "sDate", nullable = false)
    private DateTime date;

    @JsonProperty(value = "nID_Subject")
    @Column(name = "nID_Subject", nullable = false, columnDefinition = "int default 0")
    private Long id_subject;

    @JsonProperty(value = "sMail")
    @Column(name = "sMail", length = 100)
    private String mail;

    @JsonProperty(value = "sContacts")
    @Column(name = "sContacts", length = 200)
    private String contacts;

    @JsonProperty(value = "sData")
    @Column(name = "sData", length = 200)
    private String data;

    @JsonProperty(value = "oSubjectMessageType")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nID_SubjectMessageType", nullable = false)
    private SubjectMessageType subjectMessageType = SubjectMessageType.DEFAULT;

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public Long getId_subject() {
        return id_subject;
    }

    public void setId_subject(Long id_subject) {
        this.id_subject = id_subject;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public SubjectMessageType getSubjectMessageType() {
        return subjectMessageType;
    }

    public void setSubjectMessageType(SubjectMessageType subjectMessageType) {
        this.subjectMessageType = subjectMessageType;
    }
}
