package com.analyzer.commons;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class KillThread implements Runnable {

	private static final Logger Log = LogManager.getLogger(KillThread.class);
	
	@Override
	public void run() {
		try {
			Log.warn("stopping agent in next 5 seconds, waiting for thread to complete");
			Thread.sleep(5000);
			System.exit(2);
		} catch (InterruptedException e) {
			Log.error(e);
		}

	}

}
