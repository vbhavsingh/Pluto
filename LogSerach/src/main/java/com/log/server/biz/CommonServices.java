/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.server.biz;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.log.server.data.db.entity.Config;
import com.log.server.data.db.repository.ConfigRepository;
import com.log.server.data.db.service.ConfigurationService;
import com.log.server.data.db.service.UserDataService;
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
	private ConfigurationService configurationService;

	@Autowired
	private UserDataService userDataService;

	@Autowired
	private ConfigRepository configRepository;

	public SearchPageModel getSearchPageModel() {
		SearchPageModel model = new SearchPageModel();
		model.setLabelList(configurationService.getLabels());
		return model;
	}

	public List<String> getSearchHelpKeywords() {
		return configurationService.getLabels();
	}

	public List<String> getLabelList() {
		return configurationService.getLabels();
	}

	public List<LabelCounter> getLabelListWithNodeCounter() {
		Map<String, Long> labelCountMap = configurationService.getLabelCountByLabelName();

		return labelCountMap.entrySet().stream().map(m -> new LabelCounter(m.getKey(), m.getValue().intValue()))
				.collect(Collectors.toList());
	}

	public List<String> getPreviousSearchCriterias(String fieldName) {
		return configurationService.getPreviousSearchCriterias(fieldName);
	}


	/**
	 *
	 * @param userName
	 * @param nodeName
	 */
	public void deleteUserNodeMapping(String userName, String nodeName) {
		userDataService.deleteUserNodeMapping(userName, nodeName);
	}

	public void createConfiguration(String cfgid, String value) {
		configRepository.save(new Config(cfgid, value));
	}

	public void updateConfiguration(String cfgid, String value) {
		configRepository.save(new Config(cfgid, value));
	}

	public String getConfiguration(String cfgid) {
		Optional<Config> config = configRepository.findById(cfgid);
		if (config.isPresent()) {
			return config.get().getValue();
		}
		return null;
	}

}
