/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.security.common;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.log.server.model.Group;
import com.log.server.model.UserCredentialsModel;

/**
 *
 * @author Vaibhav Singh
 */
public class PlutoSecurityPrinicipal extends User {

	private static final long serialVersionUID = 7504592784721477212L;

	public PlutoSecurityPrinicipal(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}
	
	private String user;

	private UserCredentialsModel userDetail;
	
	private List<Group> assignedGroups;

	private String assignedRole;

	private List<String> nodesAllowed;

	private String salutation;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public List<Group> getAssignedGroups() {
		return assignedGroups;
	}

	public void setAssignedGroups(List<Group> assignedGroups) {
		this.assignedGroups = assignedGroups;
	}

	public String getAssignedRole() {
		return assignedRole;
	}

	public void setAssignedRole(String assignedRole) {
		this.assignedRole = assignedRole;
	}

	public List<String> getNodesAllowed() {
		return nodesAllowed;
	}

	public void setNodesAllowed(List<String> nodesAllowed) {
		this.nodesAllowed = nodesAllowed;
	}

	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}
	

	public UserCredentialsModel getUserDetail() {
		return userDetail;
	}

	public void setUserDetail(UserCredentialsModel userDetail) {
		this.userDetail = userDetail;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
