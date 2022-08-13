package com.log.server.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Vaibhav Pratap Singh
 *
 */
public class RestSearchResultModel {

	private List<LogRecordModel> datedList;
	private List<LogRecordModel> unDatedList;
	private Map<String, List<String>> nodeMessages = new HashMap<String, List<String>>();
	private Map<String, String> faultNodeMessages;
	private int serversSearched;
	private int filesWithMatchCount;
	private int searchedFilesCount;
	private long searchTime;
	private String error;

	public RestSearchResultModel(String error) {
		this.error = error;
	}

	public RestSearchResultModel(ViewResultModel model) {
		this.datedList = model.getDatedList();
		this.unDatedList = model.getUnDatedList();
		this.nodeMessages = model.getNodeMessages();
		this.faultNodeMessages = model.getFaultNodeMessages();
		this.serversSearched = model.getServersSearched();
		this.filesWithMatchCount = model.getFilesWithMatchCount();
		this.searchedFilesCount = model.getSearchedFilesCount();
		this.searchTime = model.getSearchTime();
	}

	public List<LogRecordModel> getDatedList() {
		return datedList;
	}

	public void setDatedList(List<LogRecordModel> datedList) {
		this.datedList = datedList;
	}

	public List<LogRecordModel> getUnDatedList() {
		return unDatedList;
	}

	public void setUnDatedList(List<LogRecordModel> unDatedList) {
		this.unDatedList = unDatedList;
	}

	public Map<String, List<String>> getNodeMessages() {
		return nodeMessages;
	}

	public void setNodeMessages(Map<String, List<String>> nodeMessages) {
		this.nodeMessages = nodeMessages;
	}

	public Map<String, String> getFaultNodeMessages() {
		return faultNodeMessages;
	}

	public void setFaultNodeMessages(Map<String, String> faultNodeMessages) {
		this.faultNodeMessages = faultNodeMessages;
	}

	public int getServersSearched() {
		return serversSearched;
	}

	public void setServersSearched(int serversSearched) {
		this.serversSearched = serversSearched;
	}

	public int getFilesWithMatchCount() {
		return filesWithMatchCount;
	}

	public void setFilesWithMatchCount(int filesWithMatchCount) {
		this.filesWithMatchCount = filesWithMatchCount;
	}

	public int getSearchedFilesCount() {
		return searchedFilesCount;
	}

	public void setSearchedFilesCount(int searchedFilesCount) {
		this.searchedFilesCount = searchedFilesCount;
	}

	public long getSearchTime() {
		return searchTime;
	}

	public void setSearchTime(long searchTime) {
		this.searchTime = searchTime;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return "SearchResultModel [nodeMessages=" + nodeMessages + ", faultNodeMessages=" + faultNodeMessages + ", serversSearched=" + serversSearched + ", filesWithMatchCount="
				+ filesWithMatchCount + ", searchedFilesCount=" + searchedFilesCount + ", searchTime=" + searchTime + "]";
	}

}
