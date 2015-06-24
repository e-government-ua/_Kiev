package org.wf.dp.dniprorada.base.model;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.List;

/**
 * Slot of the Flow. Stored in regional server.
 *
 * User: goodg_000
 * Date: 14.06.2015
 * Time: 15:26
 */
@javax.persistence.Entity
public class FlowSlot extends NamedEntity {

   @Column
   @Type(type= DATETIME_TYPE)
   private DateTime sDate;

   @Column
   private String sDuration;

   @Column
   private String sData;

   @ManyToOne(targetEntity = Flow_ServiceData.class)
   @JoinColumn(name = "nID_Flow_ServiceData")
   private Flow_ServiceData flow;

   @OneToMany(mappedBy = "oFlowSlot", cascade = CascadeType.ALL, orphanRemoval = true)
   @LazyCollection(LazyCollectionOption.FALSE)
   private List<SubjectTicket> subjectTickets;

   public String getsData() {
      return sData;
   }
   public void setsData(String sData) {
      this.sData = sData;
   }

   public List<SubjectTicket> getSubjectTickets() {
      return subjectTickets;
   }

   public void setSubjectTickets(List<SubjectTicket> subjectTickets) {
      this.subjectTickets = subjectTickets;
   }

   public DateTime getsDate() {
      return sDate;
   }
   public void setsDate(DateTime sDate) {
      this.sDate = sDate;
   }

   public String getsDuration() {
      return sDuration;
   }

   public void setsDuration(String sDuration) {
      this.sDuration = sDuration;
   }

   public Flow_ServiceData getFlow() {
      return flow;
   }
   public void setFlow(Flow_ServiceData flow) {
      this.flow = flow;
   }

}
