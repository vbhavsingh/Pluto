/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.analyzer;

import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.analyzer.service.comm.RegistrationService;
import com.google.gson.Gson;
import com.log.analyzer.commons.Constants;
import com.log.analyzer.commons.model.AgentConfigurationModel;

/**
 *
 * @author Vaibhav Pratap Singh
 * 
 *         this routine sends heartbeat of to master server, so that master
 *         knows whether the service is up or not. while sending status this
 *         model gets back configuration items along with current agent version,
 *         If current agent version is not equal to one supported by master,
 *         service downloads new version from master and restarts itself to
 *         bring in effect new changes.
 */
public class HeartBeatMonitor implements Job {

	private static final Logger Log = LogManager.getLogger(HeartBeatMonitor.class);

	public void execute(JobExecutionContext context) throws JobExecutionException {
		Log.debug("Sending heartbeat signal to master");
		RegistrationService svc = new RegistrationService();
		String result = "";
		try {
			result = svc.sendHeartbeat();
		} catch (Exception e) {
			result = Constants.FAILURE;
			Log.error("heartbeat signal failed ", e);
		}
		if (Constants.FAILURE.equals(result)) {
			Log.error("hearbeat registration failed at master, trying to re-register the agent");
			svc.registerAgent();
		} else {
			Log.debug("heartbeat successfully registered at master");
			Gson gson = new Gson();
			try {
				Log.debug("applying configurations recieved from master");
				AgentConfigurationModel conf = gson.fromJson(result, AgentConfigurationModel.class);
				Log.debug("current agent version is "+Constants.CURRENT_AGENT_VERSION +", version at master is "+conf.getCurrentAgentVersion());
				if(!Constants.CURRENT_AGENT_VERSION.equals(conf.getCurrentAgentVersion())){
					Log.info("new version "+conf.getCurrentAgentVersion()+" of client is available. The current version is "+Constants.CURRENT_AGENT_VERSION);
					Log.info("Trying to downlaod new version and restart agent");
					/*start downloading and restarting the current agent*/
					posixDownloadAndRestart();
				}
				LocalUtil.updateAgentConfiguration(conf);
			} catch (Exception e) {
				Log.error(e);
			}
		}
	}
	
	/**
	 * Download new agent jar from master and restart the client on linux/ unix platform
	 */
	private void posixDownloadAndRestart(){
		try {
			String url=LocalUtil.getMasterURL()+Constants.DOWNLOAD_URL_SUFFIX;
			
			Log.info("downloading new client file from master");
			
			ReadableByteChannel in=Channels.newChannel(new URL(url).openStream());
			FileChannel file = new FileOutputStream(LocalUtil.getCharonTempLocation()).getChannel();
			file.transferFrom(in, 0, Long.MAX_VALUE);
			
			Log.info("new client file downloaded successfully from master");
			Log.debug("initiating client restart with "+LocalUtil.getPosixtRestartCommand());
			
			Runtime.getRuntime().exec(LocalUtil.getPosixtRestartCommand());
			
			Log.info("stopping old client");
			System.exit(2);
		} catch (Exception e) {
			Log.error(e);
			Log.fatal("Unable to download and restart new client. Please do it manually. Installing new agent soon is highly recommeneded");
		} 
	}
}
