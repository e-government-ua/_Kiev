package org.wf.dp.dniprorada.base.dao;

import org.wf.dp.dniprorada.base.model.FlowSlotTicket;

/**
 * User: goodg_000
 * Date: 21.06.2015
 * Time: 15:44
 */
public interface FlowSlotTicketDao extends EntityDao<FlowSlotTicket> {

    FlowSlotTicket findFlowSlotTicket(Long nID_FlowSlot);

    /**
     * Unbind flow slot ticket from activiti task
     *
     * @param nID_FlowSlotTicket id of FlowSlotTicket
     * @return true - if unbind was successful. false - if ticket is not bound to task.
     */
    boolean unbindFromTask(Long nID_FlowSlotTicket);

}
