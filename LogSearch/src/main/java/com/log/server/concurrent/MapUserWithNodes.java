package com.log.server.concurrent;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.log.server.biz.AdminServices;
import com.log.server.comm.http.VerifyUserCall;
import com.log.server.data.db.service.UserDataService;
import com.log.server.model.NodeAgentViewModel;
import com.log.server.model.UserCredentialsModel;

public class MapUserWithNodes implements Runnable {
	
	/**
	 * This service associates newly added user object {@link UserCredentialsModel} with nodes already present in the system.
	 */

	private static final Logger Log = LoggerFactory.getLogger(MapUserWithNodes.class);
	
	private VerifyUserCall call = new VerifyUserCall();

	private UserCredentialsModel user;

	@Autowired
	private AdminServices svc;
	
	@Autowired
	private UserDataService userDataService;

	public MapUserWithNodes(UserCredentialsModel user, AdminServices svc) {
		this.svc = svc;
		this.user = user;
	}

	@Override
	public void run() {
		Log.info("automatically mapping user: {}, against all present nodes",this.user);
		List<NodeAgentViewModel> nodeList = svc.getAllAgentsForUserMapping();
		for (NodeAgentViewModel node : nodeList) {
			if (call.verifyUser(node, user.getUsername())) {
				userDataService.createUserNodeMapping(user, node.getNodeName());
				//svc.getDao().createUserNodeMapping(user, node.getNodeName());
			}
		}
		//userDataService.createUserNodeMapping(user, null);

	}

}
