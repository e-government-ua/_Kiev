package org.wf.dp.dniprorada.util.run;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class Escalation implements Job {

    private final static Logger oLog = LoggerFactory.getLogger(Escalation.class);
    
    public Escalation() {
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        oLog.info("[execute]:In QuartzJob - executing JOB at " + new Date() + " by context.getTrigger().getName()="+context.getTrigger().getName());
        //TODO: ��� ����� �������� ����� ������� ���������!
    }
}