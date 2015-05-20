package org.wf.dp.dniprorada.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;

@javax.persistence.Entity
public class SubjectMessage implements Serializable {

    private static final long serialVersionUID = -5269544412868933212L;

    @JsonProperty(value = "nID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "nID")
    private Long id;

    @JsonProperty(value = "sHead")
    @Column(name = "sHead", length = 200, nullable = false)
    private String head;

    @JsonProperty(value = "sBody")
    @Column(name = "sBody", nullable = false)
    private String body;

    @JsonProperty(value = "sDate")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss.SSS")
    @Column(name = "sDate", nullable = false)
    private Timestamp date;

    @JsonProperty(value = "nID_Subject")
    @Column(name = "nID_Subject",nullable = false, columnDefinition = "int default 0")
    private Integer id_subject;

    @JsonProperty(value = "sMail")
    @Column(name = "sMail", length = 100)
    private String mail;

    @JsonProperty(value = "sContacts")
    @Column(name = "sContacts", length = 200)
    private String contacts;

    @JsonProperty(value = "sData")
    @Column(name = "sData",length = 200)
    private String data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Integer getId_subject() {
        return id_subject;
    }

    public void setId_subject(Integer id_subject) {
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
}
