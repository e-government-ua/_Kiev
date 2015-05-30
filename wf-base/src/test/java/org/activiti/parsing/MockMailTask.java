package org.activiti.parsing;

import org.activiti.engine.delegate.*;
import org.springframework.stereotype.*;

/**
 * Mock e-mail service task.
 * Do nothing.
 * 
 * @author Tereshchenko
 *
 */
@Component("mockMailTask")
public class MockMailTask implements JavaDelegate {

	@SuppressWarnings("unused")
	private Expression to;
	@SuppressWarnings("unused")
	private Expression subject;
	@SuppressWarnings("unused")
	private Expression charset;
	@SuppressWarnings("unused")
	private Expression from;
	@SuppressWarnings("unused")
	private Expression text;

	@Override
	public void execute(DelegateExecution execution) throws Exception {

	}

}
