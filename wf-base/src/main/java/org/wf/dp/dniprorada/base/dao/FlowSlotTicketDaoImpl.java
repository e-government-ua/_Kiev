package org.wf.dp.dniprorada.base.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.wf.dp.dniprorada.base.model.FlowSlotTicket;

/**
 * User: goodg_000
 * Date: 22.06.2015
 * Time: 0:03
 */
@Repository
public class FlowSlotTicketDaoImpl extends GenericEntityDao<FlowSlotTicket> implements FlowSlotTicketDao {

    private static final Logger log = LoggerFactory.getLogger(FlowSlotTicketDaoImpl.class);

    public FlowSlotTicketDaoImpl() {
        super(FlowSlotTicket.class);
    }

    @Override
    public FlowSlotTicket findFlowSlotTicket(Long nID_FlowSlot) {
        return findBy("oFlowSlot.id", nID_FlowSlot).orNull();
    }

    public boolean unbindFromTask(Long nID_FlowSlotTicket) {
        FlowSlotTicket flowSlotTicket = findByIdExpected(nID_FlowSlotTicket);

        if (flowSlotTicket.getnID_Task_Activiti() == null) {
            log.info("Ticket [id=%s] is not bound to any task. Skip unbind operation.", flowSlotTicket.getId());

            return false;
        }

        log.info("Ticket [id=%s] is unbound from Task [id=%s]", flowSlotTicket.getId(),
                flowSlotTicket.getnID_Task_Activiti());

        flowSlotTicket.setnID_Task_Activiti(null);
        // flowSlotTicket.setsDateEdit(DateTime.now());  do we need this?
        saveOrUpdate(flowSlotTicket);

        return true;
    }
}
