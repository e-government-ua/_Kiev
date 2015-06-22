package org.wf.dp.dniprorada.base.dao;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.wf.dp.dniprorada.base.model.SubjectTicket;

/**
 * User: goodg_000
 * Date: 22.06.2015
 * Time: 0:03
 */
public class SubjectTicketDaoImp extends AbstractEntityDao<SubjectTicket> implements SubjectTicketDao {

   public SubjectTicketDaoImp() {
      super(SubjectTicket.class);
   }


   @Override
   public SubjectTicket findSubjectTicket(Long nID_FlowSlot) {
      DetachedCriteria criteria = DetachedCriteria.forClass(getEntityClass());

      criteria.add(Restrictions.eq("oFlowSlot.id", nID_FlowSlot));

      return (SubjectTicket) criteria.getExecutableCriteria(getSession()).uniqueResult();
   }
}
