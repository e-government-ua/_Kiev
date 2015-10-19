package org.wf.dp.dniprorada.dao;

import org.wf.dp.dniprorada.base.dao.EntityDao;
import org.wf.dp.dniprorada.model.HistoryEvent_Service;
import org.wf.dp.dniprorada.util.luna.CRCInvalidException;

import java.util.List;
import java.util.Map;

public interface HistoryEvent_ServiceDao extends EntityDao<HistoryEvent_Service> {

    HistoryEvent_Service getHistoryEvent_ServiceBynID(Long nID);

    HistoryEvent_Service getHistoryEvent_ServiceBysID(String sID);

    HistoryEvent_Service getHistoryEvent_ServiceBynID_Task(Long nID_Task);

    HistoryEvent_Service getHistoryEvent_ServiceByID_Protected(Long nID_Protected) throws CRCInvalidException;

    HistoryEvent_Service addHistoryEvent_Service(Long nID_Task, String sStatus, Long nID_Subject,
            String sID_Status, Long nID_Service,
            Long nID_Region, String sID_ua, Integer nRate,
            String soData, String sToken, String sHead, String sBody);

    HistoryEvent_Service updateHistoryEvent_Service(HistoryEvent_Service historyEvent_Service);

    List<Map<String, Long>> getHistoryEvent_ServiceBynID_Service(Long nID_Service);

    HistoryEvent_Service getOrgerByID(String sID_Order) throws CRCInvalidException;

    HistoryEvent_Service getOrgerByProcessID(Long nID_Process, Integer nID_Server) throws CRCInvalidException;

    HistoryEvent_Service getOrgerByProtectedID(Long nID_Protected, Integer nID_Server) throws CRCInvalidException;
}
