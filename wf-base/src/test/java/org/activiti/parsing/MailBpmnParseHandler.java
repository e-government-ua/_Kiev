package org.activiti.parsing;

import org.activiti.bpmn.model.ImplementationType;
import org.activiti.bpmn.model.ServiceTask;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.ServiceTaskParseHandler;

/**
 * Custom, BpmnParseHendler for skipping real e-mail tasks
 *
 * @author Tereshchenko
 */
public class MailBpmnParseHandler extends ServiceTaskParseHandler {

    protected void executeParse(BpmnParse bpmnParse, ServiceTask serviceTask) {

        if (ServiceTask.MAIL_TASK.equals(serviceTask.getType())) {

            // изменить тип
            serviceTask.setType(null);
            serviceTask
                    .setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_CLASS);
            serviceTask
                    .setImplementation("org.activiti.parsing.MockMailTask");

        }

    }

}
