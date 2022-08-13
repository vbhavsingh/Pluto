package com.log.server.data.db.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.log.server.LocalConstants;
import com.log.server.data.db.entity.UserCredential;
import com.log.server.data.db.entity.UserRole;
import com.log.server.data.db.entity.UserRoleMap;
import com.log.server.data.db.repository.UserRepository;
import com.log.server.data.db.repository.UserRoleMappingRepository;
import com.log.server.data.db.repository.UserRoleRepository;
import com.log.server.model.Role;
import com.security.common.PlutoSecurityPrinicipal;

@Component
public class UserRoleService {

	@Autowired
	UserRoleRepository roleRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserRoleMappingRepository userRoleMappingRepository;
	
	public boolean isRoboRolePresent() {
		Optional<UserRole> optionalUserRole =  roleRepository.findById(LocalConstants.ROLE.BOT);
		return optionalUserRole.isPresent();
	}
	
	public UserRole getRoleByRoleName(String roleName) {
		return roleRepository.findById(roleName).get();
	}
	
	public void createRole(Role role) {
		roleRepository.save(mapRoleToEntity(role));
	}
	
	public void createUserRoleMapping(String userName, String roleName) {
		UserRoleMap userRoleMapEntity = new UserRoleMap();
		userRoleMapEntity.setRole(new UserRole(roleName));
		userRoleMapEntity.setUser(new UserCredential(userName));
		userRoleMappingRepository.save(userRoleMapEntity);
	}
	
	public List<Role> getRoleListByAuthority(PlutoSecurityPrinicipal security){
		String hasRole = security.getAssignedRole();
		if (LocalConstants.ROLE.ROLES.ADMIN.roleName.equals(hasRole)) {
			return roleRepository.findAll().stream()
					.map(entity -> mapEntityToRole(entity))
					.collect(Collectors.toList());			
		}
		List<String> roleList = new ArrayList<String>();
		roleList.add(LocalConstants.ROLE.ROLES.ADMIN.roleName);
		if (LocalConstants.ROLE.ROLES.GROUP_ADMIN.roleName.equals(hasRole)) {
			return roleRepository.findAllById(roleList).stream()
					.map(entity -> mapEntityToRole(entity))
					.collect(Collectors.toList());
		}
		roleList.add(LocalConstants.ROLE.ROLES.GROUP_ADMIN.roleName);
		return roleRepository.findAllById(roleList).stream()
				.map(entity -> mapEntityToRole(entity))
				.collect(Collectors.toList());
	}
	
	public UserRole mapRoleToEntity(Role role) {
		UserRole entity = new UserRole();
		entity.setRoleName(role.getRolename());
		entity.setDesciption(role.getDescription());
		entity.setVisbible(role.isVisible());
		return entity;
	}
	
	public Role mapEntityToRole(UserRole entity) {
		Role role = new Role();
		role.setRolename(entity.getRoleName());
		role.setDescription(entity.getDesciption());
		role.setVisible(entity.getVisbible());
		return role;
	}

}
