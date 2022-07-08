package com.log.server.data.db.entity.pk;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class NodeMappingPK implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3325353039234883041L;

	@Column(name = "USERNAME")
	private String userName;
	
	@Column(name = "NODE_NAME")
	private String nodeName;
	
	public NodeMappingPK() {
		super();
	}

	public NodeMappingPK(String userName, String nodeName) {
		super();
		this.userName = userName;
		this.nodeName = nodeName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(nodeName, userName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NodeMappingPK other = (NodeMappingPK) obj;
		return Objects.equals(nodeName, other.nodeName) && Objects.equals(userName, other.userName);
	}
	
	
	
}
