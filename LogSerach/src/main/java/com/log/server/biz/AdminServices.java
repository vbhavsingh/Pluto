/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.server.biz;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.sound.midi.Patch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.log.analyzer.commons.Constants;
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
import com.log.server.data.db.Dao;
import com.log.server.data.db.patch.Patch2018;
import com.log.server.data.db.patch.PatchAugust2021;
import com.log.server.model.AgentLabelMapModel;
import com.log.server.model.Group;
import com.log.server.model.NodeAgentViewModel;
import com.log.server.model.Role;
import com.log.server.model.UserCredentials;
import com.log.server.util.Utilities;
import com.security.common.PlutoSecurityPrinicipal;

/**
 *
 * @author Vaibhav Singh
 */
@Component
public class AdminServices {

	private final static Logger Log = LoggerFactory.getLogger(AdminServices.class);

	@Autowired
	private Dao dao;
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private Patch2018 patch2018;
	
	
	public Dao getDao() {
		return dao;
	}

	/**
	 * 
	 * @param node
	 */
	public void registerAgent(AgentRegistrationForm node) {
		node.setCommKey(UUID.randomUUID().toString());
		Log.trace("genertaed comm key: {} for node: {}", node.getCommKey(), node.getClientNode());
		Log.trace("saving agent into database, agent: " + node.getClientNode());
		this.dao.saveAgent(node);
		List<String> userList = dao.getNonMappedUsersForNode(node.getClientName());
		NodeAgentViewModel nodeModel = new NodeAgentViewModel();
		int port = Integer.parseInt(node.getClientConnectPort());
		nodeModel.setNodeName(node.getClientNode());
		nodeModel.setPort(port);
		MapNodeWithUsers mapper = new MapNodeWithUsers(userList, nodeModel, this);
		Thread t = new Thread(mapper);
		t.start();
	}

	/**
	 * 
	 * @return String
	 */
	public String deRegisterAgent(AgentTerminatorRequestModel killRequest) throws Exception {
		TerminateAgent terminator = new TerminateAgent();
		AgentModel agent = dao.getRegisteredAgent(killRequest.getNodeName());
		Log.info("requesting termintaion of remote node: {}, for user: {} in session: {}", killRequest.getNodeName(), killRequest.getUser(), killRequest.getSession());
		Log.trace("requesting remote agatent termination for request : {}", killRequest);
		String result = terminator.terminate(killRequest, agent);

		if (LocalConstants.ERROR.UNREACHABLE.equals(result)) {
			Log.info("node {} is not reachable for termintation, db cleanup will be done assuming its dead", killRequest.getNodeName());
		}

		if (Constants.SUCCESS.equals(result) || LocalConstants.ERROR.UNREACHABLE.equals(result)) {
			try {
				Log.info("remote agent: {} terminated by user: {}, cleaning up database references", killRequest.getNodeName(), killRequest.getUser());

				Log.trace("agent termination cleanup: deleting user-node mapping for node: {}", killRequest.getNodeName());
				int count = dao.deletAllNodeUserMapping(killRequest.getNodeName());
				Log.trace("agent termination cleanup: deleted {} user-node mapping for node: {}", count, killRequest.getNodeName());

				count = 0;
				Log.trace("agent termination cleanup: deleting node-label mapping for node: {}", killRequest.getNodeName());
				count = dao.deleteAllLabelsForNode(killRequest.getNodeName());
				Log.trace("agent termination cleanup: deleted {} node-label mapping for node: {}", count, killRequest.getNodeName());

				count = 0;
				Log.trace("agent termination cleanup: deleting node-filepattern mapping for node: {}", killRequest.getNodeName());
				dao.deleteAllFileNamePatternsForNode(killRequest.getNodeName());
				Log.trace("agent termination cleanup: deleted {} node-filepattern mapping for node: {}", count, killRequest.getNodeName());

				dao.deleteNode(killRequest.getNodeName());
				Log.info("remote agent: {} terminated by user: {}, database celanup successful", killRequest.getNodeName(), killRequest.getUser());
			} catch (Exception e) {
				throw new Exception("agent already removed from system");
			}

		}
		return result;
	}

	/**
	 * 
	 * @param node
	 * @return
	 */
	public AgentModel getRegisteredAgentdetail(AgentRegistrationForm node) {
		return this.dao.getRegisteredAgent(node);
	}

	/**
	 * Get the list of agents(nodes) for a user
	 * 
	 * @param security
	 * @return
	 */
	public List<NodeAgentViewModel> getAgentListForAdministration(PlutoSecurityPrinicipal security) {
		List<NodeAgentViewModel> agentList = dao.getAgentListForAdministration(security);
		Log.trace("Fetched node list {} for security prinicipal: {} ", security, Utilities.listToString(agentList));
		for (NodeAgentViewModel agent : agentList) {
			agent.setLabels(dao.getLabels(agent.getNodeName()));
			agent.setUsers(dao.getAllUsersOnNode(agent.getNodeName()));
		}
		return agentList;
	}

