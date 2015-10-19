package org.wf.dp.dniprorada.scheduler;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.wf.dp.dniprorada.util.run.Escalation;

/**
 * User: goodg_000
 * Date: 27.08.2015
 * Time: 1:05
 */
public class JobsInitializer implements InitializingBean, ApplicationContextAware {

    private final static Logger oLog = LoggerFactory.getLogger(JobsInitializer.class);

    private static ApplicationContext applicationContext;
    private Scheduler scheduler;

    /**
     * @return used by {@link AutowiredSpringJob} to autowire property beans into jobs.
     */
    static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        addEscalationJob(scheduler);
    }

    private void addEscalationJob(Scheduler scheduler) throws SchedulerException {
        JobDetail oJobDetail_Escalation_Standart = new JobDetail("oJobDetail_Escalation_Standart",
                "oJobDetail_Escalation_Group", Escalation.class);

        CronTrigger oCronTrigger_EveryNight_Deep = new CronTrigger("oCronTrigger_EveryNight_Deep",
                "oCronTrigger_EveryNight_Group");
        try {
            oLog.info("[init]:oCronExpression__EveryNight_Deep...");
            CronExpression oCronExpression__EveryNight_Deep = new CronExpression("0 0 2 1/1 * ?");
            oLog.info("[init]:oCronExpression__EveryNight_Deep.setCronExpression...");
            oCronTrigger_EveryNight_Deep.setCronExpression(oCronExpression__EveryNight_Deep);
        } catch (Exception oException) {
            oLog.error("[init]:", oException);
        }
        oLog.info("[init]:scheduleJob...");
        scheduler.scheduleJob(oJobDetail_Escalation_Standart, oCronTrigger_EveryNight_Deep);
    }

}
