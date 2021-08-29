package com.security.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.log.server.LocalConstants;
import com.log.server.data.db.Dao;
import com.log.server.model.Group;
import com.log.server.model.Role;
import com.log.server.model.UserCredentials;

@Service 
class DbAuthenticationService implements UserDetailsService {
	private static final Logger  Log = LoggerFactory.getLogger(DbAuthenticationService.class);

	@Autowired
	private Dao dao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Log.info("Login Attempt from user: {}" , username);
		LocalConstants.USER_ACCESS_LOG.info("{} : login attempt",username);
		UserCredentials user = dao.getUserProfileForAuthetication(username);
		List<Group> groupList = dao.getAssignedGroupsForUser(username);
		List<Role> roles = dao.getAssignedRoleForUser(username);

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		for(Role role:roles) {
			authorities.add(new SimpleGrantedAuthority(role.getRolename()));	
		}	

		PlutoSecurityPrinicipal ud = new PlutoSecurityPrinicipal(username, user.getPassword(), authorities);
		
		for(Role role:roles) {	
			if(role.isVisible()) {
				ud.setAssignedRole(role.getRolename());
			}
		}	

		List<String> groupListbyName = new ArrayList<String>();
		for (Group g : groupList) {
			groupListbyName.add(g.getName());
		}
		ud.setAssignedGroups(groupListbyName);

		String message ="";
		if (user.getFirstname() == null) {
			message =  user.getUsername();
		} else {
			message = WordUtils.capitalize(user.getFirstname());
			if (user.getLastname()!=null && user.getLastname().length()<10){
				message=message + " "+WordUtils.capitalize(user.getLastname());
			}
		}

		ud.setSalutation(message);
		return ud;
	}

}
