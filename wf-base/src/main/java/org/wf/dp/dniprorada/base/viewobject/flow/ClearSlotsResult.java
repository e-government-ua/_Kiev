package org.wf.dp.dniprorada.base.viewobject.flow;

import java.util.ArrayList;
import java.util.List;

/**
 * User: goodg_000
 * Date: 30.06.2015
 * Time: 20:05
 */
public class ClearSlotsResult {

    private List<FlowSlotVO> aDeletedSlot = new ArrayList<>();
    private List<FlowSlotVO> aSlotWithTickets = new ArrayList<>();

    public List<FlowSlotVO> getaDeletedSlot() {
        return aDeletedSlot;
    }

    public void setaDeletedSlot(List<FlowSlotVO> aDeletedSlot) {
        this.aDeletedSlot = aDeletedSlot;
    }

    public List<FlowSlotVO> getaSlotWithTickets() {
        return aSlotWithTickets;
    }

    public void setaSlotWithTickets(List<FlowSlotVO> aSlotWithTickets) {
        this.aSlotWithTickets = aSlotWithTickets;
    }
}
