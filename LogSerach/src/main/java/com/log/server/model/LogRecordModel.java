/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.server.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 *
 * @author Vaibhav Pratap Singh
 */
public class LogRecordModel implements Comparable<LogRecordModel>, Serializable {

	private static final long serialVersionUID = 7836736250854442503L;

	private String clientNode;

    private String logFileName;

    private String displayDate;

    private String logText;
    
    private int logLineNumber;

    private long sequence;

    private String message;

    public String getClientNode() {
        return clientNode;
    }

    public void setClientNode(String clientNode) {
        this.clientNode = clientNode;
    }

    public String getLogFileName() {
        return logFileName;
    }

    public void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }

    public String getDisplayDate() {
        return displayDate;
    }

    public void setDisplayDate(String displayDate) {
        this.displayDate = displayDate;
    }

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    public String getLogText() {
        return logText;
    }

    public void setLogText(String logText) {
        this.logText = logText;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public int getLogLineNumber() {
		return logLineNumber;
	}

	public void setLogLineNumber(int logLineNumber) {
		this.logLineNumber = logLineNumber;
	}

	@Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    @Override
    public int compareTo(LogRecordModel o) {
        return sequence < o.getSequence() ? -1 : sequence > o.getSequence() ? 1 : doSecodaryOrderSort(o);
    }

    public int doSecodaryOrderSort(LogRecordModel o) {
        return sequence < o.getSequence() ? -1 : sequence > o.getSequence() ? 1 : 0;
    }

}
