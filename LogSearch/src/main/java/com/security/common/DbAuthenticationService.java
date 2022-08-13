package com.security.common;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.log.server.LocalConstants;
import com.log.server.data.db.entity.UserCredential;
import com.log.server.data.db.entity.UserGroupMap;
import com.log.server.data.db.entity.UserRoleMap;
import com.log.server.data.db.service.UserDataService;
import com.log.server.data.db.service.UserGroupService;
import com.log.server.model.Group;

@Service 
class DbAuthenticationService implements UserDetailsService {
	private static final Logger  Log = LoggerFactory.getLogger(DbAuthenticationService.class);

	@Autowired
	private UserDataService userDataService;
	
	@Autowired
	private UserGroupService userGroupService;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Log.info("Login Attempt from user: {}" , username);
		LocalConstants.USER_ACCESS_LOG.info("{} : login attempt",username);
		
		UserCredential user = userDataService.getUserEntityById(username);// us.getById(username);
		
		List<UserGroupMap> groupMapList = user.getGroupMapList();
		List<UserRoleMap> roleMapList = user.getRoleMapList();

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		for(UserRoleMap roleMap:roleMapList) {
			authorities.add(new SimpleGrantedAuthority(roleMap.getRole().getRoleName()));	
		}	

		PlutoSecurityPrinicipal ud = new PlutoSecurityPrinicipal(username, user.getPassword(), authorities);
		ud.setUserDetail(userDataService.mapEntityToModelNoPassword(user));
		
		for(UserRoleMap roleMap:roleMapList) {	
			if(roleMap.getRole().getVisbible() && !roleMap.getRole().getRoleName().equals(LocalConstants.ROLE.BOT)) {
				ud.setAssignedRole(roleMap.getRole().getRoleName());
			}
		}	

		List<Group> groupList = new ArrayList<Group>();
		for (UserGroupMap g : groupMapList) {
			//groupListbyName.add(g.getGroup().getGroupName());
			groupList.add(userGroupService.mapEntityToGroup(g.getGroup()));
		}
		ud.setAssignedGroups(groupList);

		String message ="";
		if (user.getFirstName() == null) {
			message =  user.getUserName();
		} else {
			message = StringUtils.capitalize(user.getFirstName());
			if (user.getLastName()!=null && user.getLastName().length()<10){
				message=message + " "+StringUtils.capitalize(user.getLastName());
			}
		}

		ud.setSalutation(message);
		return ud;
	}

}
