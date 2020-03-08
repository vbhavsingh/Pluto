/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.analyzer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.analyzer.commons.FileIndexModel;
import com.analyzer.commons.LocalConstants;
import com.analyzer.commons.SystemStreamCapture;
import com.log.analyzer.commons.Commons;
import com.log.analyzer.commons.Constants;
import com.log.analyzer.commons.Util;
import com.log.analyzer.commons.err.InputParsingException;
import com.log.analyzer.commons.model.AgentConfigurationModel;
import com.log.analyzer.commons.model.AgentRegistrationForm;
import com.log.analyzer.commons.model.SearchModel;

import ch.qos.logback.classic.Level;
import net.rationalminds.util.TextFileUtility;

/**
 *
 * @author Vaibhav Singh
 */
public class LocalUtil {

	private static final Logger Log = Logger.getLogger(LocalUtil.class);
	
	public static boolean consoleLoggingEnabled() {
		String op = System.getProperty(LocalConstants.CONSOLE_OUTPUT);
		if ("true".equalsIgnoreCase(op)) {
			return true;
		}	
		return false;
	}

	public static Level getLogLevel() {
		String level = System.getProperty(LocalConstants.LOG_LEVEL);
		if (level == null) {
			return null;
		}
		level = level.toLowerCase();
		if ("error".equalsIgnoreCase(level)) {
			return Level.ERROR;
		}
		if ("info".equalsIgnoreCase(level)) {
			return Level.INFO;
		}
		if ("debug".equalsIgnoreCase(level)) {
			return Level.DEBUG;
		}
		if ("trace".equalsIgnoreCase(level)) {
			return Level.TRACE;
		}
		return null;
	}

	public static String getMasterURL() {
		String url = "";
		url = System.getenv().get(LocalConstants.MASTER_SERVER_URL);
		if (url == null || "".equals(url.trim())) {
			url = System.getProperty(LocalConstants.MASTER_SERVER_URL);
		} else if (url == null || "".equals(url.trim())) {
			url = System.getenv(LocalConstants.MASTER_SERVER_URL);
		} else {
			url = null;
		}
		return url;
	}

	public static String[] getPosixtRestartCommand() throws IOException {
		List<String> scriptLets = new ArrayList<String>();
		scriptLets.add("/bin/sh");
		scriptLets.add("-c");
		scriptLets.add("nohup " + createScript() + " &");
		return scriptLets.toArray(new String[0]);
	}

	public static final String getCharonLocation() {
		String home = System.getProperty("user.dir");
		String path = home + "/" + Constants.CLIENT_FILE_NAME;
		return path;
	}

	public static final String getCharonTempLocation() {
		return getCharonLocation() + ".tmp";
	}

	private static String createScript() throws IOException {
		String path = System.getProperty("user.dir");
		path = path + "/restart-charon.sh";
		Log.info("creating restart script: " + path);
		File f = new File(path);

		if (f.exists()) {
			f.delete();
			f.createNewFile();
		}

		f.setExecutable(true);

		FileWriter fw = new FileWriter(f.getAbsoluteFile());

		BufferedWriter writer = new BufferedWriter(fw);

		writer.write("sleep 10");
		writer.newLine();
		writer.write("rm -rf " + getCharonLocation());
		writer.newLine();
		writer.write("mv " + getCharonTempLocation() + " " + getCharonLocation());
		writer.newLine();
		writer.write("nohup java -jar -Dlog.master.url=" + LocalUtil.getMasterURL() + " " +getCharonLocation() +" "+ LocalConstants.RESTART
				+ " &> /dev/null &disown");
		writer.newLine();
		// self destruct
		writer.write("rm -rf " + path);
		try {
			writer.close();
			fw.close();
		} catch (Exception e) {
			Log.error(e);
		}

		return path;
	}

