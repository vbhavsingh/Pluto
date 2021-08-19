/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.server.data.db;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.log.analyzer.commons.Constants;
import com.log.analyzer.commons.model.AgentConfigurationModel;
import com.log.analyzer.commons.model.AgentModel;
import com.log.analyzer.commons.model.AgentRegistrationForm;
import com.log.server.LocalConstants;
import com.log.server.data.db.mapper.AgentConfigDbMapper;
import com.log.server.data.db.mapper.AgentViewModelDbMapper;
import com.log.server.data.db.mapper.ClientModelRowMapper;
import com.log.server.data.db.mapper.GroupModelMapper;
import com.log.server.data.db.mapper.LabelNodeCountMapper;
import com.log.server.data.db.mapper.RoleModelRowMapper;
import com.log.server.data.db.mapper.SearchHelpKeywordMapper;
import com.log.server.data.db.mapper.UserCredentialsMapper;
import com.log.server.model.AgentLabelMapModel;
import com.log.server.model.DaoSearchModel;
import com.log.server.model.Group;
import com.log.server.model.LabelCounter;
import com.log.server.model.NodeAgentViewModel;
import com.log.server.model.Role;
import com.log.server.model.SearchHelpKeyword;
import com.log.server.model.UserCredentials;
import com.log.server.util.Utilities;
import com.security.common.PlutoSecurityPrinicipal;

import net.rationalminds.es.EnvironmentalControl;

/**
 *
 * @author Vaibhav Pratap Singh
 */
@Transactional
@Component
public class Dao {

	private static final Logger Log = LoggerFactory.getLogger(Dao.class);
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		Log.debug("creating data source assignement");
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	/**
	 * 
	 * @return
	 */
	public List<LabelCounter> getLabelNodeCounter() {
		String query = "SELECT LABEL_NAME, COUNT(*) FROM LABELS GROUP BY LABEL_NAME";
		List<LabelCounter> labelList = (List<LabelCounter>) jdbcTemplate.query(query, new LabelNodeCountMapper());
		return labelList;
	}

	/**
	 *
	 * @return
	 */
	public List<String> getLabels() {
		String query = "SELECT DISTINCT LABEL_NAME FROM LABELS";
		List<String> labelList = (List<String>) jdbcTemplate.queryForList(query, String.class);
		return labelList;
	}

	/**
	 * Labels are tags associated with an agent(node), like prod, dev, test can
	 * be labels
	 * 
	 * @return
	 */
	public List<String> getLabels(String nodeName) {
		String query = "SELECT LABEL_NAME FROM LABELS WHERE NODE_NAME='" + nodeName + "'";
		List<String> labelList = (List<String>) jdbcTemplate.queryForList(query, String.class);
		return labelList;
	}
	
	/**
	 * 
	 * @param fieldName
	 * @return
	 */
	public List<String> getPreviousSearchCriterias(String fieldName){
		String query ="SELECT KEYTEXT FROM SEARCH_ASSISTANT WHERE FIELD='"+fieldName+"'";
		List<String> keywords = (List<String>) jdbcTemplate.queryForList(query, String.class);
		return keywords;
	}
	
