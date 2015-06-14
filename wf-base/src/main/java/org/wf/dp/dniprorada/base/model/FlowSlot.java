package org.wf.dp.dniprorada.base.model;

import org.joda.time.DateTime;

import javax.persistence.Column;

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
   private String sData;

   @Column
   private DateTime sDate;

   public String getsData() {
      return sData;
   }
   public void setsData(String sData) {
      this.sData = sData;
   }

   public DateTime getsDate() {
      return sDate;
   }
   public void setsDate(DateTime sDate) {
      this.sDate = sDate;
   }
}
