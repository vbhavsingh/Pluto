/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.analyzer.commons.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vaibhav Singh
 */
public class WarningFlags {

    private boolean maxFileLimitBreached = false;

    private boolean maxLineLimitBreached = false;

    private boolean maxDataLimitBreached = false;

    private boolean maxLinesPerFileLimitBreached = false;

    private List<String> messages;

    public WarningFlags() {
        this.messages = new ArrayList<String>();
    }

    public boolean isMaxFileLimitBreached() {
        return maxFileLimitBreached;
    }

    public void setMaxFileLimitBreached(boolean maxFileLimitBreached) {
        this.maxFileLimitBreached = maxFileLimitBreached;
    }

    public boolean isMaxLineLimitBreached() {
        return maxLineLimitBreached;
    }

    public void setMaxLineLimitBreached(boolean maxLineLimitBreached) {
        this.maxLineLimitBreached = maxLineLimitBreached;
    }

    public boolean isMaxDataLimitBreached() {
        return maxDataLimitBreached;
    }

    public void setMaxDataLimitBreached(boolean maxDataLimitBreached) {
        this.maxDataLimitBreached = maxDataLimitBreached;
    }


    public List<String> getMessages() {
        return messages;
    }

    public boolean isMaxLinesPerFileLimitBreached() {
        return maxLinesPerFileLimitBreached;
    }

    public void setMaxLinesPerFileLimitBreached(boolean maxLinesPerFileLimitBreached) {
        this.maxLinesPerFileLimitBreached = maxLinesPerFileLimitBreached;
    }

	@Override
	public String toString() {
		return "WarningFlags [maxFileLimitBreached=" + maxFileLimitBreached + ", maxLineLimitBreached=" + maxLineLimitBreached + ", maxDataLimitBreached=" + maxDataLimitBreached
				+ ", maxLinesPerFileLimitBreached=" + maxLinesPerFileLimitBreached + "]";
	}

   
}
