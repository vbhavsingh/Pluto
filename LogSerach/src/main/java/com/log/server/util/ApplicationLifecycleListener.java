/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.server.util;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.log.analyzer.commons.Commons;
import com.log.analyzer.commons.Constants;
import com.log.analyzer.commons.Util;
import com.log.server.LocalConstants;
import com.log.server.biz.CommonServices;


/**
 *
 * @author Vaibhav Singh
 */
public class ApplicationLifecycleListener implements ServletContextListener, HttpSessionListener {

    private final static Logger Log = LoggerFactory.getLogger(ApplicationLifecycleListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
    	// set the log level if present in arguments
    	if(Util.isValidLogLevel(System.getProperty(Constants.LOG_LEVEL))){
    		String level = System.getProperty(Constants.LOG_LEVEL);
    		org.apache.log4j.Logger.getRootLogger().setLevel(Commons.getLogLevel(level));
    		Log.info("log level set to :"+ level);
    	}
    	
    	Log.info("Current Pluto version : {}, Current charon version: {}",Constants.VERSION,Constants.CURRENT_AGENT_VERSION);
        Log.info("initializing database");
        Path path=Paths.get(System.getProperty("user.home"),".pluto/db","configuration.lck");
        File file=new File(path.toUri());
        Log.info("destroying any lock on database, due to bad shutdown at: "+file.getAbsolutePath());
        if(file.exists()){
        	try{
        		file.delete();
        		Log.info("deleted previously craeted lock file on database");
        	}catch(Exception e){
        		Log.error("exeception while deleting bad lock file, application may fail to start, if application fails manually delete lock file at: "+file.getAbsolutePath());
        	}
        }
        try {
			CommonServices.initializedDatabse();
		} catch (Exception e) {
			Log.error("db cannot be initialized, application exiting",e);
			System.exit(100);
		}
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        Log.info("destroying executor pool");
        List<Runnable> tasks = LocalConstants.executor.shutdownNow();
        if (tasks == null) {
            Log.info("executor pool destroyed, no task was in progress");
        }
        Log.info("executor pool destroyed, aborting {} task/s that were under progress", tasks.size());
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        //do nothing
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        //do nothing
    }

}
