package org.wf.dp.dniprorada.base.dao;

import org.hibernate.criterion.*;
import org.joda.time.DateTime;
import org.wf.dp.dniprorada.base.dao.util.QueryBuilder;
import org.wf.dp.dniprorada.base.model.FlowSlot;

import java.util.*;

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
   public List<FlowSlot> getFlowSlotsOrderByDateAsc(Long nID_ServiceData, DateTime startDate, DateTime stopDate) {

      DetachedCriteria criteria = DetachedCriteria.forClass(getEntityClass());
      criteria.add(Restrictions.ge("sDate", startDate));
      criteria.add(Restrictions.lt("sDate", stopDate));

      criteria.createCriteria("flow").add(Restrictions.eq("nID_ServiceData", nID_ServiceData));
      criteria.addOrder(Order.asc("sDate"));

      return criteria.getExecutableCriteria(getSession()).list();
   }

   @Override
   public Set<DateTime> getFlowSlotsDates(Long nID_Flow_ServiceData, DateTime startDate, DateTime stopDate) {
      DetachedCriteria criteria = DetachedCriteria.forClass(getEntityClass());
      criteria.add(Restrictions.eq("flow.id", nID_Flow_ServiceData));
      criteria.add(Restrictions.ge("sDate", startDate));
      criteria.add(Restrictions.le("sDate", stopDate));
      criteria.setProjection(Projections.property("sDate"));

      Collection<DateTime> dates = criteria.getExecutableCriteria(getSession()).list();
      return new TreeSet<>(dates);
   }

   @Override
   public boolean containsFlowSlot(Long nID_Flow_ServiceData, DateTime dateTime) {

      DetachedCriteria criteria = DetachedCriteria.forClass(getEntityClass());
      criteria.add(Restrictions.eq("flow.id", nID_Flow_ServiceData));
      criteria.add(Restrictions.eq("sDate", dateTime));
      criteria.setProjection(Projections.count("id"));

      Number count = (Number) criteria.getExecutableCriteria(getSession()).uniqueResult();

      return count.intValue() > 0;
   }

   public int updateSlots(Long nID_Flow_ServiceData, Collection<DateTime> dates, String newDuration) {
      QueryBuilder qb = new QueryBuilder(getSession(), "update FlowSlot s set ");
      qb.append("s.sDuration = :DURATION ", newDuration);
      qb.append("where s.flow.id = :FLOW_ID and ", nID_Flow_ServiceData);
      qb.appendInSafe("s.sDate", "DATE", new ArrayList<>(dates));
      return qb.toQuery().executeUpdate();
   }
}
