package com.log.server.data.db.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.log.server.data.db.entity.pk.GroupMappingPk;

@Entity
@Table(name = "USER_GROUP_MAP")
public class UserGroupMap implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8620860184768265436L;

	@EmbeddedId
	private GroupMappingPk id;
	
	@ManyToOne
	@MapsId("userName")
	@JoinColumn(name = "USERNAME" )
	private UserCredential user;
	
	@ManyToOne
	@MapsId("groupName")
	@JoinColumn(name = "GROUPNAME")
	private UserGroup group;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "CREATED_TIME", insertable = true, updatable = true)
	@CreationTimestamp
	private Timestamp createdTime;

	

	public UserGroupMap() {
		super();
	}

	public UserGroupMap(UserCredential user, UserGroup group) {
		this.id = new GroupMappingPk(user.getUserName(), group.getGroupName());
		this.user = user;
		this.group = group;
	}

	public GroupMappingPk getId() {
		return id;
	}

	public void setId(GroupMappingPk id) {
		this.id = id;
	}

	public UserCredential getUser() {
		return user;
	}

	public void setUser(UserCredential user) {
		this.user = user;
	}

	public UserGroup getGroup() {
		return group;
	}

	public void setGroup(UserGroup group) {
		this.group = group;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}
	
	
}
