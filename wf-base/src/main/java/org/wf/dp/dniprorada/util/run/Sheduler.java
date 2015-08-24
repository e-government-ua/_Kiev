/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wf.dp.dniprorada.base.util.run;

import org.wf.dp.dniprorada.base.util.run.Escalation;
import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Belyavtsev Vladimir Vladimirovich (BW)
 */
public class Sheduler {

    private final static Logger oLog = LoggerFactory.getLogger(Sheduler.class);
    
    public void init() throws SchedulerException {
        // Запускаем Schedule Factory
        oLog.info("[init]:Schedule Factory...");
        SchedulerFactory oSchedulerFactory = new StdSchedulerFactory();
        // Извлекаем планировщик из schedule factory
        oLog.info("[init]:getScheduler...");
        Scheduler oScheduler = oSchedulerFactory.getScheduler();
        
        // текущее время
        long nNowMS = System.currentTimeMillis(); 
        oLog.info("[init]:nNowMS="+nNowMS);
        
        // Запускаем JobDetail с именем задания,
        // группой задания и классом выполняемого задания
        JobDetail oJobDetail_Escalation_Standart = new JobDetail("oJobDetail_Escalation_Standart", "oJobDetail_Escalation_Group", Escalation.class);
        // Запускаем CronTrigger с его именем и именем группы
        CronTrigger oCronTrigger_EveryNight_Deep = new CronTrigger("oCronTrigger_EveryNight_Deep", "oCronTrigger_EveryNight_Group");
        try {
            // Устанавливаем CronExpression
            oLog.info("[init]:oCronExpression__EveryNight_Deep...");
            //CronExpression oCronExpression__EveryNight_Deep = new CronExpression("0/5 * * * * ?");
            //http://www.ibm.com/developerworks/ru/library/j-quartz/
            /*
            <p>Выражение cron состоит из следующих семи полей:</p>
            <ul class="ibm-bullet-list">
                <li>Секунды</li>
                <li>Минуты</li>
                <li>Часы</li>
                <li>День месяца</li>
                <li>Месяц</li>
                <li>День недели</li>
                <li>Год (необязательное поле)</li>
            </ul>
            <h3 id="N10100">Специальные символы</h3>
            <p>Триггеры cron используют серию специальных символов, например:</p>
            <ul class="ibm-bullet-list">
                <li>Символ косая черта (/) обозначает приращение значения. Например, "5/15" в поле "секунды" означает каждые 15 секунд, начиная с пятой секунды.</li>
                <li>Знак вопроса (?) и букву L (L) разрешается использовать только в полях "день месяца" и "день недели". Знак вопроса означает, что в поле не должно быть указанной величины. Таким образом, если вы устанавливаете день недели, вы можете вставить "?" в поле "день недели" для обозначения того, что значение "день недели" несущественно. Буква L - это сокращение от <em>last (последний)</em>. Если она помещается в поле "день месяца", задание будет запланировано на последний день месяца. В поле "день недели" "L" равнозначно "7", если помещается само по себе, или означает последний экземпляр "дня недели" в этом месяце. Так, "0L" запланирует выполнение задания на последнее воскресенье данного месяца.</li>
                <li> Буква W (W) в поле "день месяца" планирует выполнение задания на ближайший к заданному значению рабочий день. Введя "1W" в поле "день месяца" вы планируете выполнение задания на рабочий день, ближайший к первому числу месяца.</li>
                <li>Знак фунта (#) устанавливает конкретный рабочий день данного месяца. Ввод "MON#2" в поле "день недели" планирует задание на второй понедельник месяца.</li>
                <li>Знак астериска (*) является подстановочным знаком и обозначает, что любое возможное значение может быть принято для данного отдельного поля. </li>
            </ul>
            */
            CronExpression oCronExpression__EveryNight_Deep = new CronExpression("0 0 2 1/1 * ?");//В 2 часа ночи каждый день
            // Присваиваем CronExpression CronTrigger'у
            oLog.info("[init]:oCronExpression__EveryNight_Deep.setCronExpression...");
            oCronTrigger_EveryNight_Deep.setCronExpression(oCronExpression__EveryNight_Deep);
        } catch (Exception oException) {
            oLog.error("[init]:",oException);
            //oException.printStackTrace();
        }
        // Планируем задание с помощью JobDetail и Trigger
        oLog.info("[init]:scheduleJob...");
        oScheduler.scheduleJob(oJobDetail_Escalation_Standart, oCronTrigger_EveryNight_Deep);
        
        // Запускаем планировщик
        oLog.info("[init]:start...");
        oScheduler.start();
        oLog.info("[init]:Ok!!");
        
    }    
        
}


