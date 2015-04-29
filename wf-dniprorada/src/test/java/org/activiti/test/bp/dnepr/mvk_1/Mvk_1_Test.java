package org.activiti.test.bp.dnepr.mvk_1;

import static com.plexiti.activiti.test.fluent.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.impl.test.TestHelper;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.plexiti.activiti.test.fluent.ActivitiFluentTestHelper;

@RunWith(SpringJUnit4ClassRunner.class)
//@ActiveProfiles("default")
@ContextConfiguration(locations = {
		"classpath:META-INF\spring\dnepr\mvk\1\activiti.cfg.mock.xml",
		"classpath:META-INF\spring\dnepr\mvk\1\activiti.cfg.scan.xml",
		"classpath:activiti-custom-context-test.xml" })
public class Mvk_1_Test extends PluggableActivitiTestCase {

	static final String PROCESS_KEY = "CivilCopyDocRequest";
	static final String PROCESS_RESOURCE = "bpmn/autodeploy/dnepr_mvk-1.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule(
			"activiti-custom-context-test.xml");

	@Before
	public void injectDependencies() throws Exception {

		new TestContextManager(getClass()).prepareTestInstance(this);
		processEngine = TestHelper.getProcessEngine(activitiRule
				.getConfigurationResource());
		activitiRule.setProcessEngine(processEngine);
		runtimeService = activitiRule.getRuntimeService();
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

	@Test
	@Deployment(resources = { PROCESS_RESOURCE })
	public void isDeployed() {
		assertThat(
				processEngine.getRepositoryService()
						.createProcessDefinitionQuery()
						.processDefinitionKey(PROCESS_KEY).singleResult())
				.isDeployed();
	}

	@Test
	@Deployment(resources = { PROCESS_RESOURCE })
	public void startProcessInstance() throws InterruptedException {
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

		ProcessInstance pi = runtimeService.startProcessInstanceByKey(
				PROCESS_KEY, procVars);

		assertNotNull(pi);		

		Task task = activitiRule.getTaskService().createTaskQuery()
				.singleResult();
		assertNotNull(task);	
		assertEquals("usertask4", task.getTaskDefinitionKey());
		assertEquals("Прийняття рішення: доцільність запиту", task.getName());
		Map<String, Object> taskVars = new HashMap<String, Object>();
		taskVars.put("decide", "reject");		
		processEngine.getTaskService().complete(task.getId(),taskVars);	
		
		assertEquals(0, runtimeService.createProcessInstanceQuery().count()); 
		
	}

}
