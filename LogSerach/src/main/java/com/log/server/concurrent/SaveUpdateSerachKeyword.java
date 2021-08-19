package com.log.server.concurrent;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.log.server.LocalConstants;
import com.log.server.data.db.Dao;
import com.log.server.model.SearchHelpKeyword;
import com.log.server.model.SearchInput;

public class SaveUpdateSerachKeyword implements Runnable {

	SearchInput input;
	
	@Autowired
	private Dao dao;

	public SaveUpdateSerachKeyword(SearchInput input) {
		this.input = input;
	}

	@Override
	public void run() {
		if (!StringUtils.isEmpty(input.getLogPathPatterns())) {
			SearchHelpKeyword keyword = new SearchHelpKeyword(LocalConstants.FIELD_LOGPATH, input.getLogPathPatterns(), input.getUserName());
			dao.saveUpdateSearchKeywords(keyword);
		}
		if (!StringUtils.isEmpty(input.getLogFileNamePtterns())) {
			SearchHelpKeyword keyword = new SearchHelpKeyword(LocalConstants.FIELD_FILENAME, input.getLogFileNamePtterns(), input.getUserName());
			dao.saveUpdateSearchKeywords(keyword);
		}
		if (!StringUtils.isEmpty(input.getSearch())) {
			SearchHelpKeyword keyword = new SearchHelpKeyword(LocalConstants.FIELD_KEYTEXT, input.getSearch(), input.getUserName());
			dao.saveUpdateSearchKeywords(keyword);
		}
	}

}
