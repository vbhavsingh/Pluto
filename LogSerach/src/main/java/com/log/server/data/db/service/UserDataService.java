package com.log.server.data.db.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.log.server.LocalConstants;
import com.log.server.data.db.entity.Node;
import com.log.server.data.db.entity.UserCredential;
import com.log.server.data.db.entity.UserGroup;
import com.log.server.data.db.entity.UserGroupMap;
import com.log.server.data.db.entity.UserNodeMap;
import com.log.server.data.db.entity.UserRole;
import com.log.server.data.db.entity.UserRoleMap;
import com.log.server.data.db.repository.NodeRepository;
import com.log.server.data.db.repository.UserGroupRepository;
import com.log.server.data.db.repository.UserNodeMapRepository;
import com.log.server.data.db.repository.UserRepository;
import com.log.server.model.Group;
import com.log.server.model.UserCredentialsModel;
import com.security.common.PlutoSecurityPrinicipal;

@Service
public class UserDataService {

	private static final Logger Log = LoggerFactory.getLogger(UserDataService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserGroupRepository groupRepository;

	@Autowired
	private UserNodeMapRepository userNodeMapRepository;

	@Autowired
	private NodeRepository nodeRepository;

	public void saveUser(UserCredential user) {
		userRepository.save(user);
	}

	public void saveUser(UserCredentialsModel user) {
		UserCredential entity = mapUserToEntity(user, new UserCredential());
		userRepository.save(entity);
	}

	public void delteUser(String userName) {
		userRepository.deleteById(userName);
	}

	public UserCredentialsModel getUserById(String userName) {
		return mapEntityToModel(userRepository.findById(userName).get());
	}

	public List<UserCredentialsModel> getAllUsers() {
		return userRepository.findAll().stream().map(e -> mapEntityToModel(e)).collect(Collectors.toList());
	}

	public UserCredential getUserEntityById(String userName) {
		return userRepository.findById(userName).get();
	}

	public void updateUser(UserCredentialsModel user) {
		Optional<UserCredential> optionalEntity = userRepository.findById(user.getUsername());
		if (optionalEntity.isPresent()) {
			UserCredential entity = mapUserToEntity(user, optionalEntity.get());
			userRepository.save(entity);
		}
	}

	public void createUserNodeMapping(UserCredentialsModel user, String nodeName) {
		List<String> nodeNameList = new ArrayList<String>();
		nodeNameList.add(nodeName);
		createUserNodeMapping(user, nodeNameList);
	}

	public void createUserNodeMapping(UserCredentialsModel user, List<String> nodeName) {
		Optional<UserCredential> optionalEntity = userRepository.findById(user.getUsername());
		List<Node> nodeList = nodeRepository.findAllById(nodeName);
		if (optionalEntity.isPresent()) {
			UserCredential userEntity = optionalEntity.get();
			for (Node node : nodeList) {
				userEntity.getNodeMappingList().add(new UserNodeMap(userEntity, node));
			}
			userRepository.save(userEntity);
		} else {
			Log.error("User {} or Node {} doesn't exist", user.getUsername(), nodeName);
		}
	}

	public void deleteUserNodeMapping(String userName, String nodeName) {
		userNodeMapRepository.delete(new UserNodeMap(userName, nodeName));
	}

	public List<UserCredentialsModel> getUserListByAuthority(PlutoSecurityPrinicipal security) {
		if (LocalConstants.ROLE.ROLES.ADMIN.roleName.equals(security.getAssignedRole())) {
			return userRepository.findAll().stream().map(entity -> mapEntityToModelNoPassword(entity))
					.collect(Collectors.toList());
		} else if (LocalConstants.ROLE.ROLES.GROUP_ADMIN.roleName.equals(security.getAssignedRole())) {
			List<String> assignedGroupNames = security.getAssignedGroups().stream().map(Group::getName)
					.collect(Collectors.toList());

			groupRepository.findAllById(assignedGroupNames);
			List<UserGroup> groupEntityList = groupRepository.findAllById(assignedGroupNames);

			List<UserCredentialsModel> returnList = groupEntityList.stream().map(UserGroup::getUserGoupMapList)
					.flatMap(List::stream).map(UserGroupMap::getUser).map(entity -> mapEntityToModelNoPassword(entity))
					.collect(Collectors.toList());
			return returnList;
		}
		Optional<UserCredential> optionalEntity = userRepository.findById(security.getUsername());
		if (optionalEntity.isPresent()) {
			List<UserCredentialsModel> returnList = new ArrayList<UserCredentialsModel>();
			returnList.add(mapEntityToModelNoPassword(optionalEntity.get()));
			return returnList;
		}
		return null;
	}

	private UserCredential mapUserToEntity(UserCredentialsModel userModel, UserCredential userEntity) {
		// entity.setUserName(user.getUsername());
		userEntity.setEmail(userModel.getEmail());
		userEntity.setFirstName(userModel.getFirstname());
		userEntity.setLastName(userModel.getLastname());
		userEntity.setCreatebBy(userModel.getCreatedBy());

		if (StringUtils.hasText(userModel.getPassword())) {
			userEntity.setPassword(userModel.getPassword());
		}

		List<UserGroupMap> entityGroupMappingsList = userEntity.getGroupMapList() == null
				? new ArrayList<UserGroupMap>()
				: userEntity.getGroupMapList();

		if (userModel.getGroups() != null) {

			List<UserGroupMap> removedGroupMapList = entityGroupMappingsList.stream()
					.filter(gMap -> !Arrays.asList(userModel.getGroups()).contains(gMap.getGroup().getGroupName()))
					.collect(Collectors.toList());

			entityGroupMappingsList.removeAll(removedGroupMapList);

			List<UserGroupMap> addedgroupMapList = Arrays.stream(userModel.getGroups())
					.filter(gpName -> userEntity.getGroupMapList().stream()
							.filter(gp -> gp.getGroup().getGroupName().equals(gpName)).count() == 0)
					.map(gpName -> new UserGroupMap(userEntity, new UserGroup(gpName))).collect(Collectors.toList());

			entityGroupMappingsList.addAll(addedgroupMapList);

			userEntity.setGroupMapList(addedgroupMapList);

		}

		List<UserRoleMap> roleMappingsList = userEntity.getRoleMapList() == null ? new ArrayList<UserRoleMap>()
				: userEntity.getRoleMapList();

		Optional<UserRoleMap> entityRobotRole = roleMappingsList.stream()
				.filter(roleMap -> roleMap.getRole().getRoleName().equals(LocalConstants.ROLE.BOT)).findFirst();

		Optional<UserRoleMap> entityRole = roleMappingsList.stream()
				.filter(roleMap -> !roleMap.getRole().getRoleName().equals(LocalConstants.ROLE.BOT)).findFirst();

		if (LocalConstants.ROLE.ROLES.isValidRole(userModel.getRole())) {
			if (!entityRole.isPresent() && entityRole.get().getRole().getRoleName().equals(userModel.getRole())) {
				roleMappingsList.remove(entityRole.get());
			}
			roleMappingsList.add(new UserRoleMap(userEntity, new UserRole(userModel.getRole())));
		}

		// ROBOTIC Role is an extra record in Table 'USER_ROLE_MAP'
		// Check if robot role need to be added
		if (userModel.isRobot()) {
			// if ROBOT role in not in Table but UI object has it, add ROBOT role
			if (!entityRobotRole.isPresent()) {
				UserRoleMap robotRole = new UserRoleMap(userEntity, new UserRole(LocalConstants.ROLE.BOT));
				roleMappingsList.add(robotRole);
				userEntity.setRoleMapList(roleMappingsList);
			}
		} else {
			// if ROBOT role in not in Table but UI object has it, add ROBOT role
			// Check if robot role need to be removed
			if (entityRobotRole.isPresent()) {
				roleMappingsList.remove(entityRobotRole.get());
			}

		}

		return userEntity;
	}

	public UserCredentialsModel mapEntityToModelNoPassword(UserCredential entity) {
		UserCredentialsModel user = mapEntityToModel(entity);
		user.setPassword(null);
		return user;
	}

	public UserCredentialsModel mapEntityToModel(UserCredential entity) {
		if (entity == null) {
			return null;
		}
		UserCredentialsModel user = new UserCredentialsModel();
		user.setCreatedBy(entity.getCreatebBy());
		user.setCreatedDate(entity.getCreatedTime());
		user.setEmail(entity.getEmail());
		user.setFirstname(entity.getFirstName());

		String[] gpArray = entity.getGroupMapList().stream().map(UserGroupMap::getGroup).map(UserGroup::getGroupName)
				.toArray(String[]::new);
		user.setGroups(gpArray);

		user.setLastname(entity.getLastName());
		user.setModifiedDate(entity.getLastModified());
		user.setPassword(entity.getPassword());

		boolean isRobot = entity.getRoleMapList().stream()
				.anyMatch(roleMap -> roleMap.getRole().getRoleName().equals(LocalConstants.ROLE.BOT));
		user.setRobot(isRobot);

		Optional<UserRoleMap> role = entity.getRoleMapList().stream()
				.filter(roleMap -> !roleMap.getRole().getRoleName().equals(LocalConstants.ROLE.BOT)).findAny();
		role.ifPresent(r -> user.setRole(r.getRole().getRoleName()));

		user.setUsername(entity.getUserName());

		return user;
	}

}
