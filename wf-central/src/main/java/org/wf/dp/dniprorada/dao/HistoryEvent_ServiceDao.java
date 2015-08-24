package org.wf.dp.dniprorada.dao;


import java.util.List;
import java.util.Map;

import org.wf.dp.dniprorada.model.HistoryEvent_Service;
import org.wf.dp.dniprorada.util.luna.CRCInvalidException;

public interface HistoryEvent_ServiceDao {

	public HistoryEvent_Service getHistoryEvent_ServiceBynID(Long nID);
	
    public HistoryEvent_Service getHistoryEvent_ServiceBysID(String sID) ;
    
    public HistoryEvent_Service getHistoryEvent_ServiceBynID_Task(Long nID_Task);

    public HistoryEvent_Service getHistoryEvent_ServiceByID_Protected(Long nID_Protected) throws CRCInvalidException;

    public HistoryEvent_Service addHistoryEvent_Service(Long nID_Task, String sStatus, Long nID_Subject, String sID_Status, Long nID_Service, Long nID_Region, String sID_UA);

    public HistoryEvent_Service updateHistoryEvent_Service(HistoryEvent_Service historyEvent_Service);
    
    public List<Map<String, Long>> getHistoryEvent_ServiceBynID_ServicenID_Region(Long nID_Service, Long nID_Region);
}
