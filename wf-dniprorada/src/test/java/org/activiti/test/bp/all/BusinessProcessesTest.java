package org.activiti.test.bp.all;

import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.impl.test.TestHelper;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.plexiti.activiti.test.fluent.ActivitiFluentTestHelper;
import com.plexiti.activiti.test.fluent.mocking.Mockitos;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("default")
@ContextConfiguration(locations = {
		"classpath:META-INF/spring/bp/all/activiti.cfg.mock.xml",
		"classpath:META-INF/spring/bp/all/activiti.cfg.scan.xml",
		"classpath:activiti-custom-context-all-test.xml" })
public class BusinessProcessesTest extends PluggableActivitiTestCase {

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule(
			"activiti-custom-context-all-test.xml");

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

	@Test
//	@Deployment
	public void startProcessInstance() throws InterruptedException {
		;
		for (ProcessDefinition processDefinition : activitiRule
				.getRepositoryService().createProcessDefinitionQuery().list()) {
			System.out.println(processDefinition.getKey());
			ProcessInstance pi = activitiRule.getRuntimeService()
					.startProcessInstanceByKey(processDefinition.getKey());
			assertNotNull(pi);

			activitiRule.getRuntimeService().deleteProcessInstance(
					pi.getProcessInstanceId(),
					"not needed, created only for test");
		}

	}

}
