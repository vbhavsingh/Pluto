/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.server.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.log.server.model.graphics.GraphicsData;
import com.log.server.util.Utilities;

/**
 *
 * @author Vaibhav Pratap Singh
 */
public class ViewResultModel implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -920618608601248587L;
	private SearchInput input;
    private List<LogRecordModel> datedList;
    private List<LogRecordModel> unDatedList;
    private Map<String, List<String>> nodeMessages = new HashMap<String, List<String>>();
    private Map<String,String> faultNodeMessages;
    private GraphicsData graphicsData;
    private int serversSearched;
    private int filesWithMatchCount;
    private int linesFetched;
    private int datedTextLines;
    private int unDatedTextLines;
    private int searchedFilesCount; 
    private boolean maxFileLimitBreached=false;
    private List<String> allowedTimeZoneList;
    private String[] searchKeyword;
    private String[] madatorySearchKeyword;
    private String explicitErrorMessage;
    private long searchTime;

    public ViewResultModel() {
    }
    
    

    public ViewResultModel(SearchInput input) {
		super();
		this.input = input;
	}



	public ViewResultModel(List<LogRecordModel> datedList, List<LogRecordModel> unDatedList) {
        this.datedList = datedList;
        this.unDatedList = unDatedList;
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

    public int getServersSearched() {
        return serversSearched;
    }

    public void setServersSearched(int serversSearched) {
        this.serversSearched = serversSearched;
    }

    public int getFilesWithMatchCount() {
        return filesWithMatchCount;
    }

    public void setFilesWithMatchCount(int filesSearched) {
        this.filesWithMatchCount = filesSearched;
    }

    public int getLinesFetched() {
        return linesFetched;
    }

    public void setLinesFetched(int linesFetched) {
        this.linesFetched = linesFetched;
    }

    public int getDatedTextLines() {
        return datedTextLines;
    }

    public void setDatedTextLines(int datedTextLines) {
        this.datedTextLines = datedTextLines;
    }

    public int getUnDatedTextLines() {
        return unDatedTextLines;
    }

    public void setUnDatedTextLines(int unDatedTextLines) {
        this.unDatedTextLines = unDatedTextLines;
    }   

    public Map<String, String> getFaultNodeMessages() {
		return faultNodeMessages;
	}



	public void setFaultNodeMessages(Map<String, String> faultNodeMessages) {
		this.faultNodeMessages = faultNodeMessages;
	}

	public SearchInput getInput() {
        return input;
    }

    public void setInput(SearchInput input) {
        this.input = input;
    }
    

	public List getAllowedTimeZoneList() {
        if (this.allowedTimeZoneList == null) {
            this.allowedTimeZoneList = Utilities.getAllowedTimeZoneList();
        }
        return allowedTimeZoneList;
    }

    public void setAllowedTimeZoneList(List allowedTimeZoneList) {
        if (this.allowedTimeZoneList == null) {
            getAllowedTimeZoneList();
        }
    }

    public String[] getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String[] searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public String[] getMadatorySearchKeyword() {
        return madatorySearchKeyword;
    }

    public void setMadatorySearchKeyword(String[] madatorySearchKeyword) {
        this.madatorySearchKeyword = madatorySearchKeyword;
    }
    
    public boolean isMaxFileLimitBreached() {
		return maxFileLimitBreached;
	}

	public void setMaxFileLimitBreached(boolean maxFileLimitBreached) {
		this.maxFileLimitBreached = maxFileLimitBreached;
	}
	

	public int getSearchedFilesCount() {
		return searchedFilesCount;
	}

	public void setSearchedFilesCount(int searchableFiles) {
		this.searchedFilesCount = searchableFiles;
	}
	

	public String getExplicitErrorMessage() {
		return explicitErrorMessage;
	}

	public void setExplicitErrorMessage(String explicitErrorMessage) {
		this.explicitErrorMessage = explicitErrorMessage;
	}

	public long getSearchTime() {
		return searchTime;
	}

	public void setSearchTime(long searchTime) {
		this.searchTime = searchTime;
	}


	public GraphicsData getGraphicsData() {
		return graphicsData;
	}



	public void setGraphicsData(GraphicsData graphicsData) {
		this.graphicsData = graphicsData;
	}



	@Override
	public String toString() {
		return "ViewResultModel [input=" + input + ", datedList=" + datedList + ", unDatedList=" + unDatedList + ", nodeMessages=" + nodeMessages + ", faultNodeMessages="
				+ faultNodeMessages + ", serversSearched=" + serversSearched + ", filesWithMatchCount=" + filesWithMatchCount + ", linesFetched=" + linesFetched + ", datedTextLines="
				+ datedTextLines + ", unDatedTextLines=" + unDatedTextLines + ", searchedFilesCount=" + searchedFilesCount + ", maxFileLimitBreached=" + maxFileLimitBreached
				+ ", allowedTimeZoneList=" + allowedTimeZoneList + ", searchKeyword=" + Arrays.toString(searchKeyword) + ", madatorySearchKeyword="
				+ Arrays.toString(madatorySearchKeyword) + ", explicitErrorMessage=" + explicitErrorMessage +", searchTime="+searchTime +"]";
	}

	public String info() {
		return "ViewResultModel [input=" + input + ", nodeMessages=" + nodeMessages + ", serversSearched=" + serversSearched + ", filesWithMatchCount=" + filesWithMatchCount
				+ ", linesFetched=" + linesFetched + ", datedTextLines=" + datedTextLines + ", unDatedTextLines=" + unDatedTextLines + ", searchedFilesCount=" + searchedFilesCount
				+ ", maxFileLimitBreached=" + maxFileLimitBreached + ", explicitErrorMessage=" + explicitErrorMessage + ", searchTime="+searchTime+"]";
	}

		
	

	
}
