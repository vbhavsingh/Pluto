/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.server.biz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.log.server.data.db.Dao;
import com.log.server.model.LabelCounter;
import com.log.server.model.SearchPageModel;

/**
 *
 * @author Vaibhav Pratap Singh
 */
@Component
public class CommonServices {

	private final static Logger Log = LoggerFactory.getLogger(CommonServices.class);
	
	@Autowired
	private Dao dao;
	
	@Autowired
	private AdminServices adminServices;

	public SearchPageModel getSearchPageModel() {
		SearchPageModel model = new SearchPageModel();
		model.setLabelList(dao.getLabels());
		return model;
	}
	
	public List<String> getSearchHelpKeywords() {
		if (dao == null) {
			Log.debug("database bean not created, returning no labels to user");
			return null;
		}
		return dao.getLabels();
	}
	
	public List<String> getLabelList() {
		if (dao == null) {
			Log.debug("database bean not created, returning no labels to user");
			return null;
		}
		return dao.getLabels();
	}

	public List<LabelCounter> getLabelListWithNodeCounter() {
		if (dao == null) {
			Log.debug("database bean not created, returning no labels to user");
			return null;
		}
		return dao.getLabelNodeCounter();
	}
	
	public List<String> getPreviousSearchCriterias(String fieldName) {
		if (dao == null) {
			Log.debug("database bean not created, returning no labels to user");
			return null;
		}
		return dao.getPreviousSearchCriterias(fieldName);
	}

	public void initializedDatabse() throws Exception {
		adminServices.initializeDataBase();
	}

	/**
	 *
	 * @param userName
	 * @param nodeName
	 */
	public int deleteUserNodeMapping(String userName, String nodeName) {
		if (dao == null) {
			Log.debug("database bean not created, cannot initialize tables");
		}
		return dao.deleteUserNodeMapping(userName, nodeName);
	}
	
}
