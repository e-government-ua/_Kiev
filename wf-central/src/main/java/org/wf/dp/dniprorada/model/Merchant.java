package org.wf.dp.dniprorada.model;

import org.wf.dp.dniprorada.base.model.NamedEntity;

import javax.persistence.*;

@Entity
@AttributeOverrides({@AttributeOverride(name="name", column = @Column(name="sName", nullable = true))})
public class Merchant extends NamedEntity {

   @Column(nullable = false, unique = true)
	private String sID;

   @Column
   private String sURL_CallbackStatusNew;

   @Column
   private String sURL_CallbackPaySuccess;

   //OKPO
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "nID_SubjectOrgan")
   private SubjectOrgan owner;


	public void setsID(String sID) {
		this.sID = sID;
	}

   public String getsID() {
      return sID;
   }

   public String getsURL_CallbackStatusNew() {
      return sURL_CallbackStatusNew;
   }

   public void setsURL_CallbackStatusNew(String sURL_CallbackStatusNew) {
      this.sURL_CallbackStatusNew = sURL_CallbackStatusNew;
   }

   public String getsURL_CallbackPaySuccess() {
      return sURL_CallbackPaySuccess;
   }

   public void setsURL_CallbackPaySuccess(String sURL_CallbackPaySuccess) {
      this.sURL_CallbackPaySuccess = sURL_CallbackPaySuccess;
   }

   public SubjectOrgan getOwner() {
      return owner;
   }

   public void setOwner(SubjectOrgan owner) {
      this.owner = owner;
   }
}
