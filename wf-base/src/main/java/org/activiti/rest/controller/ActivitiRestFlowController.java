package org.activiti.rest.controller;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.time.DateUtils;
import org.egov.util.QuartzUtil;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wf.dp.dniprorada.base.dao.FlowServiceDataDao;
import org.wf.dp.dniprorada.base.dao.GenericEntityDao;
import org.wf.dp.dniprorada.base.dao.FlowSlotTicketDao;
import org.wf.dp.dniprorada.base.model.FlowProperty;
import org.wf.dp.dniprorada.base.model.FlowPropertyClass;
import org.wf.dp.dniprorada.base.model.FlowSlotTicket;
import org.wf.dp.dniprorada.base.model.Flow_ServiceData;
import org.wf.dp.dniprorada.base.service.flow.FlowService;
import org.wf.dp.dniprorada.base.util.JsonDateSerializer;
import org.wf.dp.dniprorada.base.util.JsonDateTimeSerializer;
import org.wf.dp.dniprorada.base.util.JsonRestUtils;
import org.wf.dp.dniprorada.base.viewobject.flow.ClearSlotsResult;
import org.wf.dp.dniprorada.base.viewobject.flow.Days;
import org.wf.dp.dniprorada.base.viewobject.flow.FlowSlotVO;
import org.wf.dp.dniprorada.base.viewobject.flow.SaveFlowSlotTicketResponse;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: goodg_000
 * Date: 21.06.2015
 * Time: 14:02
 */
@Controller
@RequestMapping(value = "/flow")
public class ActivitiRestFlowController {

   private static final long DEFAULT_FLOW_PROPERTY_CLASS = 1l;

   private static final Logger log = LoggerFactory.getLogger(ActivitiRestFlowController.class);

   @Autowired
   private FlowService flowService;

   @Autowired
   private TaskService taskService;

   @Autowired
   private RepositoryService repositoryService;

   @Autowired
   @Qualifier("flowPropertyDao")
   private GenericEntityDao<FlowProperty> flowPropertyDao;

   @Autowired
   private FlowServiceDataDao flowServiceDataDao;

   @Autowired
   @Qualifier("flowPropertyClassDao")
   private GenericEntityDao<FlowPropertyClass> flowPropertyClassDao;

   @Autowired
   private FlowSlotTicketDao flowSlotTicketDao;

   @RequestMapping(value = "/getFlowSlots_ServiceData", method = RequestMethod.GET)
   public
   @ResponseBody
   ResponseEntity getFlowSlots(@RequestParam(value = "nID_ServiceData", required = false) Long nID_ServiceData,
                               @RequestParam(value = "sID_BP", required = false) String sID_BP,
                               @RequestParam(value = "nID_SubjectOrganDepartment", required = false) Long nID_SubjectOrganDepartment,
                               @RequestParam(value = "bAll", required = false, defaultValue = "false") boolean bAll,
                               @RequestParam(value = "nFreeDays", required = false, defaultValue = "60") int nFreeDays,
                               @RequestParam(value = "nDays", required = false, defaultValue = "177") int nDays,
                               @RequestParam(value = "sDateStart", required = false) String sDateStart
   ) throws Exception {


      DateTime oDateStart = DateTime.now().withTimeAtStartOfDay();
      oDateStart = oDateStart.plusDays(2);
      DateTime oDateEnd = oDateStart.plusDays(nDays);

      if (sDateStart != null) {
         oDateStart = JsonDateSerializer.DATE_FORMATTER.parseDateTime(sDateStart);
         oDateEnd = oDateStart.plusDays(nDays);
      }


      Days res = flowService.getFlowSlots(nID_ServiceData, sID_BP, nID_SubjectOrganDepartment,
              oDateStart, oDateEnd, bAll, nFreeDays);

      return JsonRestUtils.toJsonResponse(res);
   }

