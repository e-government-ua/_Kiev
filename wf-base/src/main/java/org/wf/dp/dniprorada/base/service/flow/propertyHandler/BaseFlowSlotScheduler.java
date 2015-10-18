package org.wf.dp.dniprorada.base.service.flow.propertyHandler;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;
import org.wf.dp.dniprorada.base.model.FlowSlot;
import org.wf.dp.dniprorada.base.model.Flow_ServiceData;

/**
 * User: goodg_000
 * Date: 29.06.2015
 * Time: 20:03
 */
public abstract class BaseFlowSlotScheduler implements FlowPropertyHandler<FlowSlot> {

    protected DateTime startDate;
    protected DateTime endDate;
    protected int defaultIntervalDaysLength;
    protected Flow_ServiceData flow;

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public int getDefaultIntervalDaysLength() {
        return defaultIntervalDaysLength;
    }

    @Required
    public void setDefaultIntervalDaysLength(int defaultIntervalDaysLength) {
        this.defaultIntervalDaysLength = defaultIntervalDaysLength;
    }

    public Flow_ServiceData getFlow() {
        return flow;
    }

    public void setFlow(Flow_ServiceData flow) {
        this.flow = flow;
    }

    @Override
    public Class<FlowSlot> getTargetObjectClass() {
        return FlowSlot.class;
    }

    protected void prepareInterval() {
        if (startDate == null) {
            startDate = DateTime.now();
        }
        if (endDate == null) {
            endDate = startDate.plusDays(defaultIntervalDaysLength);
        }
        Assert.isTrue(startDate.isBefore(endDate));
    }
}
