package com.log.server.concurrent;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.log.server.comm.http.VerifyUserCall;
import com.log.server.data.db.service.UserDataService;
import com.log.server.model.NodeAgentViewModel;
import com.log.server.model.UserCredentialsModel;

public class MapNodeWithUsers implements Runnable {

	private static final Logger Log = LoggerFactory.getLogger(MapNodeWithUsers.class);

	private VerifyUserCall call = new VerifyUserCall();

	private List<String> userList;

	private NodeAgentViewModel node;
	
	@Autowired
	private UserDataService userDataService;

	public MapNodeWithUsers(List<String> userList, NodeAgentViewModel node) {
		super();
		this.node = node;
		this.userList = userList;
	}

	@Override
	public void run() {
		/*Wait for 30 seconds so that agent get online properly*/
		try {
			Log.trace("sleeping for 2 minutes before starting user verification against the new server, this will give charon time to boot up");
			Thread.sleep(2*60*1000);
		} catch (InterruptedException e) {
			Log.error("error while making user verification thread to go sleep",e);
		}
		List<String> validatedUsers = call.verifyUserList(node, userList);
		if (validatedUsers != null) {
			for (String user : validatedUsers) {
				UserCredentialsModel u=new UserCredentialsModel();
				u.setUsername(user);
				u.setCreatedBy("AUTO");
				userDataService.createUserNodeMapping(u, node.getNodeName());
			}
		}

	}
}
