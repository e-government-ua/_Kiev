package org.wf.dp.dniprorada.base.viewobject.flow;

/**
 * User: goodg_000
 * Date: 21.06.2015
 * Time: 21:29
 */
public class SaveSubjectTicketResponse {

   private Long nID_Ticket;

   public SaveSubjectTicketResponse() {
   }

   public SaveSubjectTicketResponse(Long nID_Ticket) {
      this.nID_Ticket = nID_Ticket;
   }

   public Long getnID_Ticket() {
      return nID_Ticket;
   }

   public void setnID_Ticket(Long nID_Ticket) {
      this.nID_Ticket = nID_Ticket;
   }
}