	/**
	 * 
	 * @return
	 */
	public static AgentRegistrationForm getRegistrationObject() {
		AgentRegistrationForm form = new AgentRegistrationForm();
		form.setClientConnectPort(String.valueOf(LocalConstants.CURRENT_PORT));
		form.setClientNode(getNodeFQDN());
		form.setClientName(getAgentCustomName());
		form.setTimeZone(getTimeZone());
		form.setAgentVersion(Constants.CURRENT_AGENT_VERSION);
		form.setOsName(System.getProperty("os.name"));
		return form;
	}

	/**
	 * This computed the address of the local environment. complete location has
	 * system name, tier name and server name.
	 *
	 * @return complete_host_name: String
	 */
	public static String getNodeFQDN() {
		String hostname = "";
		try {
			InetAddress addr = InetAddress.getLocalHost();
			hostname = addr.getCanonicalHostName();
		} catch (UnknownHostException e) {
			hostname = "unknown";
		}
		return hostname;
	}

	/**
	 * 
	 * @return
	 */
	public static String getAgentCustomName() {
		String hostname = System.getProperty(LocalConstants.AGENT_NAME);
		if (hostname == null || "".equals(hostname.trim())) {
			try {
				InetAddress addr = InetAddress.getLocalHost();
				hostname = addr.getHostName();
			} catch (UnknownHostException e) {
				hostname = "unknown";
			}
		}
		return hostname;
	}

	/**
	 * 
	 * @return
	 */
	public static String getTimeZone() {
		TimeZone tz = Calendar.getInstance().getTimeZone();
		return tz.getDisplayName(true, TimeZone.SHORT, Locale.ENGLISH);
	}

	/**
	 * 
	 * @param conf
	 */
	public static void updateAgentConfiguration(AgentConfigurationModel conf) {
		if (StringUtils.isNotEmpty(conf.getFindCommand())) {
			LocalConstants.FIND_COMMAND = conf.getFindCommand();
		}
		if (conf.getMaxDataInKb() > 1000) {
			LocalConstants.MAX_DATA_TO_READ = conf.getMaxDataInKb() * 1024;
		}
		if (conf.getMaxFilesToBeSearched() > 10) {
			LocalConstants.MAX_FILES_TO_BE_SEARCHED = conf.getMaxFilesToBeSearched();
		}
		if (conf.getMaxLinesAllowedInResult() > 10) {
			LocalConstants.MAX_LINES_ALLOWED_IN_RESULT = conf.getMaxLinesAllowedInResult();
		}
		if (conf.getMaxLinesAllowedPerFile() > 10) {
			LocalConstants.MAX_LINES_ALLOWED_PER_FILE = conf.getMaxLinesAllowedPerFile();
		}
	}

	/**
	 * 
	 * @param matchingPathMap
	 * @return
	 */
	public static int getTotalCount(Map<String, List<FileIndexModel>> matchingPathMap) {
		int i = 0;
		for (List list : matchingPathMap.values()) {
			if (list != null) {
				i += list.size();
			}
		}
		return i;
	}

	/**
	 * 
	 * @param matchingPathMap
	 * @return
	 * @throws InputParsingException
	 * @throws ParseException 
	 */
	public static int getTotalCount(SearchModel model, Map<String, List<FileIndexModel>> matchingPathMap) throws InputParsingException, ParseException {
		if (matchingPathMap == null) {
			return 0;
		}
		boolean filePathsPresent = (model.getPathList() != null && model.getPathList().size() > 0) ? true : false;
		boolean fileNamesPresent = (model.getFileList() != null && model.getFileList().size() > 0) ? true : false;
		if ((filePathsPresent && fileNamesPresent) || fileNamesPresent) {
			int count = 0;
			for (List<FileIndexModel> fileListOnPath : matchingPathMap.values()) {
				count += getMatchingFileCount(model, fileListOnPath);
			}
			return count;
		} else {
			return getTotalCount(matchingPathMap);
		}
	}
	
