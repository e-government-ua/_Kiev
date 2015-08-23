package org.wf.dp.dniprorada.base.util.run;
import java.util.Date;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Escalation implements Job {

    private final static Logger oLog = LoggerFactory.getLogger(Escalation.class);
    
    public Escalation() {
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        oLog.info("[execute]:In QuartzJob - executing JOB at " + new Date() + " by context.getTrigger().getName()="+context.getTrigger().getName());
        //TODO: Тут нужно написать вызов запуска эскалации!
    }
}