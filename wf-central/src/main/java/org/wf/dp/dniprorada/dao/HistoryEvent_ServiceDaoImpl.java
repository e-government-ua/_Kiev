package org.wf.dp.dniprorada.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Required;
import org.wf.dp.dniprorada.model.HistoryEvent_Service;


public class HistoryEvent_ServiceDaoImpl implements HistoryEvent_ServiceDao{

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
    public HistoryEvent_Service getHistoryEvent_ServiceBysID(String sID) {
        Criteria criteria = getSession().createCriteria(HistoryEvent_Service.class);
        criteria.add(Restrictions.eq("sID", sID));
        return (HistoryEvent_Service) criteria.uniqueResult();
    }

    @Override
    public String addHistoryEvent_Service(Long nID_task, String sStatus, Long nID_subject, String sID_status) {
        HistoryEvent_Service event_service = new HistoryEvent_Service();
        event_service.setnID_Task(nID_task);
        event_service.setsStatus(sStatus);
        event_service.setsID_Status(sID_status);
        event_service.setnID_Subject(nID_subject);
        event_service.setsID("sID");//??
        Session session = getSession();
        session.saveOrUpdate(event_service);
        Long nID = event_service.getId();
        String sID = generatesID(nID, nID_subject, sID_status);
        event_service.setsID(sID);
        session.saveOrUpdate(event_service);
        return sID;
    }

    @Override
    public void updateHistoryEvent_Service(Long nID, String sStatus, String sID_status) {
        Session session = getSession();
        HistoryEvent_Service event_service = (HistoryEvent_Service) session.get(HistoryEvent_Service.class, nID);
        event_service.setsStatus(sStatus);
        if (sID_status != null) {
            event_service.setsID_Status(sID_status);
        }
        session.saveOrUpdate(event_service);
    }

    //todo generate sID
    private String generatesID(Long nID, Long nID_subject, String sID_status) {
        /*поле нИД генерируется автоматом
поле сИД --  генерируется (используя алг.Луна) на основе нИД И (если есть сИД_Статус) на основе поля сИД_Статус.
 еще в основу и вкладываем часть данных как логин и часть как пароль...
на самом деле все просто)
Вот калькулятор: http://planetcalc.ru/2464/
а) допустим у нас есть в nID число "12345678"
по алгоритму получаем:
Последний разряд контрольной суммы: 4
Следующая проверочная цифра: 2
б) так-же берем nID_Subject (как логин), например 16
по алгоритму получаем:
Последний разряд контрольной суммы: 8
Следующая проверочная цифра: 6
в) "собираем воедино все цифры":
12345678 & 4 & 2 & 16 & 8 & 6
т.е.: 12345678421686
г) превращаем это десятиричное число в 27+27+10=64-ричное, символьное из латинских больших(27) и маленьких(27) букв и цифр(10) 64-тиричная (0..9a..zA..Z)
получим что-то вроде G8hi3 (т.е. 5 символов)
д) остается еще 5 свободных (до 10-ти обозначенных в задании), для случаянно-сгенерированного пароля
например, 7Klg2
е) в итоге получаем уникальный sID="G8hi37Klg2", которы никогда не повторится, если nID и nID_Subject будут другими нежели в нашем случае (даже если когдато сгенерится такой-же пароль)
* */
        return "" + nID;
    }


}
