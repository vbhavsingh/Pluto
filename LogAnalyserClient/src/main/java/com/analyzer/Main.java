package com.analyzer;

import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.analyzer.commons.LocalConstants;
import com.analyzer.service.comm.RegistrationService;
import com.log.analyzer.commons.model.AgentCommands;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.ConsoleAppender;

@SpringBootApplication
public class Main {

	private static final Logger Log = Logger.getLogger(Main.class);

	public static void main(String args[]) {
		setLogLevel();
		if (LocalUtil.getMasterURL() == null) {
			Log.fatal("master server is not defined, define master server as '-D" + LocalConstants.MASTER_SERVER_URL + "' in startup");
			Log.fatal("client is exiting as master server is not defined");
			System.exit(1);
		}

		if (args != null && args.length > 0 && LocalConstants.RESTART.equals(args[0])) {
			Log.info("automatically restarting charon after downloading new version");
		}
		setApplicatonPort();

		// Registering agent with master
		RegistrationService svc = new RegistrationService();
		svc.registerAgent();
		
		// Getting helper command from master
		AgentCommands commands = svc.getMasterCommands();
		LocalConstants.FIND_COMMAND = commands.getFileIndexingCommand();
		LocalConstants.ARCHIVE_LINE_REDAER_COMMAND = commands.getArchiveDateLineExtractorCommand();

		// Start the scheduled jobs
		JobService jobs = new JobService();
		Log.info("jobs scheduled");
		SpringApplication.run(Main.class, args);
		/* springs resets the programmatic logging overrides, therefore reset is required*/
		setLogLevel();
	}

	/**
	 * 
	 */
	private static void setApplicatonPort() {
		while (LocalConstants.CURRENT_PORT < LocalConstants.PORT_UPPER_BOUND && !LocalUtil.available(LocalConstants.CURRENT_PORT)) {
			LocalConstants.CURRENT_PORT++;
		}
		System.getProperties().put("server.port", LocalConstants.CURRENT_PORT);
	}

	private static void setLogLevel() {
		Level level = LocalUtil.getLogLevel();
		if (level != null) {
			LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
			context.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME).setLevel(level);
			//context.getLogger("com.analyzer").setLevel(level);
		}
		
		if(LocalUtil.consoleLoggingEnabled()) {
			LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
			
			ch.qos.logback.classic.Logger logger =context.getLogger("com.analyzer");
			logger.detachAppender("FILE_LOGGER");
			
			String logPattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%5p] %-5logger{2}.%M:%L : %m%n";
			
			PatternLayoutEncoder logEncoder = new PatternLayoutEncoder();
			logEncoder.setContext(context);
			logEncoder.setPattern(logPattern);
			logEncoder.start();
			
			ConsoleAppender consoleAppender = new ConsoleAppender();
			consoleAppender.setContext(context);
			consoleAppender.setName("console");
			consoleAppender.setEncoder(logEncoder);
			consoleAppender.start();

			logger.addAppender(consoleAppender);
			Log.info("CONSOLE LOGGING IS ENABLED.");
		}	
	}

}