	/**
	 * Get the list of all agents(nodes), a new user need to be validated
	 * against present nodes, and associated to nodes on which users id match id
	 * done
	 * 
	 * @param security
	 * @return
	 */
	public List<NodeAgentViewModel> getAllAgentsForUserMapping() {
		List<NodeAgentViewModel> agentList = dao.getAgentListForAdministration(null);
		Log.trace("Fetched list of all users: {} ", Utilities.listToString(agentList));
		for (NodeAgentViewModel agent : agentList) {
			agent.setLabels(dao.getLabels(agent.getNodeName()));
			agent.setUsers(dao.getAllUsersOnNode(agent.getNodeName()));
		}
		return agentList;
	}

	/**
	 * 
	 * @param map
	 * @return
	 */
	public int createAgentLabelMapping(AgentLabelMapModel map) {
		return dao.createAgentLabelMapping(map);
	}

	/**
	 * 
	 * @param map
	 * @return
	 */
	public int deleteAgentLabelMapping(AgentLabelMapModel map) {
		return dao.deleteAgentLabelMapping(map);
	}

	/**
	 *
	 * @param userName
	 * @param nodeName
	 * @return
	 */
	public int deleteUserNodeMapping(String userName, String nodeName) {
		return dao.deleteUserNodeMapping(userName, nodeName);
	}