	/**
	 * 
	 * @param fieldName
	 * @param userName
	 * @return
	 */
	public List<String> getPreviousSearchCriterias(String fieldName, String userName){
		String query ="SELECT KEYTEXT FROM SEARCH_ASSISTANT WHERE FIELD='"+fieldName+"' AND USERNAME='"+userName+"'";
		List<String> keywords = (List<String>) jdbcTemplate.queryForList(query, String.class);
		return keywords;
	}
	/**
	 *
	 * @param model
	 */
	public void saveAgent(AgentRegistrationForm model) {
		boolean nodeExists = ifNodeExists(model.getClientNode());
		String nodeName = model.getClientNode().toLowerCase();
		String agentName = model.getClientName().toLowerCase();
		int port = Integer.parseInt(model.getClientConnectPort());
		String timeZone = model.getTimeZone();
		java.sql.Date timestamp = new java.sql.Date(new Date().getTime());
		if (nodeExists) {
			Log.info("node: {} already exists, just updating parameters", nodeName);
			Object values[] = { port, timeZone, timestamp, model.getAgentVersion(), nodeName };
			int types[] = { Types.INTEGER, Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR, Types.VARCHAR };
			jdbcTemplate.update(LocalConstants.SQL.UPDATE_AGENTS, values, types);
		} else {
			Log.info("node does not exist, registering node: {} as new node", nodeName);
			Object values[] = { nodeName, agentName, model.getCommKey(), model.getOsName(), model.getAgentVersion(), port, Constants.PROPERTIES.MAX_THREADS, timeZone,
					Constants.PROPERTIES.MAX_FILES_TO_SEARCH, Constants.PROPERTIES.MAX_LINES_PER_FILE, Constants.PROPERTIES.MAX_LINES_IN_RESULT,
					Constants.PROPERTIES.RESULT_SIZE_KB, timestamp, timestamp };
			int types[] = { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.INTEGER, Types.INTEGER,
					Types.INTEGER, Types.INTEGER, Types.TIMESTAMP, Types.TIMESTAMP };
			jdbcTemplate.update(LocalConstants.SQL.INSERT_AGENT, values, types);
			Log.debug("saving agent name & node as primary search labels");
			/* Registering node name or fqdn as Label */
			values = new String[] { nodeName, Utilities.getNameFromFqdn(nodeName) };
			types = new int[] { Types.VARCHAR, Types.VARCHAR };
			jdbcTemplate.update(LocalConstants.SQL.LABEL_INSERT, values, types);
			/* Registering agent given name as a Label */
			if (!nodeName.equals(agentName)) {
				if (!agentName.equals(Utilities.getNameFromFqdn(nodeName))) {
					AgentLabelMapModel map = new AgentLabelMapModel(nodeName, agentName);
					createAgentLabelMapping(map);
				}
			}
		}
	}
	/**
	 * 
	 * @param nodeName
	 * @return
	 */
	public AgentConfigurationModel getAgentConfig(String nodeName) {
		String q = "SELECT * FROM AGENTS WHERE NODE_NAME = ?";
		Object obj[] = { nodeName };
		return jdbcTemplate.queryForObject(q, obj, new AgentConfigDbMapper());
	}
	/**
	 * 
	 * @param nodeName
	 * @return osName
	 */
	public String getOsType(String nodeName) {
		String q = "SELECT OS_TYPE FROM AGENTS WHERE NODE_NAME = ?";
		Object obj[] = { nodeName };
		try {
			return jdbcTemplate.queryForObject(q, obj, String.class);
		}catch(Exception ex) {
			return null;
		}
	}

	/**
	 * 
	 * @param map
	 */
	public int createAgentLabelMapping(AgentLabelMapModel map) {
		Object values[] = new String[] { map.getNodeName(), map.getLabelName() };
		int types[] = new int[] { Types.VARCHAR, Types.VARCHAR };
		return jdbcTemplate.update(LocalConstants.SQL.LABEL_INSERT, values, types);
	}

	/**
	 * 
	 * @param map
	 */
	public int deleteAgentLabelMapping(AgentLabelMapModel map) {
		Object values[] = new String[] { map.getNodeName(), map.getLabelName() };
		int types[] = new int[] { Types.VARCHAR, Types.VARCHAR };
		return jdbcTemplate.update(LocalConstants.SQL.LABEL_DELETE, values, types);
	}

	/**
	 * 
	 * @param nodeName
	 * @return
	 */
	public int deleteAllLabelsForNode(String nodeName) {
		String delete = "DELETE FROM LABELS WHERE NODE_NAME='" + nodeName + "'";
		int count = jdbcTemplate.update(delete);
		return count;
	}

	/**
	 * 
	 * @param node
	 * @throws Exception
	 */
	public void recordHeartbeat(String node) throws Exception {
		if (ifNodeExists(node)) {
			java.sql.Date timestamp = new java.sql.Date(new Date().getTime());
			Object values[] = { timestamp, node };
			int types[] = { Types.TIMESTAMP, Types.VARCHAR };
			jdbcTemplate.update(LocalConstants.SQL.HEARTBEAT, values, types);
		} else {
			throw new Exception(node + " is not a registered node");
		}
	}

	/**
	 *
	 * @param model
	 * @return
	 */
	@EnvironmentalControl(devMethod="com.log.server.util.DevEnvironmentMocker.getClients()")
	public List<AgentModel> getClients(DaoSearchModel model) {
		String query = createQuery(model);
		Log.info("query to fetch matching clients : {}", query);
		return jdbcTemplate.query(query, new ClientModelRowMapper());
	}

