package com.security.common;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.log.server.model.Group;

public class AppUserDetails extends User {

	private static final long serialVersionUID = -1973944465509090570L;
	
	private List<Group> groupList;
	
	
	public AppUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}


	public List<Group> getGroupList() {
		return groupList;
	}


	public void setGroupList(List<Group> groupList) {
		this.groupList = groupList;
	}
	
	

}
