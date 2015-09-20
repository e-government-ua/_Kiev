package org.wf.dp.dniprorada.base.service.flow;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.wf.dp.dniprorada.base.dao.FlowServiceDataDao;
import org.wf.dp.dniprorada.base.dao.FlowSlotDao;
import org.wf.dp.dniprorada.base.dao.FlowSlotTicketDao;
import org.wf.dp.dniprorada.base.model.FlowProperty;
import org.wf.dp.dniprorada.base.model.FlowSlot;
import org.wf.dp.dniprorada.base.model.FlowSlotTicket;
import org.wf.dp.dniprorada.base.model.Flow_ServiceData;
import org.wf.dp.dniprorada.base.service.flow.propertyHandler.BaseFlowSlotScheduler;
import org.wf.dp.dniprorada.base.service.flow.propertyHandler.FlowPropertyHandler;
import org.wf.dp.dniprorada.base.util.DurationUtil;
import org.wf.dp.dniprorada.base.viewobject.flow.ClearSlotsResult;
import org.wf.dp.dniprorada.base.viewobject.flow.Day;
import org.wf.dp.dniprorada.base.viewobject.flow.Days;
import org.wf.dp.dniprorada.base.viewobject.flow.FlowSlotVO;

import javax.xml.datatype.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * User: goodg_000
 * Date: 29.06.2015
 * Time: 18:11
 */
@Service
public class FlowService implements ApplicationContextAware {

   private static final Logger log = LoggerFactory.getLogger(FlowService.class);
    
   private FlowSlotDao flowSlotDao;
   private FlowSlotTicketDao oFlowSlotTicketDao;

   private FlowServiceDataDao flowServiceDataDao;

   private ApplicationContext applicationContext;

   @Autowired
   public void setFlowSlotDao(FlowSlotDao flowSlotDao) {
      this.flowSlotDao = flowSlotDao;
   }

   @Autowired
   public void setFlowSlotTicketDao(FlowSlotTicketDao oFlowSlotTicketDao) {
      this.oFlowSlotTicketDao = oFlowSlotTicketDao;
   }

   @Autowired
   public void setFlowServiceDataDao(FlowServiceDataDao flowServiceDataDao) {
      this.flowServiceDataDao = flowServiceDataDao;
   }

