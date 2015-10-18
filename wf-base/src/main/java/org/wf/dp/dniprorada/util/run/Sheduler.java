/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wf.dp.dniprorada.util.run;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Belyavtsev Vladimir Vladimirovich (BW)
 */
public class Sheduler {

    private final static Logger oLog = LoggerFactory.getLogger(Sheduler.class);

    public void init() throws SchedulerException {
        // ��������� Schedule Factory
        oLog.info("[init]:Schedule Factory...");
        SchedulerFactory oSchedulerFactory = new StdSchedulerFactory();
        // ��������� ����������� �� schedule factory
        oLog.info("[init]:getScheduler...");
        Scheduler oScheduler = oSchedulerFactory.getScheduler();

        // ������� �����
        long nNowMS = System.currentTimeMillis();
        oLog.info("[init]:nNowMS=" + nNowMS);

        // ��������� JobDetail � ������ �������,
        // ������� ������� � ������� ������������ �������
        JobDetail oJobDetail_Escalation_Standart = new JobDetail("oJobDetail_Escalation_Standart",
                "oJobDetail_Escalation_Group", Escalation.class);
        // ��������� CronTrigger � ��� ������ � ������ ������
        CronTrigger oCronTrigger_EveryNight_Deep = new CronTrigger("oCronTrigger_EveryNight_Deep",
                "oCronTrigger_EveryNight_Group");
        try {
            // ������������� CronExpression
            oLog.info("[init]:oCronExpression__EveryNight_Deep...");
            //CronExpression oCronExpression__EveryNight_Deep = new CronExpression("0/5 * * * * ?");
            //http://www.ibm.com/developerworks/ru/library/j-quartz/
            /*
            <p>��������� cron ������� �� ��������� ���� �����:</p>
            <ul class="ibm-bullet-list">
                <li>�������</li>
                <li>������</li>
                <li>����</li>
                <li>���� ������</li>
                <li>�����</li>
                <li>���� ������</li>
                <li>��� (�������������� ����)</li>
            </ul>
            <h3 id="N10100">����������� �������</h3>
            <p>�������� cron ���������� ����� ����������� ��������, ��������:</p>
            <ul class="ibm-bullet-list">
                <li>������ ����� ����� (/) ���������� ���������� ��������. ��������, "5/15" � ���� "�������" �������� ������ 15 ������, ������� � ����� �������.</li>
                <li>���� ������� (?) � ����� L (L) ����������� ������������ ������ � ����� "���� ������" � "���� ������". ���� ������� ��������, ��� � ���� �� ������ ���� ��������� ��������. ����� �������, ���� �� �������������� ���� ������, �� ������ �������� "?" � ���� "���� ������" ��� ����������� ����, ��� �������� "���� ������" �������������. ����� L - ��� ���������� �� <em>last (���������)</em>. ���� ��� ���������� � ���� "���� ������", ������� ����� ������������� �� ��������� ���� ������. � ���� "���� ������" "L" ����������� "7", ���� ���������� ���� �� ����, ��� �������� ��������� ��������� "��� ������" � ���� ������. ���, "0L" ����������� ���������� ������� �� ��������� ����������� ������� ������.</li>
                <li> ����� W (W) � ���� "���� ������" ��������� ���������� ������� �� ��������� � ��������� �������� ������� ����. ����� "1W" � ���� "���� ������" �� ���������� ���������� ������� �� ������� ����, ��������� � ������� ����� ������.</li>
                <li>���� ����� (#) ������������� ���������� ������� ���� ������� ������. ���� "MON#2" � ���� "���� ������" ��������� ������� �� ������ ����������� ������.</li>
                <li>���� ��������� (*) �������� �������������� ������ � ����������, ��� ����� ��������� �������� ����� ���� ������� ��� ������� ���������� ����. </li>
            </ul>
            */
            CronExpression oCronExpression__EveryNight_Deep = new CronExpression(
                    "0 0 2 1/1 * ?");//� 2 ���� ���� ������ ����
            // ����������� CronExpression CronTrigger'�
            oLog.info("[init]:oCronExpression__EveryNight_Deep.setCronExpression...");
            oCronTrigger_EveryNight_Deep.setCronExpression(oCronExpression__EveryNight_Deep);
        } catch (Exception oException) {
            oLog.error("[init]:", oException);
            //oException.printStackTrace();
        }
        // ��������� ������� � ������� JobDetail � Trigger
        oLog.info("[init]:scheduleJob...");
        oScheduler.scheduleJob(oJobDetail_Escalation_Standart, oCronTrigger_EveryNight_Deep);

        // ��������� �����������
        oLog.info("[init]:start...");
        oScheduler.start();
        oLog.info("[init]:Ok!!");

    }

}


