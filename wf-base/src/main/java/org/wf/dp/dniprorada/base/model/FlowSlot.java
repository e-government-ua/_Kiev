package org.wf.dp.dniprorada.base.model;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Slot of the Flow. Stored in regional server.
 * <p/>
 * User: goodg_000
 * Date: 14.06.2015
 * Time: 15:26
 */
@javax.persistence.Entity
public class FlowSlot extends NamedEntity {

    @Column
    @Type(type = DATETIME_TYPE)
    private DateTime sDate;

    @Column
    private String sDuration;

    /**
     * not used now, just in case, will hold some json.
     */
    @Column
    private String sData;

    @ManyToOne(targetEntity = Flow_ServiceData.class)
    @JoinColumn(name = "nID_Flow_ServiceData")
    private Flow_ServiceData flow;

    @OneToMany(mappedBy = "oFlowSlot")
    private List<FlowSlotTicket> aFlowSlotTicket = new ArrayList<>();

    public String getsData() {
        return sData;
    }

    public void setsData(String sData) {
        this.sData = sData;
    }

    public List<FlowSlotTicket> getFlowSlotTickets() {
        return aFlowSlotTicket;
    }

    public void setFlowSlotTickets(List<FlowSlotTicket> aFlowSlotTicket) {
        this.aFlowSlotTicket = aFlowSlotTicket;
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
