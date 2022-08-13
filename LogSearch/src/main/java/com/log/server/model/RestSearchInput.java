package com.log.server.model;

import com.log.server.LocalConstants;

public class RestSearchInput {

	private String logPathPatterns;

	private String logFileNamePatterns;

	private String search;

	private String searchOnLabels;
	
	private String fromDateTime;
	
	private String toDateTime;
	
	private String viewTz;
	
	public SearchInput getSearchInput() {
		SearchInput input = new SearchInput();
		input.setLogPathPatterns(this.logPathPatterns);
		input.setLogFileNamePtterns(this.logFileNamePatterns);
		input.setSearch(this.search);
		input.setSearchOnLabels(this.searchOnLabels);
		input.setFromDateTime(this.fromDateTime);
		input.setToDateTime(this.toDateTime);
		input.setViewTz(this.viewTz);
		input.setMedium(LocalConstants.REST_METHOD);
		return input;
	}

	public String getLogPathPatterns() {
		return logPathPatterns;
	}

	public void setLogPathPatterns(String logPathPatterns) {
		this.logPathPatterns = logPathPatterns;
	}

	public String getLogFileNamePtterns() {
		return logFileNamePatterns;
	}

	public void setLogFileNamePtterns(String logFileNamePtterns) {
		this.logFileNamePatterns = logFileNamePtterns;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getSearchOnLabels() {
		return searchOnLabels;
	}

	public void setSearchOnLabels(String searchOnLabels) {
		this.searchOnLabels = searchOnLabels;
	}

	public String getFromDateTime() {
		return fromDateTime;
	}

	public void setFromDateTime(String fromDateTime) {
		this.fromDateTime = fromDateTime;
	}

	public String getToDateTime() {
		return toDateTime;
	}

	public void setToDateTime(String toDateTime) {
		this.toDateTime = toDateTime;
	}
    
	public String getViewTz() {
		return viewTz;
	}

	public void setViewTz(String viewTz) {
		this.viewTz = viewTz;
	}


	
}
