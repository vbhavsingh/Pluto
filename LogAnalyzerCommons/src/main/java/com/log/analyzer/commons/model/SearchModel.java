/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.analyzer.commons.model;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.log.analyzer.commons.Constants;

/**
 *
 * @author Vaibhav Pratap Singh
 */
public class SearchModel implements SecureInterface, Cloneable {

	private List<String> pathList;

	private List<String> fileList;

	private String textCommand;

	private String archiveCommand;

	private String sessionId;

	private String userName;

	private boolean vip = false;
	
	private String requestedTz;

	private String startDateTime;

	private String endDateTime;

	private String securityTag = Constants.REQUEST_SANITY;

	public List<String> getPathList() {
		return pathList;
	}

	public void setPathList(List<String> pathList) {
		this.pathList = pathList;
	}

	public List<String> getFileList() {
		return fileList;
	}

	public void setFileList(List<String> fileList) {
		this.fileList = fileList;
	}

	public String getTextCommand() {
		return textCommand;
	}

	public void setTextCommand(String textCommand) {
		this.textCommand = textCommand;
	}

	public String getArchiveCommand() {
		return archiveCommand;
	}

	public void setArchiveCommand(String archiveCommand) {
		this.archiveCommand = archiveCommand;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isVip() {
		return vip;
	}

	public void setVip(boolean vip) {
		this.vip = vip;
	}

	public String getStartDateString(){
		return startDateTime;
	}
	
	public Date getStartDateTime() throws ParseException {
		if(StringUtils.isEmpty(startDateTime)) {
			return null;
		}
		return Constants.SEARCH_DATE_FORMAT.parse(startDateTime);
	}

	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}
	
	public void setStartDateTime(Date startDateTime) {
		if(startDateTime != null) {
			this.startDateTime = Constants.SEARCH_DATE_FORMAT.format(startDateTime);	
		}
	}
	
	public String getEndDateString(){
		return endDateTime;
	}

	public Date getEndDateTime() throws ParseException {
		if(StringUtils.isEmpty(endDateTime)) {
			return null;
		}
		return Constants.SEARCH_DATE_FORMAT.parse(endDateTime);
	}

	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}
	
	public void setEndDateTime(Date endDateTime) {
		if(endDateTime != null) {
			this.endDateTime = Constants.SEARCH_DATE_FORMAT.format(endDateTime);	
		}
	}

	public String getSecurityTag() {
		return securityTag;
	}

	public void setSecurityTag(String securityTag) {
		this.securityTag = securityTag;
	}

	public boolean isTimedSearch() {
		if(startDateTime ==null || startDateTime.isEmpty()) {
			return false;
		}
		if(endDateTime ==null || endDateTime.isEmpty()) {
			return false;
		}
		return true;
	}
	

	public String getRequestedTz() {
		return requestedTz;
	}

	public void setRequestedTz(String requestedTz) {
		this.requestedTz = requestedTz;
	}


	@Override
	public String toString() {
		return "SearchModel [pathList=" + pathList + ", fileList=" + fileList + ", textCommand=" + textCommand + ", archiveCommand=" + archiveCommand + ", sessionId=" + sessionId
				+ ", userName=" + userName + ", vip=" + vip + ", requestedTz=" + requestedTz + ", startDateTime=" + startDateTime + ", endDateTime=" + endDateTime
				+ ", timedSearch=" + isTimedSearch() + ", securityTag=" + securityTag + "]";
	}

	public String info() {
		return "SearchModel [pathList=" + pathList + ", fileList=" + fileList + ", sessionId=" + sessionId + ", userName=" + userName + ", vip=" + vip + ", startDateTime="
				+ startDateTime + ", endDateTime=" + endDateTime + "]";
	}

	@Override
	public boolean validateObject(String criteria) {
		return this.securityTag.equalsIgnoreCase(criteria);
	}

	public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
