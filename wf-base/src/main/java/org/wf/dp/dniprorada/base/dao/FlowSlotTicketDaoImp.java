package org.wf.dp.dniprorada.base.dao;

import org.springframework.stereotype.Repository;
import org.wf.dp.dniprorada.base.model.FlowSlotTicket;

/**
 * User: goodg_000
 * Date: 22.06.2015
 * Time: 0:03
 */
@Repository
public class FlowSlotTicketDaoImp extends GenericEntityDao<FlowSlotTicket> implements FlowSlotTicketDao {

   public FlowSlotTicketDaoImp() {
      super(FlowSlotTicket.class);
   }

   @Override
   public FlowSlotTicket findFlowSlotTicket(Long nID_FlowSlot) {
      return findBy("oFlowSlot.id", nID_FlowSlot).orNull();
   }
}
