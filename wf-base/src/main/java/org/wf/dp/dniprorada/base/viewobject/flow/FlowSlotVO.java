package org.wf.dp.dniprorada.base.viewobject.flow;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.wf.dp.dniprorada.base.model.FlowSlot;
import org.wf.dp.dniprorada.base.model.SubjectTicket;
import org.wf.dp.dniprorada.base.util.DurationUtil;

/**
 * User: goodg_000
 * Date: 21.06.2015
 * Time: 18:10
 */
public class FlowSlotVO {

   private static final DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm");

   private static final int TICKET_WITHOUT_TASK_EXPIRATION_TIME_MINUTES = 5;

   private Long nID;
   private String sTime;
   private int nMinutes;
   boolean bFree;

   public FlowSlotVO() {
   }

   public FlowSlotVO(FlowSlot flowSlot) {
      nID = flowSlot.getId();
      sTime = timeFormatter.print(flowSlot.getsDate());

      nMinutes = DurationUtil.parseDuration(flowSlot.getsDuration()).getMinutes();
      
      DateTime now = DateTime.now();

      bFree = true;
      for (SubjectTicket ticket : flowSlot.getSubjectTickets()) {
         if (ticket.getnID_Task_Activiti() != null || ticket.getsDateEdit().compareTo(now.minusMinutes(
                 TICKET_WITHOUT_TASK_EXPIRATION_TIME_MINUTES)) >= 0) {
            bFree = false;
            break;
         }
      }

   }

   public Long getnID() {
      return nID;
   }

   public void setnID(Long nID) {
      this.nID = nID;
   }

   public String getsTime() {
      return sTime;
   }

   public void setsTime(String sTime) {
      this.sTime = sTime;
   }

   public int getnMinutes() {
      return nMinutes;
   }

   public void setnMinutes(int nMinutes) {
      this.nMinutes = nMinutes;
   }

   public boolean isbFree() {
      return bFree;
   }

   public void setbFree(boolean bFree) {
      this.bFree = bFree;
   }
}
