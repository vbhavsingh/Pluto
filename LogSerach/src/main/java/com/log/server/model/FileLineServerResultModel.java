package com.log.server.model;

import java.util.List;

public class FileLineServerResultModel {
	
	private String readableFileSize;
	
	private String createdDate;
	
	private String lastModifiedDate;
	
	private List<LogRecordModel> logLines;

	public String getReadableFileSize() {
		return readableFileSize;
	}

	public void setReadableFileSize(String readableFileSize) {
		this.readableFileSize = readableFileSize;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public List<LogRecordModel> getLogLines() {
		return logLines;
	}

	public void setLogLines(List<LogRecordModel> logLines) {
		this.logLines = logLines;
	}


}
