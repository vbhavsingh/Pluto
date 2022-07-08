package com.log.server.data.db.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.log.server.data.db.entity.UserGroup;
import com.log.server.data.db.entity.UserGroupMap;
import com.log.server.data.db.entity.pk.GroupMappingPk;
import com.log.server.data.db.repository.UserGroupMapRepository;
import com.log.server.data.db.repository.UserGroupRepository;
import com.log.server.model.Group;

@Component
public class UserGroupService {

	@Autowired
	private UserGroupRepository groupRepository;
	
	@Autowired
	private UserGroupMapRepository groupMapRepository;
	
	public void createGroup(Group group) {
		UserGroup entity = mapGroupToEntity(group);
		groupRepository.save(entity);
	};
	
	@Transactional
	public void updateGroup(Group group) {
		groupRepository.updateGroup(group.getName(),group.getOldName(), group.getDescription(), group.getModifiedBy());
		groupMapRepository.updateUserGroupMapping(group.getName(),group.getOldName());
	};
	
	public void deleteGroup(String groupName) {
		groupRepository.deleteById(groupName);
	}
	
	public List<String> getAllUsersInGroup(String groupName) {
		Optional<UserGroup> optionalGroup = groupRepository.findById(groupName);
		
		if(optionalGroup.isPresent()) {
			 return optionalGroup.get().getUserGoupMapList().stream()
					 .map(UserGroupMap::getId)
					 .map(GroupMappingPk::getUserName)
					 .collect(Collectors.toList());
		}
		
		return null;
	}
	
	public List<Group> getAllGroups() {
		List<UserGroup> groupEntityList = groupRepository.findAll();
		return groupEntityList.stream().map(entity -> mapEntityToGroup(entity)).collect(Collectors.toList());
		
	}
	
	public List<Group> getAllGroups(List<String> groupNameList) {
		List<UserGroup> groupEntityList = groupRepository.findAllById(groupNameList);
		return groupEntityList.stream().map(entity -> mapEntityToGroup(entity)).collect(Collectors.toList());
		
	}
	
	
	public Group mapEntityToGroup(UserGroup entity) {
		Group group = new Group(entity.getGroupName());
		group.setCreatedBy(entity.getCreatedBy());
		group.setCreatedDate(entity.getCreatedTime());
		group.setDescription(entity.getDescription());
		group.setModifiedBy(entity.getModifiedBy());
		group.setModifiedDate(entity.getLastModified());
		
		List<String> userList = entity.getUserGoupMapList().stream()
				.map(UserGroupMap::getId)
				.map(GroupMappingPk::getUserName)
				.collect(Collectors.toList());
		
		group.setUsers(userList);
		return group;
	}
	
	public UserGroup mapGroupToEntity(Group group) {
		UserGroup entity = new UserGroup();
		entity.setCreatedBy(group.getCreatedBy());
		entity.setDescription(group.getDescription());
		entity.setGroupName(group.getName());
		return entity;
	}

}
