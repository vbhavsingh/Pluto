package com.log.server.comm.http;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.log.analyzer.commons.CipherService;
import com.log.analyzer.commons.Constants;
import com.log.analyzer.commons.err.CommonErrorModel;
import com.log.analyzer.commons.model.AgentModel;
import com.log.analyzer.commons.model.FileLineClientResultModel;
import com.log.analyzer.commons.model.FileLineRequestModel;
import com.log.server.util.Utilities;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class FileLineReader {
	private final static Logger Log = LoggerFactory.getLogger(FileLineReader.class);
	private Gson gson = new Gson();

	public FileLineClientResultModel readLines(FileLineRequestModel request, AgentModel node) throws Exception {
		String fileName = request.getFileName();
		request = (FileLineRequestModel) CipherService.encryptWithOpenkey(request, node.getCommKey());
		String jsonRequest = gson.toJson(request);

		try {
			Client c = Client.create();
			Log.debug("sending json request for reading remote file lines: {}", jsonRequest);
			WebResource resource = c.resource(Utilities.getResourceURL(node) + Constants.FILE_LINES_IN_RANGE);
			ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonRequest);

			String result = response.getEntity(String.class);

			FileLineClientResultModel resultModel = gson.fromJson(result, FileLineClientResultModel.class);

			if (response.getStatus() == 200) {
				return resultModel;
			} else {
				CommonErrorModel error = gson.fromJson(result, CommonErrorModel.class);
				Log.error("cannot read file: {} for lines on node: {}, because {}", node.getClientNode(), fileName, error);
				throw new Exception(error.getMessage());
			}

		} catch (ClientHandlerException ex) {
			Log.warn("cannot terminate remote node: {}, because of {}", node.getClientNode(), ex.getMessage(), ex);
			throw ex;
		}
	}
	
	
}