	/**
	 * 
	 * @param agent
	 */
	public int updateNodeAgent(NodeAgentViewModel agent) {
		int cnt = dao.updateNodeAgent(agent);
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
	public AgentConfigurationModel recordHeartBeat(String node) throws Exception {
		dao.recordHeartbeat(node);
		return dao.getAgentConfig(node);
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	public AgentCommands getCommands(String node) {
		String osName = dao.getOsType(node);
		String archiveReadCmd = Util.getLinesFromArchiveFilePosixCmd(osName);
		AgentCommands commands =  new AgentCommands();
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
	public String addUser(UserCredentials user) {
		Log.trace("adding new user: {}", user);
		this.dao.createUser(user);
		Log.trace("adding new user: {}, role in system {}", user.getEmail(), user.getRole());
		this.dao.createUserRoleMapping(user, user.getRole());
		for (String gp : user.getGroups()) {
			Log.trace("adding group: {} for user: {}, role in system {}", gp, user.getEmail());
			this.dao.createUserGroupMapping(user, gp);
		}
		/*if user has robotic role*/
		if(user.isRobot()) {
			this.dao.createUserRoleMapping(user, LocalConstants.ROLE.BOT);
		}
		this.dao.updateConfiguration(LocalConstants.KEYS.NEW_INSTALL, LocalConstants.FALSE);
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
	public String updateUser(UserCredentials user) {
		Log.trace("Updating user: {}, deleting old role and group mappings", user);
		this.dao.deleteUserRoleMapping(user.getUsername());
		this.dao.deleteUserGroupMapping(user.getUsername());
		Log.trace("Updating user: {}, creating new role and group mappings", user);
		if (StringUtils.hasText(user.getPassword())) {
			this.dao.updateUserProfile(user);
		} else {
			this.dao.updateUserProfileAndPassword(user);
		}
		this.dao.createUserRoleMapping(user, user.getRole());
		for (String gp : user.getGroups()) {
			this.dao.createUserGroupMapping(user, gp);
		}
		if(user.isRobot()) {
			this.dao.createUserRoleMapping(user, LocalConstants.ROLE.BOT);
		}else {
			this.dao.deleteUserRoleMapping(user.getUsername(), LocalConstants.ROLE.BOT);
		}
		return LocalConstants.SUCCESS;
	}

	/**
	 * 
	 * @param user
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class })
	public String deleteUser(String username) {
		Log.trace("Deleting user: {}", username);
		this.dao.deleteUser(username);
		Log.trace("Deleting group mapping for user: {}", username);
		this.dao.deleteUserGroupMapping(username);
		Log.trace("Deleting role mapping for user: {}", username);
		this.dao.deleteUserRoleMapping(username);
		Log.trace("Deleting node mapping for user: {}", username);
		this.dao.deletAlleUserNodeMapping(username);
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
			this.dao.createGroup(group);
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
		this.dao.updateGroup(group);
		this.dao.updateGroupMap(group);
		return LocalConstants.SUCCESS;
	}

	/**
	 * 
	 * @param group
	 * @throws Exception
	 */
	public void deleteGroup(String groupname) throws Exception {
		List<String> userList = this.dao.getAllUsersInGroup(groupname);
		if (userList == null || userList.size() == 0) {
			this.dao.deleteGroup(groupname);
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
	public List<UserCredentials> getUserListForAuthority(PlutoSecurityPrinicipal security) {
		try {
			Log.trace("Fetching user list fro admin function by user: {}", security);
			List<UserCredentials> result = this.dao.getUserListByAuthority(security);
			for (UserCredentials user : result) {
				List<Role> roles = this.dao.getAssignedRoleForUser(user.getUsername());
				String r = null;
				for(Role role:roles) {
					if(role.isVisible()) {
						r = role.getRolename();
					}
					if(LocalConstants.ROLE.BOT.equals(role.getRolename())) {
						user.setRobot(true);
					}
				}
				
				user.setRole(r);
				List<Group> gObjList = this.dao.getAssignedGroupsForUser(user.getUsername());
				if (gObjList != null) {
					String gArray[] = new String[gObjList.size()];
					int i = 0;
					for (Group g : gObjList) {
						gArray[i++] = g.getName();
					}
					user.setGroups(gArray);
				}
				user.setMyNodes(dao.getNodeListForUser(user.getUsername()));
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
	public UserCredentials getProfile(String username) {
		Log.trace("Getting profile for user: {}", username);
		UserCredentials profile = this.dao.getUserProfile(username);
		List<Role> roles = this.dao.getAssignedRoleForUser(username);
		String r = null;
		for(Role role:roles) {
			if(role.isVisible()) {
				profile.setRole(role.getRolename());
			}
			if(LocalConstants.ROLE.BOT.equals(role.getRolename())) {
				profile.setRobot(true);
			}
		}
		List<Group> gpList = this.dao.getAssignedGroupsForUser(username);
		String gpArray[] = new String[gpList.size()];
		int i = 0;
		for (Group gp : gpList) {
			gpArray[i++] = gp.getName();
		}
		profile.setGroups(gpArray);
		return profile;
	}

	/**
	 * 
	 * @param security
	 * @return
	 */
	public List<Role> getAllApplicaleRole(PlutoSecurityPrinicipal security) {
		return this.dao.getRoleListByAuthority(security);
	}

	/**
	 * 
	 * @param security
	 * @return
	 */
	@Transactional
	public List<Group> getAllApplicableGroups(PlutoSecurityPrinicipal security) {
		List<Group> gpList = this.dao.getGroupListByAuthority(security);
		for (Group gp : gpList) {
			gp.setUsers(this.dao.getAllUsersInGroup(gp.getName()));
		}
		return gpList;
	}

	/**
	 * 
	 * @param security
	 * @return
	 */
	public List<NodeAgentViewModel> getAllPermittedNodes(PlutoSecurityPrinicipal security) {
		return dao.getAgentListForAdministration(security);

	}

	/**
	 * 
	 * @param cfgid
	 * @return
	 */
	public String getConfiguration(String cfgid) {
		return dao.getConfiguration(cfgid);
	}

	/**
	 * @throws Exception
	 *
	 */
	@Transactional(rollbackFor = { Exception.class })
	public void initializeDataBase() throws Exception {
		if (this.dao.isDbBlank()) {
			Role adminRole = new Role(LocalConstants.ROLE.ADMIN, LocalConstants.ROLE.ADMIN_DESC);
			Role grpAdmin = new Role(LocalConstants.ROLE.GROUP_ADMIN, LocalConstants.ROLE.GROUP_ADMIN_DESC);
			Role grpMember = new Role(LocalConstants.ROLE.GROUP_MEMBER, LocalConstants.ROLE.GROUP_MEMBER_DESC);

			Group group = new Group();
			group.setCreatedBy("system");
			group.setCreatedDate(new Date());
			group.setDescription(LocalConstants.SUPERGROUP_DESC);
			group.setName(LocalConstants.SUPEGROUP);

			UserCredentials user = new UserCredentials(LocalConstants.USER_DEFAULTS.USERNAME, LocalConstants.USER_DEFAULTS.PASSWORD);
			user.setFirstname(LocalConstants.USER_DEFAULTS.NAME);
			user.setLastname(null);
			user.setEmail(LocalConstants.USER_DEFAULTS.EMAIL);
			user.setCreatedBy(LocalConstants.USER_DEFAULTS.CREATED_BY);
			user.setRole(LocalConstants.ROLE.ADMIN);
			user.setGroups(new String[] { LocalConstants.SUPEGROUP });
			try {
				Log.debug("Creating base roles: {}, {}, {}", adminRole, grpAdmin, grpMember);
				this.dao.createRole(adminRole);
				this.dao.createRole(grpAdmin);
				this.dao.createRole(grpMember);

				Log.debug("Creating base group: {} and user: {}", group, user);
				this.dao.createGroup(group);
				addUser(user);

				this.dao.createConfiguration(LocalConstants.KEYS.NEW_INSTALL, LocalConstants.TRUE);
				this.dao.createConfiguration("FIND_COMMAND", LocalConstants.FIND_COMMAND);

				Log.info("database initialized successfully");
			} catch (Exception e) {
				Log.error("error initlizing the database", e);
				throw e;
			}
		}
		if(this.dao.ifRoleExists(LocalConstants.ROLE.BOT)==false) {
			UserCredentials user = new UserCredentials(LocalConstants.USER_DEFAULTS.USERNAME, LocalConstants.USER_DEFAULTS.PASSWORD);
			patch2018.applyPatch(user);
		}
		
		if(Boolean.parseBoolean(System.getProperty(LocalConstants.KEYS.APPLY_PATCH))) {
			PatchAugust2021 patch2021 = context.getBean(PatchAugust2021.class);
			patch2021.applyPatch();
		}
	}

}
