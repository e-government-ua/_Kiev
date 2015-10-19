package org.wf.dp.dniprorada.dao;

import org.wf.dp.dniprorada.base.dao.EntityDao;
import org.wf.dp.dniprorada.model.HistoryEvent;

import java.io.IOException;
import java.util.List;

public interface HistoryEventDao extends EntityDao<HistoryEvent> {

    public HistoryEvent getHistoryEvent(Long id);

    public List<HistoryEvent> getHistoryEvents(Long nID_Subject);

    public Long setHistoryEvent(Long nID_Subject, Long nID_HistoryEventType,
            String sEventName_Custom, String sMessage) throws IOException;

}
