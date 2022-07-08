package com.log.server.data.db.entity.pk;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class GroupMappingPk implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7400091093709372369L;

	@Column(name = "USERNAME")
	private String userName;
	
	@Column(name= "GROUPNAME")
	private String groupName;
	

	public GroupMappingPk() {
		super();
	}

	public GroupMappingPk(String userName, String groupName) {
		super();
		this.userName = userName;
		this.groupName = groupName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(groupName, userName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GroupMappingPk other = (GroupMappingPk) obj;
		return Objects.equals(groupName, other.groupName) && Objects.equals(userName, other.userName);
	}
	
	
}
