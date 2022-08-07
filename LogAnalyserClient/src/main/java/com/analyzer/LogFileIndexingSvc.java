/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.analyzer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.analyzer.posix.LogFileNameFinder;

/**
 *
 * @author vs1953
 */
public class LogFileIndexingSvc implements Job {

    private static final Logger Log = LogManager.getLogger(LogFileIndexingSvc.class);

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        try {
            LogFileNameFinder.findAllFile();
        } catch (Exception ex) {
            Log.error("indexing log files encountered error", ex);
        } 
    }

}
