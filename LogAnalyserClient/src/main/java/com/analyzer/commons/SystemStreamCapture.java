/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.analyzer.commons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

/**
 *
 * @author Vaibhav Singh
 */
public class SystemStreamCapture extends Thread {

	InputStream is;
	String type;
	volatile boolean done = false;
	AtomicLong resultSize;
	boolean maxdataLimitBreached = false;
	boolean maxLineLimitBreached = false;
	String fName;

	List<String> buffer;

	private final static Logger Log = Logger.getLogger(SystemStreamCapture.class);

	public SystemStreamCapture(InputStream is, String type) {
		this.is = is;
		this.type = type;
		this.buffer = new ArrayList<String>();
	}

	public SystemStreamCapture(InputStream is, String type, AtomicLong resultSize, String fName) {
		this.is = is;
		this.type = type;
		this.resultSize = resultSize;
		this.buffer = new ArrayList<String>();
		this.fName = fName;
	}

	public void run() {
		try {
			if (LocalConstants.FILE_TYPE.equals(this.type)) {
				Log.debug("reading output for " + fName);
				if (this.resultSize.get() <= LocalConstants.MAX_DATA_TO_READ) {
					fileTypeRead();
				} else {
					Log.debug("max data limit reached while reading " + fName);
				}
			} else {
				genericRead();
			}
		} catch (IOException ioe) {
			Log.error(ioe);
		} finally {
			this.done = true;
		}
	}

	private void fileTypeRead() {
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		int i = 0;
		try {
			readloop: {
				while (resultSize.get() <= LocalConstants.MAX_DATA_TO_READ && (line = br.readLine()) != null) {
					if ("".equals(line)) {
						continue;
					}
					Log.trace("charcters in buffer :" + resultSize);
					if (resultSize.addAndGet(line.length()) > LocalConstants.MAX_DATA_TO_READ) {
						this.maxdataLimitBreached = true;
						Log.debug("max data limit reached while reading " + fName);
						break readloop;
					}
					if (i > LocalConstants.MAX_LINES_ALLOWED_PER_FILE) {
						this.maxLineLimitBreached = true;
						Log.debug("max line limit reached while reading " + fName);
						break readloop;
					}
					this.buffer.add(line);
					i++;
				}
			}
		} catch (IOException ex) {
			Log.error(this.fName, ex);
		} finally {
			try {
				br.close();
				isr.close();
			} catch (IOException ex) {
				Log.error(ex);
			}
		}
	}

	private void genericRead() throws IOException {
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				this.buffer.add(line);
			}
			br.close();
			isr.close();
		} catch (Exception e) {
			Log.error(e);
		} 
	}

	public List<String> getData() {
		return this.buffer;
	}

	public boolean isDone() {
		return this.done;
	}

	public boolean isMaxdataLimitBreached() {
		return maxdataLimitBreached;
	}

	public boolean isMaxLineLimitBreached() {
		return maxLineLimitBreached;
	}

}
