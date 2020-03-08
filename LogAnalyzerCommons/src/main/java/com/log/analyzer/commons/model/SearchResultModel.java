/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.analyzer.commons.model;

import java.util.List;

/**
 *
 * @author Vaibhav Pratap Singh
 */
public class SearchResultModel {

    private String nodeName;

    private String timeZone;

    private WarningFlags warnings = new WarningFlags();

    private List<FileSearchResult> resultList;

    private String errorCode;
    
    private String errorMessage;
    
    private int filesFoundCount=0;
    
    private int filesSelectedCount=0;
    
    private boolean failed = false;
    
    private long timeTakenInMilliseconds=0;

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public List<FileSearchResult> getResultList() {
        return resultList;
    }

    public void setResultList(List<FileSearchResult> resultList) {
        this.resultList = resultList;
    }

    public WarningFlags getWarnings() {
        return warnings;
    }

    public void setWarnings(WarningFlags warnings) {
        this.warnings = warnings;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public int getFilesFoundCount() {
		return filesFoundCount;
	}

	public void setFilesFoundCount(int filesFoundCount) {
		this.filesFoundCount = filesFoundCount;
	}

	public boolean isFailed() {
		return failed;
	}

	public void setFailed(boolean failed) {
		this.failed = failed;
	}
	

	public int getFilesSelectedCount() {
		return filesSelectedCount;
	}

	public void setFilesSelectedCount(int filesSelectedCount) {
		this.filesSelectedCount = filesSelectedCount;
	}
	

	public long getTimeTakenInMilliseconds() {
		return timeTakenInMilliseconds;
	}

	public void setTimeTakenInMilliseconds(long timeTakenInMilliseconds) {
		this.timeTakenInMilliseconds = timeTakenInMilliseconds;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public boolean hasWarnings() {
        if (this.warnings == null) {
            return false;
        }
        if (this.warnings.getMessages() == null) {
            return false;
        }
        if (this.warnings.getMessages().size() > 0) {
            return true;
        }
        boolean val = this.warnings.isMaxDataLimitBreached()
                | this.warnings.isMaxFileLimitBreached()
                | this.warnings.isMaxLineLimitBreached()
                | this.warnings.isMaxLinesPerFileLimitBreached();
        return val;
    }

	@Override
	public String toString() {
		return "SearchResultModel [nodeName=" + nodeName + ", timeZone=" + timeZone + ", warnings=" + warnings + ", errorCode=" + errorCode + ", errorMessage=" + errorMessage
				+ ", filesFoundCount=" + filesFoundCount + ", filesSelectedCount=" + filesSelectedCount + ", failed=" + failed + ", timeTakenInMilliseconds="
				+ timeTakenInMilliseconds + "]";
	}
	
   
}
