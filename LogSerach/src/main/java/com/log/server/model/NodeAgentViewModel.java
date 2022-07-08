/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.server.model;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 *
 * @author Vaibhav Singh
 */
public class NodeAgentViewModel {

	private String nodeName;

	private String agentName;

	private int port;

	@Min(value = 1)
	@Max(value = 100)
	private int parallelism;

	@Min(value = 1)
	@Max(value = 100)
	private int maxFilesToSearch;

	@Min(value = 1)
	@Max(value = 500)
	private int maxLinesPerFile;

	@Min(value = 1)
	@Max(value = 1999)
	private int maxLinesInresult;

	@Min(value = 1)
	@Max(value = 10*1024)
	private int resultInKb;

	private Date lastModifed;

	private String lastHeartbeat;

	private List<String> labels;
	
	private List<String> users;
	
	private String version;
	
	private boolean alive = true;
	
	private String deadSince;
	
	private String osName;

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getParallelism() {
		return parallelism;
	}

	public void setParallelism(int parallelism) {
		this.parallelism = parallelism;
	}

	public int getMaxFilesToSearch() {
		return maxFilesToSearch;
	}

	public void setMaxFilesToSearch(int maxFilesToSearch) {
		this.maxFilesToSearch = maxFilesToSearch;
	}

	public int getMaxLinesPerFile() {
		return maxLinesPerFile;
	}

	public void setMaxLinesPerFile(int maxLinesPerFile) {
		this.maxLinesPerFile = maxLinesPerFile;
	}

	public int getMaxLinesInresult() {
		return maxLinesInresult;
	}

	public void setMaxLinesInresult(int maxLinesInresult) {
		this.maxLinesInresult = maxLinesInresult;
	}

	public int getResultInKb() {
		return resultInKb;
	}

	public void setResultInKb(int resultInKb) {
		this.resultInKb = resultInKb;
	}

	public Date getLastModifed() {
		return lastModifed;
	}

	public void setLastModifed(Date lastModifed) {
		this.lastModifed = lastModifed;
	}

	public String getLastHeartbeat() {
		return lastHeartbeat;
	}

	public void setLastHeartbeat(String lastHeartbeat) {
		this.lastHeartbeat = lastHeartbeat;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}
	

	public List<String> getUsers() {
		return users;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}
	

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public String getDeadSince() {
		return deadSince;
	}

	public void setDeadSince(String deadSince) {
		this.deadSince = deadSince;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}
	

}
