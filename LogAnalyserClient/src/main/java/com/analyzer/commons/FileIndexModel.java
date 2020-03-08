package com.analyzer.commons;

import java.util.Date;

public class FileIndexModel {
	private String fileName;
	
	private Date lastModifiedDate;
	
	private String dateFormat;
	
	private String filePath;

	public FileIndexModel(String fileName) {
		this.fileName = fileName;
	}
	
	public FileIndexModel(String filePath,String fileName) {
		this.fileName = fileName;
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public String getAbsoluteFileName() {
		return filePath + fileName;
	}
	

	@Override
	public String toString() {
		return "FileIndexModel [fileName=" + fileName + ", lastModifiedDate=" + lastModifiedDate + ", dateFormat=" + dateFormat + "]";
	}

	
	

}
