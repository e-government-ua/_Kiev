package org.wf.dp.dniprorada.dao;


import org.wf.dp.dniprorada.model.HistoryEvent_Service;

public interface HistoryEvent_ServiceDao {

    public HistoryEvent_Service getHistoryEvent_ServiceBysID(String sID) ;

    public HistoryEvent_Service getHistoryEvent_ServiceByID_Protected(Long nID_Protected);

    public HistoryEvent_Service addHistoryEvent_Service(Long nID_Task, String sStatus, Long nID_Subject, String sID_Status);

    public void updateHistoryEvent_Service(Long nID, String sStatus, String sID_status);
}
