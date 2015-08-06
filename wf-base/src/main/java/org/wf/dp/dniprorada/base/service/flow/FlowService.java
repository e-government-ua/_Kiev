package org.wf.dp.dniprorada.base.service.flow;

import org.joda.time.DateTime;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.wf.dp.dniprorada.base.dao.BaseEntityDao;
import org.wf.dp.dniprorada.base.dao.FlowSlotDao;
import org.wf.dp.dniprorada.base.dao.FlowSlotTicketDao;
import org.wf.dp.dniprorada.base.model.FlowProperty;
import org.wf.dp.dniprorada.base.model.FlowSlot;
import org.wf.dp.dniprorada.base.model.Flow_ServiceData;
import org.wf.dp.dniprorada.base.model.FlowSlotTicket;
import org.wf.dp.dniprorada.base.service.flow.propertyHandler.BaseFlowSlotScheduler;
import org.wf.dp.dniprorada.base.service.flow.propertyHandler.FlowPropertyHandler;
import org.wf.dp.dniprorada.base.util.DurationUtil;
import org.wf.dp.dniprorada.base.viewobject.flow.ClearSlotsResult;
import org.wf.dp.dniprorada.base.viewobject.flow.Day;
import org.wf.dp.dniprorada.base.viewobject.flow.Days;
import org.wf.dp.dniprorada.base.viewobject.flow.FlowSlotVO;

import javax.xml.datatype.Duration;
import java.util.*;

/**
 * User: goodg_000
 * Date: 29.06.2015
 * Time: 18:11
 */
public class FlowService implements ApplicationContextAware {

   private FlowSlotDao flowSlotDao;
   private FlowSlotTicketDao oFlowSlotTicketDao;
   private BaseEntityDao baseEntityDao;

   private ApplicationContext applicationContext;

   public FlowSlotTicketDao getFlowSlotTicketDao() {
      return oFlowSlotTicketDao;
   }

   public FlowSlotDao getFlowSlotDao() {
      return flowSlotDao;
   }
   @Required
   public void setFlowSlotDao(FlowSlotDao flowSlotDao) {
      this.flowSlotDao = flowSlotDao;
   }

   @Required
   public void setFlowSlotTicketDao(FlowSlotTicketDao oFlowSlotTicketDao) {
      this.oFlowSlotTicketDao = oFlowSlotTicketDao;
   }

   public BaseEntityDao getBaseEntityDao() {
      return baseEntityDao;
   }

   @Required
   public void setBaseEntityDao(BaseEntityDao baseEntityDao) {
      this.baseEntityDao = baseEntityDao;
   }

   @Override
   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      this.applicationContext = applicationContext;
   }


   public Days getFlowSlots(Long nID_ServiceData, DateTime startDate, DateTime endDate, boolean bAll) {
      List<FlowSlot> flowSlots = flowSlotDao.findFlowSlotsByServiceData(nID_ServiceData, startDate, endDate);

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
      for (Map.Entry<DateTime, Day> entry : daysMap.entrySet()) {
         Day day = entry.getValue();
         if (bAll || day.isbHasFree()) {
            res.getaDay().add(day);
         }
      }

      return res;
   }

   public FlowSlotTicket saveFlowSlotTicket(Long nID_FlowSlot, Long nID_Subject, Long nID_Task_Activiti) {

      FlowSlotTicket oFlowSlotTicket = oFlowSlotTicketDao.findFlowSlotTicket(nID_FlowSlot);
      if (oFlowSlotTicket == null) {
         oFlowSlotTicket = new FlowSlotTicket();
      }

      oFlowSlotTicket.setnID_Subject(nID_Subject);
      oFlowSlotTicket.setnID_Task_Activiti(nID_Task_Activiti);

      FlowSlot flowSlot = baseEntityDao.getById(FlowSlot.class, nID_FlowSlot);

      oFlowSlotTicket.setoFlowSlot(flowSlot);
      oFlowSlotTicket.setsDateStart(flowSlot.getsDate());

      Duration duration = DurationUtil.parseDuration(flowSlot.getsDuration());
      DateTime finishDateTime = flowSlot.getsDate().plusMinutes(duration.getMinutes());
      oFlowSlotTicket.setsDateFinish(finishDateTime);

      oFlowSlotTicket.setsDateEdit(DateTime.now());

      baseEntityDao.saveOrUpdate(oFlowSlotTicket);
      return oFlowSlotTicket;
   }

   
   /**
    * Generates FlowSlots in given interval for specified flow. Slots will not be generated if they are already exist.
     * @param sID_BP
    * @return 
    */
   public Long nID_Flow_ServiceData(String sID_BP) {
      List<Flow_ServiceData> aFlow = baseEntityDao.getAll(Flow_ServiceData.class);
       for(Flow_ServiceData oFlow : aFlow){
           if (oFlow.getsID_BP().equalsIgnoreCase(sID_BP)){
               return oFlow.getId();
           }
       }
       return null;
   }

           
   /**
    * Generates FlowSlots in given interval for specified flow. Slots will not be generated if they are already exist.
    * @param nID_Flow_ServiceData ID of flow
    * @param startDate start date of generation (inclusive)
    * @param stopDate stop date of generation (exclusive)
    * @return generated slots.
    */
   public List<FlowSlotVO> buildFlowSlots(Long nID_Flow_ServiceData, DateTime startDate, DateTime stopDate) {

      Flow_ServiceData flow = baseEntityDao.getById(Flow_ServiceData.class, nID_Flow_ServiceData);

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

               List<FlowSlot> generatedSlots = handler.generateObjects(flowProperty.getsData());
               for (FlowSlot slot : generatedSlots) {
                  res.add(new FlowSlotVO(slot));
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

      flowSlotDao.deleteAll(flowSlotsToDelete);
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
