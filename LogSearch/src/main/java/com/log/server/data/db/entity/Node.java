package com.log.server.data.db.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "AGENTS")
public class Node implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2074206866513949852L;

	@Id
	@Column(name = "NODE_NAME")
	private String nodeName;
	
	@Column(name = "AGENT_NAME")
	private String agentName;
	
	@Column(name = "COMM_KEY")
	private String commKey;
	
	@Column(name = "OS_TYPE")
	private String osType;
	
	@Column(name = "AGENT_VERSION")
	private String agentVersion;
	
	@Column(name = "NODE_PORT", nullable = false)
	private Integer nodePort;
	
	@Column(name = "PARRALEL")
	private Integer parallel = 20;
	
	@Column(name = "NODE_TZ")
	private String nodeTimeZone;
	
	@Column(name = "MAX_FILES_TO_SEARCH")
	private Integer maxFilesToSearch = 50;
	
	@Column(name = "MAX_LINES_PER_FILE")
	private Integer maxLinesPerFile = 50;
	
	@Column(name = "MAX_LINES_IN_RESULT")
	private Integer maxLinesInResult = 200;
	
	@Column(name = "RESULT_SIZE_KB")
	private Integer resultSizeinKB = 2048;
	
	@Column(name = "LAST_MODIFIED")
	private Timestamp lastModified;
	
	@Column(name = "LAST_HEARTBEAT")
	private Timestamp lastHeartbeat;
	
	@OneToMany(mappedBy = "node")
	private List<Label> labels = new ArrayList<Label>();
	
	@OneToMany(mappedBy = "node")
	private List<FilePattern> filePattern = new ArrayList<FilePattern>();
	
	
	public void addLabels(String labelText) {
		Label label = new Label(this, labelText);	
		this.labels.add(label);
	}

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

	public String getCommKey() {
		return commKey;
	}

	public void setCommKey(String commKey) {
		this.commKey = commKey;
	}

	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}

	public String getAgentVersion() {
		return agentVersion;
	}

	public void setAgentVersion(String agentVersion) {
		this.agentVersion = agentVersion;
	}

	public Integer getNodePort() {
		return nodePort;
	}

	public void setNodePort(Integer nodePort) {
		this.nodePort = nodePort;
	}

	public Integer getParallel() {
		return parallel;
	}

	public void setParallel(Integer parallel) {
		this.parallel = parallel;
	}

	public String getNodeTimeZone() {
		return nodeTimeZone;
	}

	public void setNodeTimeZone(String nodeTimeZone) {
		this.nodeTimeZone = nodeTimeZone;
	}

	public Integer getMaxFilesToSearch() {
		return maxFilesToSearch;
	}

	public void setMaxFilesToSearch(Integer maxFilesToSearch) {
		this.maxFilesToSearch = maxFilesToSearch;
	}

	public Integer getMaxLinesPerFile() {
		return maxLinesPerFile;
	}

	public void setMaxLinesPerFile(Integer maxLinesPerFile) {
		this.maxLinesPerFile = maxLinesPerFile;
	}

	public Integer getMaxLinesInResult() {
		return maxLinesInResult;
	}

	public void setMaxLinesInResult(Integer maxLinesInResult) {
		this.maxLinesInResult = maxLinesInResult;
	}

	public Integer getResultSizeinKB() {
		return resultSizeinKB;
	}

	public void setResultSizeinKB(Integer resultSizeinKB) {
		this.resultSizeinKB = resultSizeinKB;
	}

	public Timestamp getLastModified() {
		return lastModified;
	}

	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}

	public Timestamp getLastHeartbeat() {
		return lastHeartbeat;
	}

	public void setLastHeartbeat(Timestamp lastHeartbeat) {
		this.lastHeartbeat = lastHeartbeat;
	}

	public List<Label> getLabels() {
		return labels;
	}

	public void setLabels(List<Label> labels) {
		this.labels = labels;
	}

	public List<FilePattern> getFilePattern() {
		return filePattern;
	}

	public void setFilePattern(List<FilePattern> filePattern) {
		this.filePattern = filePattern;
	}

	@Override
	public int hashCode() {
		return Objects.hash(agentName, agentVersion, commKey, filePattern, labels, lastHeartbeat, lastModified,
				maxFilesToSearch, maxLinesInResult, maxLinesPerFile, nodeName, nodePort, nodeTimeZone, osType, parallel,
				resultSizeinKB);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		return Objects.equals(agentName, other.agentName) && Objects.equals(agentVersion, other.agentVersion)
				&& Objects.equals(commKey, other.commKey) && Objects.equals(filePattern, other.filePattern)
				&& Objects.equals(labels, other.labels) && Objects.equals(lastHeartbeat, other.lastHeartbeat)
				&& Objects.equals(lastModified, other.lastModified)
				&& Objects.equals(maxFilesToSearch, other.maxFilesToSearch)
				&& Objects.equals(maxLinesInResult, other.maxLinesInResult)
				&& Objects.equals(maxLinesPerFile, other.maxLinesPerFile) && Objects.equals(nodeName, other.nodeName)
				&& Objects.equals(nodePort, other.nodePort) && Objects.equals(nodeTimeZone, other.nodeTimeZone)
				&& Objects.equals(osType, other.osType) && Objects.equals(parallel, other.parallel)
				&& Objects.equals(resultSizeinKB, other.resultSizeinKB);
	}
	
	
}
