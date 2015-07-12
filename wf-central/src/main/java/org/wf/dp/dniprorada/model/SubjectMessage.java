package org.wf.dp.dniprorada.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.wf.dp.dniprorada.base.model.Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

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
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
   @Column(name = "sDate", nullable = false)
   private Timestamp date;

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

   public Timestamp getDate() {
      return date;
   }

   public void setDate(Timestamp date) {
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
