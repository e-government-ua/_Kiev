package org.activiti.test.bp.dnepr.mvk_1;

import com.plexiti.activiti.test.fluent.ActivitiFluentTestHelper;
import com.plexiti.activiti.test.fluent.mocking.Mockitos;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.impl.test.TestHelper;
import org.activiti.engine.runtime.JobQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Ignore;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("default")
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/dnepr/mvk/1/activiti.cfg.mock.xml",
        "classpath:META-INF/spring/dnepr/mvk/1/activiti.cfg.scan.xml",
        "classpath:context_test-region.xml" })
public class Mvk_1_Test extends PluggableActivitiTestCase {

    static final String PROCESS_KEY = "CivilCopyDocRequest";
    static final String PROCESS_RESOURCE = "bpmn/autodeploy/dnepr_mvk-1.bpmn";

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("context_test-region.xml");

    @Before
    public void injectDependencies() throws Exception {

        MockitoAnnotations.initMocks(this);
        Mockitos.register(this);

        new TestContextManager(getClass()).prepareTestInstance(this);
        processEngine = TestHelper.getProcessEngine(activitiRule
                .getConfigurationResource());
        activitiRule.setProcessEngine(processEngine);

        ActivitiFluentTestHelper.setUpEngineAndServices(activitiRule
                .getProcessEngine(), activitiRule.getProcessEngine()
                .getRepositoryService(), activitiRule.getProcessEngine()
                .getRuntimeService(), activitiRule.getProcessEngine()
                .getTaskService(), activitiRule.getProcessEngine()
                .getHistoryService(), activitiRule.getProcessEngine()
                .getIdentityService(), activitiRule.getProcessEngine()
                .getManagementService(), activitiRule.getProcessEngine()
                .getFormService());

    }

    @After
    public void cleanUp() {

    }

    private Map<String, Object> createStartFormVariables() {
        Map<String, Object> procVars = new HashMap<String, Object>();

        procVars.put("bankIdlastName", "Кочубей");
        procVars.put("bankIdfirstName", "Микола");
        procVars.put("bankIdmiddleName", "Батькович");
        procVars.put("bankIdPassport", "АЕ654987ВВ");
        procVars.put("docType",
                "копія рішення виконавчого комітету Дніпропетровської міської ради");
        procVars.put("docDate", "01/01/1971");
        procVars.put("docNumber", "5789");
        procVars.put("docName", "Наказ");
        procVars.put("goal", "знати усе");
        procVars.put("email", "kochubey@gmail.egov.ua");
        procVars.put(
                "processName",
                "Надання копій рішень міської ради та її виконкому, розпоряджень міського голови за зверненням юридичних та фізичних осіб, правоохоронних органів,адвокатів");
        return procVars;
    }

    @Ignore
    @Test
    @Deployment(resources = { PROCESS_RESOURCE })
    public void startProcessInstance() throws InterruptedException {
        Map<String, Object> procVars = createStartFormVariables();

        ProcessInstance pi = activitiRule.getProcessEngine()
                .getRuntimeService()
                .startProcessInstanceByKey(PROCESS_KEY, procVars);

        //		assertNotNull(pi);

        activitiRule
                .getProcessEngine()
                .getRuntimeService()
                .deleteProcessInstance(pi.getProcessInstanceId(),
                        "not needed, created only for test");

    }

    @Ignore
    @Test
    @Deployment(resources = { PROCESS_RESOURCE })
    public void processRejected() throws InterruptedException {
        Map<String, Object> procVars = createStartFormVariables();

        ProcessInstance pi = activitiRule.getProcessEngine()
                .getRuntimeService()
                .startProcessInstanceByKey(PROCESS_KEY, procVars);

        //		assertNotNull(pi);

        Task task = activitiRule.getTaskService().createTaskQuery()
                .singleResult();
        //		assertNotNull(task);

        Map<String, Object> taskVars = new HashMap<String, Object>();
        taskVars.put("decide", "reject");
        activitiRule.getProcessEngine().getTaskService()
                .complete(task.getId(), taskVars);

        HistoryService historyService = activitiRule.getHistoryService();

        HistoricProcessInstance historicProcessInstance = historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceId(pi.getProcessInstanceId()).singleResult();

        //		assertNotNull(historicProcessInstance);
        // check that only one process running
        //		assertEquals(pi.getProcessInstanceId(), historicProcessInstance.getId());

        List<HistoricActivityInstance> activityList = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(pi.getProcessInstanceId()).list();

        JobQuery jquery = activitiRule.getManagementService().createJobQuery();

        // check how many tasks must be done
        //		assertEquals("done task count", 6, activityList.size());

        // and the job is done
        //		assertEquals("job is done", 0, jquery.count());

        //		assertEquals(0, activitiRule.getProcessEngine().getRuntimeService()
        //				.createProcessInstanceQuery().count());

    }

}
