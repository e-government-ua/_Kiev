package org.wf.dp.dniprorada.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.wf.dp.dniprorada.constant.HistoryEventType;
import org.wf.dp.dniprorada.model.HistoryEvent;
import ua.org.egov.utils.storage.durable.impl.GridFSBytesDataStorage;

import java.io.IOException;
import java.util.List;

public class HistoryEventDaoImpl implements HistoryEventDao {
    private SessionFactory sessionFactory;

    @Autowired
    private GridFSBytesDataStorage durableBytesDataStorage;


    @Required
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public HistoryEvent getHistoryEvent(Long id) {
        HistoryEvent historyEvent = (HistoryEvent) getSession().get(HistoryEvent.class, id);
        if (historyEvent.getHistoryEventTypeKey() == null ||
                historyEvent.getHistoryEventTypeKey().equals(0L)) {
        } else {
            historyEvent.setEventNameCustom(HistoryEventType.getById(historyEvent.getHistoryEventTypeKey()).getsName());
        }
        return historyEvent;
    }

    @Override
    public byte[] getHistoryEventSubject(Long id) {
        HistoryEvent historyEvent = (HistoryEvent) getSession().get(HistoryEvent.class, id);
        return durableBytesDataStorage.getData(historyEvent.getSubjectKey().toString());
    }

    @Override
    public byte[] getHistoryEventType(Long id) {
        HistoryEvent historyEvent = (HistoryEvent) getSession().get(HistoryEvent.class, id);
        return durableBytesDataStorage.getData(historyEvent.getHistoryEventTypeKey().toString());
    }

    @Override
    public List<HistoryEvent> getHistoryEvents(String nID_Subject) {
        return (List<HistoryEvent>) getSession().createCriteria(HistoryEvent.class, nID_Subject).list();
    }

    @Override
    public Long setHistoryEvent(Long nID_Subject, Long nID_HistoryEventType, String sEventName_Custom, String sMessage) throws IOException {
        HistoryEvent historyEvent = new HistoryEvent();
        historyEvent.setSubjectKey(nID_Subject);
        historyEvent.setHistoryEventTypeKey(nID_HistoryEventType);
        historyEvent.setEventNameCustom(sEventName_Custom);
        historyEvent.setsMessage(sMessage);

        return historyEvent.getId();
    }
}