   @RequestMapping(value = "/setFlowSlot_ServiceData", method = RequestMethod.POST)
   public
   @ResponseBody
   ResponseEntity saveFlowSlotTicket(@RequestParam(value = "nID_FlowSlot") Long nID_FlowSlot,
                                     @RequestParam(value = "nID_Subject") Long nID_Subject,
                                     @RequestParam(value = "nID_Task_Activiti", required = false) Long nID_Task_Activiti) throws Exception {

      FlowSlotTicket oFlowSlotTicket = flowService.saveFlowSlotTicket(nID_FlowSlot, nID_Subject, nID_Task_Activiti);

      return JsonRestUtils.toJsonResponse(new SaveFlowSlotTicketResponse(oFlowSlotTicket.getId()));
   }

   @RequestMapping(value = "/buildFlowSlots", method = RequestMethod.POST)
   public
   @ResponseBody
   ResponseEntity buildFlowSlots(
           @RequestParam(value = "nID_Flow_ServiceData", required = false) Long nID_Flow_ServiceData,
           @RequestParam(value = "sID_BP", required = false) String sID_BP,
           @RequestParam(value = "nID_SubjectOrganDepartment", required = false) Long nID_SubjectOrganDepartment,
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

      if (nID_Flow_ServiceData == null) {
         if (sID_BP != null) {
            nID_Flow_ServiceData = flowServiceDataDao.findFlowId(sID_BP, nID_SubjectOrganDepartment);
         } else {
            String sError = "nID_Flow_ServiceData==null and sID_BP==null";
            log.error(sError);
            return new ResponseEntity<>(sError, HttpStatus.INTERNAL_SERVER_ERROR);
         }
      }
      if (nID_Flow_ServiceData == null) {
         String sError = "nID_Flow_ServiceData==null";
         log.error(sError);
         return new ResponseEntity<>(sError, HttpStatus.INTERNAL_SERVER_ERROR);
      }
      log.info("sID_BP=" + sID_BP + ",nID_Flow_ServiceData=" + nID_Flow_ServiceData);

      List<FlowSlotVO> res = flowService.buildFlowSlots(nID_Flow_ServiceData, startDate, stopDate);

      return JsonRestUtils.toJsonResponse(res);
   }

   @RequestMapping(value = "/clearFlowSlots", method = RequestMethod.DELETE)
   public
   @ResponseBody
   ResponseEntity clearFlowSlots(
           @RequestParam(value = "nID_Flow_ServiceData", required = false) Long nID_Flow_ServiceData,
           @RequestParam(value = "sID_BP", required = false) String sID_BP,
           @RequestParam(value = "nID_SubjectOrganDepartment", required = false) Long nID_SubjectOrganDepartment,
           @RequestParam(value = "sDateStart") String sDateStart,
           @RequestParam(value = "sDateStop") String sDateStop,
           @RequestParam(value = "bWithTickets", required = false, defaultValue = "false")
           boolean bWithTickets) {
      DateTime startDate = null;
      if (sDateStart != null) {
         startDate = JsonDateTimeSerializer.DATETIME_FORMATTER.parseDateTime(sDateStart);
      }

      DateTime stopDate = null;
      if (sDateStop != null) {
         stopDate = JsonDateTimeSerializer.DATETIME_FORMATTER.parseDateTime(sDateStop);
      }

      if (nID_Flow_ServiceData == null) {
         if (sID_BP != null) {
            nID_Flow_ServiceData = flowServiceDataDao.findFlowId(sID_BP, nID_SubjectOrganDepartment);
         } else {
            String sError = "nID_Flow_ServiceData==null and sID_BP==null";
            log.error(sError);
            return new ResponseEntity<>(sError, HttpStatus.INTERNAL_SERVER_ERROR);
         }
      }
      if (nID_Flow_ServiceData == null) {
         String sError = "nID_Flow_ServiceData==null";
         log.error(sError);
         return new ResponseEntity<>(sError, HttpStatus.INTERNAL_SERVER_ERROR);
      }
      log.info("sID_BP=" + sID_BP + ",nID_Flow_ServiceData=" + nID_Flow_ServiceData);


      ClearSlotsResult res = flowService.clearFlowSlots(nID_Flow_ServiceData, startDate, stopDate, bWithTickets);
      return JsonRestUtils.toJsonResponse(res);
   }

