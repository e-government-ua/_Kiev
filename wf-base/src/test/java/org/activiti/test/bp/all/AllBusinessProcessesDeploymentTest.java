package org.activiti.test.bp.all;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.plexiti.activiti.test.fluent.ActivitiFluentTestHelper;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.impl.test.TestHelper;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("default")
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/bp/all/activiti.cfg.mock.xml"
        , "classpath:META-INF/spring/bp/all/activiti.cfg.scan.xml"
        , "classpath:context_test-base.xml"
        //, "classpath:context.xml"
})
public class AllBusinessProcessesDeploymentTest extends
        PluggableActivitiTestCase {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("context_test-base.xml");

    @Before
    public void injectDependencies() throws Exception {

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
    public void testProcessDeployment() {
        Map<String, Map<String, String>> bpEmtyAssociation = new TreeMap<String, Map<String, String>>();

        List<ProcessDefinition> pd = processEngine.getRepositoryService()
                .createProcessDefinitionQuery().list();
        assertNotNull(pd);
        assertTrue(pd.size() > 0);
        for (ProcessDefinition processDefinition : pd) {
            assertNotNull(processDefinition);
            // получаем модель
            BpmnModel model = processEngine.getRepositoryService()
                    .getBpmnModel(processDefinition.getId());

            Map<String, String> emptyElements = getAllEmptyElements(
                    processDefinition, model);
            if (emptyElements != null && !emptyElements.isEmpty()) {
                bpEmtyAssociation.put(processDefinition.getId().toUpperCase(),
                        emptyElements);
            }

        }
        assertTrue(
                "BP: "
                        + " Delete empty element: "
                        + Maps.filterValues(bpEmtyAssociation,
                        Predicates.notNull()),
                bpEmtyAssociation.isEmpty());

    }

    /**
     * Получаем список всех пустых элементов
     *
     * @param processDefinition
     * @param model
     * @return
     */
    private Map<String, String> getAllEmptyElements(
            ProcessDefinition processDefinition, BpmnModel model) {

        // лист Id serviceTask
        List<String> ids = new ArrayList<String>();

        // Map содержащая emtyAssociation, вызывающих nullPointer при Deployed
        // схем
        Map<String, String> emptyAssociation = new HashMap<String, String>();
        List<Process> processes = model.getProcesses();
        // сортируем
        Collections.sort(processes, new Comparator<Process>() {

            @Override
            public int compare(Process o1, Process o2) {
                return o1.getId().compareToIgnoreCase(o2.getId());
            }

        });
        // получаем процесс
        for (Process process : processes) {

            // получаем ServiceTask id
            ids.addAll(getFlowElements(process, ServiceTask.class));

            // получаем ExclusiveGateway id
            ids.addAll(getFlowElements(process, ExclusiveGateway.class));

            // получаем StartEvent id
            ids.addAll(getFlowElements(process, StartEvent.class));

            // получаем CallActivity id
            ids.addAll(getFlowElements(process, CallActivity.class));

            // получить Artifact id
            ids.addAll(getAllArtifactsId(process));

            // выбрать Acssociation у которых нет ссылок на ID
            List<Artifact> bpmnAssociations = getAllAssociation(process
                    .getArtifacts());

            for (Artifact artifact : bpmnAssociations) {
                if (artifact instanceof Association) {
                    if (!ids.contains(((Association) artifact).getTargetRef())) {
                        emptyAssociation.put(artifact.getId(),
                                ((Association) artifact).getTargetRef());
                    }

                }

            }

        }
        return emptyAssociation;
    }

    /**
     * Получаем All Associations
     *
     * @param artifacts
     * @return
     */
    private List<Artifact> getAllAssociation(Collection<Artifact> artifacts) {
        return Lists.newArrayList(Collections2.filter(artifacts,
                new Predicate<Artifact>() {
                    @Override
                    public boolean apply(Artifact artifact) {
                        if (artifact instanceof Association) {
                            return true;
                        }
                        return false;
                    }
                }));
    }

    /**
     * Получаем FlowElement
     *
     * @param process
     * @return
     */
    private <T extends FlowElement> List<String> getFlowElements(
            Process process, Class<T> classToFind) {
        return Lists.newArrayList(Collections2.transform(
                process.findFlowElementsOfType(classToFind),
                new Function<FlowElement, String>() {
                    public String apply(FlowElement flowElement) {

                        return flowElement.getId();
                    }
                }));
    }

    /**
     * Получаем Artifacts id
     *
     * @param tasks
     * @param process
     * @return
     */
    private List<String> getAllArtifactsId(Process process) {
        return Lists.newArrayList(Collections2.transform(
                process.getArtifacts(), new Function<Artifact, String>() {
                    public String apply(Artifact artifact) {
                        return artifact.getId();

                    }
                }));
    }

}