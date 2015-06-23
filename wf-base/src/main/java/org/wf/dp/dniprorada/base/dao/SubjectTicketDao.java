package org.wf.dp.dniprorada.base.dao;

import org.joda.time.DateTime;
import org.wf.dp.dniprorada.base.model.FlowSlot;
import org.wf.dp.dniprorada.base.model.SubjectTicket;

import java.util.List;

/**
 * User: goodg_000
 * Date: 21.06.2015
 * Time: 15:44
 */
public interface SubjectTicketDao extends EntityDao<SubjectTicket> {


   SubjectTicket findSubjectTicket(Long nID_FlowSlot);

}
