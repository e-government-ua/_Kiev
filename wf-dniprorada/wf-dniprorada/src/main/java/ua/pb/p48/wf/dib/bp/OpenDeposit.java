package ua.pb.p48.wf.dib.bp;

import org.activiti.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;



/**
 * Открытие депозита
 *
 */
@Component("OpenDeposit")
public class OpenDeposit implements org.activiti.engine.delegate.JavaDelegate{
	private static final transient Logger LOG = LoggerFactory.getLogger(OpenDeposit.class);
	
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {						
		
		LOG.info("TODO: OPEN DEPOSIT"); //TODO
		LOG.info("type:"+ execution.getVariable("type")); //TODO
		LOG.info("amount:"+ execution.getVariable("amount")); //TODO
		LOG.info("currency:"+ execution.getVariable("currency")); //TODO
	}
	
  



}