	/**
	 *
	 * @param model
	 * @return
	 */
	public AgentModel getRegisteredAgent(AgentRegistrationForm node) {
		String query = "SELECT * FROM AGENTS WHERE NODE_NAME=?";
		return jdbcTemplate.queryForObject(query, new Object[] { node.getClientNode() }, new ClientModelRowMapper());
	}

	/**
	 *
	 * @param model
	 * @return
	 */
	public AgentModel getRegisteredAgent(String nodeName) {
		String query = "SELECT * FROM AGENTS WHERE NODE_NAME=?";
		return jdbcTemplate.queryForObject(query, new Object[] { nodeName }, new ClientModelRowMapper());
	}

	/**
	 *
	 * @param user
	 * @return
	 */
	public List<NodeAgentViewModel> getAgentListForUser(String user) {
		/* find the role of the user */
		Object values[] = { user };
		List<String> roleList = (List<String>) jdbcTemplate.queryForList(LocalConstants.SQL.GET_ROLE_FOR_USER, values, String.class);
		if (roleList.contains(LocalConstants.ROLE.ADMIN)) {
			String q = "SELECT * FROM AGENTS ORDER BY NODE_NAME,AGENT_NAME";
			return jdbcTemplate.query(q, new AgentViewModelDbMapper());
		} else {
			int types[] = { Types.VARCHAR };
			return jdbcTemplate.query(LocalConstants.SQL.GET_NODE_FOR_USER, values, types, new AgentViewModelDbMapper());
		}
	}

	/**
	 * 
	 * @param security
	 * @return
	 */
	public List<NodeAgentViewModel> getAgentListForAdministration(PlutoSecurityPrinicipal security) {
		/*
		 * If security is null give back all agents, this will be case when a
		 * new user need to be mapped against the present nodes on system
		 */
		if (security == null) {
			String q = "SELECT * FROM AGENTS ORDER BY NODE_NAME,AGENT_NAME";
			return jdbcTemplate.query(q, new AgentViewModelDbMapper());
		}
		/*
		 * Id there is security context associated and user is admin return all
		 * agents
		 */
		String role = security.getAssignedRole();
		if (LocalConstants.ROLE.ADMIN.equals(role)) {
			String q = "SELECT * FROM AGENTS ORDER BY NODE_NAME,AGENT_NAME";
			return jdbcTemplate.query(q, new AgentViewModelDbMapper());
		}
		if (LocalConstants.ROLE.GROUP_ADMIN.equals(role)) {
			List<String> groupList = security.getAssignedGroups();
			List<String> userList = new ArrayList<String>();
			for (String groupname : groupList) {
				userList.addAll(getAllUsersInGroup(groupname));
			}
			String inclause = "";
			for (String user : userList) {
				inclause = inclause + "'" + user + "',";
			}
			if (inclause.length() > 0) {
				inclause.substring(0, inclause.length() - 1);
			}
			String q = LocalConstants.SQL.GET_NODE_FOR_MULTIPLE_USERS + "(" + inclause + ")";
			return jdbcTemplate.query(q, new AgentViewModelDbMapper());
		}
		return getAgentListForUser(security.getUser());
	}

	/**
	 * 
	 * @param agent
	 * @return
	 */
	public int updateNodeAgent(NodeAgentViewModel agent) {
		java.sql.Date timestamp = getSqlTime(new Date());
		Object values[] = { agent.getAgentName(), agent.getParallelism(), agent.getMaxFilesToSearch(), agent.getMaxLinesPerFile(), agent.getMaxLinesInresult(),
				agent.getResultInKb(), timestamp, agent.getNodeName() };
		int types[] = { Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.TIMESTAMP, Types.VARCHAR };
		int count = jdbcTemplate.update(LocalConstants.SQL.UPDATE_NODE_AGENT, values, types);
		return count;
	}

	/**
	 * 
	 * @param nodeName
	 * @return
	 */
	public int deleteNode(String nodeName) {
		String delete = "DELETE FROM AGENTS WHERE NODE_NAME='" + nodeName + "'";
		int count = jdbcTemplate.update(delete);
		return count;
	}

	/**
	 * 
	 * @param nodeName
	 * @return
	 */
	public int deleteAllFileNamePatternsForNode(String nodeName) {
		String delete = "DELETE FROM FILE_NAME_PATTERN WHERE NODE_NAME='" + nodeName + "'";
		int count = jdbcTemplate.update(delete);
		return count;
	}

