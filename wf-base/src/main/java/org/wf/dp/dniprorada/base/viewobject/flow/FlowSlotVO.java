package org.wf.dp.dniprorada.base.viewobject.flow;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.wf.dp.dniprorada.base.model.FlowSlot;
import org.wf.dp.dniprorada.base.model.FlowSlotTicket;
import org.wf.dp.dniprorada.base.util.DurationUtil;

/**
 * User: goodg_000
 * Date: 21.06.2015
 * Time: 18:10
 */
public class FlowSlotVO {

    private static final DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm");

    private static final int TICKET_WITHOUT_TASK_EXPIRATION_TIME_MINUTES = 5;
    boolean bFree;
    private Long nID;
    private String sTime;
    private int nMinutes;

    public FlowSlotVO() {
    }

    public FlowSlotVO(FlowSlot flowSlot) {
        nID = flowSlot.getId();
        sTime = timeFormatter.print(flowSlot.getsDate());

        nMinutes = DurationUtil.parseDuration(flowSlot.getsDuration()).getMinutes();

        //DateTime now = DateTime.now();

        bFree = true;
        for (FlowSlotTicket ticket : flowSlot.getFlowSlotTickets()) {
            if (bBusy(ticket)) {
                bFree = false;
                break;
            }
         /*if (ticket.getnID_Task_Activiti() != null || ticket.getsDateEdit().compareTo(now.minusMinutes(
                 TICKET_WITHOUT_TASK_EXPIRATION_TIME_MINUTES)) >= 0) {
            bFree = false;
            break;
         }*/
        }

    }

    public static Boolean bBusy(FlowSlotTicket oFlowSlotTicket) {
        return bBusyStatic(oFlowSlotTicket) || bBusyTemp(oFlowSlotTicket);
    }

    public static Boolean bBusyStatic(FlowSlotTicket oFlowSlotTicket) {
        return oFlowSlotTicket.getnID_Task_Activiti() != null;
    }

    public static Boolean bBusyTemp(FlowSlotTicket oFlowSlotTicket) {
        DateTime oDateTimeNow = DateTime.now();
        return oFlowSlotTicket.getsDateEdit()
                .compareTo(oDateTimeNow.minusMinutes(TICKET_WITHOUT_TASK_EXPIRATION_TIME_MINUTES)) >= 0;
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
