package com.log.server.model;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class AddEditUserModel {

	private UserCredentials profile;

	private List<Role> assignableRoles;

	private List<Group> assignableGroups;
	

	public UserCredentials getProfile() {
		return profile;
	}

	public void setProfile(UserCredentials profile) {
		this.profile = profile;
	}

	public List<Role> getAssignableRoles() {
		return assignableRoles;
	}

	public void setAssignableRoles(List<Role> assignableRoles) {
		this.assignableRoles = assignableRoles;
	}

	public List<Group> getAssignableGroups() {
		return assignableGroups;
	}

	public void setAssignableGroups(List<Group> assignableGroups) {
		this.assignableGroups = assignableGroups;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
