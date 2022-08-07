/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.analyzer.commons;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.log.analyzer.commons.Constants;
import com.log.analyzer.commons.model.FileLineRequestModel;
import com.log.analyzer.commons.model.SearchModel;

/**
 *
 * @author Vaibhav Singh
 */
public class ValidateUser {

	private final static Logger Log = LogManager.getLogger(ValidateUser.class);
	
	public static boolean isUserValid(FileLineRequestModel model) throws Exception {
		if (model.getUserName() == null && "".equals(model.getUserName().trim())) {
			Log.debug("user name is null");
			return false;
		}
		if(model.isVip()==true){
			return true;
		}
		return isUserValid(model.getUserName());
	}

	public static boolean isUserValid(SearchModel model) throws Exception {
		if (model.getUserName() == null && "".equals(model.getUserName().trim())) {
			Log.debug("user name is null");
			return false;
		}
		if(model.isVip()==true){
			return true;
		}
		return isUserValid(model.getUserName());
	}

	public static boolean isUserValid(String user) throws Exception {
		String command = LocalConstants.DEFAULT_POSIX_USER_VALIDATE_COMMAND.replace(Constants.USER_NAME_PLACEHOLDER, user);

		String[] script = { "/bin/sh", "-c", command };
		Log.debug("executing search command : " + command);

		Process p = Runtime.getRuntime().exec(script);

		SystemStreamCapture errStream = new SystemStreamCapture(p.getErrorStream(), "ERROR");
		SystemStreamCapture outStream = new SystemStreamCapture(p.getInputStream(), LocalConstants.GENERIC_TYPE);

		errStream.start();
		outStream.start();

		while (!outStream.isDone()) {
			Thread.sleep(10);
		}

		List<String> lineData = outStream.getData();
		if (lineData == null || lineData.size() < 1) {
			return false;
		}
		String str = "";
		for (String s : lineData) {
			if (!" ".equals(s)) {
				str = str + s;
			}
		}
		str = str.trim();
		if ("".equals(str)) {
			return false;
		}
		int i = Integer.parseInt(str);

		if (i == 1) {
			return true;
		}

		return false;
	}
}
