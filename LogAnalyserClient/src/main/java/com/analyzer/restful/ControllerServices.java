package com.analyzer.restful;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.analyzer.Business;
import com.analyzer.LocalUtil;
import com.analyzer.restful.err.CharonRequestException;
import com.log.analyzer.commons.Constants;
import com.log.analyzer.commons.Messages;
import com.log.analyzer.commons.err.CommonErrorModel;
import com.log.analyzer.commons.model.AgentConfigurationModel;
import com.log.analyzer.commons.model.AgentTerminatorRequestModel;
import com.log.analyzer.commons.model.FileLineClientResultModel;
import com.log.analyzer.commons.model.FileLineRequestModel;
import com.log.analyzer.commons.model.SearchModel;
import com.log.analyzer.commons.model.SearchResultModel;

@RestController
public class ControllerServices {

	private static final Logger Log = LogManager.getLogger(ControllerServices.class);

	/**
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = Constants.SEARCH_URI_SUFFIX, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public @ResponseBody SearchResultModel search(@RequestBody SearchModel model) {
		Log.debug("search request received as : " + model);
		Business biz = new Business();
		long starttime = System.currentTimeMillis();
		SearchResultModel response = biz.doSearch(model);
		response.setTimeTakenInMilliseconds(System.currentTimeMillis() - starttime);
		return response;
	}

	/**
	 * 
	 * @param terminate
	 * @return
	 */
	@RequestMapping(value = Constants.TERMINATE_AGENT_URI_SUFFIX, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public @ResponseBody String terminateThisCharon(@RequestBody AgentTerminatorRequestModel terminate) {
		Log.debug("self termination request recieved as : " + terminate);
		Business biz = new Business();
		biz.killCharon(terminate);
		return Constants.SUCCESS;
	}

	/**
	 * 
	 * @param config
	 * @return
	 */
	@RequestMapping(value = Constants.UPDATE_CONFIG_URI_SUFFIX, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public @ResponseBody String reloadConfiguration(@RequestBody AgentConfigurationModel config) {
		Log.debug("config updation request recieved as : " + config);
		LocalUtil.updateAgentConfiguration(config);
		return Constants.SUCCESS;
	}

	/**
	 * 
	 * @param userList
	 * @return
	 */
	@RequestMapping(value = Constants.VALIDATE_USER_URI_SUFFIX, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public @ResponseBody List<String> validateUserList(@RequestBody String[] userList) {
		Log.debug("recieved request for validating users");
		if (userList == null || userList.length == 0) {
			Log.error("bad http request, no data recieved in in user search request");
			CommonErrorModel errors = new CommonErrorModel();
			errors.setErrorCode(Constants.ERRORS.BAD_REQUEST_FOR_USER_VALIDATION);
			errors.setHttpErrorCode(HttpStatus.BAD_REQUEST.value());
			errors.setMessage(Messages.NO_USER_IN_LIST);
			throw new CharonRequestException(errors);
		}
		Log.trace("user list recieved for validation : " + Arrays.toString(userList));
		Business business = new Business();
		return business.validateUserList(userList);
	}

	/**
	 * 
	 * @param userList
	 * @return
	 */
	@RequestMapping(value = Constants.FILE_LINES_IN_RANGE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public @ResponseBody FileLineClientResultModel readLinesFromFile(@RequestBody FileLineRequestModel request) {
		Log.debug("recieved request for fetching line with details " + request);
		Business biz = new Business();
		return biz.getLinesFromFile(request);
		
	}
}
