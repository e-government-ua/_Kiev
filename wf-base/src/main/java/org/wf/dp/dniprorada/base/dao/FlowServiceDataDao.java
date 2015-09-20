package org.wf.dp.dniprorada.base.dao;

import org.joda.time.DateTime;
import org.wf.dp.dniprorada.base.model.FlowSlot;
import org.wf.dp.dniprorada.base.model.Flow_ServiceData;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * User: goodg_000
 * Date: 21.06.2015
 * Time: 15:44
 */
public interface FlowServiceDataDao extends EntityDao<Flow_ServiceData> {

   /**
    * Gets id of entity Flow_ServiceData with given sID_BP and nID_SubjectOrganDepartment
    * @param sID_BP id of business process
    * @param nID_SubjectOrganDepartment id department
    */
   Long findFlowId(String sID_BP, Long nID_SubjectOrganDepartment);




}
