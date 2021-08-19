/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.server.concurrent;

import java.util.concurrent.Callable;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.log.analyzer.commons.CipherService;
import com.log.analyzer.commons.Constants;
import com.log.analyzer.commons.err.CommonErrorModel;
import com.log.analyzer.commons.model.AgentModel;
import com.log.analyzer.commons.model.SearchModel;
import com.log.analyzer.commons.model.SearchResultModel;
import com.log.server.LocalConstants;
import com.log.server.SpringHelper;
import com.log.server.biz.AdminServices;
import com.log.server.util.Utilities;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import net.rationalminds.es.EnvironmentalControl;

/**
 *
 * @author Vaibhav Pratap Singh
 */
public class NodalSearch implements Callable<SearchResultModel> {

	private static final Logger Log = LoggerFactory.getLogger(NodalSearch.class);
	
	@Autowired
	private AdminServices adminServices;

	AgentModel node;
	SearchModel searchModel;

	public NodalSearch(AgentModel node, SearchModel searchModel) {
		this.node = node;
		this.searchModel = searchModel;
		String df = Constants.SEARCH_DATE_FORMAT.toPattern();
		String nodeTz = node.getTimeZone();
		String reqTz = searchModel.getRequestedTz();
		if(searchModel.isTimedSearch()) {
			String rStartDt = Utilities.covertDateToStringInTargetTz(searchModel.getStartDateString(),df, reqTz, nodeTz);
			this.searchModel.setStartDateTime(rStartDt);
			
			String rEndDt = Utilities.covertDateToStringInTargetTz(searchModel.getEndDateString(),df, reqTz, nodeTz);
			this.searchModel.setEndDateTime(rEndDt);		
		}
	}

	@Override
	@EnvironmentalControl(devMethod="com.log.server.util.DevEnvironmentMocker.call(this)")
	public SearchResultModel call() throws Exception {
		Log.info("Calling node: {} for search", this.node.getClientNode());
		try {
			Log.debug("sending json reponse: {}, to node: {}", searchModel, this.node.getClientNode());
			Client c = Client.create();
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			/* encrypting data before sending */
			searchModel = (SearchModel) CipherService.encryptWithOpenkey(searchModel, node.getCommKey());
			Log.debug("encypted search object: {}", searchModel);
			String input = gson.toJson(searchModel);
			Log.trace("encrypted request: {}, for node: {} ", input, node.getClientNode());
			Log.debug("sending json request: {}, to node: {}", input, node.getClientNode());
			Log.trace("calling url: {} for search", Utilities.getResourceURL(node) + Constants.SEARCH_URI_SUFFIX);

			WebResource resource = c.resource(Utilities.getResourceURL(node) + Constants.SEARCH_URI_SUFFIX);
			ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(ClientResponse.class, input);
			String result = response.getEntity(String.class);

			if (response.getStatus() == 200) {
				SearchResultModel resultModel = gson.fromJson(result, SearchResultModel.class);
				return resultModel;
			} else {
				CommonErrorModel error = gson.fromJson(result, CommonErrorModel.class);
				if (error == null) {
					Log.error("{}, for request on node {}, by {}", response.toString(), node.getClientNode(), searchModel.getUserName());
					SearchResultModel resultModel = new SearchResultModel();
					resultModel.setNodeName(this.node.getClientName());
					resultModel.setErrorCode(LocalConstants.ERROR.UNDEFINED);
					resultModel.setFailed(true);
					return resultModel;
				}
				return decorateErrorMessage(error);
			}
		} catch (Exception e) {
			SearchResultModel resultModel = new SearchResultModel();
			resultModel.setNodeName(this.node.getClientName());
			resultModel.setErrorCode(LocalConstants.ERROR.UNREACHABLE);
			Log.error("error while calling url: {} for search", Utilities.getResourceURL(node), e);
			resultModel.setFailed(true);
			return resultModel;
		}
	}

	/**
	 * 
	 * @param result
	 * @param gson
	 * @return
	 */
	private SearchResultModel decorateErrorMessage(CommonErrorModel error) {
		SearchResultModel resultModel = null;
		if (HttpStatus.NOT_ACCEPTABLE.value() == error.getHttpErrorCode()) {
			Log.error("client {} is not able to decrypt search request by user {}, error is {}, stacktrace : {}", node.getClientName(),searchModel.getUserName(),error.getMessage(),error.getFullTrace());
			resultModel = new SearchResultModel();
			resultModel.setNodeName(this.node.getClientName());
			resultModel.setErrorCode(LocalConstants.ERROR.SECURE_COMM_FAILED);
			resultModel.setErrorMessage(error.getMessage());
			resultModel.setFailed(true);
			return resultModel;
		} else if (HttpStatus.UNAUTHORIZED.value() == error.getHttpErrorCode()) {
			Log.warn("user {}, is not authorized to issue search command on node {}", searchModel.getUserName(), node.getClientName());
			resultModel = new SearchResultModel();
			resultModel.setNodeName(this.node.getClientName());
			resultModel.setErrorCode(LocalConstants.ERROR.UNAUTHORIZED);
			resultModel.setErrorMessage(error.getMessage());
			resultModel.setFailed(true);
			// remove user mapping
			Log.info("since user: {}, is not having access on node: {}, removing his mapping automatically", searchModel.getUserName(), this.node.getClientName());
			removeUserMapping();
			return resultModel;
		} else {
			resultModel = new SearchResultModel();
			resultModel.setNodeName(this.node.getClientName());
			resultModel.setErrorCode(LocalConstants.ERROR.UNDEFINED);
			resultModel.setFailed(true);
		}
		return resultModel;
	}

	/**
	 * 
	 */
	private void removeUserMapping() {
		try {
			adminServices.deleteUserNodeMapping(searchModel.getUserName(), node.getClientNode());
		} catch (Exception e) {
			Log.error("error while deleting bad user: {} node: {} mapping", searchModel.getUserName(), node.getClientName(), e);
		}
	}

}
