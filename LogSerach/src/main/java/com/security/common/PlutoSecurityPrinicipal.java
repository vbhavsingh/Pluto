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

	private List<String> assignedGroups;

	private String assignedRole;

	private List<String> nodesAllowed;

	private String salutation;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public List<String> getAssignedGroups() {
		return assignedGroups;
	}

	public void setAssignedGroups(List<String> assignedGroups) {
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

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
