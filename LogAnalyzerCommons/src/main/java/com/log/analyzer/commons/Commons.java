/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.analyzer.commons;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import java.text.DecimalFormat;
import java.util.List;

import org.apache.logging.log4j.Level;


/**
 *
 * @author Vaibhav Pratap Singh
 */
public class Commons {

	private static final DecimalFormat threePrecisionDf = new DecimalFormat("#.###");

	public static List<String> getDelimatedList(String arg) {
		if (arg == null) {
			return null;
		}
		Iterable<String> results = Splitter.on(CharMatcher.anyOf(Constants.INPUT_DELIMATORS)).trimResults().omitEmptyStrings().split(arg);
		return Lists.newArrayList(results);
	}

	public static void getCommandMap(String searchString) {

	}

	public static String threeDecimal(float number) {
		return threePrecisionDf.format(number);
	}

	/**
	 * 
	 * @param level
	 * @return
	 */
	public static Level getLogLevel(String level) {
		if ("trace".equals(level.toLowerCase())) {
			return Level.TRACE;
		}
		if ("debug".equals(level.toLowerCase())) {
			return Level.DEBUG;
		}
		if ("info".equals(level.toLowerCase())) {
			return Level.INFO;
		}
		if ("error".equals(level.toLowerCase())) {
			return Level.ERROR;
		}
		if ("fatal".equals(level.toLowerCase())) {
			return Level.FATAL;
		}
		if ("all".equals(level.toLowerCase())) {
			return Level.ALL;
		}
		return Level.INFO;
	}

	/**
	 * this function appends escape character to avoid dangling meta character
	 * error for regex Pattern compilation
	 * 
	 * @param pattern
	 * @return
	 */
	public static String sanitizeRegexInput(String pattern) {
		char token = pattern.charAt(0);
		// 42=*, 92=\ ?=63 +=43 [=91 (=40 )=41 {=123 "=34
		if (token == 42 || token == 92 || token == 63 || token == 43 || token == 91 || token == 40 || token == 41 || token == 123 || token == 34) {
			return "\\" + pattern;
		}
		return pattern;
	}
}