	/**
	 *
	 * @param user
	 * @return
	 */
	public int createUser(UserCredentials user) {
		java.sql.Date timestamp = getSqlTime(new Date());
		Object values[] = { user.getUsername(), user.getPassword(), user.getEmail(), user.getFirstname(), user.getLastname(), user.getCreatedBy(), timestamp, timestamp };
		int types[] = { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.TIMESTAMP };
		int count = jdbcTemplate.update(LocalConstants.SQL.USER_INSERT, values, types);
		return count;
	}

	/**
	 *
	 * @param userName
	 * @return
	 */
	public int deleteUser(String userName) {
		String delete = "DELETE FROM CREDENTIALS_USER WHERE USERNAME='" + userName + "'";
		int count = jdbcTemplate.update(delete);
		return count;
	}

	/**
	 *
	 * @param userName
	 * @return
	 */
	public int updateUserProfile(UserCredentials user) {
		java.sql.Date timestamp = getSqlTime(new Date());
		Object values[] = { user.getEmail(), user.getFirstname(), user.getLastname(), timestamp, user.getUsername() };
		int types[] = { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR };
		int count = jdbcTemplate.update(LocalConstants.SQL.USER_PROFILE, values, types);
		return count;
	}

	/**
	 * 
	 * @param user
	 * @return
	 */
	public int updateUserProfileAndPassword(UserCredentials user) {
		java.sql.Date timestamp = getSqlTime(new Date());
		Object values[] = { user.getPassword(), user.getEmail(), user.getFirstname(), user.getLastname(), timestamp, user.getUsername() };
		int types[] = { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR };
		int count = jdbcTemplate.update(LocalConstants.SQL.USER_PROFILE_AND_PASSWORD, values, types);
		return count;
	}

	/**
	 * 
	 * @param username
	 * @return
	 */
	public UserCredentials getUserProfile(String username) {
		Object obj[] = { username };
		UserCredentials user = jdbcTemplate.queryForObject(LocalConstants.SQL.GET_USER_PROFILE, obj, new UserCredentialsMapper(false));
		return user;
	}

	/**
	 * 
	 * @param username
	 * @return
	 */
	public UserCredentials getUserProfileForAuthetication(String username) {
		Object obj[] = { username };
		UserCredentials user = jdbcTemplate.queryForObject(LocalConstants.SQL.GET_USER_PROFILE, obj, new UserCredentialsMapper(true));
		return user;
	}

	/**
	 * 
	 * @param groupname
	 * @return
	 */
	public List<String> getAllUsersInGroup(String groupname) {
		String q = "SELECT USERNAME FROM USER_GROUP_MAP WHERE GROUPNAME='" + groupname + "'";
		List<String> userNameList = (List<String>) this.jdbcTemplate.queryForList(q, String.class);
		return userNameList;
	}

	/**
	 * 
	 * @param nodeName
	 * @return
	 */
	public List<String> getAllUsersOnNode(String nodeName) {
		String q = "SELECT USERNAME FROM USER_NODE_MAP WHERE NODE_NAME='" + nodeName + "'";
		List<String> userNameList = (List<String>) this.jdbcTemplate.queryForList(q, String.class);
		return userNameList;
	}

	/**
	 * 
	 * @param groupname
	 * @return
	 */
	public List<String> getNonMappedUsersForNode(String nodename) {
		String q = "SELECT DISTINCT U.USERNAME FROM CREDENTIALS_USER U LEFT JOIN USER_NODE_MAP N " + "ON U.USERNAME = N.USERNAME "
				+ "WHERE U.USERNAME NOT IN (SELECT DISTINCT USERNAME FROM USER_NODE_MAP WHERE " + "NODE_NAME='" + nodename + "')";

		List<String> userNameList = (List<String>) this.jdbcTemplate.queryForList(q, String.class);
		return userNameList;
	}

