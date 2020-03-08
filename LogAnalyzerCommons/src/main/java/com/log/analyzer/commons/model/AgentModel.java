/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.analyzer.commons.model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Vaibhav Pratap Singh
 */
public class AgentModel {

    private String clientName;

    private List<String> clientLabels;

    private String clientNode;

    private int clientConnectPort;

    private List<String> fileTypesToSearch;

    private List<String> filePathToSearch;

    private List<String> fileNameToSearch;

    private int searchThreadCount;

    private String timeZone;

    private Date lastModified;

    private Date lastContactTime;
    
    private int threads;
    
    private String commKey;
    
    private String osName;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public List<String> getClientLabels() {
        return clientLabels;
    }

    public void setClientLabels(List<String> clientLabels) {
        this.clientLabels = clientLabels;
    }

    public String getClientNode() {
        return clientNode;
    }

    public void setClientNode(String clientNode) {
        this.clientNode = clientNode;
    }

    public int getClientConnectPort() {
        return clientConnectPort;
    }

    public void setClientConnectPort(int clientConnectPort) {
        this.clientConnectPort = clientConnectPort;
    }

    public List<String> getFileTypesToSearch() {
        return fileTypesToSearch;
    }

    public void setFileTypesToSearch(List<String> fileTypesToSearch) {
        this.fileTypesToSearch = fileTypesToSearch;
    }

    public List<String> getFilePathToSearch() {
        return filePathToSearch;
    }

    public void setFilePathToSearch(List<String> filePathToSearch) {
        this.filePathToSearch = filePathToSearch;
    }

    public List<String> getFileNameToSearch() {
        return fileNameToSearch;
    }

    public void setFileNameToSearch(List<String> fileNameToSearch) {
        this.fileNameToSearch = fileNameToSearch;
    }

    public int getSearchThreadCount() {
        return searchThreadCount;
    }

    public void setSearchThreadCount(int searchThreadCount) {
        this.searchThreadCount = searchThreadCount;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public Date getLastContactTime() {
        return lastContactTime;
    }

    public void setLastContactTime(Date lastContactTime) {
        this.lastContactTime = lastContactTime;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }
    

    public String getCommKey() {
		return commKey;
	}

	public void setCommKey(String commKey) {
		this.commKey = commKey;
	}
	

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	@Override
	public String toString() {
		return "AgentModel [clientName=" + clientName + ", clientLabels=" + clientLabels + ", clientNode=" + clientNode + ", clientConnectPort=" + clientConnectPort
				+ ", fileTypesToSearch=" + fileTypesToSearch + ", filePathToSearch=" + filePathToSearch + ", fileNameToSearch=" + fileNameToSearch + ", searchThreadCount="
				+ searchThreadCount + ", timeZone=" + timeZone + ", lastModified=" + lastModified + ", lastContactTime=" + lastContactTime + ", threads=" + threads + ", commKey="
				+ commKey + ", osName=" + osName + "]";
	}

	

}
