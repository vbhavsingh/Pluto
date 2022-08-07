/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.server.util;

import java.net.InetAddress;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.log.analyzer.commons.CipherService;
import com.log.analyzer.commons.Constants;
import com.log.analyzer.commons.model.AgentModel;
import com.log.analyzer.commons.model.SearchResultModel;
import com.log.server.LocalConstants;
import com.log.server.model.NodeAgentViewModel;

/**
 *
 * @author Vaibhav Pratap Singh
 */
public class Utilities {

	private static final Logger Log = LogManager.getLogger(Utilities.class);

	public static String getVerion() {
		return Constants.VERSION;
	}

	public static String getCharonVerion() {
		return Constants.CURRENT_AGENT_VERSION;
	}

	public static String getNameFromFqdn(String name) {
		if (name == null || !name.contains(".")) {
			return name;
		} else {
			String s = name.substring(0, name.indexOf("."));
			return s;
		}
	}

	public static String posixSanitizer(String str) {
		str = str.replace("[", "\\[");
		str = str.replace("{", "\\{");
		str = str.replace("(", "\\(");
		str = str.replace("]", "\\]");
		str = str.replace("}", "\\}");
		str = str.replace(")", "\\)");
		return str;
	}

	public static String getResourceURL(AgentModel agent) {
		return "http://" + agent.getClientNode() + ":" + agent.getClientConnectPort();
	}

	public static String getResourceURL(NodeAgentViewModel agent) {
		return "http://" + agent.getNodeName() + ":" + agent.getPort();
	}

