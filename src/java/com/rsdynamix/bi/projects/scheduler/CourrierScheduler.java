/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsdynamix.bi.projects.scheduler;

import com.rsdynamix.bi.projects.report.bean.UlticoreReportBean;
import com.rsdynamix.projects.report.entities.ReportScheduleEntity;
import com.rsdynamix.projects.report.entities.ReportTriggerEntity;
import com.rsdynamix.projects.commons.messages.beans.MessageSetupBean;
import java.io.Serializable;
import java.util.Date;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author root
 */
public class CourrierScheduler implements Serializable {

    private static Scheduler scheduler;

    /** Creates a new instance of CourrierScheduler */
    public CourrierScheduler() { }

    public void startSchedulerService(ReportScheduleEntity schedule, ReportTriggerEntity repTrigger,
            UlticoreReportBean ulticoreReportBean, MessageSetupBean messageSetupBean){
        try{
            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            scheduler = schedulerFactory.getScheduler();

            JobDetail job = new JobDetail("job_"+String.valueOf(schedule.getReportScheduleID()),
                    "group_"+String.valueOf(schedule.getReportScheduleID()), ReportScheduleJob.class);
            job.getJobDataMap().put("ulticoreReportBean", ulticoreReportBean);
            job.getJobDataMap().put("messageSetupBean", messageSetupBean);

            CronTrigger trigger = new CronTrigger(
                    "trigger_"+String.valueOf(schedule.getReportScheduleID()),
                    "group_"+String.valueOf(schedule.getReportScheduleID()),
                    "job_"+String.valueOf(schedule.getReportScheduleID()),
                    "group_"+String.valueOf(schedule.getReportScheduleID()),
                    repTrigger.getTriggerCronExpression());

            Date ft = scheduler.scheduleJob(job, trigger);
            scheduler.start();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void startSchedulerService(ReportScheduleEntity schedule, 
            ReportTriggerEntity repTrigger, MessageSetupBean messageSetupBean){
        try{
            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            scheduler = schedulerFactory.getScheduler();

            JobDetail job = new JobDetail("job_"+String.valueOf(schedule.getReportScheduleID()),
                    "group_"+String.valueOf(schedule.getReportScheduleID()), MessageScheduleJob.class);
            
            job.getJobDataMap().put("messageSetupBean", messageSetupBean);

            CronTrigger trigger = new CronTrigger(
                    "trigger_"+String.valueOf(schedule.getReportScheduleID()),
                    "group_"+String.valueOf(schedule.getReportScheduleID()),
                    "job_"+String.valueOf(schedule.getReportScheduleID()),
                    "group_"+String.valueOf(schedule.getReportScheduleID()),
                    repTrigger.getTriggerCronExpression());

            Date ft = scheduler.scheduleJob(job, trigger);
            scheduler.start();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void stopSchedulerService()throws Exception{
        scheduler.shutdown();
    }

}
