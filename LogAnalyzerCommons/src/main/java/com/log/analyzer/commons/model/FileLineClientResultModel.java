package com.log.analyzer.commons.model;

import java.util.List;

public class FileLineClientResultModel {
	
	private long creationTime;
	
	private long lastModifiedTime;
	
	private long fileSize;
	
	private List<String> textInRange;

	public long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public long getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(long lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public List<String> getTextInRange() {
		return textInRange;
	}

	public void setTextInRange(List<String> textInRange) {
		this.textInRange = textInRange;
	}

	@Override
	public String toString() {
		return "FileLineResultModel [creationTime=" + creationTime + ", lastModifiedTime=" + lastModifiedTime + ", fileSize=" + fileSize + ", textInRange=" + textInRange + "]";
	}

		

}