	/**
	 * 
	 * @param dateFormat
	 * @param dateText
	 * @return
	 */
	public static Date StringToDate(String dateFormat, String dateText) {
		DateFormat format = new SimpleDateFormat(dateFormat);
		Date date = null;
		try {
			date = format.parse(dateText);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 
	 * @param plainText
	 * @param node
	 * @return
	 */
	public static String enCipher(String plainText, AgentModel node) {
		if (Constants.CIPHER_KEY == null) {
			String key = node.getClientNode() + node.getClientConnectPort() + node.getTimeZone();
			try {
				key = key + InetAddress.getLocalHost().getCanonicalHostName();
				CipherService.setCipherKey(key);
			} catch (Exception e) {
				Log.fatal("error while encrypting payload, agents may not be communicated because of this", e);
			}
		}

		try {
			return CipherService.encrypt(plainText);
		} catch (Exception e) {
			Log.fatal("error while message encoding", e);
		}
		return null;
	}

	/**
	 * 
	 * @param result
	 * @param filesSearched
	 * @param totalFilesFound
	 * @return
	 */
	public static String getMaxFileLimitViolationMsg(SearchResultModel result, int filesSearched) {
		int totalFilesFound = result.getFilesFoundCount();
		String msg = result.getNodeName() + " : ";
		if (filesSearched == 0) {
			msg = msg + "No match found in any file that was searched. ";
		}
		if (filesSearched == 1) {
			msg = msg + "One file was searched, but no match found. ";
		}
		if (filesSearched > 1) {
			msg = msg + filesSearched + " files were searched, but no match found. ";
		}
		msg = msg + "Due to restarctions all files can't be searched. Although " + totalFilesFound
				+ " files matched the path/name pattern provided.";
		msg = msg + " Try narrowing your search.";
		return msg;
	}

	public static String getMaxFileLimitViolationMsg(int filesSearched, SearchResultModel result) {
		int totalFilesFound = result.getFilesFoundCount();
		int filesSelectedForSearch = result.getFilesSelectedCount();
		String msg = "";
		if (filesSearched == 1 && filesSelectedForSearch > filesSearched) {
			msg = msg + "Only one file with match found out of " + filesSelectedForSearch + " searched files. ";
		}
		if (filesSearched > 1 && filesSelectedForSearch > filesSearched) {
			msg = msg + filesSearched + " files with match found out of " + filesSelectedForSearch
					+ " selected files. ";
		}
		if (filesSearched == 1 && filesSelectedForSearch == filesSearched) {
			msg = msg + "Only one file was searched. ";
		}
		if (filesSearched > 1 && filesSelectedForSearch == filesSearched) {
			msg = msg + filesSearched + " files were searched. ";
		}
		if (totalFilesFound > filesSelectedForSearch) {
			msg = msg + "Due to restarctions all files can't be searched. Although " + totalFilesFound
					+ " files matched the path/name pattern provided. ";
		}
		if (filesSelectedForSearch > filesSearched) {
			msg = msg + " Try narrowing your search, for more close results.";
		}

		return msg;
	}

	public static String getFaultMessage(SearchResultModel result, boolean appendNodeName) {
		String msg = "";
		if (appendNodeName) {
			msg = result.getNodeName() + " : ";
		}
		if (LocalConstants.ERROR.UNREACHABLE.equals(result.getErrorCode())) {
			msg = msg + "Node can't be reached. May be client is down or there is some network problem.";
		}
		if (LocalConstants.ERROR.UNAUTHORIZED.equals(result.getErrorCode())) {
			msg = msg + "You do not have access on this server. This incident is security breach and will be logged.";
		}
		if (LocalConstants.ERROR.SECURE_COMM_FAILED.equals(result.getErrorCode())) {
			msg = msg + "There are encryption issues with agent communication. Have admin to look into it.";
		}
		if (LocalConstants.ERROR.UNDEFINED.equals(result.getErrorCode())
				&& (result.getResultList() == null || result.getResultList().size() == 0)) {
			msg = msg + " No match found for provided criteria.";
		}
		return msg;
	}

	public static String listToString(List<?> list) {
		if (list == null || list.size() == 0) {
			return "[]";
		}
		int i = 1;
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		for (Object l : list) {
			if (l instanceof String) {
				builder.append(l);
				i++;
				if (i < list.size()) {
					builder.append(",");
				}
			}
		}
		builder.append("]");
		return builder.toString();
	}

	/**
	 * 
	 * @param zone
	 * @return
	 */
	public static String dayLightNormalizer(String zone) {
		if ("PDT".equalsIgnoreCase(zone))
			return "PST";
		if ("EDT".equalsIgnoreCase(zone))
			return "EST";
		if ("MDT".equalsIgnoreCase(zone))
			return "MST";
		if ("CDT".equalsIgnoreCase(zone))
			return "CST";
		if ("HDT".equalsIgnoreCase(zone))
			return "HST";
		return zone;

	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String trim(String str) {
		if (str == null) {
			return "";
		} else
			return str.trim();
	}

	public static String millisecondsToDate(long mills, String tz) {
		/*
		 * SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm:ss.SSS");
		 * GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone(tz));
		 * calendar.setTimeInMillis(mills);
		 */
		DateTime jodaTime = new DateTime(mills, DateTimeZone.forTimeZone(TimeZone.getTimeZone(tz)));
		DateTimeFormatter parser = DateTimeFormat.forPattern("MM/dd/yy HH:mm:ss");
		return parser.print(jodaTime);
	}

	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static String fromatSizeFromBytes(long bytes) {
		if (bytes < 1000) {
			return bytes + "b";
		}
		if (bytes < 1000 * 1000) {
			double d = bytes / 1024d;
			String number = decimalFormat(d);
			return number + "Kb";
		}
		if (bytes < 1000 * 1000 * 1000) {
			double d = bytes / (1024d * 1024d);
			String number = decimalFormat(d);
			return number + "Mb";
		}
		double d = bytes / (1024d * 1024d * 1024d);
		String number = decimalFormat(d);
		return number + "Gb";
	}

	/**
	 * 
	 * @param number
	 * @return
	 */
	private static String decimalFormat(double d) {
		String number = new DecimalFormat("#.##").format(d);
		return number;
	}

	/**
	 * 
	 * @param date
	 * @param dateFormat
	 * @param frmTz
	 * @param toTz
	 * @return
	 */
	public static String covertDateToStringInTargetTz(String date, String dateFormat, String frmTz, String toTz) {
		/*
		 * Convert the time string into a string with valid timezone appeneded to it
		 */
		if (toTz == null) {
			toTz = frmTz;
		}

		frmTz = Utilities.dayLightNormalizer(frmTz);
		toTz = Utilities.dayLightNormalizer(toTz);

		String fromDateTime = date + " " + DateTimeZone.forTimeZone(TimeZone.getTimeZone(frmTz));

		DateTimeFormatter dtf = DateTimeFormat.forPattern(dateFormat + " ZZZ");
		DateTime origDate = new DateTime(dtf.parseDateTime(fromDateTime));

		DateTime dtToTz = origDate.withZone(DateTimeZone.forTimeZone(TimeZone.getTimeZone(toTz)));

		return dtToTz.toString(dateFormat);
	}

	/**
	 * 
	 * @param date
	 * @param format
	 * @param timezone
	 * @return
	 */
	public static Date convertStringToTimeZonedDate(String date, String format, String timezone) {
		timezone = Utilities.dayLightNormalizer(timezone);
		date = date + " " + DateTimeZone.forTimeZone(TimeZone.getTimeZone(timezone));
		DateTimeFormatter dtf = DateTimeFormat.forPattern(format + " ZZZ");
		DateTime tzDateTime = new DateTime(dtf.parseDateTime(date));
		return tzDateTime.toDate();
	}

	/**
	 * 
	 * @param fromDate
	 * @param frmTz
	 * @param toTz
	 * @return
	 */
	public static DateTime convertTimeZonesinJoda(Date fromDate, String frmTz, String toTz) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		/*
		 * Convert the time string into a string with valid timezone appeneded to it
		 */
		frmTz = Utilities.dayLightNormalizer(frmTz);
		toTz = Utilities.dayLightNormalizer(toTz);

		String fromDateTime = df.format(fromDate) + " " + DateTimeZone.forTimeZone(TimeZone.getTimeZone(frmTz));

		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS ZZZ");
		DateTime origDate = new DateTime(dtf.parseDateTime(fromDateTime));
		DateTime dtToTz = origDate.withZone(DateTimeZone.forTimeZone(TimeZone.getTimeZone(toTz)));

		return dtToTz;
	}

	/**
	 * 
	 * @param eventDT
	 * @param startDT
	 * @param endDT
	 * @return
	 */
	public static boolean isEventDateTimeInRange(Date eventDT, Date startDT, Date endDT) {
		long eventMills = eventDT.getTime();
		long startMills = startDT.getTime();
		long endMills = endDT.getTime();

		if (eventMills >= startMills && eventMills <= endMills) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @return
	 */
	public static final List<String> getAllowedTimeZoneList() {
		List<String> allowedTimeZoneList = new ArrayList<String>();
		allowedTimeZoneList.add("EST");
		allowedTimeZoneList.add("CST");
		allowedTimeZoneList.add("MST");
		allowedTimeZoneList.add("PST");
		allowedTimeZoneList.add("IST");
		allowedTimeZoneList.add("ACT");
		allowedTimeZoneList.add("AET");
		allowedTimeZoneList.add("AGT");
		allowedTimeZoneList.add("ART");
		allowedTimeZoneList.add("AST");
		allowedTimeZoneList.add("BET");
		allowedTimeZoneList.add("BST");
		allowedTimeZoneList.add("CAT");
		allowedTimeZoneList.add("CET");
		allowedTimeZoneList.add("CNT");
		allowedTimeZoneList.add("CTT");
		allowedTimeZoneList.add("EAT");
		allowedTimeZoneList.add("ECT");
		allowedTimeZoneList.add("EET");
		allowedTimeZoneList.add("GB");
		allowedTimeZoneList.add("GMT");
		allowedTimeZoneList.add("HST");
		allowedTimeZoneList.add("IET");
		allowedTimeZoneList.add("JST");
		allowedTimeZoneList.add("MET");
		allowedTimeZoneList.add("MIT");
		allowedTimeZoneList.add("NET");
		allowedTimeZoneList.add("NST");
		allowedTimeZoneList.add("NZ");
		allowedTimeZoneList.add("PLT");
		allowedTimeZoneList.add("PNT");
		allowedTimeZoneList.add("PRC");
		allowedTimeZoneList.add("PRT");
		allowedTimeZoneList.add("ROK");
		allowedTimeZoneList.add("SST");
		allowedTimeZoneList.add("UCT");
		allowedTimeZoneList.add("UTC");
		allowedTimeZoneList.add("VST");
		allowedTimeZoneList.add("WET");
		return allowedTimeZoneList;
	}
}
