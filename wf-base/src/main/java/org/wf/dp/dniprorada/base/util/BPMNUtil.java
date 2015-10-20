package org.wf.dp.dniprorada.base.util;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.IdentityLinkType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class BPMNUtil {

    private static final Logger LOG = LoggerFactory.getLogger(BPMNUtil.class);

    public static List<User> getUsersFromGroup(IdentityService identityService, String groupName) {
        LOG.info(String.format("Getting list of users who belong to the group %s", groupName));
        List<User> users = identityService.createUserQuery().memberOfGroup(groupName).list();

        return users != null ? users : new LinkedList<User>();
    }

    public static List<User> getUsersInfoBelongToProcess(RepositoryService repositoryService,
            IdentityService identityService, String processDefinitionId, String taskId) {
        List<User> res = new LinkedList<User>();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId).singleResult();

        LOG.info("Retrieved process definition id: " + processDefinition);

        Set<String> groupsToCheck = new HashSet<String>();

        loadCandidateGroupsFromTasks(repositoryService, processDefinition, groupsToCheck, taskId);

        LOG.info("Retrieved user groups from task: " + groupsToCheck);

        for (String groupName : groupsToCheck) {
            res.addAll(getUsersFromGroup(identityService, groupName));
        }

        return res;
    }

    public static void loadCandidateGroupsFromTasks(RepositoryService repositoryService, ProcessDefinition processDef,
            Set<String> candidateCroupsToCheck, String taskId) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDef.getId());

        for (FlowElement flowElement : bpmnModel.getMainProcess().getFlowElements()) {
            if (flowElement instanceof UserTask) {
                UserTask userTask = (UserTask) flowElement;
                if (userTask.getId().equals(taskId)) {
                    LOG.info("Found user task which matches task ID: " + taskId);
                    List<String> candidateGroups = userTask.getCandidateGroups();
                    if (candidateGroups != null && !candidateGroups.isEmpty()) {
                        candidateCroupsToCheck.addAll(candidateGroups);
                        LOG.info(String.format("Added candidate groups %s from user task %s", candidateGroups,
                                userTask.getId()));
                    }
                } else {
                    LOG.info(
                            "Checking task " + userTask.getId() + ":" + userTask.getName() + " with task ID:" + taskId);
                }
            }
        }
    }

    public static void loadCandidateStarterGroup(RepositoryService repositoryService, ProcessDefinition processDef,
            Set<String> candidateCroupsToCheck) {
        List<IdentityLink> identityLinks = repositoryService.getIdentityLinksForProcessDefinition(processDef.getId());
        LOG.info(
                String.format("Found %d identity links for the process %s", identityLinks.size(), processDef.getKey()));
        for (IdentityLink identity : identityLinks) {
            if (IdentityLinkType.CANDIDATE.equals(identity.getType())) {
                String groupId = identity.getGroupId();
                candidateCroupsToCheck.add(groupId);
                LOG.info(String.format("Added candidate starter group %s ", groupId));
            }
        }
    }

}
