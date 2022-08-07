/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.analyzer.posix.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.analyzer.commons.ClientFileModel;
import com.analyzer.commons.LocalConstants;
import com.analyzer.commons.SystemStreamCapture;

/**
 *
 * @author vs1953
 */
public class SearchFileTask implements Callable<ClientFileModel> {

	private final static Logger Log = LogManager.getLogger(SearchFileTask.class);
	String command;
	AtomicLong resultSize;
	String absFileName;

	public SearchFileTask(String command, AtomicLong resultSize, String absFileName) {
		this.command = command;
		this.resultSize = resultSize;
		this.absFileName = absFileName;
	}

	@Override
	public ClientFileModel call() throws Exception {
		String[] script = { "/bin/sh", "-c", command };
		Log.trace("executing search command : " + command + " on file:" + this.absFileName + ", current data captured for current request is : " + this.resultSize + " lines");
		List<String> resultText = new ArrayList<String>();

		Log.debug("executing command : "+command);
		Process p = Runtime.getRuntime().exec(script);

		SystemStreamCapture errStream = new SystemStreamCapture(p.getErrorStream(), "ERROR");
		SystemStreamCapture outStream = new SystemStreamCapture(p.getInputStream(), LocalConstants.FILE_TYPE, this.resultSize, this.absFileName);

		errStream.start();
		outStream.start();

		while (!outStream.isDone()) {
			Thread.sleep(100);
		}

		Log.trace("command '" + command + "', Done"); 

		List<String> lineData = outStream.getData();
		resultText.addAll(lineData);
		PosixSearchThreadKiller.deathQueue().offer(p);
		Log.trace("search done on file:" + this.absFileName + ", current data captured for current request is : " + this.resultSize + " lines, this files contribution is "
				+ lineData.size() + " lines");
		Log.trace("command '" + command + "', Destroyed");

		ClientFileModel result = new ClientFileModel(resultText, resultText.size());
		result.setMaxLineLimitBreached(outStream.isMaxLineLimitBreached());
		result.setMaxdataLimitBreached(outStream.isMaxdataLimitBreached());
		result.setFileName(this.absFileName);
		return result;
	}
	/**
	 * 
	 * @return
	 * @throws Exception
	 */

}
