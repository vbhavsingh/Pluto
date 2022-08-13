package com.log.server;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

import com.log.analyzer.commons.Commons;
import com.log.analyzer.commons.Constants;
import com.log.analyzer.commons.Util;
import com.log.server.biz.AdminServices;
import com.log.server.util.Utilities;

@SpringBootConfiguration
@SpringBootApplication(scanBasePackages = {
		"com.log.server",
		"com.log.server.data.db",
		"com.security.common"
})
@ImportResource({
	"classpath*:spring-security.xml"
})
public class Pluto extends SpringBootServletInitializer {
    
	private final static Logger Log = LoggerFactory.getLogger(Pluto.class);
	
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Pluto.class);
    }
    
    
    public static void main(String[] args) throws Exception {
    	ConfigurableApplicationContext context = SpringApplication.run(Pluto.class, args);    
    	
    	 try {
         	((AdminServices)context.getBean("adminServices")).initializeDataBase();
         	
         }catch (Exception e) {
 			Log.error("db cannot be initialized, application exiting",e);
 			System.exit(100);
 		}
    }
    
    @PostConstruct
    public void afterStartup() {
    	// set the log level if present in arguments
    	if(Util.isValidLogLevel(System.getProperty(Constants.LOG_LEVEL))){
    		String level = System.getProperty(Constants.LOG_LEVEL);
    		LogManager.getRootLogger().atLevel(Commons.getLogLevel(level));
    		Log.info("log level set to :"+ level);
    	}
    	
    	Log.info("Current Pluto version : {}, Current charon version: {}",Constants.VERSION,Constants.CURRENT_AGENT_VERSION);
        Log.info("initializing database");
        Path path=Utilities.getEmbededDBLockFilePath();
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
    }
    
    @PreDestroy
    public void preShutDown() {
    	 Log.info("destroying executor pool");
         List<Runnable> tasks = LocalConstants.executor.shutdownNow();
         if (tasks == null) {
             Log.info("executor pool destroyed, no task was in progress");
         }
         Log.info("executor pool destroyed, aborting {} task/s that were under progress", tasks.size());
    }

}