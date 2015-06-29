package org.activiti.rest.controller;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wf.dp.dniprorada.base.model.FlowSlot;
import org.wf.dp.dniprorada.base.model.SubjectTicket;
import org.wf.dp.dniprorada.base.service.flow.FlowService;
import org.wf.dp.dniprorada.base.util.JsonDateSerializer;
import org.wf.dp.dniprorada.base.util.JsonRestUtils;
import org.wf.dp.dniprorada.base.viewobject.flow.Days;
import org.wf.dp.dniprorada.base.viewobject.flow.FlowSlotVO;
import org.wf.dp.dniprorada.base.viewobject.flow.SaveSubjectTicketResponse;

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
   ResponseEntity saveSubjectTicket(@RequestParam(value = "nID_FlowSlot") Long nID_FlowSlot,
                               @RequestParam(value = "nID_Subject") Long nID_Subject,
                               @RequestParam(value = "nID_Task_Activiti", required = false) Long nID_Task_Activiti) {

      SubjectTicket subjectTicket = flowService.saveSubjectTicket(nID_FlowSlot, nID_Subject, nID_Task_Activiti);

      return JsonRestUtils.toJsonResponse(new SaveSubjectTicketResponse(subjectTicket.getId()));
   }

   @RequestMapping(value = "/buildFlowSlots", method = RequestMethod.POST)
   public
   @ResponseBody
   ResponseEntity buildFlowSlots(@RequestParam(value = "nID_Flow_ServiceData") Long nID_Flow_ServiceData,
                                    @RequestParam(value = "sDateStart", required = false) String sDateStart,
                                    @RequestParam(value = "sDateStop", required = false) String sDateStop) {

      DateTime startDate = null;
      if (sDateStart != null) {
         startDate = JsonDateSerializer.DATE_FORMATTER.parseDateTime(sDateStart);
      }

      DateTime stopDate = null;
      if (sDateStop != null) {
         stopDate = JsonDateSerializer.DATE_FORMATTER.parseDateTime(sDateStop);
      }

      List<FlowSlotVO> res = flowService.buildFlowSlots(nID_Flow_ServiceData, startDate, stopDate);

      return JsonRestUtils.toJsonResponse(res);
   }

}
