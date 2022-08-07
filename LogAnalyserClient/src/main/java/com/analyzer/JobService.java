/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.analyzer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.log.analyzer.commons.Constants;

/**
 *
 * @author vs1953
 */
public class JobService {

    private final static Logger Log = LogManager.getLogger(JobService.class);
    private final static SchedulerFactory schFactory = new StdSchedulerFactory();

    public JobService() {
        try {
            heartBeatMonitor();
        } catch (SchedulerException ex) {
            Log.error("error while scheduling heartbeat service", ex);
        }
        try {
            logFileIndexingSvc();
        } catch (SchedulerException ex) {
            Log.error("error while scheduling log indexing service", ex);
        }
    }

    private void heartBeatMonitor() throws SchedulerException {
        JobDetail job = JobBuilder.newJob(HeartBeatMonitor.class).withIdentity("heartbeat").build();
        Log.debug("creating job heartBeatMonitor");
        Trigger trigger = TriggerBuilder.newTrigger().withSchedule(
                SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(Constants.HEARTBEAT_INTRVAL_IN_MIN).repeatForever()
        ).build();
        Scheduler sch = schFactory.getScheduler();
        sch.start();
        sch.scheduleJob(job, trigger);
        Log.debug("scheduled job heartBeatMonitor");
    }

    private void logFileIndexingSvc() throws SchedulerException {
        JobDetail job = JobBuilder.newJob(LogFileIndexingSvc.class).withIdentity("logindexer").build();
        Log.debug("creating job logindexer");
        Trigger trigger = TriggerBuilder.newTrigger().withSchedule(
                SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(Constants.LOG_INDEXING_INTRVAL_IN_MIN).repeatForever()
        ).build();
        Scheduler sch = schFactory.getScheduler();
        sch.start();
        sch.scheduleJob(job, trigger);
        Log.debug("scheduled job heartBeatMonitor");
    }

}