   @Override
   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      this.applicationContext = applicationContext;
   }


   public Days getFlowSlots(Long nID_ServiceData, String sID_BP, Long nID_SubjectOrganDepartment,
                            DateTime startDate, DateTime endDate, boolean bAll,
                            int nFreeDays) throws Exception {
      List<FlowSlot> flowSlots = flowSlotDao.findFlowSlotsByServiceData(nID_ServiceData, sID_BP,
              nID_SubjectOrganDepartment, startDate, endDate);

      Map<DateTime, Day> daysMap = new TreeMap<>();
      if (bAll) {
         DateTime currDate = startDate;
         while (currDate.isBefore(endDate)) {
            Day day = new Day(currDate);
            daysMap.put(currDate, day);
            currDate = currDate.plusDays(1);
         }
      }

      for (FlowSlot flowSlot : flowSlots) {
         DateTime currDate = flowSlot.getsDate().withTimeAtStartOfDay();
         FlowSlotVO flowSlotVO = new FlowSlotVO(flowSlot);
         if (!bAll && !flowSlotVO.isbFree()) {
            continue;
         }

         Day day = daysMap.get(currDate);
         if (day == null) {
            day = new Day(currDate);
            daysMap.put(currDate, day);
         }

         day.getaSlot().add(flowSlotVO);

         if (!day.isbHasFree() && flowSlotVO.isbFree()) {
            day.setbHasFree(true);
         }
      }

      Days res = new Days();
      int freeDaysCount = 0;

      for (Map.Entry<DateTime, Day> entry : daysMap.entrySet()) {
         Day day = entry.getValue();
         if (bAll || day.isbHasFree()) {
            res.getaDay().add(day);
         }

         if (day.isbHasFree()) {
            freeDaysCount++;
            if (freeDaysCount >= nFreeDays) {
               break;
            }
         }
      }

      return res;
   }

   public FlowSlotTicket saveFlowSlotTicket(Long nID_FlowSlot, Long nID_Subject, Long nID_Task_Activiti) throws Exception {

      FlowSlotTicket oFlowSlotTicket = oFlowSlotTicketDao.findFlowSlotTicket(nID_FlowSlot);
      if (oFlowSlotTicket == null) {
         oFlowSlotTicket = new FlowSlotTicket();
      }else{
        //if(oFlowSlotTicket.getnID_Task_Activiti()!=null){
        if(FlowSlotVO.bBusyStatic(oFlowSlotTicket)){
                  //oFlowSlotTicket.getnID_Subject(nID_Subject);
            String sError="FlowSlotTicket with nID_FlowSlot="+nID_FlowSlot+" is bBusyStatic by getnID_Task_Activiti()="+oFlowSlotTicket.getnID_Task_Activiti();
            log.error(sError);
            throw new Exception(sError);
        }else if(FlowSlotVO.bBusyTemp(oFlowSlotTicket)){//oFlowSlotTicket.getsDateEdit(). <oFlowSlotTicket.getsDateEdit()
            //bBusyStatic
            log.info("nID_Subject="+nID_Subject);
            log.info("getnID_Subject()="+oFlowSlotTicket.getnID_Subject());
            if(!nID_Subject.equals(oFlowSlotTicket.getnID_Subject())){
                String sError="FlowSlotTicket with nID_FlowSlot="+nID_FlowSlot+" is bBusyTemp from getsDateEdit()="+oFlowSlotTicket.getsDateEdit();
                log.error(sError);
                throw new Exception(sError);
            }
        }
      }
      
      //oFlowSlotTicket
              
      oFlowSlotTicket.setnID_Subject(nID_Subject);
      oFlowSlotTicket.setnID_Task_Activiti(nID_Task_Activiti);

      FlowSlot flowSlot = flowSlotDao.findByIdExpected(nID_FlowSlot);

      oFlowSlotTicket.setoFlowSlot(flowSlot);
      oFlowSlotTicket.setsDateStart(flowSlot.getsDate());

      Duration duration = DurationUtil.parseDuration(flowSlot.getsDuration());
      DateTime finishDateTime = flowSlot.getsDate().plusMinutes(duration.getMinutes());
      oFlowSlotTicket.setsDateFinish(finishDateTime);

      oFlowSlotTicket.setsDateEdit(DateTime.now());

      return oFlowSlotTicketDao.saveOrUpdate(oFlowSlotTicket);
   }

   /**
    * Generates FlowSlots in given interval for specified flow. Slots will not be generated if they are already exist.
    * @param nID_Flow_ServiceData ID of flow
    * @param startDate start date of generation (inclusive)
    * @param stopDate stop date of generation (exclusive)
    * @return generated slots.
    */
   public List<FlowSlotVO> buildFlowSlots(Long nID_Flow_ServiceData, DateTime startDate, DateTime stopDate) {

      Flow_ServiceData flow = flowServiceDataDao.findByIdExpected(nID_Flow_ServiceData);

      List<FlowSlotVO> res = new ArrayList<>();

      for (FlowProperty flowProperty : flow.getFlowProperties()) {
        if(flowProperty.getbExclude()==null || !flowProperty.getbExclude()){
            Class<FlowPropertyHandler> flowPropertyHandlerClass = getFlowPropertyHandlerClass(flowProperty);
            if (BaseFlowSlotScheduler.class.isAssignableFrom(flowPropertyHandlerClass)) {

               BaseFlowSlotScheduler handler = getFlowPropertyHandlerInstance(
                       flowProperty.getoFlowPropertyClass().getsBeanName(), flowPropertyHandlerClass);
               handler.setStartDate(startDate);
               handler.setEndDate(stopDate);
               handler.setFlow(flow);

               log.info("startDate="+startDate+",stopDate="+stopDate+",flowProperty.getsData()="+flowProperty.getsData());
               
               if(flowProperty.getsData()!=null && !"".equals(flowProperty.getsData().trim()) ){
                    List<FlowSlot> generatedSlots = handler.generateObjects(flowProperty.getsData());
                    for (FlowSlot slot : generatedSlots) {
                       res.add(new FlowSlotVO(slot));
                    }
               }
            }
        }
      }

      return res;
   }

   public ClearSlotsResult clearFlowSlots(Long nID_Flow_ServiceData, DateTime startDate, DateTime stopDate,
                                          boolean bWithTickets) {

      List<FlowSlot> flowSlots = flowSlotDao.findFlowSlotsByFlow(nID_Flow_ServiceData, startDate, stopDate);
      DateTime operationTime = DateTime.now();

      ClearSlotsResult res = new ClearSlotsResult();
      List<FlowSlot> flowSlotsToDelete = new ArrayList<>();
      for (FlowSlot slot : flowSlots) {
         if (bWithTickets || slot.getFlowSlotTickets().isEmpty()) {
            flowSlotsToDelete.add(slot);

            // detach existing tickets from slots
            for (FlowSlotTicket oFlowSlotTicket : slot.getFlowSlotTickets()) {
               oFlowSlotTicket.setoFlowSlot(null);
               oFlowSlotTicket.setsDateEdit(operationTime);
            }
            res.getaDeletedSlot().add(new FlowSlotVO(slot));
         }

         if (!slot.getFlowSlotTickets().isEmpty()) {
            res.getaSlotWithTickets().add(new FlowSlotVO(slot));
         }
      }

      flowSlotDao.delete(flowSlotsToDelete);
      return res;
   }

   private Class<FlowPropertyHandler> getFlowPropertyHandlerClass(FlowProperty flowProperty) {
      String fullClassName = flowProperty.getoFlowPropertyClass().getsPath();
      try {
         return (Class<FlowPropertyHandler>) Class.forName(fullClassName);
      } catch (ClassNotFoundException e) {
         throw new RuntimeException("Can't find class of controller: " + fullClassName, e);
      }
   }

   private <T extends FlowPropertyHandler> T getFlowPropertyHandlerInstance(String beanName,
                                                                            Class flowPropertyHandlerClass) {
      Object bean = null;
      if (beanName != null) {
         bean = applicationContext.getBean(beanName);
      }
      else {
         bean = applicationContext.getBean(flowPropertyHandlerClass);
      }
      Assert.isTrue(flowPropertyHandlerClass.isAssignableFrom(flowPropertyHandlerClass));
      return (T) bean;
   }
}
