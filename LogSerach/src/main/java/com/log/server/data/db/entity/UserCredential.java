package com.log.server.data.db.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "CREDENTIALS_USER")
public class UserCredential implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 146821982707537773L;

	@Id
	@Column(name = "USERNAME")
	private String userName;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "CREATED_BY")
	private String createbBy;

	@Column(name = "CREATED_TIME", insertable = true, updatable = false)
	@CreationTimestamp
	private Timestamp createdTime;

	@Column(name = "LAST_MODIFIED", insertable = false, updatable = true)
	@UpdateTimestamp
	private Timestamp lastModified;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserGroupMap> groupMapList;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserRoleMap> roleMapList;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserNodeMap> nodeMappingList;
	
	@OneToMany(mappedBy = "user")
	private List<SearchAssistant> serachAssistantList;
	

	public void addGroupMapping(String groupName, String createdBy) {
		if(groupMapList == null) {
			groupMapList = new ArrayList<>();
		}
		UserGroupMap mapping = new UserGroupMap(this,new UserGroup(groupName));
		mapping.setCreatedBy(createdBy);
		groupMapList.add(mapping);
	}
	
	public void addRoleMapping(String roleName,  String createdBy) {
		if(roleMapList == null) {
			roleMapList = new ArrayList<>();
		}
		UserRole role = new UserRole(roleName);
		UserRoleMap mapping = new UserRoleMap(this, role);
		mapping.setCreatedBy(createdBy);
		roleMapList.add(mapping);
	}

	public UserCredential() {
		super();
	}

	public UserCredential(String userName) {
		super();
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCreatebBy() {
		return createbBy;
	}

	public void setCreatebBy(String createbBy) {
		this.createbBy = createbBy;
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
	

	public List<UserGroupMap> getGroupMapList() {
		return groupMapList;
	}

	public void setGroupMapList(List<UserGroupMap> groupMapList) {
		this.groupMapList = groupMapList;
	}

	public List<UserRoleMap> getRoleMapList() {
		return roleMapList;
	}

	public void setRoleMapList(List<UserRoleMap> roleMapList) {
		this.roleMapList = roleMapList;
	}

	public List<SearchAssistant> getSerachAssistantList() {
		return serachAssistantList;
	}

	public void setSerachAssistantList(List<SearchAssistant> serachAssistantList) {
		this.serachAssistantList = serachAssistantList;
	}
	

	public List<UserNodeMap> getNodeMappingList() {
		return nodeMappingList;
	}

	public void setNodeMappingList(List<UserNodeMap> nodeMappingList) {
		this.nodeMappingList = nodeMappingList;
	}

	@Override
	public int hashCode() {
		return Objects.hash(createbBy, createdTime, email, firstName, groupMapList, lastModified, lastName,
				nodeMappingList, password, roleMapList, serachAssistantList, userName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserCredential other = (UserCredential) obj;
		return Objects.equals(createbBy, other.createbBy) && Objects.equals(createdTime, other.createdTime)
				&& Objects.equals(email, other.email) && Objects.equals(firstName, other.firstName)
				&& Objects.equals(groupMapList, other.groupMapList) && Objects.equals(lastModified, other.lastModified)
				&& Objects.equals(lastName, other.lastName) && Objects.equals(nodeMappingList, other.nodeMappingList)
				&& Objects.equals(password, other.password) && Objects.equals(roleMapList, other.roleMapList)
				&& Objects.equals(serachAssistantList, other.serachAssistantList)
				&& Objects.equals(userName, other.userName);
	}

	@Override
	public String toString() {
		return "UserCredential [userName=" + userName + ", password= ******"  + ", email=" + email + ", firstName="
				+ firstName + ", lastName=" + lastName + ", createbBy=" + createbBy + ", createdTime=" + createdTime
				+ ", lastModified=" + lastModified + ", groupMapList=" + groupMapList + ", roleMapList=" + roleMapList
				+ ", nodeMappingList=" + nodeMappingList + ", serachAssistantList=" + serachAssistantList + "]";
	}

	

}
