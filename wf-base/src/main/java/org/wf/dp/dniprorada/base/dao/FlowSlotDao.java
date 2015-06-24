package org.wf.dp.dniprorada.base.dao;

import org.joda.time.DateTime;
import org.wf.dp.dniprorada.base.model.FlowSlot;

import java.util.List;

/**
 * User: goodg_000
 * Date: 21.06.2015
 * Time: 15:44
 */
public interface FlowSlotDao extends EntityDao<FlowSlot> {


   List<FlowSlot> getFlowSlotsOrderByDateAsc(Long nID_ServiceData, DateTime startDate, DateTime endDate);

}