   /**
    * Returns list of included schedules within flow
    *
    * @param nID_Flow_ServiceData - ID of flow
    * @return List of schedule with bExclude=false
    */
   @RequestMapping(value = "/getSheduleFlowIncludes", method = RequestMethod.GET)
   public
   @ResponseBody
   List<FlowProperty> getSheduleFlowIncludes(
           @RequestParam(value = "nID_Flow_ServiceData", required = false) Long nID_Flow_ServiceData,
           @RequestParam(value = "sID_BP", required = false) String sID_BP,
           @RequestParam(value = "nID_SubjectOrganDepartment", required = false) Long nID_SubjectOrganDepartment

   ) throws Exception {
      //if (nID_Flow_ServiceData != null) {
      //log.info("nID_Flow_ServiceData is not null. Getting flow property for the flow with ID: " + nID_Flow_ServiceData);
      return getFilteredFlowPropertiesForFlowServiceData(nID_Flow_ServiceData, sID_BP, nID_SubjectOrganDepartment,
              Boolean.FALSE);
      //}
      //return new LinkedList<FlowProperty>();
   }

   /**
    * Returns list of excluded schedules within flow
    *
    * @param nID_Flow_ServiceData - ID of flow
    * @return List of schedule with bExclude=false
    */
   @RequestMapping(value = "/getSheduleFlowExcludes", method = RequestMethod.GET)
   public
   @ResponseBody
   List<FlowProperty> getSheduleFlowExcludes(
           @RequestParam(value = "nID_Flow_ServiceData", required = false) Long nID_Flow_ServiceData,
           @RequestParam(value = "sID_BP", required = false) String sID_BP,
           @RequestParam(value = "nID_SubjectOrganDepartment", required = false) Long nID_SubjectOrganDepartment
   ) throws Exception {
      //if (nID_Flow_ServiceData != null) {
      //log.info("nID_Flow_ServiceData is not null. Getting flow property for the flow with ID: " + nID_Flow_ServiceData);
      return getFilteredFlowPropertiesForFlowServiceData(nID_Flow_ServiceData, sID_BP, nID_SubjectOrganDepartment,
              Boolean.TRUE);
      //}
      //return new LinkedList<FlowProperty>();
   }

