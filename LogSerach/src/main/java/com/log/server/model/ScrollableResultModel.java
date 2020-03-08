package com.log.server.model;

import java.util.List;

public class ScrollableResultModel {
	
	
	private int scrollAtIndex;
	
	private List<LogRecordModel> data;


	public int getScrollAtIndex() {
		return scrollAtIndex;
	}

	public void setScrollAtIndex(int scrollAtIndex) {
		this.scrollAtIndex = scrollAtIndex;
	}

	public List<LogRecordModel> getData() {
		return data;
	}

	public void setData(List<LogRecordModel> data) {
		this.data = data;
	}
	
	

}
