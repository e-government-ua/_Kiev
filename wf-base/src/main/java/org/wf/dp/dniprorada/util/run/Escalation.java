package org.wf.dp.dniprorada.util.run;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.logging.Level;
import org.activiti.rest.controller.ActivitiRestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.wf.dp.dniprorada.base.service.escalation.EscalationService;

public class Escalation implements Job {

    @Autowired
    private EscalationService escalationService;
    
    private final static Logger oLog = LoggerFactory.getLogger(Escalation.class);
    
    public Escalation() {
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        oLog.info("[execute]:In QuartzJob - executing JOB at " + new Date() + " by context.getTrigger().getName()="+context.getTrigger().getName());
        try {
            //TODO: ��� ����� �������� ����� ������� ���������!
            escalationService.runEscalationAll();
        } catch (ActivitiRestException ex) {
            //java.util.logging.Logger.getLogger(Escalation.class.getName()).log(Level.SEVERE, null, ex);
            oLog.info("[execute]:", ex);
        }
    }
}