package org.wf.dp.dniprorada.base.viewobject.flow;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.joda.time.DateTime;
import org.wf.dp.dniprorada.base.util.JsonDateDeserializer;
import org.wf.dp.dniprorada.base.util.JsonDateSerializer;

import java.util.ArrayList;
import java.util.List;

/**
 * User: goodg_000
 * Date: 21.06.2015
 * Time: 14:06
 */
public class Day {

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private DateTime sDate;

    private boolean bHasFree;

    private List<FlowSlotVO> aSlot = new ArrayList<>();

    public Day() {
    }

    public Day(DateTime sDate) {
        this.sDate = sDate;
    }

    public DateTime getsDate() {
        return sDate;
    }

    public void setsDate(DateTime sDate) {
        this.sDate = sDate;
    }

    public boolean isbHasFree() {
        return bHasFree;
    }

    public void setbHasFree(boolean bHasFree) {
        this.bHasFree = bHasFree;
    }

    public List<FlowSlotVO> getaSlot() {
        return aSlot;
    }

    public void setaSlot(List<FlowSlotVO> aSlot) {
        this.aSlot = aSlot;
    }
}
