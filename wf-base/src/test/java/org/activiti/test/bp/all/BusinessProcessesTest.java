package org.activiti.test.bp.all;

import com.plexiti.activiti.test.fluent.*;
import com.plexiti.activiti.test.fluent.mocking.*;
import org.activiti.engine.impl.test.*;
import org.activiti.engine.repository.*;
import org.activiti.engine.runtime.*;
import org.activiti.engine.test.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;

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
	public void startProcessInstance() throws InterruptedException {

		for (ProcessDefinition processDefinition : activitiRule
				.getProcessEngine().getRepositoryService()
				.createProcessDefinitionQuery().list()) {
			ProcessInstance pi = activitiRule.getRuntimeService()
					.startProcessInstanceByKey(processDefinition.getKey());
			assertNotNull(String.format(
					"process did not start for key:{%s}, resourceName:{%s}",
					processDefinition.getKey(),
					processDefinition.getResourceName()), pi);

			activitiRule.getRuntimeService().deleteProcessInstance(
					pi.getProcessInstanceId(),
					"not needed, created only for test");
		}

	}

}
