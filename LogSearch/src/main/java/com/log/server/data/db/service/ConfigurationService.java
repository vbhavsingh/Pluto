package com.log.server.data.db.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.log.analyzer.commons.model.AgentConfigurationModel;
import com.log.analyzer.commons.model.AgentModel;
import com.log.analyzer.commons.model.AgentRegistrationForm;
import com.log.server.LocalConstants;
import com.log.server.data.db.entity.Label;
import com.log.server.data.db.entity.Node;
import com.log.server.data.db.entity.SearchAssistant;
import com.log.server.data.db.entity.UserCredential;
import com.log.server.data.db.entity.UserGroup;
import com.log.server.data.db.entity.UserNodeMap;
import com.log.server.data.db.entity.UserRole;
import com.log.server.data.db.entity.UserRoleMap;
import com.log.server.data.db.entity.pk.LabelKey;
import com.log.server.data.db.entity.pk.NodeMappingPK;
import com.log.server.data.db.repository.CredentialsRoleRepository;
import com.log.server.data.db.repository.LabelRepository;
import com.log.server.data.db.repository.NodeRepository;
import com.log.server.data.db.repository.SearchAssistantRepository;
import com.log.server.data.db.repository.UserGroupRepository;
import com.log.server.data.db.repository.UserNodeMapRepository;
import com.log.server.data.db.repository.UserRepository;
import com.log.server.data.db.repository.UserRoleMappingRepository;
import com.log.server.model.DaoSearchModel;
import com.log.server.model.Group;
import com.log.server.model.NodeAgentViewModel;
import com.log.server.model.SearchHelpKeyword;
import com.log.server.util.Utilities;
import com.security.common.PlutoSecurityPrinicipal;

@Component
public class ConfigurationService {

	private static final Logger Log = LoggerFactory.getLogger(ConfigurationService.class);

	@Autowired
	LabelRepository labelRepository;

	@Autowired
	SearchAssistantRepository searchAssistantRepository;

	@Autowired
	NodeRepository nodeRepository;

	@Autowired
	UserNodeMapRepository userNodeMapRepository;

	@Autowired
	UserRoleMappingRepository userRoleMappingRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserGroupRepository userGroupRepository;

	@Autowired
	CredentialsRoleRepository credentialsRoleRepository;

	public boolean isDbBlank() {
		return credentialsRoleRepository.count() > 0 ? false : true;
	}

	public List<String> getLabels() {
		return labelRepository.findAll().stream().map(Label::getLabelKey).map(LabelKey::getLabelName)
				.collect(Collectors.toList());
	}

	public List<String> getLabelsByNode(String nodeName) {
		return labelRepository.getLabelsByNode(nodeName).stream().map(Label::getLabelName).collect(Collectors.toList());
	}

	public Map<String, Long> getLabelCountByLabelName() {
		return labelRepository.findAll().stream()
				.collect(Collectors.groupingBy(Label::getLabelName, Collectors.counting()));
	}

	public List<String> getPreviousSearchCriterias(String fieldName) {
		return searchAssistantRepository.getPreviousSearchCriterias(fieldName);
	}

	public void saveUpdateSearchKeywords(SearchHelpKeyword keyword) {
		SearchAssistant entity = new SearchAssistant();
		entity.setField(keyword.getField());
		entity.setKeyText(keyword.getKeyWord());
		entity.setUser(new UserCredential(keyword.getUser()));

		searchAssistantRepository.save(entity);
	}

	public AgentConfigurationModel getAgentConfig(String nodeName) {
		Optional<Node> entity = nodeRepository.findById(nodeName);
		if (entity.isPresent()) {
			return mapNodeEntitytoConfigModel(entity.get());
		}
		return null;
	}

	public List<String> getNonMappedUsersForNode(String nodeName) {
		return userNodeMapRepository.getNonMappedUsersForNode(nodeName);
	}

	public AgentRegistrationForm getAgentRegistrationForm(String nodeName) {
		Optional<Node> entity = nodeRepository.findById(nodeName);
		if (entity.isPresent()) {
			return mapNodeEntitytoAgentRegistrationForm(entity.get());
		}
		return new AgentRegistrationForm();
	}

	public AgentModel getAgentModel(String nodeName) {
		Optional<Node> entity = nodeRepository.findById(nodeName);
		if (entity.isPresent()) {
			return mapNodeEntitytoAgentModel(entity.get());
		}
		return new AgentModel();
	}

	public int createAgentLabelMapping(String nodeName, String labelText) {
		Optional<Node> nodeEntity = nodeRepository.findById(nodeName);
		if (nodeEntity.isPresent()) {
			Node entity = nodeEntity.get();
			entity.addLabels(labelText);
			Label labelEntity = new Label(entity, labelText);	
			labelRepository.save(labelEntity);
			return 1;
		}
		return 0;
	}

	public int deleteAgentLabelMapping(String nodeName, String labelText) {
		Optional<Node> nodeEntity = nodeRepository.findById(nodeName);
		if (nodeEntity.isPresent()) {
			Node entity = nodeEntity.get();
			entity.getLabels().removeIf(label -> label.getLabelName().equals(labelText));
			nodeRepository.save(entity);
			return 1;
		}
		return 0;
	}

