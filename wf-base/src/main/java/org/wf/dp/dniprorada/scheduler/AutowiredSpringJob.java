package org.wf.dp.dniprorada.scheduler;

import org.quartz.Job;

/**
 * Extend from this class if you need to perform autowiring of all property beans inside your job pojo.
 * <p/>
 * User: goodg_000
 * Date: 11.09.2015
 * Time: 22:34
 */
public abstract class AutowiredSpringJob implements Job {

    public AutowiredSpringJob() {

        // dirty hack to perform autowiring
        JobsInitializer.getApplicationContext().getAutowireCapableBeanFactory().autowireBean(this);
    }
}