   /**
    * Adds/removes schedule include slot
    *
    * @param nID                  - ID of flow property
    * @param nID_Flow_ServiceData - ID of flow
    * @param sName                - name of the slot
    * @param sRegionTime          - time period, "14:16-16:30"
    * @param saRegionWeekDay      - array of days in a week ("su,mo,tu")
    * @return ID of new FlowProperty
    */
   @RequestMapping(value = "/setSheduleFlowInclude", method = RequestMethod.GET)
   public
   @ResponseBody
   FlowProperty setSheduleFlowInclude(
           @RequestParam(value = "nID", required = false) Long nID,
           @RequestParam(value = "nID_Flow_ServiceData", required = false) Long nID_Flow_ServiceData,
           @RequestParam(value = "sID_BP", required = false) String sID_BP,
           @RequestParam(value = "nID_SubjectOrganDepartment", required = false) Long nID_SubjectOrganDepartment,
           @RequestParam(value = "sName") String sName,
           @RequestParam(value = "sRegionTime") String sRegionTime,

           @RequestParam(value = "nLen", required = false) Integer nLen,
           @RequestParam(value = "sLenType", required = false) String sLenType,
           @RequestParam(value = "sData", required = false) String sData,

           @RequestParam(value = "saRegionWeekDay") String saRegionWeekDay,
           @RequestParam(value = "sDateTimeAt") String sDateTimeAt,
           @RequestParam(value = "sDateTimeTo") String sDateTimeTo) throws Exception {
      FlowProperty flowProperty = null;
      if (sRegionTime != null && saRegionWeekDay != null && nLen != null){
    	  sData = QuartzUtil.getQuartzFormulaByParameters(sRegionTime, saRegionWeekDay, nLen);
      }
      if (nID != null) {
         log.info("nID is not null. Updating existing FLowProperty with parameters");
         flowProperty = flowPropertyDao.findByIdExpected(nID);
         if (flowProperty != null) {
            flowProperty = fillFlowProperty(sName, sRegionTime, saRegionWeekDay,
                    sDateTimeAt, sDateTimeTo, nLen, sLenType, sData, flowProperty);
            flowProperty.setbExclude(false);

            flowPropertyDao.saveOrUpdate(flowProperty);
            log.info("nID is not null. Updating existing FLowProperty with parameters");
         } else {
            log.info("Have not found FlowProperty object with ID: " + nID);
         }
      } else {


         if (nID_Flow_ServiceData == null) {
            if (sID_BP != null) {
               nID_Flow_ServiceData = flowServiceDataDao.findFlowId(sID_BP, nID_SubjectOrganDepartment);
            } else {
               String sError = "nID_Flow_ServiceData==null and sID_BP==null";
               log.error(sError);
               throw new Exception(sError);
            }
         }
         if (nID_Flow_ServiceData == null) {
            String sError = "nID_Flow_ServiceData==null";
            log.error(sError);
            throw new Exception(sError);
         }

         log.info("Creating new flow property for the flow with ID: "
                 + nID_Flow_ServiceData);
         flowProperty = new FlowProperty();

         FlowPropertyClass flowPropertyClass = flowPropertyClassDao.findByIdExpected(DEFAULT_FLOW_PROPERTY_CLASS);
         log.info("Loaded flow propetry service class: " + flowPropertyClass);
         Flow_ServiceData flowServiceData = flowServiceDataDao.findByIdExpected(nID_Flow_ServiceData);
         log.info("Loaded flow service data class: " + flowServiceData);

         flowProperty = fillFlowProperty(sName, sRegionTime, saRegionWeekDay, sDateTimeAt, sDateTimeTo, nLen, sLenType, sData, flowProperty);
         flowProperty.setoFlowPropertyClass(flowPropertyClass);
         flowProperty.setbExclude(false);
         flowProperty.setoFlow_ServiceData(flowServiceData);

         flowServiceData.getFlowProperties().add(flowProperty);

         flowServiceDataDao.saveOrUpdate(flowServiceData);
         log.info("Successfully updated flow with new FlowProperty.");
      }
      return flowProperty;
   }

