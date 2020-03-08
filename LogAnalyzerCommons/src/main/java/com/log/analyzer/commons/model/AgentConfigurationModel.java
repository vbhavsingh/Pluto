package com.log.analyzer.commons.model;

public class AgentConfigurationModel {

	private String findCommand;

	private int maxLinesAllowedInResult;

	private int maxLinesAllowedPerFile;

	private int maxFilesToBeSearched;

	private int maxDataInKb;

	private String currentAgentVersion;

	public String getFindCommand() {
		return findCommand;
	}

	public void setFindCommand(String findCommand) {
		this.findCommand = findCommand;
	}

	public int getMaxLinesAllowedInResult() {
		return maxLinesAllowedInResult;
	}

	public void setMaxLinesAllowedInResult(int maxLinesAllowedInResult) {
		this.maxLinesAllowedInResult = maxLinesAllowedInResult;
	}

	public int getMaxLinesAllowedPerFile() {
		return maxLinesAllowedPerFile;
	}

	public void setMaxLinesAllowedPerFile(int maxLinesAllowedPerFile) {
		this.maxLinesAllowedPerFile = maxLinesAllowedPerFile;
	}

	public int getMaxFilesToBeSearched() {
		return maxFilesToBeSearched;
	}

	public void setMaxFilesToBeSearched(int maxFilesToBeSearched) {
		this.maxFilesToBeSearched = maxFilesToBeSearched;
	}

	public int getMaxDataInKb() {
		return maxDataInKb;
	}

	public void setMaxDataInKb(int maxDataInKb) {
		this.maxDataInKb = maxDataInKb;
	}

	public String getCurrentAgentVersion() {
		return this.currentAgentVersion;
	}

	public void setCurrentAgentVersion(String currentAgentVersion) {
		this.currentAgentVersion = currentAgentVersion;
	}

	@Override
	public String toString() {
		return "AgentConfigurationModel [findCommand=" + findCommand + ", maxLinesAllowedInResult=" + maxLinesAllowedInResult + ", maxLinesAllowedPerFile=" + maxLinesAllowedPerFile
				+ ", maxFilesToBeSearched=" + maxFilesToBeSearched + ", maxDataInKb=" + maxDataInKb + ", currentAgentVersion=" + currentAgentVersion + "]";
	}

	

}