	public List<String> getAllUsersOnNode(String nodeName) {
		return userNodeMapRepository.getAllUsersOnNode(nodeName);
	}

	public List<AgentModel> getClients(DaoSearchModel model) {
		if (model.getSearchOnLabels() != null && model.getSearchOnLabels().size() > 0) {
			List<Label> labelList = model.getSearchOnLabels().stream().map(labelText -> new Label(null, labelText))
					.collect(Collectors.toList());
			List<AgentModel> agentModelList = nodeRepository.getNodesByLabelNames(labelList).stream()
					.map(entity -> mapNodeEntitytoAgentModel(entity)).collect(Collectors.toList());
			return agentModelList;
		}

		return nodeRepository.findAll().stream().map(entity -> mapNodeEntitytoAgentModel(entity))
				.collect(Collectors.toList());
	}

	public List<NodeAgentViewModel> getAgentListForUser(String user) {
		/* find the role of the user */
		List<String> roleList = userRoleMappingRepository.getRolesOfAnUser(user).stream().map(UserRoleMap::getRole)
				.map(UserRole::getRoleName).collect(Collectors.toList());

		if (roleList.contains(LocalConstants.ROLE.ROLES.ADMIN.roleName)) {
			return nodeRepository.findAll().stream().map(entity -> mapNodeEntitytoNodeAgentViewModel(entity))
					.collect(Collectors.toList());
		} else {
			Optional<UserCredential> userOptionalEntity = userRepository.findById(user);
			if (userOptionalEntity.isPresent()) {
				List<String> nodeNameList = userOptionalEntity.get().getNodeMappingList().stream()
						.map(UserNodeMap::getNodeMappingPK).map(NodeMappingPK::getNodeName)
						.collect(Collectors.toList());
				return nodeRepository.findAllById(nodeNameList).stream()
						.map(entity -> mapNodeEntitytoNodeAgentViewModel(entity)).collect(Collectors.toList());
			}
			return null;
		}
	}

	public List<NodeAgentViewModel> getAgentListForAdministration(PlutoSecurityPrinicipal security) {
		/*
		 * If security is NULL or ADMIN give back all agents, this will be case when a
		 * new user need to be mapped against the present nodes on system
		 */
		if (security == null || LocalConstants.ROLE.ROLES.ADMIN.roleName.equals(security.getAssignedRole())) {
			List<Node> nodeList = nodeRepository.findAll();
			return nodeList.stream().map(node -> mapNodeEntitytoNodeAgentViewModel(node)).collect(Collectors.toList());
		}

		if (LocalConstants.ROLE.ROLES.GROUP_ADMIN.roleName.equals(security.getAssignedRole())) {
			List<String> groupList = security.getAssignedGroups().stream().map(Group::getName)
					.collect(Collectors.toList());
			List<String> userList = new ArrayList<String>();
			for (String groupname : groupList) {
				Optional<UserGroup> userGroupOptional = userGroupRepository.findById(groupname);
				if (userGroupOptional.isPresent()) {
					userList.addAll(userGroupOptional.get().getUserGoupMapList().stream()
							.map(m -> m.getUser().getUserName()).collect(Collectors.toList()));
				}
			}
			List<Node> nodeList = userNodeMapRepository.getMappedNodesForUserList(userList);
			List<NodeAgentViewModel> returnList = nodeList.stream().map(n -> mapNodeEntitytoNodeAgentViewModel(n))
					.collect(Collectors.toList());

			return returnList;
		}
		return getAgentListForUser(security.getUsername());
	}

	public void recordHeartBeat(String nodeName) {
		java.sql.Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		nodeRepository.recordHeartBeat(nodeName, timestamp);
	}

	public void saveAgent(AgentRegistrationForm model) {
		String nodeName = model.getClientNode().toLowerCase();
		String agentName = model.getClientName().toLowerCase();
		Optional<Node> optionalEntity = nodeRepository.findById(model.getClientNode());
		Node entity = mapNodeModeltoEntity(model, optionalEntity.isPresent() ? optionalEntity.get() : null);
		nodeRepository.save(entity);
		Log.debug("saving agent name & node as primary search labels");

		if (optionalEntity.isPresent()) {
			Log.info("node: {} already exists, just updating parameters", nodeName);
		} else {
			Log.info("node does not exist, registering node: {} as new node", nodeName);
			/* Registering node name or fqdn as Label */
			Label labelEntityNodeName = new Label(entity, Utilities.getNameFromFqdn(nodeName));
			labelRepository.save(labelEntityNodeName);
			/* Registering agent given name as a Label */
			if (!nodeName.equals(agentName)) {
				Label labelEntityAgentName = new Label(entity, Utilities.getNameFromFqdn(agentName));
				labelRepository.save(labelEntityAgentName);
			}
		}
	}

