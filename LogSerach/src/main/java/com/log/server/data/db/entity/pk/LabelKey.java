package com.log.server.data.db.entity.pk;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class LabelKey implements Serializable{


	private static final long serialVersionUID = 7231891742881758080L;

	@Column(name = "NODE_NAME")
	private String nodeName;
	
	@Column(name= "LABEL_NAME")
	private String labelName;

	public LabelKey() {
		super();
	}

	public LabelKey(String nodeName, String labelName) {
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
	public int hashCode() {
		return Objects.hash(labelName, nodeName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LabelKey other = (LabelKey) obj;
		return Objects.equals(labelName, other.labelName) && Objects.equals(nodeName, other.nodeName);
	}

	@Override
	public String toString() {
		return "LabelKey [nodeName=" + nodeName + ", labelName=" + labelName + "]";
	}
	
	
	
}
