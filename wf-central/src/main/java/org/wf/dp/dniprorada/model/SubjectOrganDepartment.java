package org.wf.dp.dniprorada.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.wf.dp.dniprorada.base.model.Entity;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * User: goodg_000
 * Date: 14.06.2015
 * Time: 14:57
 */
@javax.persistence.Entity
public class SubjectOrganDepartment extends Entity {

   @Column
   private String sName;

   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "nID_SubjectOrgan")
   private SubjectOrgan aSubjectOrgan;

   @Column
   private String sGroup_Activiti;

   public String getsName() {
      return sName;
   }
   public void setsName(String sName) {
      this.sName = sName;
   }

   public SubjectOrgan getaSubjectOrgan() {
      return aSubjectOrgan;
   }

   public void setaSubjectOrgan(SubjectOrgan aSubjectOrgan) {
      this.aSubjectOrgan = aSubjectOrgan;
   }

   public String getsGroup_Activiti() {
      return sGroup_Activiti;
   }
   public void setsGroup_Activiti(String sGroup_Activiti) {
      this.sGroup_Activiti = sGroup_Activiti;
   }
}
