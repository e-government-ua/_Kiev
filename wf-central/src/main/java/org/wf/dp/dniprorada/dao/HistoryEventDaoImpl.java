package org.wf.dp.dniprorada.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Required;
import org.wf.dp.dniprorada.constant.HistoryEventType;
import org.wf.dp.dniprorada.model.HistoryEvent;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class HistoryEventDaoImpl implements HistoryEventDao {
    private SessionFactory sessionFactory;

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
        if (!historyEvent.getHistoryEventTypeKey().equals(0L)) {
            historyEvent.setEventNameCustom(HistoryEventType.getById(historyEvent.getHistoryEventTypeKey()).getsName());
        }
        return historyEvent;
    }

    @Override
    public List<HistoryEvent> getHistoryEvents(Long nID_Subject) {
        List<HistoryEvent> historyEvents =  getSession().createCriteria(HistoryEvent.class)
                .add(Restrictions.eq("subjectKey", nID_Subject))
                .list();
        for (HistoryEvent historyEvent : historyEvents){
            if (!historyEvent.getHistoryEventTypeKey().equals(0L)) {
                historyEvent.setEventNameCustom(HistoryEventType.getById(historyEvent.getHistoryEventTypeKey()).getsName());
            }
        }

        return historyEvents;
    }

    @Override
    public Long setHistoryEvent(Long nID_Subject, Long nID_HistoryEventType, String sEventName_Custom, String sMessage, String sDate) throws IOException {
        HistoryEvent historyEvent = new HistoryEvent();
        historyEvent.setSubjectKey(nID_Subject);
        historyEvent.setHistoryEventTypeKey(nID_HistoryEventType);
        historyEvent.setEventNameCustom(sEventName_Custom);
        historyEvent.setsMessage(sMessage);
        historyEvent.setDate(new Date());
        getSession().saveOrUpdate(historyEvent);
        return historyEvent.getId();
    }
}