	/**
	 * 
	 * @param security
	 * @return
	 */
	public List<UserCredentials> getUserListByAuthority(PlutoSecurityPrinicipal security) {
		String role = security.getAssignedRole();
		String query;
		if (LocalConstants.ROLE.ADMIN.equals(role)) {
			query = "SELECT * FROM CREDENTIALS_USER";
		} else if (LocalConstants.ROLE.GROUP_ADMIN.equals(role)) {
			String clause = inClause(security.getAssignedGroups());
			query = "SELECT U.USERNAME, U.EMAIL, U.FIRST_NAME " + "U.LAST_NAME, U.CREATED_BY, U.CREATED_TIME " + "U.LAST_MODIFIED " + "FROM CREDENTIALS_USER U, USER_GROUP_MAP M "
					+ "WHERE U.USERNAME=M.USERNAME " + "AND M.GROUPNAME IN (" + clause + ")";
		} else {
			query = "SELECT * FROM CREDENTIALS_USER WHERE USERNAME ='" + security.getUsername() + "'";
		}
		List<UserCredentials> result = jdbcTemplate.query(query, new UserCredentialsMapper(false));
		return result;
	}

	/**
	 *
	 * @param role
	 * @return
	 */
	public boolean createRole(Role role) {
		try {
			Object values[] = { role.getRolename(), role.getDescription() };
			int types[] = { Types.VARCHAR, Types.VARCHAR };
			jdbcTemplate.update(LocalConstants.SQL.ROLE_INSERT, values, types);
		} catch (Exception e) {
			Log.error("error while creating new role: {}", role, e);
			return false;
		}
		return true;
	}

	/**
	 *
	 * @param roleName
	 * @return
	 */
	public int deleteRole(String roleName) {
		String delete = "DELETE FROM CREDENTIALS_ROLE WHERE ROLENAME='" + roleName + "'";
		int count = jdbcTemplate.update(delete);
		return count;
	}

	/**
	 * 
	 * @param username
	 * @return
	 */
	public List<Role> getAssignedRoleForUser(String username) {
		Object values[] = { username };
		int types[] = { Types.VARCHAR };
		List<Role> roleList = jdbcTemplate.query(LocalConstants.SQL.ASSIGNED_ROLE_FOR_USER, values, types, new RoleModelRowMapper());
		return roleList;
	}

	/**
	 * 
	 * @param security
	 * @return
	 */
	public List<Role> getRoleListByAuthority(PlutoSecurityPrinicipal security) {
		String hasRole = security.getAssignedRole();
		String query;
		if (LocalConstants.ROLE.ADMIN.equals(hasRole)) {
			query = "SELECT * FROM CREDENTIALS_ROLE";
		} else if (LocalConstants.ROLE.GROUP_ADMIN.equals(hasRole)) {
			query = "SELECT * FROM CREDENTIALS_ROLE WHERE ROLENAME <> '" + LocalConstants.ROLE.ADMIN + "'";
		} else {
			query = "SELECT * FROM CREDENTIALS_ROLE WHERE ROLENAME NOT IN ('" + LocalConstants.ROLE.ADMIN + "','" + LocalConstants.ROLE.GROUP_ADMIN + "')";
		}
		return jdbcTemplate.query(query, new RoleModelRowMapper());
	}
	
	/**
	 * 
	 * @param groupname
	 * @return
	 */
	public boolean ifRoleExists(String rolename) {
		String query = "SELECT COUNT(*) FROM CREDENTIALS_ROLE WHERE ROLENAME = ?";
		int count = jdbcTemplate.queryForObject(query, new Object[] { rolename }, Integer.class);
		if(count==0)
			return false;
		return true;			
	}


	/**
	 * 
	 * @param security
	 * @return
	 */
	public List<Group> getGroupListByAuthority(PlutoSecurityPrinicipal security) {
		String groupList = security.getAssignedRole();
		String query;
		if (LocalConstants.ROLE.ADMIN.equals(security.getAssignedRole())) {
			query = "SELECT * FROM CREDENTIALS_GROUP";
			return jdbcTemplate.query(query, new GroupModelMapper());
		}

		String applicaleGroups = "";
		for (String gp : security.getAssignedGroups()) {
			applicaleGroups = applicaleGroups + "'" + gp + "',";
		}
		if (applicaleGroups.length() >= 1) {
			applicaleGroups = applicaleGroups.substring(0, applicaleGroups.length() - 1);
		}

		query = "SELECT * FROM CREDENTIALS_GROUP WHERE GROUPNAME IN (" + applicaleGroups + ")";
		return jdbcTemplate.query(query, new GroupModelMapper());
	}

