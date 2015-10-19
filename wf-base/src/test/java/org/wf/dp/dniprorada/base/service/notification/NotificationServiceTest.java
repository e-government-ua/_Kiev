package org.wf.dp.dniprorada.base.service.notification;

import org.activiti.rest.controller.IntegrationTestsApplicationConfiguration;
import org.apache.commons.mail.EmailException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * User: goodg_000
 * Date: 26.08.2015
 * Time: 1:13
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = IntegrationTestsApplicationConfiguration.class)
public class NotificationServiceTest {

    //@Autowired
    //private NotificationService notificationService;

    //private SimpleSmtpServer server;

    @Before
    public void onBefore() {
        //server = SimpleSmtpServer.start();
    }

    @After
    public void onAfter() {
        //server.stop();
    }

    @Test
    public void testSendTaskCreatedInfoEmail() throws EmailException {
      /*notificationService.sendTaskCreatedInfoEmail("test.email@gmail.com", 123L);
      Assert.assertEquals(1, server.getReceivedEmailSize());*/
    }
}
