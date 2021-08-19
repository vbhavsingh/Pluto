package com.log.server.controller.rs;

import java.text.ParseException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.log.analyzer.commons.Constants;
import com.log.server.SpringHelper;
import com.log.server.biz.SearchBiz;
import com.log.server.model.RestSearchInput;
import com.log.server.model.RestSearchResultModel;
import com.log.server.model.SearchInput;
import com.log.server.model.ViewResultModel;
import com.log.server.util.Utilities;

@RestController
public class SearchRestService {
	
	@Autowired
	private SearchBiz searchBiz;
	
	@RequestMapping(value="/rs/search/json", method = RequestMethod.POST)
	public RestSearchResultModel search(@RequestBody RestSearchInput input) {
        SearchInput in = input.getSearchInput();
        RestSearchResultModel restModel;
		try {
			validateInput(input);
			ViewResultModel model = searchBiz.getSearchResult(in);
			restModel = new RestSearchResultModel(model);
		} catch (Exception e) {
			restModel = new RestSearchResultModel(e.getMessage());
		}
		return restModel;
	}
	
	@RequestMapping(value="/rs/search", method = {RequestMethod.GET,RequestMethod.POST})
	public RestSearchResultModel search(
			@RequestParam(value = "logPathPatterns", required=false) String logPathPatterns,
			@RequestParam(value = "logFileNamePatterns", required=false) String logFileNamePtterns,
			@RequestParam(value = "search", required=true) String search,
			@RequestParam(value = "searchOnLabels", required=true) String searchOnLabels,
			@RequestParam(value = "fromDateTime", required=false) String fromDateTime,
			@RequestParam(value = "toDateTime", required=false) String toDateTime,
			@RequestParam(value = "viewTz", required=true) String viewTz) {
		//Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		//String username = auth.getName();
		RestSearchInput input = new RestSearchInput();
		input.setLogPathPatterns(logPathPatterns);
		input.setLogFileNamePtterns(logFileNamePtterns);
		input.setSearch(search);
		input.setSearchOnLabels(searchOnLabels);
		input.setFromDateTime(fromDateTime);
		input.setToDateTime(toDateTime);
		input.setViewTz(viewTz);
		return search(input);
	}

	/**
	 * 
	 * @param input
	 * @throws Exception
	 */
	private void validateInput(RestSearchInput input) throws Exception{
		if(StringUtils.hasText(input.getSearch())){
			throw new Exception("search keywords are mandatory");
		}
		if(StringUtils.hasText(input.getSearchOnLabels())){
			throw new Exception("nodenames or labels are mandatory");
		}
		if(StringUtils.hasText(input.getLogPathPatterns()) && StringUtils.hasText(input.getLogFileNamePtterns())) {
			throw new Exception("log path or log file or both are requied");
		}
		if(StringUtils.hasText(input.getFromDateTime())==false) {
			try {
			Constants.SEARCH_DATE_FORMAT.parse(input.getFromDateTime());
			}catch(ParseException e) {
				throw new Exception("from date must be in "+Constants.SEARCH_DATE_FORMAT.toPattern()+" format");
			}
		}
		if(StringUtils.hasText(input.getToDateTime())==false) {
			try {
			Constants.SEARCH_DATE_FORMAT.parse(input.getToDateTime());
			}catch(ParseException e) {
				throw new Exception("to date must be in "+Constants.SEARCH_DATE_FORMAT.toPattern()+" format");
			}
		}
		if(StringUtils.hasText(input.getViewTz())) {
			throw new Exception("view timezone is mandatory field");
		}
		if(Utilities.getAllowedTimeZoneList().contains(input.getViewTz().toUpperCase()) ==  false) {
			throw new Exception("supported timezones are "+Utilities.getAllowedTimeZoneList().toString());
		}
		
	}

}
