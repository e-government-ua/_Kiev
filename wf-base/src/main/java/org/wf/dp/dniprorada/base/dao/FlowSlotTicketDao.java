package org.wf.dp.dniprorada.base.dao;

import org.joda.time.DateTime;
import org.wf.dp.dniprorada.base.model.FlowSlot;
import org.wf.dp.dniprorada.base.model.FlowSlotTicket;

import java.util.List;

/**
 * User: goodg_000
 * Date: 21.06.2015
 * Time: 15:44
 */
public interface FlowSlotTicketDao extends EntityDao<FlowSlotTicket> {


   FlowSlotTicket findFlowSlotTicket(Long nID_FlowSlot);

}
