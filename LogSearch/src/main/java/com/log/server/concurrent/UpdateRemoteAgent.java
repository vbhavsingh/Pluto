package com.log.server.concurrent;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.log.analyzer.commons.Constants;
import com.log.analyzer.commons.err.CommonErrorModel;
import com.log.analyzer.commons.model.AgentConfigurationModel;
import com.log.server.model.NodeAgentViewModel;
import com.log.server.util.Utilities;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class UpdateRemoteAgent implements Runnable {
	private static final Logger Log = LoggerFactory.getLogger(UpdateRemoteAgent.class);
	NodeAgentViewModel node;
	AgentConfigurationModel config;

	public UpdateRemoteAgent(NodeAgentViewModel node) {
		this.node = node;
		config = new AgentConfigurationModel();
		config.setMaxDataInKb(node.getResultInKb());
		config.setMaxFilesToBeSearched(node.getMaxFilesToSearch());
		config.setMaxLinesAllowedInResult(node.getMaxLinesInresult());
		config.setMaxLinesAllowedPerFile(node.getMaxLinesPerFile());
	}

	@Override
	public void run() {
		try {
			Gson gson = new Gson();
			Log.info("attempting configuration update on node: {}", node.getNodeName());
			Log.trace("attempting configuration update on node: {}, configuration is: {}", node, config);
			Client c = Client.create();
			String json = gson.toJson(config);

			WebResource resource = c.resource(Utilities.getResourceURL(node) + Constants.UPDATE_CONFIG_URI_SUFFIX);
			ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, json);
			String result = response.getEntity(String.class);

			if (response.getStatus() == 200) {
				Log.info("configuration updated on node: {} ", node.getNodeName());
			} else {
				CommonErrorModel error = gson.fromJson(result, CommonErrorModel.class);
				Log.warn("configuration not updated on node: {}, it will be send with next heartbeat, error: {}", node.getNodeName(), error);
			}
		} catch (Exception e) {
			Log.error("error while updating config on node: {}", node.getNodeName(), e);
		}

	}

}
