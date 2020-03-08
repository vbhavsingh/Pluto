/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.server.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.log.server.LocalConstants;
import com.log.server.util.Utilities;

/**
 *
 * @author Vaibhav Pratap Singh
 */
public class SearchInput implements Serializable{

	private static final long serialVersionUID = -4502884456791312086L;

	private String logPathPatterns;

	private String logFileNamePtterns;

	private String search;

	private String searchOnLabels;

	private String sessionId;

	private String userName;

	private String viewTz;

	private int paginationIndex = LocalConstants.DEFAULT_PAGE_LENGTH;

	private int pageLength = 100;
	
	private String fromDateTime;
	
	private String toDateTime;

	private String scrollType;
	
	private String medium;

	public String getKey() {
		return Utilities.trim(this.search).toLowerCase() + ":" + 
			   Utilities.trim(this.logPathPatterns).toLowerCase() + ":" + 
			   Utilities.trim(this.logFileNamePtterns).toLowerCase()+ ":" + 
			   Utilities.trim(this.searchOnLabels).toLowerCase() + ":" + 
			   Utilities.trim(sessionId)+":" + 
			   Utilities.trim(fromDateTime)+":" + 
			   Utilities.trim(fromDateTime)+":"
			   +this.viewTz;
	}
	
	public boolean isTimedSearch() {
		if(fromDateTime ==null || fromDateTime.isEmpty()) {
			return false;
		}
		if(toDateTime ==null || toDateTime.isEmpty()) {
			return false;
		}
		return true;
	}

	public String getLogPathPatterns() {
		return logPathPatterns;
	}

	public void setLogPathPatterns(String logPathPatterns) {
		this.logPathPatterns = logPathPatterns;
	}

	public String getLogFileNamePtterns() {
		return logFileNamePtterns;
	}

	public void setLogFileNamePtterns(String logFileNamePtterns) {
		this.logFileNamePtterns = logFileNamePtterns;
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

	public String getViewTz() {
		return viewTz;
	}

	public void setViewTz(String viewTz) {
		this.viewTz = viewTz;
	}

	public int getPaginationIndex() {
		return paginationIndex;
	}

	public int getPageLength() {
		return pageLength;
	}

	public void setPageLength(int pageLength) {
		this.pageLength = pageLength;
	}

	public String getScrollType() {
		return scrollType;
	}

	public void setScrollType(String scrollType) {
		this.scrollType = scrollType;
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

	public String getMedium() {
		return medium;
	}

	public void setMedium(String medium) {
		this.medium = medium;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
