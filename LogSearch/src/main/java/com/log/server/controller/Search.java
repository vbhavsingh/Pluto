/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.server.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.log.analyzer.commons.Constants;
import com.log.analyzer.commons.model.FileLineRequestModel;
import com.log.server.biz.CachingService;
import com.log.server.biz.CommonServices;
import com.log.server.biz.SearchBiz;
import com.log.server.model.FileLineServerResultModel;
import com.log.server.model.ScrollableResultModel;
import com.log.server.model.SearchInput;
import com.log.server.model.ViewResultModel;

/**
 *
 * @author Vaibhav Pratap Singh
 */
@Controller
public class Search {

	private final static Logger Log = LoggerFactory.getLogger(Search.class);
	
	@Autowired
	private SearchBiz searchBiz;
	
	@Autowired
	private CommonServices commonServices;
	
	@Autowired
	CachingService svc;
	
	@RequestMapping(value = "/")
    public String redirectToServices(){
		return("forward:/application.htm");
    }
	
	@RequestMapping(
			value = "/version.htm", 
			produces=MediaType.TEXT_PLAIN,
			method = RequestMethod.GET)
	@ResponseBody
	public String version(){
		return Constants.VERSION;
	}
	/**
	 * 
	 * @param input
	 * @return
	 */
	@RequestMapping("/application.htm")
	public ModelAndView getSearchApplication() {
		ViewResultModel model = new ViewResultModel();
		SearchInput input = new SearchInput();
		input.setViewTz("EST");
		model.setInput(input);
		ModelAndView mv = new ModelAndView("secure/application", "model", model);
		return mv;
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	@RequestMapping("/get_search_form.htm")
	public ModelAndView getSearchForm() {
		ViewResultModel model = new ViewResultModel();
		SearchInput input = new SearchInput();
		input.setViewTz("EST");
		model.setInput(input);
		ModelAndView mv = new ModelAndView("secure/views/search_page", "model", model);
		return mv;
	}

	/**
	 * 
	 * @param input
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/search.htm", produces = MediaType.APPLICATION_JSON)
	@ResponseBody
	public ViewResultModel doSearch(SearchInput input, HttpServletRequest request) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		input.setSessionId(request.getSession().getId());
		input.setUserName(auth.getName());

		Log.trace("search request: {} made", input);
	//	SearchBiz svc = new SearchBiz();
		
	//	ViewResultModel model = svc.getPaginatedSearchResult(input);
		
		ViewResultModel model = svc.getPaginatedSearchResult(input);

		return model;
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	@RequestMapping(value = "/next.htm", produces = MediaType.APPLICATION_JSON)
	@ResponseBody
	public ScrollableResultModel getNextPage(SearchInput input,HttpServletRequest request) throws Exception{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		input.setSessionId(request.getSession().getId());
		input.setUserName(auth.getName());
		//SearchBiz biz = new SearchBiz();
		CachingService svc = new CachingService();
		return svc.getNextScroll(input);
	}
	
	/**
	 * 
	 * @param input
	 * @return
	 */
	@RequestMapping(value = "/getlinesfromfile.htm", produces = MediaType.APPLICATION_JSON)
	@ResponseBody
	public FileLineServerResultModel getFileLinesInGivenRange(FileLineRequestModel input,HttpServletRequest request) throws Exception{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		input.setSessionId(request.getSession().getId());
		input.setUserName(auth.getName());
		return searchBiz.getLinesInRangeFromFile(input);
	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping("/login.htm")
	public ModelAndView getLoginPage() {
		ModelAndView mv = new ModelAndView("secure/login", "model", null);
		return mv;
	}
	

	@RequestMapping(value = "/labels.htm", produces = MediaType.APPLICATION_JSON)
	@ResponseBody
	public List<String> getLabels() {
		return commonServices.getLabelList();
	}
}
