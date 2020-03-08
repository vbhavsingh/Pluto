/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.analyzer;

import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;
/*import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;*/

import com.analyzer.commons.LocalConstants;
import com.analyzer.posix.Search;
import com.analyzer.service.comm.RegistrationService;
import com.log.analyzer.commons.Constants;
import com.log.analyzer.commons.Util;

/**
 *
 * @author Vaibhav Pratap Singh
 */
public class OldMain {

    private static final Logger Log = Logger.getLogger(OldMain.class);

 /*   public static void main(String[] args) throws Exception {
    	// set the log level if present in arguments
    	initializeLoggingSettings();
    	
        if (LocalUtil.getMasterURL() == null) {
            Log.fatal("master server is not defined, define master server as '-D" + LocalConstants.MASTER_SERVER_URL + "' in startup");
            Log.fatal("client is exiting as master server is not defined");
            System.exit(1);
        }
        if(args!=null && args.length> 0 && LocalConstants.RESTART.equals(args[0])){
        	Log.info("automatically restarting charon after downloading new version");
        }
        try {
            while (LocalConstants.CURRENT_PORT < LocalConstants.PORT_UPPER_BOUND && !available(LocalConstants.CURRENT_PORT)) {
                LocalConstants.CURRENT_PORT++;
            }

            QueuedThreadPool threadPool = new QueuedThreadPool(LocalConstants.THREAD_POOL_SIZE);
            Log.info("Created thread pool of " +LocalConstants.THREAD_POOL_SIZE +" threads");

            Server server = new Server(threadPool);
            server.manage(threadPool);
            server.setDumpAfterStart(false);
            server.setDumpBeforeStop(false);
            Log.info("created server object");
           // server.setHandler(getHandlers());
            Log.info("associated defined handlers with the server");

            HttpConnectionFactory http = new HttpConnectionFactory(getHttpConfig());
            SslContextFactory sf=getSslFactory();
            SslConnectionFactory scf=new SslConnectionFactory(sf,"http/1.1");
            Log.info("defined context and connection modules");
            
            ServerConnector httpConnector = new ServerConnector(server,http);
            httpConnector.setPort(LocalConstants.CURRENT_PORT);
            httpConnector.setIdleTimeout(10000);
            server.addConnector(httpConnector);

            Log.info("registering " + Search.class.getCanonicalName() + " as service provider");

            Log.info("client is running successfully at port " + LocalConstants.CURRENT_PORT);

            Registering agent with master
            RegistrationService svc = new RegistrationService();
            svc.registerAgent();
            Getting indexing command from master
            LocalConstants.FIND_COMMAND = svc.getIndexingCommand();

            Start the scheduled jobs 
            JobService jobs = new JobService();
            Log.info("jobs scheduled");
            server.start();
            server.join();
        } catch (Exception e) {
            Log.fatal("not able to start the client, please fix the following errors before attempting restart");
            Log.fatal(e);
            e.printStackTrace();
            System.exit(1);
        }
    }*/
  /*  
    private static HandlerCollection getHandlers(){
        ContextHandler searchContext=new ContextHandler();
        searchContext.setContextPath(Constants.SEARCH_URI_SUFFIX);
        searchContext.setHandler(new SearchHandler());
        
        ContextHandler validateUserContext=new ContextHandler();
        validateUserContext.setContextPath(Constants.VALIDATE_USER_URI_SUFFIX);
        validateUserContext.setHandler(new ValidateUserHandler());
        
        ContextHandler updateConfigContext=new ContextHandler();
        updateConfigContext.setContextPath(Constants.UPDATE_CONFIG_URI_SUFFIX);
        updateConfigContext.setHandler(new UpdateAgentConfig());
        
        ContextHandler terminateAgentContext=new ContextHandler();
        updateConfigContext.setContextPath(Constants.TERMINATE_AGENT_URI_SUFFIX);
        updateConfigContext.setHandler(new Terminator());
        
        ContextHandlerCollection contexts=new ContextHandlerCollection();
        contexts.setHandlers(new Handler[]{searchContext,validateUserContext,updateConfigContext,terminateAgentContext});
        
        HandlerCollection handlers=new HandlerCollection();
        
        handlers.setHandlers(new Handler[]{contexts,new DefaultHandler()});
        
        return handlers;
    }*/

    /**
     *
     * @param port
     * @return
     */
    private static boolean available(int port) {
        Log.info("checking availability of port " + port);
        Socket s = null;
        try {
            s = new Socket("localhost", port);
            Log.info("Port " + port + " is not available");
            return false;
        } catch (IOException e) {
            Log.info("Port " + port + " is available");
            return true;
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    /**
     * 
     * @return
     */
/*    private static HttpConfiguration getHttpConfig() {
        HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.setDelayDispatchUntilContent(false);
        httpConfig.setOutputBufferSize(1024 * 100);
        httpConfig.setOutputAggregationSize(1024 * 10);
        httpConfig.setRequestHeaderSize(1024 * 8);
        httpConfig.setResponseHeaderSize(1024 * 8);
        httpConfig.setSendDateHeader(false);
        httpConfig.setSendXPoweredBy(true);
        httpConfig.setSendServerVersion(true);
        
        httpConfig.addCustomizer(new SecureRequestCustomizer());
        
        Log.trace("creating HTTP config : "+httpConfig.toString()); 
        
        return httpConfig;
    }*/
    /**
     * 
     * @return
     */
   /* private static SslContextFactory getSslFactory() {
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath(OldMain.class.getResource("../../search.keystore.jks").toExternalForm());
        sslContextFactory.setKeyStorePassword("logsearchapplication");
        sslContextFactory.setKeyManagerPassword("logsearchapplication");
        return sslContextFactory;
    }*/
    /**
     * Initializes logging properties if user wants to override
     */
    private static void initializeLoggingSettings(){
    	boolean logLevel = Util.isValidLogLevel(System.getProperty(Constants.LOG_LEVEL));
    	boolean logOnConsole = Util.doConsoleLogging(System.getProperty(Constants.LOG_ON_CONSOLE));
    	
    	/*if( logLevel || logOnConsole){
    		LogManager.getRootLogger().getLoggerRepository().resetConfiguration();
    		
    		PatternLayout layout = new PatternLayout("%-5p %c{1} - %m%n");
    		ConsoleAppender consoleAppender = new ConsoleAppender(layout);
    
    		String level = System.getProperty(Constants.LOG_LEVEL);
    		    		
    		if(logLevel && logOnConsole){
    			LogManager.getRootLogger().addAppender(consoleAppender);
    			LogManager.getRootLogger().setLevel(Commons.getLogLevel(level));
    		}else if(logLevel){
    			LogManager.getRootLogger().setLevel(Commons.getLogLevel(level));
    		}else{
    			LogManager.getRootLogger().addAppender(consoleAppender);
    		}
    		Log.info("log level set to :"+ level);
    	}*/
    }
}
