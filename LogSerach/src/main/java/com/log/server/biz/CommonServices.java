/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.server.biz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.log.server.SpringHelper;
import com.log.server.data.db.Dao;
import com.log.server.model.LabelCounter;
import com.log.server.model.SearchPageModel;

/**
 *
 * @author Vaibhav Pratap Singh
 */
public class CommonServices {

	private final static Logger Log = LoggerFactory.getLogger(CommonServices.class);

	public static SearchPageModel getSearchPageModel() {
		Dao dao = SpringHelper.getDao();
		SearchPageModel model = new SearchPageModel();
		model.setLabelList(dao.getLabels());
		return model;
	}
	
	public static List<String> getSearchHelpKeywords() {
		Dao dao = SpringHelper.getDao();
		if (dao == null) {
			Log.debug("database bean not created, returning no labels to user");
			return null;
		}
		return dao.getLabels();
	}
	
	public static List<String> getLabelList() {
		Dao dao = SpringHelper.getDao();
		if (dao == null) {
			Log.debug("database bean not created, returning no labels to user");
			return null;
		}
		return dao.getLabels();
	}

	public static List<LabelCounter> getLabelListWithNodeCounter() {
		Dao dao = SpringHelper.getDao();
		if (dao == null) {
			Log.debug("database bean not created, returning no labels to user");
			return null;
		}
		return dao.getLabelNodeCounter();
	}
	
	public static List<String> getPreviousSearchCriterias(String fieldName) {
		Dao dao = SpringHelper.getDao();
		if (dao == null) {
			Log.debug("database bean not created, returning no labels to user");
			return null;
		}
		return dao.getPreviousSearchCriterias(fieldName);
	}

	public static void initializedDatabse() throws Exception {
		AdminServices svc = (AdminServices) SpringHelper.getBean("AdminServices");
		if (svc == null) {
			Log.debug("database bean not created, cannot initialize tables");
		}
		svc.initializeDataBase();
	}

	/**
	 *
	 * @param userName
	 * @param nodeName
	 */
	public static int deleteUserNodeMapping(String userName, String nodeName) {
		Dao dao = SpringHelper.getDao();
		if (dao == null) {
			Log.debug("database bean not created, cannot initialize tables");
		}
		return dao.deleteUserNodeMapping(userName, nodeName);
	}
	
}
