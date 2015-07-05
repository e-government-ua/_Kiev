package org.activiti.rest.controller;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wf.dp.dniprorada.base.model.FlowSlotTicket;
import org.wf.dp.dniprorada.base.service.flow.FlowService;
import org.wf.dp.dniprorada.base.util.JsonDateTimeSerializer;
import org.wf.dp.dniprorada.base.util.JsonRestUtils;
import org.wf.dp.dniprorada.base.viewobject.flow.ClearSlotsResult;
import org.wf.dp.dniprorada.base.viewobject.flow.Days;
import org.wf.dp.dniprorada.base.viewobject.flow.FlowSlotVO;
import org.wf.dp.dniprorada.base.viewobject.flow.SaveFlowSlotTicketResponse;

import java.util.List;

/**
 * User: goodg_000
 * Date: 21.06.2015
 * Time: 14:02
 */
@Controller
@RequestMapping(value = "/flow")
public class ActivitiRestFlowController {

   @Autowired
   private FlowService flowService;

   @RequestMapping(value = "/getFlowSlots_ServiceData", method = RequestMethod.GET)
   public
   @ResponseBody
   ResponseEntity getFlowSlots(@RequestParam(value = "nID_ServiceData") Long nID_ServiceData,
                               @RequestParam(value = "bAll", required = false, defaultValue = "false") boolean bAll,
                               @RequestParam(value = "nDays", required = false, defaultValue = "60") int nDays) {


      DateTime startDate = DateTime.now().withTimeAtStartOfDay();
      DateTime endDate = startDate.plusDays(nDays);

      Days res = flowService.getFlowSlots(nID_ServiceData, startDate, endDate, bAll);

      return JsonRestUtils.toJsonResponse(res);
   }

   @RequestMapping(value = "/setFlowSlot_ServiceData", method = RequestMethod.POST)
   public
   @ResponseBody
   ResponseEntity saveFlowSlotTicket(@RequestParam(value = "nID_FlowSlot") Long nID_FlowSlot,
                               @RequestParam(value = "nID_Subject") Long nID_Subject,
                               @RequestParam(value = "nID_Task_Activiti", required = false) Long nID_Task_Activiti) {

      FlowSlotTicket oFlowSlotTicket = flowService.saveFlowSlotTicket(nID_FlowSlot, nID_Subject, nID_Task_Activiti);

      return JsonRestUtils.toJsonResponse(new SaveFlowSlotTicketResponse(oFlowSlotTicket.getId()));
   }

   @RequestMapping(value = "/buildFlowSlots", method = RequestMethod.POST)
   public
   @ResponseBody
   ResponseEntity buildFlowSlots(@RequestParam(value = "nID_Flow_ServiceData") Long nID_Flow_ServiceData,
                                    @RequestParam(value = "sDateStart", required = false) String sDateStart,
                                    @RequestParam(value = "sDateStop", required = false) String sDateStop) {

      DateTime startDate = null;
      if (sDateStart != null) {
         startDate = JsonDateTimeSerializer.DATETIME_FORMATTER.parseDateTime(sDateStart);
      }

      DateTime stopDate = null;
      if (sDateStop != null) {
         stopDate = JsonDateTimeSerializer.DATETIME_FORMATTER.parseDateTime(sDateStop);
      }

      List<FlowSlotVO> res = flowService.buildFlowSlots(nID_Flow_ServiceData, startDate, stopDate);

      return JsonRestUtils.toJsonResponse(res);
   }

   @RequestMapping(value = "/clearFlowSlots", method = RequestMethod.DELETE)
   public
   @ResponseBody
   ResponseEntity clearFlowSlots(@RequestParam(value = "nID_Flow_ServiceData") Long nID_Flow_ServiceData,
                                 @RequestParam(value = "sDateStart") String sDateStart,
                                 @RequestParam(value = "sDateStop") String sDateStop,
                                 @RequestParam(value ="bWithTickets", required = false, defaultValue = "false")
                                 boolean bWithTickets) {
      DateTime startDate = null;
      if (sDateStart != null) {
         startDate = JsonDateTimeSerializer.DATETIME_FORMATTER.parseDateTime(sDateStart);
      }

      DateTime stopDate = null;
      if (sDateStop != null) {
         stopDate = JsonDateTimeSerializer.DATETIME_FORMATTER.parseDateTime(sDateStop);
      }

      ClearSlotsResult res = flowService.clearFlowSlots(nID_Flow_ServiceData, startDate, stopDate, bWithTickets);
      return JsonRestUtils.toJsonResponse(res);
   }

}