	/**
	 *
	 * @param group
	 * @return
	 */
	public int createGroup(Group group) {
		java.sql.Date createTime = getSqlTime(group.getCreatedDate());
		java.sql.Date modifiedTime = getSqlTime(group.getCreatedDate());
		Object values[] = { group.getName(), group.getDescription(), group.getCreatedBy(), createTime };
		int types[] = { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, };
		int count = jdbcTemplate.update(LocalConstants.SQL.GROUP_INSERT, values, types);
		return count;
	}

	/**
	 *
	 * @param group
	 * @return
	 */
	public int updateGroup(Group group) {
		java.sql.Date modifiedTime = getSqlTime(group.getCreatedDate());
		Object values[] = { group.getName(), group.getDescription(), group.getModifiedBy(), modifiedTime, group.getOldName() };
		int types[] = { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR };
		int count = jdbcTemplate.update(LocalConstants.SQL.UPDATE_CREDENTIALS_GROUP, values, types);
		return count;
	}

	/**
	 *
	 * @param group
	 * @return
	 */
	public int updateGroupMap(Group group) {
		String q = "UPDATE USER_GROUP_MAP SET GROUPNAME=? WHERE GROUPNAME=?";
		Object values[] = { group.getName(), group.getOldName() };
		int types[] = { Types.VARCHAR, Types.VARCHAR };
		int count = jdbcTemplate.update(q, values, types);
		return count;
	}

	/**
	 *
	 * @param groupName
	 * @return
	 */
	public int deleteGroup(String groupName) {
		String delete = "DELETE FROM CREDENTIALS_GROUP WHERE GROUPNAME='" + groupName + "'";
		int count = jdbcTemplate.update(delete);
		return count;
	}

	/**
	 * 
	 * @param username
	 * @return
	 */
	public List<Group> getAssignedGroupsForUser(String username) {
		Object values[] = { username };
		int types[] = { Types.VARCHAR };
		List<Group> groupList = jdbcTemplate.query(LocalConstants.SQL.ASSIGNED_GROUPS_FOR_USER, values, types, new GroupModelMapper());
		return groupList;
	}

	/**
	 *
	 * @param user
	 * @param role
	 * @return
	 */
	public boolean createUserRoleMapping(UserCredentials user, String role) {
		try {
			java.sql.Date createTime = getSqlTime(new Date());
			Object values[] = { user.getUsername(), role, user.getCreatedBy(), createTime };
			int types[] = { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP };
			jdbcTemplate.update(LocalConstants.SQL.USER_ROLE_MAP_INSERT, values, types);
		} catch (Exception e) {
			Log.error("error while creating role: (), mapping for use: {}", role, user.getUsername(), e);
			return false;
		}
		return true;
	}

	/**
	 *
	 * @param userName
	 * @param roleName
	 * @return
	 */
	public int deleteUserRoleMapping(String userName, String roleName) {
		String delete = "DELETE FROM USER_ROLE_MAP WHERE USERNAME='" + userName + "' AND ROLENAME='" + roleName + "'";
		int count = jdbcTemplate.update(delete);
		return count;
	}

	/**
	 *
	 * @param userName
	 * @param roleName
	 * @return
	 */
	public int deleteUserRoleMapping(String userName) {
		String delete = "DELETE FROM USER_ROLE_MAP WHERE USERNAME='" + userName + "'";
		int count = jdbcTemplate.update(delete);
		return count;
	}

	/**
	 *
	 * @param user
	 * @param group
	 * @return
	 */
	public boolean createUserGroupMapping(UserCredentials user, String group) {
		java.sql.Date createTime = getSqlTime(new Date());
		Object values[] = { user.getUsername(), group, user.getCreatedBy(), createTime };
		int types[] = { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP };
		jdbcTemplate.update(LocalConstants.SQL.USER_GROUP_MAP_INSERT, values, types);
		return true;
	}

	/**
	 *
	 * @param userName
	 * @param groupName
	 * @return
	 */
	public int deleteUserGroupMapping(String userName, String groupName) {
		String delete = "DELETE FROM USER_GROUP_MAP WHERE USERNAME='" + userName + "' AND GROUPNAME'" + groupName + "'";
		int count = jdbcTemplate.update(delete);
		return count;
	}

	/**
	 *
	 * @param userName
	 * @param groupName
	 * @return
	 */
	public int deleteUserGroupMapping(String userName) {
		String delete = "DELETE FROM USER_GROUP_MAP WHERE USERNAME='" + userName + "'";
		int count = jdbcTemplate.update(delete);
		return count;
	}

