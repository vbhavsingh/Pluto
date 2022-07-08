/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.server.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.log.analyzer.commons.Util;
import com.log.analyzer.commons.model.AgentCommands;
import com.log.analyzer.commons.model.AgentConfigurationModel;
import com.log.analyzer.commons.model.AgentModel;
import com.log.analyzer.commons.model.AgentRegistrationForm;
import com.log.analyzer.commons.model.AgentTerminatorRequestModel;
import com.log.server.LocalConstants;
import com.log.server.comm.http.TerminateAgent;
import com.log.server.concurrent.MapNodeWithUsers;
import com.log.server.concurrent.MapUserWithNodes;
import com.log.server.concurrent.UpdateRemoteAgent;
import com.log.server.data.db.entity.UserCredential;
import com.log.server.data.db.entity.UserRole;
import com.log.server.data.db.entity.UserRoleMap;
import com.log.server.data.db.patch.Patch2018;
import com.log.server.data.db.patch.PatchAugust2021;
import com.log.server.data.db.service.ConfigurationService;
import com.log.server.data.db.service.UserDataService;
import com.log.server.data.db.service.UserGroupService;
import com.log.server.data.db.service.UserRoleService;
import com.log.server.model.AgentLabelMapModel;
import com.log.server.model.Group;
import com.log.server.model.NodeAgentViewModel;
import com.log.server.model.Role;
import com.log.server.model.UserCredentialsModel;
import com.log.server.util.Utilities;
import com.security.common.PlutoSecurityPrinicipal;

/**
 *
 * @author Vaibhav Singh
 */
@Service
public class AdminServices {

	private final static Logger Log = LoggerFactory.getLogger(AdminServices.class);

	@Autowired
	private ApplicationContext context;

	@Autowired
	private UserDataService userDataService;

	@Autowired
	private UserGroupService userGroupService;

	@Autowired
	private UserRoleService roleService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private CommonServices commonServices;


	/**
	 * 
	 * @param node
	 */
	public void registerAgent(AgentRegistrationForm node) {
		node.setCommKey(UUID.randomUUID().toString());
		Log.trace("genertaed comm key: {} for node: {}", node.getCommKey(), node.getClientNode());
		Log.trace("saving agent into database, agent: " + node.getClientNode());
		configurationService.saveAgent(node);
		List<String> userList = configurationService.getNonMappedUsersForNode(node.getClientName());
		NodeAgentViewModel nodeModel = new NodeAgentViewModel();
		int port = Integer.parseInt(node.getClientConnectPort());
		nodeModel.setNodeName(node.getClientNode());
		nodeModel.setPort(port);
		MapNodeWithUsers mapper = new MapNodeWithUsers(userList, nodeModel);
		Thread t = new Thread(mapper);
		t.start();
	}

	/**
	 * 
	 * @return String
	 */
	public String deRegisterAgent(AgentTerminatorRequestModel killRequest) throws Exception {
		TerminateAgent terminator = new TerminateAgent();
		AgentModel agent = configurationService.getAgentModel(killRequest.getNodeName());
		Log.info("requesting termintaion of remote node: {}, for user: {} in session: {}", killRequest.getNodeName(),
				killRequest.getUser(), killRequest.getSession());
		Log.trace("requesting remote agatent termination for request : {}", killRequest);
		String result = terminator.terminate(killRequest, agent);

		if (LocalConstants.ERROR.UNREACHABLE.equals(result)) {
			Log.info("node {} is not reachable for termintation, db cleanup will be done assuming its dead",
					killRequest.getNodeName());
		}
		configurationService.deleteNode(killRequest.getNodeName());

		return result;
	}

	/**
	 * 
	 * @param node
	 * @return
	 */
	public AgentModel getRegisteredAgentdetail(AgentRegistrationForm node) {
		return configurationService.getAgentModel(node.getClientNode());
	}

	/**
	 * Get the list of agents(nodes) for a user
	 * 
	 * @param security
	 * @return
	 */
	public List<NodeAgentViewModel> getAgentListForAdministration(PlutoSecurityPrinicipal security) {
		List<NodeAgentViewModel> agentList = configurationService.getAgentListForAdministration(security);
		Log.trace("Fetched node list {} for security prinicipal: {} ", security, Utilities.listToString(agentList));
		for (NodeAgentViewModel agent : agentList) {
			agent.setLabels(configurationService.getLabelsByNode(agent.getNodeName()));
			agent.setUsers(configurationService.getAllUsersOnNode(agent.getNodeName()));
		}
		return agentList;
	}

	/**
	 * Get the list of all agents(nodes), a new user need to be validated against
	 * present nodes, and associated to nodes on which users id match id done
	 * 
	 * @param security
	 * @return
	 */
	public List<NodeAgentViewModel> getAllAgentsForUserMapping() {
		List<NodeAgentViewModel> agentList = configurationService.getAgentListForAdministration(null);
		Log.trace("Fetched list of all users: {} ", Utilities.listToString(agentList));
		for (NodeAgentViewModel agent : agentList) {
			agent.setLabels(configurationService.getLabelsByNode(agent.getNodeName()));
			agent.setUsers(configurationService.getAllUsersOnNode(agent.getNodeName()));
		}
		return agentList;
	}

