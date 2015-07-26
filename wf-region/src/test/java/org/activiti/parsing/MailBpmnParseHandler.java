package org.activiti.parsing;

import org.activiti.bpmn.model.*;
import org.activiti.engine.impl.bpmn.parser.*;
import org.activiti.engine.impl.bpmn.parser.handler.*;

/**
 * Custom, BpmnParseHendler for skipping real e-mail tasks
 * @author Tereshchenko
 *
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
