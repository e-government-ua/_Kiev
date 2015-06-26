package org.wf.dp.dniprorada.base.dao;

import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.wf.dp.dniprorada.base.model.FlowSlot;

import java.util.List;

/**
 * User: goodg_000
 * Date: 21.06.2015
 * Time: 15:43
 */
public class FlowSlotDaoImpl extends AbstractEntityDao<FlowSlot> implements FlowSlotDao {

   protected FlowSlotDaoImpl() {
      super(FlowSlot.class);
   }

   @Override
   public List<FlowSlot> getFlowSlotsOrderByDateAsc(Long nID_ServiceData, DateTime startDate, DateTime endDate) {

      DetachedCriteria criteria = DetachedCriteria.forClass(getEntityClass());
      criteria.add(Restrictions.ge("sDate", startDate));
      criteria.add(Restrictions.lt("sDate", endDate));

      criteria.createCriteria("flow").add(Restrictions.eq("nID_ServiceData", nID_ServiceData));
      criteria.addOrder(Order.asc("sDate"));

      return criteria.getExecutableCriteria(getSession()).list();
   }

}
