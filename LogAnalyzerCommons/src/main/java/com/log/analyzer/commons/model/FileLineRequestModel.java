package com.log.analyzer.commons.model;

import com.log.analyzer.commons.Constants;

public class FileLineRequestModel implements SecureInterface {

	private String nodeName;

	private String fileName;

	private int startIndex;

	private int endIndex;

	private String sessionId;

	private String userName;

	private String command;

	private String viewTz;

	private boolean vip = false;

	private String securityTag = Constants.REQUEST_SANITY;

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
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

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public boolean isVip() {
		return vip;
	}

	public void setVip(boolean vip) {
		this.vip = vip;
	}

	public String getViewTz() {
		return viewTz;
	}

	public void setViewTz(String viewTz) {
		this.viewTz = viewTz;
	}

	@Override
	public String toString() {
		return "FileLineRequestModel [nodeName=" + nodeName + ", fileName=" + fileName + ", startIndex=" + startIndex + ", endIndex=" + endIndex + ", sessionId=" + sessionId
				+ ", userName=" + userName + ", command=" + command + "]";
	}

	@Override
	public boolean validateObject(String criteria) {
		return this.securityTag.equalsIgnoreCase(criteria);
	}

	@Override
	public String getSecurityTag() {
		return this.securityTag;
	}

	@Override
	public void setSecurityTag(String securityTag) {
		this.securityTag = securityTag;
	}

}
