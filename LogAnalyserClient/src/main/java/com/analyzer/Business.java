package com.analyzer;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

import com.analyzer.commons.KillThread;
import com.analyzer.commons.LocalConstants;
import com.analyzer.commons.ValidateUser;
import com.analyzer.posix.LineRangeFileReader;
import com.analyzer.posix.Search;
import com.analyzer.restful.err.CharonRequestException;
import com.log.analyzer.commons.CipherService;
import com.log.analyzer.commons.Constants;
import com.log.analyzer.commons.err.CommonErrorModel;
import com.log.analyzer.commons.model.AgentTerminatorRequestModel;
import com.log.analyzer.commons.model.FileLineRequestModel;
import com.log.analyzer.commons.model.FileLineClientResultModel;
import com.log.analyzer.commons.model.SearchModel;
import com.log.analyzer.commons.model.SearchResultModel;
import com.log.analyzer.commons.model.SecureInterface;

public class Business {

	private static final Logger Log = LogManager.getLogger(Business.class);

	public FileLineClientResultModel getLinesFromFile(FileLineRequestModel request) {
		securityVerification(request);
		try {
			/*LineRangeFileReader reader = new LineRangeFileReader();
			return reader.readLines(request);*/
			if (ValidateUser.isUserValid(request)) {
				LocalConstants.ACCESS_LOGGER.info("file read request recieved from session : " + request.getSessionId() + ", user:" + request.getUserName());
				LineRangeFileReader reader = new LineRangeFileReader();
				Log.debug("successfully read lines from file for request: "+ request);
				return reader.readLines(request);
			} else {
				Log.warn("user: "+ request.getUserName() +". is not authrorized on this machine");
				CommonErrorModel errors = new CommonErrorModel(HttpStatus.UNAUTHORIZED.value());
				throw new CharonRequestException(errors);
			}
		} catch (Exception e) {
			Log.warn("error while fteching lines for request: "+ request,e);
			CommonErrorModel errors = new CommonErrorModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
			throw new CharonRequestException(errors);
		}
	}

	public SearchResultModel doSearch(SearchModel model) {
		Log.info(model);
		securityVerification(model);
		try {
			if (ValidateUser.isUserValid(model)) {
				Search svc = new Search();
				Log.debug("search request received from session : " + model.getSessionId());
				LocalConstants.ACCESS_LOGGER.info("search request recieved from session : " + model.getSessionId() + ", user:" + model.getUserName());
				return svc.executeSearch(model);
			} else {
				CommonErrorModel errors = new CommonErrorModel(HttpStatus.UNAUTHORIZED.value());
				errors.setErrorCode(Constants.ERRORS.NO_AUTHORITY);
				errors.setMessage("user " + model.getUserName() + ", is not valid user on this node: " + LocalUtil.getNodeFQDN());
				throw new CharonRequestException(errors);
			}
		} catch (Exception e) {
			if (e instanceof CharonRequestException) {
				throw (CharonRequestException) e;
			}
			CommonErrorModel errors = new CommonErrorModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
			throw new CharonRequestException(errors);
		}
	}

	/**
	 * 
	 * @param userList
	 * @return
	 */
	public List<String> validateUserList(String[] userList) {
		List<String> validUserList = new ArrayList<String>();
		try {
			for (String user : userList) {
				if (ValidateUser.isUserValid(user)) {
					validUserList.add(user);
				}
			}
		} catch (Exception e) {
			CommonErrorModel errors = new CommonErrorModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
			throw new CharonRequestException(errors);
		}
		return validUserList;
	}

	/**
	 * 
	 * @param model
	 */
	public void killCharon(AgentTerminatorRequestModel model) {
		securityVerification(model);
		try {
			/* check whether user is allowed to search on this box */
			Log.trace("Terminate agent request :" + model.toString());
			if (ValidateUser.isUserValid(model.getUser())) {
				Log.warn("killing charon upon request from user: " + model.getUser() + ", session: " + model.getSession() + ", machine: " + model.getOriginIp());
				Thread kill = new Thread(new KillThread());
				kill.start();
			} else {
				Log.warn("user is not valid on this machine, termintaion cannot be processed, username is " + model.getUser());
				LocalConstants.SECURITY_LOGGER.fatal("Invalid user made request, agent will not be terminated, username is " + model.getUser());
				CommonErrorModel error = new CommonErrorModel();
				error.setHttpErrorCode(HttpStatus.UNAUTHORIZED.value());
				error.setErrorCode(Constants.ERRORS.NO_AUTHORITY);
				error.setMessage("\"" + model.getUser() + "\" is not authorized user on " + LocalUtil.getNodeFQDN() + ", cannot terminate client");
				throw new CharonRequestException(error);
			}
		} catch (Exception e) {
			CommonErrorModel errors = new CommonErrorModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
			throw new CharonRequestException(errors);
		}
	}

	private void securityVerification(Object model) {
		try {
			SecureInterface smodel = (SecureInterface) CipherService.decrypt(model);
			if (smodel.validateObject(Constants.REQUEST_SANITY) == false) {
				CommonErrorModel errors = new CommonErrorModel(HttpStatus.NOT_ACCEPTABLE.value());
				errors.setErrorCode(Constants.ERRORS.BAD_ENCYPTION);
				errors.setMessage("bad key! failed to decipher the request with agreed key");
				throw new CharonRequestException(errors);
			}
		} catch (Exception e) {
			CommonErrorModel errors = new CommonErrorModel(HttpStatus.NOT_ACCEPTABLE.value(), e);
			errors.setErrorCode(Constants.ERRORS.BAD_ENCYPTION);
			errors.setMessage("message can't be decrypted :" + e.getMessage());
			throw new CharonRequestException(errors);
		}
	}

}
