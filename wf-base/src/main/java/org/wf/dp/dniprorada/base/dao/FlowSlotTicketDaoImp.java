package org.wf.dp.dniprorada.base.dao;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.wf.dp.dniprorada.base.model.FlowSlotTicket;

/**
 * User: goodg_000
 * Date: 22.06.2015
 * Time: 0:03
 */
public class FlowSlotTicketDaoImp extends AbstractEntityDao<FlowSlotTicket> implements FlowSlotTicketDao {

   public FlowSlotTicketDaoImp() {
      super(FlowSlotTicket.class);
   }


   @Override
   public FlowSlotTicket findFlowSlotTicket(Long nID_FlowSlot) {
      DetachedCriteria criteria = DetachedCriteria.forClass(getEntityClass());

      criteria.add(Restrictions.eq("oFlowSlot.id", nID_FlowSlot));

      return (FlowSlotTicket) criteria.getExecutableCriteria(getSession()).uniqueResult();
   }
}
