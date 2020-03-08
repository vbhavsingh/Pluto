package com.log.server.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;

public class AgentLabelMapModel {
	@NotEmpty
	private String nodeName;
	@NotEmpty
	private String labelName;

	
	public AgentLabelMapModel() {
		super();
	}

	public AgentLabelMapModel(String nodeName, String labelName) {
		super();
		this.nodeName = nodeName;
		this.labelName = labelName;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
