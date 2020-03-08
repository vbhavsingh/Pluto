package com.log.server.comm.http;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.log.analyzer.commons.Constants;
import com.log.analyzer.commons.err.CommonErrorModel;
import com.log.analyzer.commons.model.SearchResultModel;
import com.log.server.model.NodeAgentViewModel;
import com.log.server.util.Utilities;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class VerifyUserCall {

	private final static Logger Log = LoggerFactory.getLogger(VerifyUserCall.class);
	private Gson gson = new Gson();

	/**
	 * 
	 * @param node
	 * @param userList
	 * @return
	 */
	public List<String> verifyUserList(NodeAgentViewModel node, List<String> userList) {
		Log.info("verifyiing existing users for automatic mapping for node: {}", node.getNodeName());
		if (userList == null || userList.size() == 0) {
			Log.info("no users in provided list, nothing to validate, returning null");
			return null;
		}
		String userJsonList = gson.toJson(userList);
		Log.debug("Calling node: {} for verifying user in list {}", node.getNodeName(), userJsonList);
		try {
			Client c = Client.create();

			WebResource resource = c.resource(Utilities.getResourceURL(node) + Constants.VALIDATE_USER_URI_SUFFIX);
			
			Log.trace("requesting user verification over post call with request body : {}",userJsonList);
			
			ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).header("Content-Type","application/json; charset=UTF-8").post(ClientResponse.class, userJsonList);
			String result = response.getEntity(String.class);

			if (response.getStatus() == 200) {
				String[] resultArray = gson.fromJson(result, String[].class);
				Log.trace("node: {} returned validated users: ", node.getNodeName(), Arrays.toString(resultArray));
				return Arrays.asList(resultArray);
			} else {
				CommonErrorModel error = gson.fromJson(result, CommonErrorModel.class);
				if(error == null){
					Log.error("cannot verify users on node {}, error is {}", node.getNodeName(), response.toString());
					return null;
				}
				Log.error("cannot verify users on node {}, error is {}", node.getNodeName(), error);
				return null;
			}
		} catch (Exception e) {
			SearchResultModel resultModel = new SearchResultModel();
			resultModel.setNodeName(node.getNodeName());
			Log.error("error while calling {}", Utilities.getResourceURL(node), e);
			return null;
		}
	}

	/**
	 * 
	 * @param node
	 * @param user
	 * @return
	 */
	public boolean verifyUser(NodeAgentViewModel node, String user) {
		List<String> userList = new ArrayList<String>();
		userList.add(user);
		List<String> result = verifyUserList(node, userList);
		if (userList == null) {
			return false;
		}
		if (userList.contains(user)) {
			return true;
		}
		return false;
	}

}
