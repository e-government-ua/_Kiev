package org.wf.dp.dniprorada.base.dao;

import org.joda.time.DateTime;
import org.wf.dp.dniprorada.base.model.FlowSlot;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * User: goodg_000
 * Date: 21.06.2015
 * Time: 15:44
 */
public interface FlowSlotDao extends EntityDao<FlowSlot> {

   /**
    * Gets flow slots ordered by date in given interval
    * @param nID_ServiceData ID services data of slot flow.
    * @param startDate start date of interval (inclusive)
    * @param stopDate end date of interval (exclusive)
    * @return
    */
   List<FlowSlot> getFlowSlotsOrderByDateAsc(Long nID_ServiceData, DateTime startDate, DateTime stopDate);

   /**
    * Gets flow slots in given interval
    * @param nID_Flow_ServiceData ID of flow
    * @param startDate start date of interval (inclusive)
    * @param stopDate stop date of interval (inclusive)
    * @return
    */
   Set<DateTime> getFlowSlotsDates(Long nID_Flow_ServiceData, DateTime startDate, DateTime stopDate);

   boolean containsFlowSlot(Long nID_Flow_ServiceData, DateTime dateTime);

   /**
    * Updates slots with new duration
    * @param nID_Flow_ServiceData
    * @param dates
    * @param newDuration
    * @return
    */
   int updateSlots(Long nID_Flow_ServiceData, Collection<DateTime> dates, String newDuration);

}
