/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.server.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


/**
 *
 * @author Vaibhav Singh
 */
public class UserCredentialsModel {

	@NotEmpty(message = "Mandatory Field")
	@Size(min = 5, max = 8, message = "User Name must be 5 to 6 charcters.")
	private String username;
	private String lastname;
	private String firstname;
	private String password;
	@NotEmpty(message = "Mandatory Field")
	@Email(message = "Invalid mail format")
	private String email;
	private String createdBy;
	private Date createdDate;
	private Date modifiedDate;
	private String role;
	private String[] groups;
	private List<String> myNodes;
	private Boolean robot = false;

	public UserCredentialsModel(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public UserCredentialsModel() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String[] getGroups() {
		return groups;
	}

	public void setGroups(String[] groups) {
		this.groups = groups;
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

	public List<String> getMyNodes() {
		return myNodes;
	}

	public void setMyNodes(List<String> myNodes) {
		this.myNodes = myNodes;
	}
	

	public Boolean isRobot() {
		if(this.robot == null)
			return false;
		return robot;
	}

	public void setRobot(Boolean robot) {
		this.robot = robot;
	}

	@Override
	public String toString() {
		return "UserCredentials [username=" + username + ", lastname=" + lastname + ", firstname=" + firstname + ", password=******"  + ", email=" + email + ", createdBy="
				+ createdBy + ", createdDate=" + createdDate + ", modifiedDate=" + modifiedDate + ", role=" + role + ", groups=" + Arrays.toString(groups) + ", myNodes=******]";
	}

	

}