	/**
	 * 
	 * @param map
	 * @return
	 */
	public int createAgentLabelMapping(AgentLabelMapModel map) {
		return configurationService.createAgentLabelMapping(map.getNodeName(), map.getLabelName());
	}

	/**
	 * 
	 * @param map
	 * @return
	 */
	public int deleteAgentLabelMapping(AgentLabelMapModel map) {
		return configurationService.deleteAgentLabelMapping(map.getNodeName(), map.getLabelName());
	}

	/**
	 *
	 * @param userName
	 * @param nodeName
	 * @return
	 */
	public void deleteUserNodeMapping(String userName, String nodeName) {
		userDataService.deleteUserNodeMapping(userName, nodeName);
	}

	/**
	 * 
	 * @param agent
	 */
	public int updateNodeAgent(NodeAgentViewModel agent) {
		int cnt = configurationService.updateNodeAgent(agent);
		UpdateRemoteAgent remoteAgent = new UpdateRemoteAgent(agent);
		Log.trace("starting new thread for updating latest properties on node: {}", agent);
		Thread t = new Thread(remoteAgent);
		t.start();
		return cnt;
	}

	/**
	 * 
	 * @param nodeName
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public AgentConfigurationModel recordHeartBeat(String node) throws Exception {
		configurationService.recordHeartBeat(node);
		return configurationService.getAgentConfig(node);
	}

	/**
	 * 
	 * @param node
	 * @return
	 */
	public AgentCommands getCommands(String node) {
		String osName = configurationService.getAgentRegistrationForm(node).getOsName();
		String archiveReadCmd = Util.getLinesFromArchiveFilePosixCmd(osName);
		AgentCommands commands = new AgentCommands();
		commands.setArchiveDateLineExtractorCommand(archiveReadCmd);
		commands.setFileIndexingCommand(LocalConstants.FIND_COMMAND);
		return commands;
	}

	/**
	 * 
	 * @param user
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class })
	public String addUser(UserCredentialsModel user) {
		Log.trace("adding new user: {}", user);

		UserCredential cred = new UserCredential();
		cred.setUserName(user.getUsername());
		cred.setCreatebBy(user.getCreatedBy());
		cred.setEmail(user.getEmail());
		cred.setFirstName(user.getFirstname());
		cred.setLastName(cred.getLastName());
		cred.setPassword(user.getPassword());

		ArrayList<UserRoleMap> roleMappingList = new ArrayList<UserRoleMap>();
		UserRoleMap map = new UserRoleMap(cred, new UserRole(user.getRole()));
		roleMappingList.add(map);
		cred.setRoleMapList(roleMappingList);

		for (String gpName : user.getGroups()) {
			Log.trace("adding group: {} for user: {}, role in system {}", gpName, user.getEmail());
			cred.addGroupMapping(gpName, user.getCreatedBy());
		}

		/* if user has robotic role */
		if (user.isRobot()) {
			cred.addRoleMapping(LocalConstants.ROLE.BOT, user.getCreatedBy());
		}

		userDataService.saveUser(cred);

