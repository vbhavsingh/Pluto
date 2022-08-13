package com.log.server.data.db.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.log.server.data.db.entity.pk.RoleMappingPK;


@Entity
@Table(name = "USER_ROLE_MAP")
@IdClass(RoleMappingPK.class)
public class UserRoleMap implements Serializable{

	private static final long serialVersionUID = 1225308355881130015L;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "USERNAME")
	private UserCredential user;
	
	@Id
	@OneToOne
	@JoinColumn(name = "ROLENAME")
	private UserRole role;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "CREATED_TIME", insertable = true, updatable = true)
	@CreationTimestamp
	private Timestamp createdTime;
	
	

	public UserRoleMap() {
		super();
	}

	public UserRoleMap(UserCredential user, UserRole role) {
		super();
		this.role = role;
		this.user = user;
	}


	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
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

	public UserCredential getUser() {
		return user;
	}

	public void setUser(UserCredential user) {
		this.user = user;
	}


//	public String getRoleName() {
//		return roleName;
//	}
//
//	public void setRoleName(String roleName) {
//		this.roleName = roleName;
//	}

	
}