	/**
	 *
	 * @param user
	 * @param nodeName
	 * @return
	 */
	public boolean createUserNodeMapping(UserCredentials user, String nodeName) {
		try {
			java.sql.Date createTime = getSqlTime(new Date());
			Object values[] = { user.getUsername(), nodeName, user.getCreatedBy(), createTime };
			int types[] = { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP };
			jdbcTemplate.update(LocalConstants.SQL.USER_NODE_MAP_INSERT, values, types);
		} catch (Exception e) {
			Log.error("error while creating node: {} mapping for user: {}", nodeName, user.getUsername(), e);
			return false;
		}
		return true;
	}

	/**
	 *
	 * @param userName
	 * @param nodeName
	 * @return
	 */
	public int deleteUserNodeMapping(String userName, String nodeName) {
		String delete = "DELETE FROM USER_NODE_MAP WHERE USERNAME='" + userName + "' AND NODE_NAME='" + nodeName + "'";
		int count = jdbcTemplate.update(delete);
		return count;
	}

	/**
	 * 
	 * @param userName
	 * @return
	 */
	public int deletAlleUserNodeMapping(String userName) {
		String delete = "DELETE FROM USER_NODE_MAP WHERE USERNAME='" + userName + "'";
		int count = jdbcTemplate.update(delete);
		return count;
	}

	/**
	 * 
	 * @param nodeName
	 * @return
	 */
	public int deletAllNodeUserMapping(String nodeName) {
		String delete = "DELETE FROM USER_NODE_MAP WHERE NODE_NAME='" + nodeName + "'";
		int count = jdbcTemplate.update(delete);
		return count;
	}

	/**
	 * 
	 * @param username
	 * @return
	 */
	public List<String> getNodeListForUser(String username) {
		String q = "SELECT  NODE_NAME FROM USER_NODE_MAP WHERE USERNAME='" + username + "'";
		return jdbcTemplate.queryForList(q, String.class);
	}

	/**
	 * 
	 * @param cfgid
	 * @param value
	 */
	public void createConfiguration(String cfgid, String value) {
		Object values[] = { cfgid, value };
		int types[] = { Types.VARCHAR, Types.VARCHAR };
		try {
			jdbcTemplate.update(LocalConstants.SQL.CREATE_CONFIG, values, types);
		} catch (Exception e) {
			Log.error("error while creating configuration: {}:{}", cfgid, value, e);
		}
	}

	/**
	 * 
	 * @param cfgid
	 * @param value
	 */
	public void updateConfiguration(String cfgid, String value) {
		Object values[] = { value, cfgid };
		int types[] = { Types.VARCHAR, Types.VARCHAR };
		jdbcTemplate.update(LocalConstants.SQL.UPDATE_CONFIG, values, types);
	}

	/**
	 * 
	 * @param cfgid
	 */
	public void deleteConfiguration(String cfgid) {
		String q = "DELETE FROM CONFIG WHERE CFGID='" + cfgid + "'";
		jdbcTemplate.update(q);
	}

	/**
	 * 
	 * @param cfgid
	 */
	public String getConfiguration(String cfgid) {
		String q = "SELECT VAL FROM CONFIG WHERE CFGID='" + cfgid + "'";
		return jdbcTemplate.queryForObject(q, String.class);
	}

