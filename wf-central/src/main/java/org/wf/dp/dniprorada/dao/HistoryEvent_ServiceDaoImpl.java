package org.wf.dp.dniprorada.dao;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.NullPrecedence;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;
import org.wf.dp.dniprorada.base.dao.EntityNotFoundException;
import org.wf.dp.dniprorada.base.dao.GenericEntityDao;
import org.wf.dp.dniprorada.model.HistoryEvent_Service;
import org.wf.dp.dniprorada.util.luna.AlgorithmLuna;
import org.wf.dp.dniprorada.util.luna.CRCInvalidException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.wf.dp.dniprorada.util.GeneralConfig;

@Repository
public class HistoryEvent_ServiceDaoImpl extends GenericEntityDao<HistoryEvent_Service>
        implements HistoryEvent_ServiceDao {

    private static final Logger log = Logger.getLogger(HistoryEvent_ServiceDaoImpl.class);
    private static final String DASH = "-";

    //    @Autowired
    //    private GeneralConfig generalConfig;

    protected HistoryEvent_ServiceDaoImpl() {
        super(HistoryEvent_Service.class);
    }

    @Override
    public HistoryEvent_Service getHistoryEvent_ServiceBynID(Long nID) {
        return findById(nID).orNull();
    }

    @Override
    public HistoryEvent_Service getHistoryEvent_ServiceBynID_Task(Long nID_Task) {
        return findBy("nID_Task", nID_Task).orNull();
    }

    @Override
    public HistoryEvent_Service getHistoryEvent_ServiceBysID(String sID) {
        return findBy("sID", sID).orNull();
    }

    @Override
    public HistoryEvent_Service getHistoryEvent_ServiceByID_Protected(Long nID_Protected) throws CRCInvalidException {

        AlgorithmLuna.validateProtectedNumber(nID_Protected);
        Criteria criteria = getSession().createCriteria(HistoryEvent_Service.class);
        criteria.addOrder(Order.desc("sDate").nulls(NullPrecedence.LAST));
        criteria.add(Restrictions.eq("nID_Task", AlgorithmLuna.getOriginalNumber(nID_Protected)));
        List<HistoryEvent_Service> list = (List<HistoryEvent_Service>) criteria.list();
        HistoryEvent_Service event_service = list.size() > 0 ? list.get(0) : null;
        if (event_service == null) {
            log.warn("Record not found");
            throw new EntityNotFoundException("Record not found");
        } else {
            log.info("Ok");
            event_service.setnID_Protected(nID_Protected);
        }
        return event_service;
    }

    @Override
    public HistoryEvent_Service addHistoryEvent_Service(Long nID_Task, String sStatus, Long nID_Subject,
            String sID_Status, Long nID_Service,
            Long nID_Region, String sID_UA, Integer nRate,
            String soData, String sToken, String sHead, String sBody) {
        HistoryEvent_Service event_service = new HistoryEvent_Service();
        event_service.setnID_Task(nID_Task);
        event_service.setsStatus(sStatus);
        event_service.setsID_Status(sID_Status);
        event_service.setnID_Subject(nID_Subject);
        event_service.setsDate(new DateTime());
        event_service.setnID_Region(nID_Region);
        event_service.setnID_Service(nID_Service);
        event_service.setsID_UA(sID_UA);
        event_service.setnRate(nRate == null ? 0 : nRate);
        event_service.setSoData(soData == null || "".equals(soData) ? "[]" : soData);
        event_service.setsToken(sToken);
        event_service.setsHead(sHead);
        event_service.setsBody(sBody);
        Long nID_Protected = AlgorithmLuna.getProtectedNumber(nID_Task);
        event_service.setnID_Protected(nID_Protected);
        int nID_Server = 0;////???? generalConfig.nID_Server();
        event_service.setnID_Server(nID_Server);
        event_service.setsID_Order(nID_Server + DASH + nID_Protected);
        Session session = getSession();
        session.saveOrUpdate(event_service);
        return event_service;
    }

    @Override
    public HistoryEvent_Service updateHistoryEvent_Service(HistoryEvent_Service event_service) {
        event_service.setsDate(new DateTime());
        return saveOrUpdate(event_service);
    }

    @Override
    public List<Map<String, Long>> getHistoryEvent_ServiceBynID_Service(Long nID_Service) {

        List<Map<String, Long>> resHistoryEventService = new LinkedList<>();
        if (nID_Service == 159) {
            Map<String, Long> currRes = new HashMap<>();
            currRes.put("sName", 5L);
            currRes.put("nCount", 1L);
            currRes.put("nRate", 0L);
            currRes.put("nTimeHours", 0L);
            resHistoryEventService.add(currRes);
        }
        Criteria criteria = getSession().createCriteria(HistoryEvent_Service.class);
        criteria.add(Restrictions.eq("nID_Service", nID_Service));
        criteria.setProjection(Projections.projectionList()
                .add(Projections.groupProperty("nID_Region"))
                .add(Projections.count("nID_Service"))
                .add(Projections.avg("nRate")) //for issue 777
                .add(Projections.avg("nTimeHours"))
        );
        Object res = criteria.list();
        log.info("Received result in getHistoryEvent_ServiceBynID_Service:" + res);
        if (res == null) {
            log.warn("List of records based on nID_Service not found" + nID_Service);
            throw new EntityNotFoundException("Record not found");
        } else {
            int i = 0;
            for (Object item : criteria.list()) {
                Object[] currValue = (Object[]) item;
                log.info(String.format("Line %s: %s, %s, %s, %s", i, currValue[0], currValue[1], currValue[2] != null ? currValue[2] : "",
                		currValue[3] != null ? currValue[3] : ""));
                i++;
                Long rate = 0L;
                try {
                    Double nRate = (Double) currValue[2];
                    log.info("nRate=" + nRate);
                    if (nRate != null) {
                    	String snRate = "" + nRate * 20;
                    	log.info("snRate=" + snRate);
                    	if (snRate.contains(".")) {
	                        rate = Long.valueOf(snRate.substring(0, snRate.indexOf(".")));
	                        log.info("total rate = " + rate);
	                    }
                	}
                } catch (Exception oException) {
                    log.error("cannot get nRate! " + currValue[2] + " caused: " + oException.getMessage(), oException);
                }
                BigDecimal timeHours = null;
                try {
                    Double nTimeHours = (Double) currValue[3];
                    log.info("nTimeHours=" + nTimeHours);
                    if (nTimeHours != null){
                    	timeHours = BigDecimal.valueOf(nTimeHours);
                    	timeHours = timeHours.abs();
                    }
                } catch (Exception oException) {
                    log.error("cannot get nTimeHours! " + currValue[3] + " caused: " + oException.getMessage(), oException);
                }
                Map<String, Long> currRes = new HashMap<>();
                currRes.put("sName", (Long) currValue[0]);
                currRes.put("nCount", (Long) currValue[1]);
                currRes.put("nRate", rate);
                currRes.put("nTimeHours", timeHours != null ? timeHours.longValue() : -1L);
                resHistoryEventService.add(currRes);
            }
            log.info("Found " + resHistoryEventService.size() + " records based on nID_Service " + nID_Service);
        }

        return resHistoryEventService;
    }

    @Override
    public HistoryEvent_Service getOrgerByID(String sID_Order) throws CRCInvalidException {
        Integer nID_Server;
        Long nID_Protected;
        try {
            int dash_position = sID_Order.indexOf(DASH);
            nID_Server = Integer.parseInt(sID_Order.substring(0, dash_position));
            nID_Protected = Long.valueOf(sID_Order.substring(dash_position + 1));
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    String.format("sID_Order has incorrect format! expected format:[XXX%sXXXXXX], actual value: %s",
                            DASH, sID_Order), e);
        }
        return getOrgerByProtectedID(nID_Protected, nID_Server);
    }

    @Override
    public HistoryEvent_Service getOrgerByProcessID(Long nID_Process, Integer nID_Server) throws CRCInvalidException {
        HistoryEvent_Service event_service = getHistoryEvent_service(nID_Server, nID_Process);
        event_service.setnID_Protected(AlgorithmLuna.getProtectedNumber(nID_Process));
        return event_service;
    }

    @Override
    public HistoryEvent_Service getOrgerByProtectedID(Long nID_Protected, Integer nID_Server)
            throws CRCInvalidException {
        AlgorithmLuna.validateProtectedNumber(nID_Protected);
        Long nID_Process = AlgorithmLuna.getOriginalNumber(nID_Protected);
        HistoryEvent_Service event_service = getHistoryEvent_service(nID_Server, nID_Process);
        event_service.setnID_Protected(nID_Protected);
        return event_service;
    }

    private HistoryEvent_Service getHistoryEvent_service(Integer nID_Server, Long nID_Process) {
        Criteria criteria = getSession().createCriteria(HistoryEvent_Service.class);
        criteria.addOrder(Order.desc("sDate").nulls(NullPrecedence.LAST));
        criteria.add(Restrictions.eq("nID_Task", nID_Process));
        criteria.add(Restrictions.eq("nID_Server", nID_Server));
        List<HistoryEvent_Service> list = (List<HistoryEvent_Service>) criteria.list();
        HistoryEvent_Service event_service = list.size() > 0 ? list.get(0) : null;
        if (event_service == null) {
            throw new EntityNotFoundException(
                    String.format("Record with nID_Server=%s and nID_Process=%s not found!", nID_Server, nID_Process));
        }
        return event_service;
    }

}