   /**
    * Adds/removes schedule exclude slot
    *
    * @param nID                  - ID of flow property
    * @param nID_Flow_ServiceData - ID of flow
    * @param sName                - name of the slot
    * @param sRegionTime          - time period, "14:16-16:30"
    * @param saRegionWeekDay      - array of days in a week ("su,mo,tu")
    * @return ID of new FlowProperty
    */
   @RequestMapping(value = "/setSheduleFlowExclude", method = RequestMethod.GET)
   public
   @ResponseBody
   FlowProperty setSheduleFlowExclude(
           @RequestParam(value = "nID", required = false) Long nID,
           @RequestParam(value = "nID_Flow_ServiceData", required = false) Long nID_Flow_ServiceData,
           @RequestParam(value = "sID_BP", required = false) String sID_BP,
           @RequestParam(value = "nID_SubjectOrganDepartment", required = false) Long nID_SubjectOrganDepartment,
           @RequestParam(value = "sName") String sName,
           @RequestParam(value = "sRegionTime") String sRegionTime,

           @RequestParam(value = "nLen", required = false) Integer nLen,
           @RequestParam(value = "sLenType", required = false) String sLenType,
           @RequestParam(value = "sData", required = false) String sData,

           @RequestParam(value = "saRegionWeekDay") String saRegionWeekDay,
           @RequestParam(value = "sDateTimeAt") String sDateTimeAt,
           @RequestParam(value = "sDateTimeTo") String sDateTimeTo) throws Exception {
      FlowProperty flowProperty = null;
      if (nID != null) {
         log.info("nID is not null. Updating existing FLowProperty with parameters");
         flowProperty = flowPropertyDao.findByIdExpected(nID);
         if (flowProperty != null) {
            flowProperty = fillFlowProperty(sName, sRegionTime,
                    saRegionWeekDay, sDateTimeAt, sDateTimeTo, nLen, sLenType, sData, flowProperty);
            flowProperty.setbExclude(true);
            flowPropertyDao.saveOrUpdate(flowProperty);
            log.info("nID is not null. Updating existing FLowProperty with parameters");
         } else {
            log.info("Have not found FlowProperty object with ID: " + nID);
         }
      } else {

         if (nID_Flow_ServiceData == null) {
            if (sID_BP != null) {
               nID_Flow_ServiceData = flowServiceDataDao.findFlowId(sID_BP, nID_SubjectOrganDepartment);
            } else {
               String sError = "nID_Flow_ServiceData==null and sID_BP==null";
               log.error(sError);
               throw new Exception(sError);
            }
         }
         if (nID_Flow_ServiceData == null) {
            String sError = "nID_Flow_ServiceData==null";
            log.error(sError);
            throw new Exception(sError);
         }

         log.info("Creating new flow property for the flow with ID: "
                 + nID_Flow_ServiceData);
         flowProperty = new FlowProperty();

         FlowPropertyClass flowPropertyClass = flowPropertyClassDao.findByIdExpected(DEFAULT_FLOW_PROPERTY_CLASS);
         log.info("Loaded flow propetry service class: " + flowPropertyClass);
         Flow_ServiceData flowServiceData = flowServiceDataDao.findByIdExpected(nID_Flow_ServiceData);
         log.info("Loaded flow service data class: " + flowServiceData);

         flowProperty = fillFlowProperty(sName, sRegionTime, saRegionWeekDay, sDateTimeAt, sDateTimeTo, nLen, sLenType, sData, flowProperty);
         flowProperty.setoFlowPropertyClass(flowPropertyClass);
         flowProperty.setbExclude(true);
         flowProperty.setoFlow_ServiceData(flowServiceData);

         flowServiceData.getFlowProperties().add(flowProperty);

         flowServiceDataDao.saveOrUpdate(flowServiceData);
         log.info("Successfully updated flow with new FlowProperty.");
      }
      return flowProperty;
   }

   @RequestMapping(value = "/removeSheduleFlowInclude", method = RequestMethod.GET)
   public
   @ResponseBody
   List<FlowProperty> removeSheduleFlowInclude(
           @RequestParam(value = "nID") Long nID,
           @RequestParam(value = "nID_Flow_ServiceData", required = false) Long nID_Flow_ServiceData,
           @RequestParam(value = "sID_BP", required = false) String sID_BP,
           @RequestParam(value = "nID_SubjectOrganDepartment", required = false) Long nID_SubjectOrganDepartment
   ) throws Exception {

      if (nID_Flow_ServiceData == null) {
         if (sID_BP != null) {
            nID_Flow_ServiceData = flowServiceDataDao.findFlowId(sID_BP, nID_SubjectOrganDepartment);
         } else {
            String sError = "nID_Flow_ServiceData==null and sID_BP==null";
            log.error(sError);
            throw new Exception(sError);
         }
      }
      if (nID_Flow_ServiceData == null) {
         String sError = "nID_Flow_ServiceData==null";
         log.error(sError);
         throw new Exception(sError);
      }

      if (nID_Flow_ServiceData != null && nID != null) {
         log.info("nID_Flow_ServiceData is not null. Removing flow property with bExclude=false and ID:" + nID);

         Flow_ServiceData flowServiceData = flowServiceDataDao.findByIdExpected(nID_Flow_ServiceData);

         Iterator<FlowProperty> iterator = flowServiceData.getFlowProperties().iterator();
         while (iterator.hasNext()) {
            FlowProperty curr = iterator.next();
            log.info("Processing flow property with ID " + nID + " and bexclude=" + curr.getbExclude());

            if (curr.getId().equals(nID) && curr.getbExclude() != null && Boolean.valueOf(curr.getbExclude()).equals(Boolean.FALSE)) {
               iterator.remove();
               flowPropertyDao.delete(curr.getId());

               log.info("Removed flow property with ID " + nID + " and bexclude=false");
               break;
            }
         }

         log.info("Updated flow data. Removed FlowProperty schedules with bExlclude=true. Returning list without removed item:" + flowServiceData.getFlowProperties().size());

         return flowServiceData.getFlowProperties();
      } else {
         log.info("nID or nID_Flow_ServiceData are empty. Skipping logic of the method removeSheduleFlowExclude");
      }
      return new LinkedList<FlowProperty>();
   }

