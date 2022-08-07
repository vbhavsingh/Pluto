/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.analyzer.commons;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.log.analyzer.commons.Constants;

/**
 *
 * @author vs1953
 */
public class LocalConstants {
	
	public static final Logger SECURITY_LOGGER = LogManager.getLogger("security");
	
	public static final Logger ACCESS_LOGGER = LogManager.getLogger("access");

    public static final String AGENT_NAME = "log.agent.name";

    public static final String MASTER_SERVER_URL = "log.master.url";
    
    public static final String LOG_LEVEL = "log.level";
    
    public static final String CONSOLE_OUTPUT = "log.console";

    public static final int PORT_LOWER_BOUND = 51000;

    public static final int PORT_UPPER_BOUND = 59999;

    public static int CURRENT_PORT = 51000;

    public static enum OSType {

        Windows, MacOS, Linux, Other
    };

    public static final String DEFAULT_FIND_COMMAND = "find $HOME /var/log -type f \\( -name \"*.log\" -o -name \"*.log.gz\" -o -name \"*.log.tar\" \\)";
    
    public static final String DEFAULT_POSIX_USER_VALIDATE_COMMAND = "cut -d: -f1 /etc/passwd | grep -i "+Constants.USER_NAME_PLACEHOLDER+" | wc -l";

    public static String FIND_COMMAND = null;
    
    public static String ARCHIVE_LINE_REDAER_COMMAND =  null;

    public final static int THREAD_POOL_SIZE = 512;

    public static int MAX_LINES_ALLOWED_IN_RESULT = 100;

    public static int MAX_LINES_ALLOWED_PER_FILE = 100;

    public static int MAX_FILES_TO_BE_SEARCHED = 50;

    /*Default accumulate maximum of 2 MB data from logs*/
    public static long MAX_DATA_TO_READ = 2 * 1024 * 1024;

    public final static String FILE_TYPE = "FILE_TYPE";

    public final static String GENERIC_TYPE = "GENERIC_TYPE";
    
    public final static String RESTART = "restart";
    
    public final static List<String> ARCHIVE_TYPES = Arrays.asList("tar","gz","zip","gzip");

}
