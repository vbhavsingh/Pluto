/*

 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.analyzer.commons;

import java.text.SimpleDateFormat;

/**
 *
 * @author Vaibhav Pratap Singh
 */
public class Constants {
	
	public static final SimpleDateFormat SEARCH_DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

	public static final String LOG_LEVEL = "log.level";

	public static final String LOG_ON_CONSOLE = "log.console";

	public static final String VERSION = "6.0";

	public static final String CURRENT_AGENT_VERSION = "6.0";

	public static final String fileTypesArg = "com.log.filetype";

	public static final String FILE_NAME_PLACEHOLDER = "$$$$";
	
	public static final String DATETIME_PATTERN_PLACEHOLDER = "####";

	public static final String USER_NAME_PLACEHOLDER = "$$$$";

	public static final String TEXT_LOG_PATTERN = "";

	public static final String ARCHIVE_LOG_PATTERN = "(.*)(\\.)(tar|gz)";

	public static final String PARAM_FILE_NAMES = "file_names";

	public static final String PARAM_PATH_NAMES = "path_names";

	public static final String PARAM_CMD_TEXT = "text_cmd";

	public static final String PARAM_CMD_ARCHIVE = "archive_cmd";

	public static final String INPUT_DELIMATORS = ",:; ";

	public static final String SUCCESS = "SUCCESS";

	public static final String FAILURE = "FAIL";

	public static final String BAD_SEARCH_CRITERIA = "FAIL";

	public static final int HEARTBEAT_INTRVAL_IN_MIN = 5;

	public final static int LOG_INDEXING_INTRVAL_IN_MIN = 5;

	public final static String HEARTBEAT_URI_SUFFIX = "/heartbeat.htm";

	public final static String REGISTER_URI_SUFFIX = "/register.htm";

	public final static String COMMAND_URI_SUFFIX = "/command.htm";

	public final static String USER_EXISTS = "TRUE";

	public static final String SEARCH_URI_SUFFIX = "/search/";

	public static final String VALIDATE_USER_URI_SUFFIX = "/user/";
	
	public static final String FILE_LINES_IN_RANGE = "/linesinfile/";

	public static final String UPDATE_CONFIG_URI_SUFFIX = "/config/";

	public static final String TERMINATE_AGENT_URI_SUFFIX = "/kill/";

	public final static String DOWNLOAD_URL_SUFFIX = "/download/charon.jar";

	public final static String CLIENT_FILE_NAME = "charon.jar";

	public final static String REQUEST_SANITY = "PLAIN_REQUEST";

	public static String CIPHER_KEY = null;
	
	public static final String UNIVERSAL_DATE_FORMAT = "[0-9]{1,4}[\\.\\\\\\s-\\/|_][0-9]{1,2}[\\.\\\\\\s-\\/|_][0-9]{1,4}";

	public static final class PROPERTIES {

		public static int MAX_FILES_TO_SEARCH = 100;

		public static int MAX_LINES_PER_FILE = 500;

		public static int MAX_LINES_IN_RESULT = 1999;

		public static int RESULT_SIZE_KB = 10 * 1024;

		public static int MAX_THREADS = 100;

	}

	public static final class ERRORS {
		public static final String BAD_REQUEST_FOR_USER_VALIDATION = "BAD-REQUEST-FOR-USER-VALIDATION";

		public final static String NO_AUTHORITY = "AUTHORIZATION_FAILED";

		public final static String BAD_ENCYPTION = "BAD-ENCYPTION";
	}

}
