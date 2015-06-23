package org.activiti.rest.controller;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wf.dp.dniprorada.base.dao.BaseEntityDao;
import org.wf.dp.dniprorada.base.dao.FlowSlotDao;
import org.wf.dp.dniprorada.base.dao.SubjectTicketDao;
import org.wf.dp.dniprorada.base.model.FlowSlot;
import org.wf.dp.dniprorada.base.model.SubjectTicket;
import org.wf.dp.dniprorada.base.util.JsonRestUtils;
import org.wf.dp.dniprorada.base.viewobject.flow.Day;
import org.wf.dp.dniprorada.base.viewobject.flow.Days;
import org.wf.dp.dniprorada.base.viewobject.flow.FlowSlotVO;
import org.wf.dp.dniprorada.base.viewobject.flow.SaveSubjectTicketResponse;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * User: goodg_000
 * Date: 21.06.2015
 * Time: 14:02
 */
@Controller
@RequestMapping(value = "/flow")
public class ActivitiRestFlowController {

   @Autowired
   private FlowSlotDao flowSlotDao;

   @Autowired
   private SubjectTicketDao subjectTicketDao;

   @Autowired
   private BaseEntityDao baseEntityDao;

   @RequestMapping(value = "/getFlowSlots_ServiceData", method = RequestMethod.GET)
   public
   @ResponseBody
   ResponseEntity getFlowSlots(@RequestParam(value = "nID_ServiceData") Long nID_ServiceData,
                               @RequestParam(value = "bAll", required = false, defaultValue = "false") boolean bAll,
                               @RequestParam(value = "nDays", required = false, defaultValue = "60") int nDays) {


      DateTime startDate = DateTime.now().withTimeAtStartOfDay();
      DateTime endDate = startDate.plusDays(nDays);

      List<FlowSlot> flowSlots = flowSlotDao.getFlowSlotsOrderByDateAsc(nID_ServiceData, startDate, endDate);

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
         Day day = daysMap.get(currDate);
         if (day == null) {
            day = new Day(currDate);
            daysMap.put(currDate, day);
         }

         FlowSlotVO flowSlotVO = new FlowSlotVO(flowSlot);
         day.getaSlot().add(flowSlotVO);

         if (!day.isbHasFree() && flowSlotVO.isbFree()) {
            day.setbHasFree(true);
         }
      }

      Days res = new Days();
      for (Map.Entry<DateTime, Day> entry : daysMap.entrySet()) {
         res.getaDay().add(entry.getValue());
      }

      return JsonRestUtils.toJsonResponse(res);
   }

   @RequestMapping(value = "/setFlowSlot_ServiceData", method = RequestMethod.POST)
   public
   @ResponseBody
   ResponseEntity saveSubjectTicket(@RequestParam(value = "nID_FlowSlot") Long nID_FlowSlot,
                               @RequestParam(value = "nID_Subject") Long nID_Subject,
                               @RequestParam(value = "nID_Task_Activiti", required = false) Long nID_Task_Activiti) {

      SubjectTicket subjectTicket = subjectTicketDao.findSubjectTicket(nID_FlowSlot);
      if (subjectTicket == null) {
         subjectTicket = new SubjectTicket();
      }

      subjectTicket.setnID_Subject(nID_Subject);
      subjectTicket.setnID_Task_Activiti(nID_Task_Activiti);

      FlowSlot flowSlot = baseEntityDao.getById(FlowSlot.class, nID_FlowSlot);

      if (flowSlot == null) {
         return new ResponseEntity<>("FlowSlot with id=" + nID_FlowSlot + " is not found!",
                 HttpStatus.INTERNAL_SERVER_ERROR);
      }

      subjectTicket.setoFlowSlot(flowSlot);
      subjectTicket.setsDateEdit(flowSlot.getsDate());

      baseEntityDao.saveOrUpdate(subjectTicket);

      return JsonRestUtils.toJsonResponse(new SaveSubjectTicketResponse(subjectTicket.getId()));
   }

}