    /**
     * Checks whether the file matching the search pattern is in the given time range. 
     * If file index has no last modified time present, the file is considered to be valid file as state of the file is yet unknown. 
     * The last modified date of file should be greater than the start time of the asked interval
     *   
     * @param fileIndex
     * @param model
     * @return
     * @throws ParseException
     */
    public static boolean isFileinTimeInterval(FileIndexModel fileIndex, SearchModel model) throws ParseException{
    	if(fileIndex.getLastModifiedDate() == null) {
    		return true;
    	}
    	if(model.isTimedSearch()) {
    		long fileDate = fileIndex.getLastModifiedDate().getTime();
    		long startDate = model.getStartDateTime().getTime();
    		if(fileDate >= startDate) {
    			return true;
    		}else {
    			return false;
    		}
    	}
    	return true;
    }

	/**
	 *
	 * @param model
	 * @param mappedFileList
	 * @param pathPrefix
	 * @return
	 * @throws ParseException 
	 * @throws Exception
	 */
	private static int getMatchingFileCount(SearchModel model, List<FileIndexModel> fileIndexListOnPath) throws InputParsingException, ParseException {
		int i = 0;
		for (String fileKey : model.getFileList()) {
			Pattern pattern;
			try {
				String key = Commons.sanitizeRegexInput(fileKey.toLowerCase());
				pattern = Pattern.compile(key);
			} catch (Exception e) {
				Log.error("cannot parse search file pattern: " + fileKey, e);
				throw new InputParsingException(e.getMessage());
			}
			for (FileIndexModel index : fileIndexListOnPath) {
				Matcher matcher = pattern.matcher(index.getFileName().toLowerCase());
				if (matcher.find() && isFileinTimeInterval(index, model)) {
					i++;
				}
			}
		}
		return i;
	}

	/**
	 *
	 * @param port
	 * @return
	 */
	public static boolean available(int port) {
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
	 * @param fileName
	 * @return
	 */
	public static FileIndexModel refreshFileIndex(FileIndexModel index) {
		try {
			File f = new File(index.getAbsoluteFileName());
			index.setLastModifiedDate(new Date(f.lastModified()));
			
			if(index.getDateFormat() == null || "".equals(index.getDateFormat())) {
				
				String fType = f.getName().substring(f.getName().lastIndexOf(".")+1);

				if(LocalConstants.ARCHIVE_TYPES.contains(fType.toLowerCase())) {
					String cmd = getLinesFromArchiveFilePosixCmd();
					cmd = cmd.replace(Constants.FILE_NAME_PLACEHOLDER, f.getAbsolutePath());
					List<String> lines = executePosixCommand(cmd);
					index.setDateFormat(TextFileUtility.findProbableFileDateFormat(lines));
				}else {
					index.setDateFormat(TextFileUtility.findProbableFileDateFormat(f.getAbsolutePath()));	
				}
				Log.trace(index.getAbsoluteFileName()+", established time pattern is " + index.getDateFormat());
			}
		} catch (Exception e) {
			Log.error(index.getAbsoluteFileName()+" ,error while indexing: " + e.getMessage());
		}
		return index;
	}
	
	/**
	 * 
	 * @return
	 */
	private static String getLinesFromArchiveFilePosixCmd() {
		String osName = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
		if(LocalConstants.ARCHIVE_LINE_REDAER_COMMAND == null) {
			return Util.getLinesFromArchiveFilePosixCmd(osName);
		}else {
			return LocalConstants.ARCHIVE_LINE_REDAER_COMMAND;
		}
	}
	
	/**
	 * 
	 * @param command
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static List<String> executePosixCommand(String command) throws IOException, InterruptedException {
		String[] script = {"/bin/sh", "-c", command};
        Process p = Runtime.getRuntime().exec(script);
        
        Log.trace("determining date format with line extract command : "+command);

        SystemStreamCapture errorStream = new SystemStreamCapture(p.getErrorStream(), LocalConstants.GENERIC_TYPE);
        SystemStreamCapture outputStream = new SystemStreamCapture(p.getInputStream(), LocalConstants.GENERIC_TYPE);

        errorStream.start();
        outputStream.start();

        while (!outputStream.isDone()) {
            Thread.sleep(100);
        }

        return outputStream.getData();
	}

}
