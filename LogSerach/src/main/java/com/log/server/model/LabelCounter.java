package com.log.server.model;

public class LabelCounter {

	private String labelName;
	
	private int nodeCount;
	

	public LabelCounter() {
		super();
	}

	public LabelCounter(String labelName, int nodeCount) {
		super();
		this.labelName = labelName;
		this.nodeCount = nodeCount;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public int getNodeCount() {
		return nodeCount;
	}

	public void setNodeCount(int nodeCount) {
		this.nodeCount = nodeCount;
	}
	
	
	
}
