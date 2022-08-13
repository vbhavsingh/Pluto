/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.server.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotEmpty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 *
 * @author Vaibhav Singh
 */
public class Group implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7347433896020080606L;
	@NotEmpty
	private String name;
	@NotEmpty
	private String description;
	private String oldName;
	private String createdBy;
	private Date createdDate;
	private Date modifiedDate;
	private String modifiedBy;
	private List<String> users;
	
	public Group(@NotEmpty String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	public List<String> getUsers() {
		return users;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	@Override
	public int hashCode() {
		return Objects.hash(createdBy, createdDate, description, modifiedBy, modifiedDate, name, oldName, users);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Group other = (Group) obj;
		return Objects.equals(createdBy, other.createdBy) && Objects.equals(createdDate, other.createdDate)
				&& Objects.equals(description, other.description) && Objects.equals(modifiedBy, other.modifiedBy)
				&& Objects.equals(modifiedDate, other.modifiedDate) && Objects.equals(name, other.name)
				&& Objects.equals(oldName, other.oldName) && Objects.equals(users, other.users);
	}
	
	

}
