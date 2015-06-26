package org.activiti.rest.controller;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.wf.dp.dniprorada.base.util.JsonDateTimeSerializer;
import org.wf.dp.dniprorada.base.util.JsonRestUtils;
import org.wf.dp.dniprorada.base.viewobject.flow.Day;
import org.wf.dp.dniprorada.base.viewobject.flow.Days;
import org.wf.dp.dniprorada.base.viewobject.flow.FlowSlotVO;
import org.wf.dp.dniprorada.base.viewobject.flow.SaveSubjectTicketResponse;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * User: goodg_000
 * Date: 21.06.2015
 * Time: 21:10
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = IntegrationTestsApplicationConfiguration.class)
public class ActivitiRestFlowControllerScenario {

   public static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json;charset=UTF-8";
   @Autowired
   private WebApplicationContext webApplicationContext;

   private MockMvc mockMvc;

   @Before
   public void setUp() {
      mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
   }

   @Test
   public void shouldSuccessfullyGetFlowSlotsAndSaveTicket() throws Exception {
      String getJsonData = mockMvc.perform(get("/flow/getFlowSlots_ServiceData").
              param("nID_ServiceData", "1")).
              andExpect(status().isOk()).
              andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
              andReturn().getResponse().getContentAsString();
      Days days = JsonRestUtils.readObject(getJsonData, Days.class);

      Assert.assertTrue(days.getaDay().size() > 0);

      Day day = findFirstFreeDay(days);
      FlowSlotVO freeSlot = findFirstFreeSlot(day);
      Assert.assertTrue(freeSlot.isbFree());

      String[] hoursAndMinutes= freeSlot.getsTime().split(":");
      int hours = Integer.parseInt(hoursAndMinutes[0]);
      int minutes = Integer.parseInt(hoursAndMinutes[1]);

      DateTime dateTime = day.getsDate().withHourOfDay(hours).withMinuteOfHour(minutes);

      String sDateTime = JsonDateTimeSerializer.DATETIME_FORMATTER.print(dateTime);

      String setJsonData = mockMvc.perform(post("/flow/setFlowSlot_ServiceData").
              param("nID_FlowSlot", ""+freeSlot.getnID()).
              param("sDate", sDateTime).
              param("nID_Subject", "2").
              param("nID_Task_Activiti", "1")).
              andExpect(status().isOk()).
              andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
              andReturn().getResponse().getContentAsString();

      SaveSubjectTicketResponse response = JsonRestUtils.readObject(setJsonData, SaveSubjectTicketResponse.class);
      Long ticketId = response.getnID_Ticket();
      Assert.assertTrue(ticketId != null);

      getJsonData = mockMvc.perform(get("/flow/getFlowSlots_ServiceData").
              param("nID_ServiceData", "1")).
              andExpect(status().isOk()).
              andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
              andReturn().getResponse().getContentAsString();
      days = JsonRestUtils.readObject(getJsonData, Days.class);

      FlowSlotVO slotAfterModification1 = findSlot(days, freeSlot.getnID());
      Assert.assertTrue(slotAfterModification1 == null); // slot is no more free, therefor it doesn't returned

      // save second time and make sure that Id is not changed
      setJsonData = mockMvc.perform(post("/flow/setFlowSlot_ServiceData").
              param("nID_FlowSlot", ""+freeSlot.getnID()).
              param("sDate", sDateTime).
              param("nID_Subject", "2").
              param("nID_Task_Activiti", "1")).
              andExpect(status().isOk()).
              andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
              andReturn().getResponse().getContentAsString();
      response = JsonRestUtils.readObject(setJsonData, SaveSubjectTicketResponse.class);
      Assert.assertEquals(ticketId, response.getnID_Ticket());
   }

   private FlowSlotVO findSlot(Days days, Long slotId) {
      FlowSlotVO res = null;
      A: for (Day day : days.getaDay()) {
         for (FlowSlotVO flowSlotVO : day.getaSlot()) {
            if (flowSlotVO.getnID().equals(slotId)) {
               res = flowSlotVO;
               break A;
            }
         }
      }

      return res;
   }

   private Day findFirstFreeDay(Days days) {
      Day res = null;
      for (Day day : days.getaDay()) {
         if (day.isbHasFree()) {
            res = day;
            break;
         }
      }

      return res;
   }

   private FlowSlotVO findFirstFreeSlot(Day day) {
      FlowSlotVO res = null;

      for (FlowSlotVO flowSlotVO : day.getaSlot()) {
         if (flowSlotVO.isbFree()) {
            res = flowSlotVO;
            break;
         }
      }

      return res;
   }
}