   @RequestMapping(value = "/removeSheduleFlowExclude", method = RequestMethod.GET)
   public
   @ResponseBody
   List<FlowProperty> removeSheduleFlowExclude(
           @RequestParam(value = "nID") Long nID,
           @RequestParam(value = "nID_Flow_ServiceData", required = false) Long nID_Flow_ServiceData,
           @RequestParam(value = "sID_BP", required = false) String sID_BP,
           @RequestParam(value = "nID_SubjectOrganDepartment", required = false) Long nID_SubjectOrganDepartment
   ) throws Exception {

      if (nID_Flow_ServiceData == null) {
         if (sID_BP != null) {
            nID_Flow_ServiceData = flowServiceDataDao.findFlowId(sID_BP, nID_SubjectOrganDepartment);
         } else {
            String sError = "nID_Flow_ServiceData==null and sID_BP==null";
            log.error(sError);
            throw new Exception(sError);
         }
      }
      if (nID_Flow_ServiceData == null) {
         String sError = "nID_Flow_ServiceData==null";
         log.error(sError);
         throw new Exception(sError);
      }

      if (nID_Flow_ServiceData != null && nID != null) {
         log.info("nID_Flow_ServiceData is not null. Removing flow property with bExclude=true and ID:" + nID);

         Flow_ServiceData flowServiceData = flowServiceDataDao.findByIdExpected(nID_Flow_ServiceData);

         Iterator<FlowProperty> iterator = flowServiceData.getFlowProperties().iterator();
         while (iterator.hasNext()) {
            FlowProperty curr = iterator.next();
            log.info("Processing flow property with ID " + nID + " and bexclude=" + curr.getbExclude());

            if (curr.getId().equals(nID) && curr.getbExclude() != null && Boolean.valueOf(curr.getbExclude()).equals(Boolean.TRUE)) {
               iterator.remove();
               flowPropertyDao.delete(curr.getId());

               log.info("Removed flow property with ID " + nID + " and bexclude=true");
               break;
            }
         }

         log.info("Updated flow data. Removed FlowProperty schedules with bExlclude=true. Returning list without removed item:" + flowServiceData.getFlowProperties().size());

         return flowServiceData.getFlowProperties();
      } else {
         log.info("nID or nID_Flow_ServiceData are empty. Skipping logic of the method removeSheduleFlowExclude");
      }
      return new LinkedList<FlowProperty>();
   }

