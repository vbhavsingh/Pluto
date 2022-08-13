package com.log.server.comm.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.log.analyzer.commons.CipherService;
import com.log.analyzer.commons.Constants;
import com.log.analyzer.commons.err.CommonErrorModel;
import com.log.analyzer.commons.model.AgentModel;
import com.log.analyzer.commons.model.AgentTerminatorRequestModel;
import com.log.server.LocalConstants;
import com.log.server.util.Utilities;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class TerminateAgent {
	private final static Logger Log = LoggerFactory.getLogger(TerminateAgent.class);
	private Gson gson = new Gson();

	public String terminate(AgentTerminatorRequestModel killRequest, AgentModel node) throws Exception {
		killRequest = (AgentTerminatorRequestModel) CipherService.encryptWithOpenkey(killRequest, node.getCommKey());
		String request = gson.toJson(killRequest);
		try {
			Client c = Client.create();

			WebResource resource = c.resource(Utilities.getResourceURL(node) + Constants.TERMINATE_AGENT_URI_SUFFIX);
			ClientResponse response = resource.accept("text/plain").post(ClientResponse.class, request);

			String result = resource.post(String.class, request);

			if (response.getStatus() == 200) {
				return result;
			} else {
				CommonErrorModel error = gson.fromJson(result, CommonErrorModel.class);
				Log.error("cannot terminate remote node: {}, because {}", node.getClientNode(), error);
				throw new Exception(error.getMessage());
			}

		} catch (ClientHandlerException ex) {
			Log.warn("cannot terminate remote node: {}, because of {}", node.getClientNode(), ex.getMessage(), ex);
			return LocalConstants.ERROR.UNREACHABLE;
		}
	}

}
