package org.wf.dp.dniprorada.engine.task;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.form.StartFormData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.wf.dp.dniprorada.base.model.AbstractModelTask;

@Component("releaseTicketsOfQueue")
public class ReleaseTicketsOfQueue extends AbstractModelTask implements JavaDelegate{

	private final static Logger LOG = LoggerFactory.getLogger(ReleaseTicketsOfQueue.class);
	
	@Override
	public void execute(DelegateExecution oExecution) throws Exception {
		StartFormData oStartformData = oExecution.getEngineServices()
				.getFormService()
				.getStartFormData(oExecution.getProcessDefinitionId());
		
		LOG.info("ReleaseTicketsOfQueue:execute start");
		scanExecutionOnQueueTickets(oExecution, oStartformData);
		
		LOG.info("ReleaseTicketsOfQueue:execute end");
	}

}