   @RequestMapping(value = "/getFlowSlotTickets", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
   public
   @ResponseBody
   String getFlowSlotTickets(
           @RequestParam(value = "sLogin") String sLogin,
           @RequestParam(value = "bEmployeeUnassigned", required = false, defaultValue = "false") Boolean bEmployeeUnassigned,
           @RequestParam(value = "sDate", required = false) String sDate
   ) throws Exception {

      List<Map<String, String>> res = new LinkedList<Map<String, String>>();

      List<Task> tasks = getTasksForChecking(sLogin, bEmployeeUnassigned);

      Map<Long, Task> taskActivityIDsMap = new HashMap<Long, Task>();
      for (Task task : tasks) {
         if (task.getProcessInstanceId() != null) {
            taskActivityIDsMap.put(Long.valueOf(task.getProcessInstanceId()), task);
         } else {
            log.info("Task with ID:" + task.getId() + " has null process instance id value");
         }
      }

      log.info("Will check tasks which belong to process definition IDs:" + taskActivityIDsMap.keySet());

      List<FlowSlotTicket> allFlowSlowTickets = flowSlotTicketDao.findAll();
      log.info("Found " + (allFlowSlowTickets != null ? allFlowSlowTickets.size() : 0) + " flow slot tickets.");
      if (allFlowSlowTickets != null) {

         Collections.sort(allFlowSlowTickets, new Comparator<FlowSlotTicket>() {
            @Override
            public int compare(FlowSlotTicket ticket1, FlowSlotTicket ticket2) {
               return ticket1.getsDateStart().compareTo(ticket2.getsDateStart());
            }
         });

         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

         Date dateOfTasks = null;
         if (sDate != null) {
            log.info("Checking for flow spot tickets for the date: " + sDate);
            dateOfTasks = new SimpleDateFormat("yyyy-MM-dd").parse(sDate);
         }
         for (FlowSlotTicket currFlowSlotTicket : allFlowSlowTickets) {
            if (taskActivityIDsMap.keySet().contains(currFlowSlotTicket.getnID_Task_Activiti())) {
               Task tasksByActivitiID = taskActivityIDsMap.get(currFlowSlotTicket.getnID_Task_Activiti());

               if (dateOfTasks != null) {
                  log.info("Comparing two dates:" + currFlowSlotTicket.getsDateStart().toDate() + " and " + dateOfTasks);
               }
               if (dateOfTasks == null || (DateUtils.isSameDay(currFlowSlotTicket.getsDateStart().toDate(), dateOfTasks))) {
                  addFlowSlowTicketToResult(res, dateFormat, currFlowSlotTicket, tasksByActivitiID);
               } else {
                  log.info("Skipping flowSlot " + currFlowSlotTicket.getId() + " for the task:" + currFlowSlotTicket.getnID_Task_Activiti() +
                          " as they have not valid  start-end date" + currFlowSlotTicket.getsDateStart().toString() + ":" +
                          currFlowSlotTicket.getsDateFinish());
               }
            } else {
               log.info("List of tasks doesn't contain tasks with ID: " + currFlowSlotTicket.getnID_Task_Activiti());
            }
         }
      }

      String jsonRes = JSONValue.toJSONString(res);
      log.info("Result" + jsonRes);
      return jsonRes;
   }

   private void addFlowSlowTicketToResult(List<Map<String, String>> res,
                                          SimpleDateFormat dateFormat, FlowSlotTicket currFlowSlowTicket,
                                          Task tasksByActivitiID) {
      Map<String, String> currRes = new HashMap<String, String>();

      StringBuilder sb = new StringBuilder();
      sb.append("Adding flow slot ticket: ");
      sb.append(currFlowSlowTicket.getId());
      sb.append(":");
      sb.append(currFlowSlowTicket.getnID_Subject());
      sb.append(":");
      sb.append(currFlowSlowTicket.getsDateStart());
      sb.append(":");
      sb.append(currFlowSlowTicket.getsDateFinish());
      log.info(sb.toString());

      currRes.put("nID", currFlowSlowTicket.getId().toString());
      currRes.put("nID_FlowSlot", currFlowSlowTicket.getoFlowSlot() != null ?
              currFlowSlowTicket.getoFlowSlot().getId().toString() : "");
      currRes.put("nID_Subject", currFlowSlowTicket.getnID_Subject().toString());
      Date startDate = new Date(currFlowSlowTicket.getsDateStart().getMillis());
      currRes.put("sDateStart", dateFormat.format(startDate));
      Date finishDate = new Date(currFlowSlowTicket.getsDateFinish().getMillis());
      currRes.put("sDateFinish", dateFormat.format(finishDate));
      Date editDate = new Date(currFlowSlowTicket.getsDateEdit().getMillis());
      currRes.put("sDateEdit", dateFormat.format(editDate));

      currRes.put("nID_Task_Activiti", tasksByActivitiID.getId());

      currRes.put("name", tasksByActivitiID.getName());
      currRes.put("id", tasksByActivitiID.getId());
      currRes.put("assignee", tasksByActivitiID.getAssignee());
      currRes.put("nID_Instance", tasksByActivitiID.getProcessInstanceId());

      currRes.put("sUserTaskName", tasksByActivitiID.getName());
      ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(tasksByActivitiID.getProcessDefinitionId()).singleResult();
      currRes.put("sNameBP", processDefinition != null ? processDefinition.getName() : "");
      currRes.put("sTaskDate", dateFormat.format(tasksByActivitiID.getCreateTime()));
      res.add(currRes);
   }

   private List<Task> getTasksForChecking(String sLogin,
                                          Boolean bEmployeeUnassigned) {
      List<Task> tasks;
      if (bEmployeeUnassigned) {
         //tasks = taskService.createTaskQuery().taskUnassigned().active().list();
         tasks = taskService.createTaskQuery().taskCandidateUser(sLogin).taskUnassigned().active().list();
         log.info("Looking for unassigned tasks. Found " + (tasks != null ? tasks.size() : 0) + " tasks");
      } else {
         tasks = taskService.createTaskQuery().taskAssignee(sLogin).active().list();
         log.info("Looking for tasks assigned to user:" + sLogin + ". Found " + (tasks != null ? tasks.size() : 0) + " tasks");
      }
      return tasks;
   }

   protected List<FlowProperty> getFilteredFlowPropertiesForFlowServiceData(Long nID_Flow_ServiceData,
                                                                            String sID_BP,
                                                                            Long nID_SubjectOrganDepartment,
                                                                            Boolean bExclude) throws Exception {

      if (nID_Flow_ServiceData == null) {
         if (sID_BP != null) {
            nID_Flow_ServiceData = flowServiceDataDao.findFlowId(sID_BP, nID_SubjectOrganDepartment);
            log.info("[getFilteredFlowPropertiesForFlowServiceData](sID_BP=" + sID_BP + ",nID_Flow_ServiceData=" + nID_Flow_ServiceData + ")");
         } else {
            String sError = "nID_Flow_ServiceData==null and sID_BP==null";
            log.error(sError);
            throw new Exception(sError);
         }
      }
      if (nID_Flow_ServiceData == null) {
         String sError = "nID_Flow_ServiceData==null";
         log.error(sError);
         throw new Exception(sError);
      }


      log.info("[getFilteredFlowPropertiesForFlowServiceData](nID_Flow_ServiceData=" + nID_Flow_ServiceData + ")");
      Flow_ServiceData flowServiceData = flowServiceDataDao.findByIdExpected(nID_Flow_ServiceData);
      List<FlowProperty> res = new LinkedList<FlowProperty>();
      if (flowServiceData != null) {
         if (flowServiceData.getFlowProperties() != null && !flowServiceData.getFlowProperties().isEmpty()) {
            log.info("nID_Flow_ServiceData contains " + flowServiceData.getFlowProperties().size() + " elements. Getting only wuth bExclude =" + bExclude.toString());
            for (FlowProperty flowProperty : flowServiceData.getFlowProperties()) {
               log.info("flowProperty " + flowProperty.getId() + ":" + flowProperty.getsName() + ":" + flowProperty.getbExclude());
               if (flowProperty.getbExclude() != null && flowProperty.getbExclude().equals(bExclude)) {
                  res.add(flowProperty);
               }
            }
            log.info("Found " + res.size() + " flow properties with bExclude=" + bExclude + " . Results:" + res.toString());
         } else {
            log.info("Flow service data contains empty list of FlowProperty");
         }
      } else {
         log.info("Have not found nID_Flow_ServiceData object with ID: " + nID_Flow_ServiceData);
      }
      return res;
   }

   protected FlowProperty fillFlowProperty(String sName, String sRegionTime,
                                           String saRegionWeekDay, String sDateTimeAt, String sDateTimeTo,
                                           Integer nLen,
                                           String sLenType,
                                           String sData,
                                           FlowProperty flowProperty) {
      flowProperty.setbExclude(false);
      flowProperty.setsName(sName);
      flowProperty.setsRegionTime(sRegionTime);
      flowProperty.setSaRegionWeekDay(saRegionWeekDay);
      if (nLen != null) {
         flowProperty.setLen(nLen);
      }
      if (sLenType != null) {
         flowProperty.setLenType(sLenType);
      }
      if (sData != null) {
         flowProperty.setsData(sData);
      }
      flowProperty.setsDateTimeAt(sDateTimeAt);
      flowProperty.setsDateTimeTo(sDateTimeTo);
      return flowProperty;
   }

}