	public int updateNodeAgent(NodeAgentViewModel agent) {
		Node nodeEntity = nodeRepository.getById(agent.getNodeName());

		nodeEntity.setAgentName(agent.getAgentName());
		nodeEntity.setParallel(agent.getParallelism());
		nodeEntity.setMaxFilesToSearch(agent.getMaxFilesToSearch());
		nodeEntity.setMaxLinesPerFile(agent.getMaxLinesPerFile());
		nodeEntity.setMaxLinesInResult(agent.getMaxLinesInresult());
		nodeEntity.setResultSizeinKB(agent.getResultInKb());

		nodeRepository.save(nodeEntity);
		return 1;
	}

	public int deleteNode(String nodeName) {
		nodeRepository.deleteById(nodeName);
		return 1;
	}

	private Node mapNodeModeltoEntity(AgentRegistrationForm model, Node entity) {
		if (entity == null) {
			entity = new Node();
			entity.setNodeName(model.getClientNode().toLowerCase());
			entity.setAgentName(model.getClientName().toLowerCase());
			entity.setCommKey(model.getCommKey());
			entity.setOsType(model.getOsName());
		}

		entity.setNodePort(Integer.parseInt(model.getClientConnectPort()));
		entity.setNodeTimeZone(model.getTimeZone());
		entity.setAgentVersion(model.getAgentVersion());

		return entity;
	}

	private AgentConfigurationModel mapNodeEntitytoConfigModel(Node entity) {
		AgentConfigurationModel model = new AgentConfigurationModel();

		model.setCurrentAgentVersion(entity.getAgentVersion());
		model.setFindCommand(model.getFindCommand());
		model.setMaxDataInKb(entity.getResultSizeinKB());
		model.setMaxFilesToBeSearched(entity.getMaxFilesToSearch());
		model.setMaxLinesAllowedInResult(entity.getMaxLinesInResult());
		model.setMaxLinesAllowedPerFile(entity.getMaxLinesPerFile());

		return model;
	}

	private AgentRegistrationForm mapNodeEntitytoAgentRegistrationForm(Node entity) {
		AgentRegistrationForm model = new AgentRegistrationForm();

		model.setAgentVersion(entity.getAgentVersion());
		model.setClientConnectPort(String.valueOf(entity.getNodePort()));
		model.setClientLabels(entity.getLabels().stream().map(Label::getLabelName).collect(Collectors.toList()));
		model.setClientName(entity.getAgentName());
		model.setClientNode(entity.getNodeName());
		model.setCommKey(entity.getCommKey());
		model.setOsName(entity.getOsType());
		model.setTimeZone(entity.getNodeTimeZone());

		return model;
	}

	private AgentModel mapNodeEntitytoAgentModel(Node entity) {
		AgentModel model = new AgentModel();

		model.setClientConnectPort(entity.getNodePort());
		model.setClientLabels(entity.getLabels().stream().map(Label::getLabelName).collect(Collectors.toList()));
		model.setClientName(entity.getAgentName());
		model.setClientNode(entity.getNodeName());
		model.setCommKey(entity.getCommKey());
		model.setFileNameToSearch(getLabels());
		model.setOsName(entity.getOsType());
		model.setTimeZone(entity.getNodeTimeZone());
		model.setThreads(entity.getParallel());

		return model;
	}

	private NodeAgentViewModel mapNodeEntitytoNodeAgentViewModel(Node entity) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z");

		NodeAgentViewModel model = new NodeAgentViewModel();
		model.setAgentName(entity.getAgentName());
		model.setOsName(entity.getOsType());
		model.setLastHeartbeat(sdf.format(entity.getLastHeartbeat()));
		model.setLastModifed(entity.getLastModified());
		model.setMaxFilesToSearch(entity.getMaxFilesToSearch());
		model.setMaxLinesInresult(entity.getMaxLinesInResult());
		model.setMaxLinesPerFile(entity.getMaxLinesPerFile());
		model.setNodeName(entity.getNodeName());
		model.setParallelism(entity.getParallel());
		model.setPort(entity.getNodePort());
		model.setResultInKb(entity.getResultSizeinKB());
		model.setVersion(entity.getAgentVersion());

		try {
			long diff = (new Date()).getTime() - sdf.parse(model.getLastHeartbeat()).getTime();
			long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
			if (minutes > LocalConstants.AGENT_DEAD_MINUTES) {
				model.setAlive(false);
			}
			if (minutes < 60) {
				String literal = minutes == 1 ? " minute" : " minutes";
				model.setDeadSince(minutes + literal);
			}
			if (minutes >= 60 && minutes < 24 * 60) {
				minutes = (int) (Math.ceil(minutes / 60));
				String literal = minutes == 1 ? " hour" : " hours";
				model.setDeadSince(minutes + literal);
			}
			if (minutes >= 24 * 60) {
				minutes = (int) Math.ceil(minutes / (24 * 60));
				String literal = minutes == 1 ? " day" : " days";
				model.setDeadSince(minutes + literal);
			}
		} catch (ParseException e) {
			Log.error("error while determining whether client is alive, by checking its last heartbeat pulse", e);
		}

		return model;
	}
}