	/**
	 * 
	 * @param field
	 * @param keyWord
	 */
	public void saveUpdateSearchKeywords(SearchHelpKeyword keyword) {
		Object object[] = { keyword.getField(), keyword.getKeyWord() };
		int types[] = { Types.VARCHAR, Types.VARCHAR };

		int exists = jdbcTemplate.queryForObject(LocalConstants.SQL.AUTOPOPULATE_EXISTS, object, types, Integer.class);
		if (exists == 0) {
			java.sql.Date timestamp = new java.sql.Date(new Date().getTime());
			object = new Object[] { keyword.getField(), keyword.getKeyWord(), 1,keyword.getUser(),timestamp };
			types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.INTEGER,Types.VARCHAR,Types.TIMESTAMP };
			jdbcTemplate.update(LocalConstants.SQL.INSERT_AUTO_POPULATE_KEYWORD, object, types);
		} else {
			jdbcTemplate.update(LocalConstants.SQL.UPDATE_FRQUENCY_OF_AUTO_POPULATE, object, types);
		}
	}

	/**
	 * 
	 * @param field
	 * @param keyWord
	 * @return
	 */
	public List<SearchHelpKeyword> getHelpSearchKeywords(String field) {
		List<SearchHelpKeyword> list;
		Object[] obj = { field };
		int[] types = { Types.VARCHAR };
		list = jdbcTemplate.query(LocalConstants.SQL.INSERT_AUTO_POPULATE_KEYWORD, obj, types, new SearchHelpKeywordMapper());
		return list;
	}

	/**
	 *
	 * @param model
	 * @return
	 */
	private String createQuery(DaoSearchModel model) {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT * FROM AGENTS AGENT ");
		boolean labelPresent = false;
		if (model.getSearchOnLabels() != null && model.getSearchOnLabels().size() > 0) {
			queryBuilder.append(", LABELS LABEL ");
			queryBuilder.append(" WHERE AGENT.NODE_NAME = LABEL.NODE_NAME AND ");
			queryBuilder.append("LABEL.LABEL_NAME IN (");
			queryBuilder.append(inClause(model.getSearchOnLabels()));
			queryBuilder.append(")");
		}
		if (labelPresent) {
			queryBuilder.append(" AND DATEDIFF('minute',CURRENT_TIMESTAMP,AGENT.LAST_HEARTBEAT) > 10)");
		}
		queryBuilder.append(";");
		return queryBuilder.toString();
	}

	/**
	 * 
	 * @param nodeName
	 * @return
	 */
	private boolean ifNodeExists(String nodeName) {
		String q = "SELECT * FROM AGENTS WHERE NODE_NAME='" + nodeName.toLowerCase() + "'";
		Log.trace("checking agent: {} in database", nodeName);
		List l = jdbcTemplate.queryForList(q);
		if (l == null) {
			Log.debug("node: {} do not exist in databse ", nodeName);
			return false;
		}
		if (l.size() == 0) {
			Log.debug("node: {} do not exist in databse ", nodeName);
			return false;
		}
		Log.debug("node: {} already exists in databse ", nodeName);
		return true;
	}

	/**
	 *
	 * @param arg
	 * @return
	 */
	private String inClause(List<String> arg) {
		String str = "";
		for (String s : arg) {
			str = str + "'" + s + "',";
			Log.debug(str);
		}
		str = str.substring(0, str.length() - 1);
		return str;
	}

	/**
	 *
	 * @param date
	 * @return
	 */
	private java.sql.Date getSqlTime(Date date) {
		if (date == null) {
			return new java.sql.Date((new Date()).getTime());
		} else {
			return new java.sql.Date(date.getTime());
		}
	}

	/**
	 * 
	 * @return
	 */
	public boolean isDbBlank() {
		String q = "SELECT * FROM CREDENTIALS_ROLE";
		List l = jdbcTemplate.queryForList(q);
		if (l == null || l.size() == 0) {
			return true;
		}
		return false;
	}
	/**
	 * 
	 * @param role
	 * @throws Exception 
	 */
	public void dbPatchJuly2018(UserCredentials user) throws Exception {
		Log.info("Applying patch version 4.0, release July 2018");
		try {
		Log.info("Trying to add column in table ");
		String alterRoleTable = "ALTER TABLE CREDENTIALS_ROLE add visible BOOLEAN";
		jdbcTemplate.execute(alterRoleTable);
		Log.info("new column sucessfully added to roles table");
		String update = "UPDATE CREDENTIALS_ROLE SET VISIBLE = TRUE";
		jdbcTemplate.update(update);
		Log.info("updated all roles with default value, for new column");		
		}catch(Exception e) {
			Log.error("cannot add column for patch",e);
			throw e;
		}
		try {
			Object values[] = { LocalConstants.ROLE.BOT,  LocalConstants.ROLE.BOT_DESC,false};
			int types[] = { Types.VARCHAR, Types.VARCHAR, Types.BOOLEAN};
			jdbcTemplate.update(LocalConstants.SQL.ROLE_INSERT_PATCH_JULY2018, values, types);
			Log.info("added new role 'robot', in non primary mode");	
			createUserRoleMapping(user, LocalConstants.ROLE.BOT);
			Log.info("mapped defualt user to new role 'robot'");
		} catch (Exception e) {
			Log.error("error while creating robot role, patching has failed", e);
			throw e;
		}
	}

}
