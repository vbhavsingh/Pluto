/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.server.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.log.analyzer.commons.Constants;
import com.log.analyzer.commons.model.AgentTerminatorRequestModel;
import com.log.server.LocalConstants;
import com.log.server.SpringHelper;
import com.log.server.biz.AdminServices;
import com.log.server.model.AddEditUserModel;
import com.log.server.model.AgentLabelMapModel;
import com.log.server.model.Group;
import com.log.server.model.NodeAgentViewModel;
import com.log.server.model.UserCredentials;
import com.security.common.PlutoSecurityPrinicipal;

import net.rationalminds.es.EnvironmentalControl;

/**
 *
 * @author Vaibhav Singh
 */
@Controller
public class Registration {
	private static final Logger Log = LoggerFactory.getLogger(Registration.class);
	private AdminServices svc = (AdminServices) SpringHelper.getBean("AdminServices");

	@RequestMapping(value = "/secure/adduserview.htm")
	@PreAuthorize("hasAuthority('"+LocalConstants.ROLE.ADMIN+"') or hasAuthority('"+LocalConstants.ROLE.GROUP_ADMIN+"')")
	public ModelAndView getAddUserPage() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		PlutoSecurityPrinicipal security = (PlutoSecurityPrinicipal) auth.getPrincipal();
		AddEditUserModel model = new AddEditUserModel();
		model.setAssignableGroups(svc.getAllApplicableGroups(security));
		model.setAssignableRoles(svc.getAllApplicaleRole(security));
		ModelAndView mv = new ModelAndView("../admin/views/adduser", "model", model);
		LocalConstants.USER_ACCESS_LOG.info("{} : add user view request from user", security.getUser());
		return mv;
	}

	/**
	 * 
	 * @param request
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/secure/adduser.htm", produces = "text/html")
	@ResponseBody
	@PreAuthorize("hasAuthority('"+LocalConstants.ROLE.ADMIN+"') or hasAuthority('"+LocalConstants.ROLE.GROUP_ADMIN+"')")
	public String addUser(@Valid UserCredentials user) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		PlutoSecurityPrinicipal security = (PlutoSecurityPrinicipal) auth.getPrincipal();
		user.setCreatedBy(security.getUsername());
		Log.info("request recieved to add user: {} from: {}" ,user.getUsername(), security.getUsername());
		Log.trace("request recieved to add user: {} from: {}" ,user, security);
		LocalConstants.USER_ACCESS_LOG.info("{}: requested to add new user: {}",security.getUsername(),user.getUsername());
		String result = LocalConstants.FAILED;
		try {
			result = svc.addUser(user);
		} catch (Exception e) {
			if (e instanceof DuplicateKeyException) {
				result = LocalConstants.DATA_EXISTS;
				Log.warn("user: {}, already exists",user.getUsername(),e);
			}
			Log.error("error occoured while creating new user: {}, by user: {}",user.getUsername(),security.getUsername(),e);
			LocalConstants.USER_ACCESS_LOG.error("{} : error occoured while creating new user: {}",security.getUsername(),user.getUsername(),e);
		}
		if (LocalConstants.SUCCESS.equals(result)) {
			String message = "User " + user.getUsername() + " registered successfully.";
			LocalConstants.USER_ACCESS_LOG.info("{} : succesfully created new user: {}",security.getUsername(),user.getUsername());
			return message;
		} else if (LocalConstants.DATA_EXISTS.equals(result)) {
			String message = "User " + user.getUsername() + " is already registered.";
			Log.info(message);
			return message;
		}
		String message = "Some problem occured while registering user " + user.getUsername() + ". Please contact administrator.";
		return message;
	}

	/**
	 * 
	 * @param request
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/secure/updateprofile.htm", produces = "text/html")
	@ResponseBody
	public String saveProfileChanges(@Valid UserCredentials user) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		PlutoSecurityPrinicipal security = (PlutoSecurityPrinicipal) auth.getPrincipal();
		LocalConstants.USER_ACCESS_LOG.info("{} : made request to update user: {}",security.getUsername(),user.getUsername());
		if (security.getAssignedRole().equals(LocalConstants.ROLE.GROUP_MEMBER)) {
			if (!user.getUsername().equals(security.getUsername())) {
				LocalConstants.USER_ACCESS_LOG.warn("{} : security violation : not permitted to update user profile for user : {} ",security.getUsername(),user.getUsername());
				return "unauthrozied access, security breach attempted";
			}
		}
		Log.info("request to update user : {}  from : {}",user.getUsername(),security.getUsername());
		Log.trace("request to update user : {}  from : {}",user,security.getUsername());
		String result = LocalConstants.FAILED;
		try {
			result = svc.updateUser(user);
		} catch (Exception e) {
			Log.error("error while updating user:{}, by : {}",user.getUsername(),security.getUsername(),e);
			LocalConstants.USER_ACCESS_LOG.error("{} : error while updating profile of user: {}",security.getUsername(),user.getUsername(),e);
		}
		if (LocalConstants.SUCCESS.equals(result)) {
			String message = user.getUsername() + " updated successfully.";
			Log.info(message);
			return message;
		}
		String message = "Some problem occured while updating user " + user.getUsername() + ". Please contact administrator.";
		Log.info(message);
		return message;
	}

	/**
	 * 
	 * @param request
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/secure/addgroup.htm", produces = "text/html")
	@ResponseBody
	@PreAuthorize("hasAuthority('"+LocalConstants.ROLE.ADMIN+"') or hasAuthority('"+LocalConstants.ROLE.GROUP_ADMIN+"')")
	public String addGroup(Group group) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		PlutoSecurityPrinicipal security = (PlutoSecurityPrinicipal) auth.getPrincipal();
		group.setCreatedBy(security.getUsername());
		Log.info("request to add group: {}, by user: {} ",group.getName(),security.getUsername());
		Log.trace("request to add group: {}, by user: {} ",group,security.getUsername());
		LocalConstants.USER_ACCESS_LOG.info("{} : request to add new group : {}",security.getUsername(),group.getName());
		String result = svc.addGroup(group);
		if (LocalConstants.SUCCESS.equals(result)) {
			String message = "Group " + group.getName() + " enrolled successfully.";
			Log.info(message);
			return message;
		} else if (LocalConstants.DATA_EXISTS.equals(result)) {
			String message = "Group " + group.getName() + " is already present in system. Use a different group name.";
			LocalConstants.USER_ACCESS_LOG.warn("{} : request to add new group : {} failed, group already exists",security.getUsername(),group.getName());
			Log.info(message);
			return message;
		}
		String message = "Some problem occured while creating group " + group.getName() + ". Please contact administrator.";
		Log.info(message);
		return message;

	}

	/**
	 * 
	 * @param request
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/secure/updategroup.htm", produces = "text/html")
	@ResponseBody
	@PreAuthorize("hasAuthority('"+LocalConstants.ROLE.ADMIN+"') or hasAuthority('"+LocalConstants.ROLE.GROUP_ADMIN+"')")
	public String updateGroup(Group group) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		PlutoSecurityPrinicipal security = (PlutoSecurityPrinicipal) auth.getPrincipal();
		group.setModifiedBy(security.getUsername());
		Log.info("request recieved to update group: {}, from: {} ",group.getName(),security.getUsername());
		LocalConstants.USER_ACCESS_LOG.info("{} : request to update group : {}",security.getUsername(),group.getName());
		String message;
		try {
			String result = svc.updateGroup(group);
			message = "Group updated successfully";
			LocalConstants.USER_ACCESS_LOG.info("{} : request to update group : {}, is sucessful",security.getUsername(),group.getName());
		} catch (Exception e) {
			message = "Error while updating group " + group.getOldName();
			Log.error(message + " attempted by: {}",security.getUsername(), e);
			LocalConstants.USER_ACCESS_LOG.error("{} : error updating group: {}",security.getUsername(),group.getName(),e);
		}
		return message;

	}

	/**
	 * 
	 * @param request
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/secure/deletegroup/{groupname}", produces = "text/html")
	@ResponseBody
	@PreAuthorize("hasAuthority('"+LocalConstants.ROLE.ADMIN+"') or hasAuthority('"+LocalConstants.ROLE.GROUP_ADMIN+"')")
	public String deleteGroup(@PathVariable String groupname) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		PlutoSecurityPrinicipal security = (PlutoSecurityPrinicipal) auth.getPrincipal();
		Log.info("request recieved to delete group: {}, from user :{} ",groupname,security.getUsername());
		LocalConstants.USER_ACCESS_LOG.info("{} : request to delete group: {}",security.getUsername(),groupname);
		try {
			svc.deleteGroup(groupname);
			Log.info("group: {} , deleted by user: {} ",groupname,security.getUsername());
			LocalConstants.USER_ACCESS_LOG.info("{} : request to delete group: {}, is successful",security.getUsername(),groupname);
			return LocalConstants.SUCCESS;
		} catch (Exception e) {
			Log.error("failed to delete group: {}, attempted by user: {}",groupname, security.getUsername(), e);
			LocalConstants.USER_ACCESS_LOG.error("{} : request to delete group: {}, failed",security.getUsername(),groupname,e);
			return LocalConstants.FAILED;
		}
	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/secure/fetchgroups", produces = "application/json")
	@ResponseBody
	@PreAuthorize("hasAuthority('"+LocalConstants.ROLE.ADMIN+"') or hasAuthority('"+LocalConstants.ROLE.GROUP_ADMIN+"')")
	public List<Group> getGroupListForAdministration() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		PlutoSecurityPrinicipal security = (PlutoSecurityPrinicipal) auth.getPrincipal();
		LocalConstants.USER_ACCESS_LOG.info("{} : request to fetch group list",security.getUsername());
		List<Group> gpList = svc.getAllApplicableGroups(security);
		return gpList;
	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/secure/fetchusers", produces = "application/json")
	@ResponseBody
	public List<UserCredentials> getUserListForAdministration() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		PlutoSecurityPrinicipal security = (PlutoSecurityPrinicipal) auth.getPrincipal();
		Log.info("request recieved to fetch user list from user: {} ", security.getUsername());
		LocalConstants.USER_ACCESS_LOG.info("{} : request to fetch user list for admin purpose",security.getUsername());
		List<UserCredentials> userList = svc.getUserListForAuthority(security);
		return userList;
	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping("/secure/profile")
	public ModelAndView getProfile() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		PlutoSecurityPrinicipal security = (PlutoSecurityPrinicipal) auth.getPrincipal();
		Log.info("request recieved from user: {} to get profile of self: ",security.getUsername());
		LocalConstants.USER_ACCESS_LOG.info("{} : request to fetch profile of self",security.getUsername());
		AddEditUserModel model = new AddEditUserModel();
		model.setAssignableGroups(svc.getAllApplicableGroups(security));
		model.setAssignableRoles(svc.getAllApplicaleRole(security));
		ModelAndView mv = new ModelAndView("../admin/views/edituser", "model", model);
		return mv;
	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/secure/delete/{username}", produces = "text/html")
	@ResponseBody
	@PreAuthorize("hasAuthority('"+LocalConstants.ROLE.ADMIN+"') or hasAuthority('"+LocalConstants.ROLE.GROUP_ADMIN+"')")
	public String deleteUser(@PathVariable String username) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		PlutoSecurityPrinicipal security = (PlutoSecurityPrinicipal) auth.getPrincipal();
		Log.info("request recieved to delete user: {} , from user: {}",username, security.getUsername());
		LocalConstants.USER_ACCESS_LOG.info("{} : request to delete user: {}",security.getUsername(),username);
		try {
			svc.deleteUser(username);
			return LocalConstants.SUCCESS;
		} catch (Exception e) {
			Log.error("error while deleting user: {}, as requested by user: {}",username,security.getUsername(),e);
			LocalConstants.USER_ACCESS_LOG.error("{} : error while deleting user: {}",security.getUsername(),username,e);
			return LocalConstants.FAILED;
		}
	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/secure/nodelist", produces = "application/json")
	@ResponseBody
	@EnvironmentalControl(devMethod="com.log.server.util.DevEnvironmentMocker.giveMeDummyNodes()")
	public List<NodeAgentViewModel> getNodeList() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		PlutoSecurityPrinicipal security = (PlutoSecurityPrinicipal) auth.getPrincipal();
		Log.info("request recieved to fetch node list from user: {}", security.getUser());
		LocalConstants.USER_ACCESS_LOG.info("{} : request to fetch node list",security.getUsername());
		return svc.getAgentListForAdministration(security);
	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/secure/updatenode", produces = "text/html")
	@ResponseBody
	public String updateNode(@Valid NodeAgentViewModel agent) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		PlutoSecurityPrinicipal security = (PlutoSecurityPrinicipal) auth.getPrincipal();
		Log.info("request recieved to update node: {} from user: {} ",agent.getNodeName(),security.getUsername());
		LocalConstants.USER_ACCESS_LOG.info("{} : request to update node: {}",security.getUsername(),agent.getNodeName());
		Log.info("request recieved to update node: {} from user: {} ",agent,security.getUsername());
		try {
			svc.updateNodeAgent(agent);
			return "agent properties updated";
		} catch (Exception e) {
			Log.error("error while updating node: {}, requested by user: {}",agent.getNodeName(),security.getUsername(),e);
			LocalConstants.USER_ACCESS_LOG.error("{} : error while updating node: {}",security.getUsername(),agent.getNodeName(),e);
			return LocalConstants.FAILED;
		}
	}
	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/secure/remove/{node}", produces = "text/html")
	@ResponseBody
	@PreAuthorize("hasAuthority('"+LocalConstants.ROLE.ADMIN+"')")
	public String deleteNode(@PathVariable String node,HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		PlutoSecurityPrinicipal security = (PlutoSecurityPrinicipal) auth.getPrincipal();
		Log.info("request recieved to delete node: {} from user: {} ",node,security.getUsername());
		LocalConstants.USER_ACCESS_LOG.info("{} : request to delete node: {}",security.getUsername(),node);
		try {
			AgentTerminatorRequestModel killRequest =new AgentTerminatorRequestModel();
			killRequest.setNodeName(node);
			killRequest.setUser(security.getUsername());
			killRequest.setSession(request.getSession().getId());
			killRequest.setOriginIp(request.getRemoteAddr());
			String result=svc.deRegisterAgent(killRequest);
			return result;
		} catch (Exception e) {
			Log.error("error while deleting node: {}, requested by user: {}",node,security.getUsername(),e);
			LocalConstants.USER_ACCESS_LOG.error("{} : error while deleting node: {}",security.getUsername(),node,e);
			return LocalConstants.FAILED +" : "+ e.getLocalizedMessage();
		}
	}
	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/secure/addnodelabelmap", produces = "text/html")
	@ResponseBody
	public String createLabelMapping(@Valid AgentLabelMapModel map) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		PlutoSecurityPrinicipal security = (PlutoSecurityPrinicipal) auth.getPrincipal();
		Log.info("request recieved to create agent label: {} for node: {}, by user: {} ",map.getLabelName(),map.getNodeName(),security.getUsername());
		LocalConstants.USER_ACCESS_LOG.info("{} : request to add label: {} for node: {}",security.getUsername(),map.getLabelName(),map.getNodeName());
		try {
			svc.createAgentLabelMapping(map);
			return "agent properties updated";
		} catch (Exception e) {
			Log.error("error while adding new label: {} for node: {} by user: {}",map.getLabelName(),map.getNodeName(),security.getUsername(),e);
			LocalConstants.USER_ACCESS_LOG.error("{} : error while adding new label: {} for node: {}",security.getUsername(),map.getLabelName(),map.getNodeName(),e);
			return LocalConstants.FAILED;
		}
	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/secure/deletenodelabelmap", produces = "text/html")
	@ResponseBody
	public String deleteLabelMapping(@Valid AgentLabelMapModel map) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		PlutoSecurityPrinicipal security = (PlutoSecurityPrinicipal) auth.getPrincipal();
		Log.info("request recieved to delete agent label: {} for node: {}, by user: {} ",map.getLabelName(),map.getNodeName(),security.getUsername());
		LocalConstants.USER_ACCESS_LOG.info("{} : request to delete label: {} for node: {}",security.getUsername(),map.getLabelName(),map.getNodeName());
		try {
			svc.deleteAgentLabelMapping(map);
			return "agent properties updated";
		} catch (Exception e) {
			Log.error("error while deleting new label: {} for node: {} by user: {}",map.getLabelName(),map.getNodeName(),security.getUsername(),e);
			LocalConstants.USER_ACCESS_LOG.error("{} : error while deleting new label: {} for node: {}",security.getUsername(),map.getLabelName(),map.getNodeName(),e);
			return LocalConstants.FAILED;
		}
	}
	/**
	 * to check whether the system is newly installed 
	 * @return
	 */
	@RequestMapping(value = "/open/fobcheck", produces = "text/html")
	@ResponseBody
	public String isNewInstallation(){
		String value=svc.getConfiguration(LocalConstants.KEYS.NEW_INSTALL);
		if(LocalConstants.TRUE.equals(value)){
			return LocalConstants.TRUE;
		}
		return LocalConstants.FALSE;
	}

}