		commonServices.updateConfiguration(LocalConstants.KEYS.NEW_INSTALL, LocalConstants.FALSE);
		MapUserWithNodes mapper = new MapUserWithNodes(user, this);
		Thread t = new Thread(mapper);
		t.start();
		return LocalConstants.SUCCESS;
	}


	/**
	 * 
	 * @param user
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class })
	public String updateUser(UserCredentialsModel user) {
		userDataService.updateUser(user);
		return LocalConstants.SUCCESS;
	}

	/**
	 * 
	 * @param user
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class })
	public String deleteUser(String username) {
		userDataService.delteUser(username);
		return LocalConstants.SUCCESS;
	}

	/**
	 * 
	 * @param group
	 * @return
	 */
	public String addGroup(Group group) {
		try {
			Log.trace("creating new group: {}", group);
			userGroupService.createGroup(group);
			return LocalConstants.SUCCESS;
		} catch (Exception e) {
			Log.error("DB exception while adding group", e);
			if (e instanceof DuplicateKeyException) {
				return LocalConstants.DATA_EXISTS;
			}
		}
		return LocalConstants.FAILED;
	}

	/**
	 * 
	 * @param group
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class })
	public String updateGroup(Group group) {
		userGroupService.updateGroup(group);
		return LocalConstants.SUCCESS;
	}

	/**
	 * 
	 * @param group
	 * @throws Exception
	 */
	public void deleteGroup(String groupname) throws Exception {
		List<String> userList = userGroupService.getAllUsersInGroup(groupname);
		if (userList == null || userList.size() == 0) {
			userGroupService.deleteGroup(groupname);
		} else {
			Log.debug("group: {} cannot be deleted as users exist in thia group", groupname);
			throw new Exception("users exist in group");
		}

	}

	/**
	 * 
	 * @param security
	 * @return
	 */
	@Transactional
	public List<UserCredentialsModel> getUserListForAuthority(PlutoSecurityPrinicipal security) {
		try {
			Log.trace("Fetching user list fro admin function by user: {}", security);
			List<UserCredentialsModel> result = userDataService.getUserListByAuthority(security);
			for (UserCredentialsModel user : result) {
				List<String> nodeNameList = configurationService.getAgentListForUser(user.getUsername()).stream()
						.map(node -> node.getNodeName()).collect(Collectors.toList());

				user.setMyNodes(nodeNameList);
			}
			return result;
		} catch (Exception e) {
			Log.error("Exception while fetching user list for admin function by user: {}", security.getUsername(), e);
			return null;
		}
	}

	/**
	 * 
	 * @param username
	 * @return
	 */
	public UserCredentialsModel getProfile(String username) {
		Log.trace("Getting profile for user: {}", username);
		UserCredentialsModel profile = userDataService.getUserById(username);
		return profile;
	}

	/**
	 * 
	 * @param security
	 * @return
	 */
	public List<Role> getAllApplicaleRole(PlutoSecurityPrinicipal security) {
		return roleService.getRoleListByAuthority(security);
	}

	/**
	 * 
	 * @param security
	 * @return
	 */
	@Transactional
	public List<Group> getAllApplicableGroups(PlutoSecurityPrinicipal security) {
		if (LocalConstants.ROLE.ROLES.ADMIN.roleName.equals(security.getAssignedRole())) {
			return userGroupService.getAllGroups();
		}
		return security.getAssignedGroups();

	}

	/**
	 * 
	 * @param security
	 * @return
	 */
	public List<NodeAgentViewModel> getAllPermittedNodes(PlutoSecurityPrinicipal security) {
		return configurationService.getAgentListForAdministration(security);

	}

	/**
	 * 
	 * @param cfgid
	 * @return
	 */
	public String getConfiguration(String cfgid) {
		return commonServices.getConfiguration(cfgid);
	}

	/**
	 * @throws Exception
	 *
	 */
	@Transactional(rollbackFor = { Exception.class })
	public void initializeDataBase() throws Exception {
		if (configurationService.isDbBlank()) {
			Role adminRole = new Role(LocalConstants.ROLE.ROLES.ADMIN);
			Role grpAdmin = new Role(LocalConstants.ROLE.ROLES.GROUP_ADMIN);
			Role grpMember = new Role(LocalConstants.ROLE.ROLES.GROUP_MEMBER);

			Group group = new Group(LocalConstants.SUPEGROUP);
			group.setCreatedBy("system");
			group.setCreatedDate(new Date());
			group.setDescription(LocalConstants.SUPERGROUP_DESC);

			UserCredentialsModel user = new UserCredentialsModel(LocalConstants.USER_DEFAULTS.USERNAME,
					LocalConstants.USER_DEFAULTS.PASSWORD);
			user.setFirstname(LocalConstants.USER_DEFAULTS.NAME);
			user.setLastname(null);
			user.setEmail(LocalConstants.USER_DEFAULTS.EMAIL);
			user.setCreatedBy(LocalConstants.USER_DEFAULTS.CREATED_BY);
			user.setRole(LocalConstants.ROLE.ROLES.ADMIN.roleName);
			user.setGroups(new String[] { LocalConstants.SUPEGROUP });
			try {
				Log.debug("Creating base roles: {}, {}, {}", adminRole, grpAdmin, grpMember);
				roleService.createRole(adminRole);
				roleService.createRole(grpAdmin);
				roleService.createRole(grpMember);

				Log.debug("Creating base group: {} and user: {}", group, user);

				userGroupService.createGroup(group);
				addUser(user);

				commonServices.createConfiguration(LocalConstants.KEYS.NEW_INSTALL, LocalConstants.TRUE);
				commonServices.createConfiguration("FIND_COMMAND", LocalConstants.FIND_COMMAND);

				Log.info("database initialized successfully");
			} catch (Exception e) {
				Log.error("error initlizing the database", e);
				throw e;
			}
		}
		if (!roleService.isRoboRolePresent()) {
			UserCredentialsModel user = new UserCredentialsModel(LocalConstants.USER_DEFAULTS.USERNAME,
					LocalConstants.USER_DEFAULTS.PASSWORD);
			if (Boolean.parseBoolean(System.getProperty(LocalConstants.KEYS.APPLY_PATCH))) {
				Patch2018 patch2018 = context.getBean(Patch2018.class);
				patch2018.applyPatch(user);
			}

		}

		if (Boolean.parseBoolean(System.getProperty(LocalConstants.KEYS.APPLY_PATCH))) {
			PatchAugust2021 patch2021 = context.getBean(PatchAugust2021.class);
			patch2021.applyPatch();
		}
	}

}
