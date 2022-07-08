package com.log.server.data.db.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "CREDENTIALS_GROUP")
public class UserGroup implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 365150326274875789L;

	@Id
	@Column(name = "GROUPNAME")
	private String groupName;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name = "CREATED_TIME", insertable = true, updatable = false)
	@CreationTimestamp
	private Timestamp createdTime;
	
	@Column(name = "LAST_MODIFIED", insertable = false, updatable = true)
	@UpdateTimestamp
	private Timestamp lastModified;
	
	@OneToMany(mappedBy = "group")
	private List<UserGroupMap> userGoupMapList;
	
	public UserGroup() {
		super();
	}

	public UserGroup(String groupName) {
		super();
		this.groupName = groupName;
	}
	
	public UserGroup(String groupName, String createdBy) {
		super();
		this.groupName = groupName;
		this.createdBy = createdBy;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public Timestamp getLastModified() {
		return lastModified;
	}

	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<UserGroupMap> getUserGoupMapList() {
		return userGoupMapList;
	}

	public void setUserGoupMapList(List<UserGroupMap> userGoupMapList) {
		this.userGoupMapList = userGoupMapList;
	}

	@Override
	public String toString() {
		return "UserGroup [groupName=" + groupName + ", createdBy=" + createdBy + ", description=" + description
				+ ", modifiedBy=" + modifiedBy + ", createdTime=" + createdTime + ", lastModified=" + lastModified
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(createdBy, createdTime, description, groupName, lastModified, modifiedBy);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserGroup other = (UserGroup) obj;
		return Objects.equals(createdBy, other.createdBy) && Objects.equals(createdTime, other.createdTime)
				&& Objects.equals(description, other.description) && Objects.equals(groupName, other.groupName)
				&& Objects.equals(lastModified, other.lastModified) && Objects.equals(modifiedBy, other.modifiedBy);
	}
	
}
