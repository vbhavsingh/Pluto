package com.log.server.data.db.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.log.analyzer.commons.model.AgentConfigurationModel;

public class AgentConfigDbMapper implements RowMapper<AgentConfigurationModel> {

	@Override
	public AgentConfigurationModel mapRow(ResultSet rs, int i) throws SQLException {
		AgentConfigurationModel config=new AgentConfigurationModel();
		config.setMaxDataInKb(rs.getInt("RESULT_SIZE_KB"));
		config.setMaxFilesToBeSearched(rs.getInt("MAX_FILES_TO_SEARCH"));
		config.setMaxLinesAllowedInResult(rs.getInt("MAX_LINES_IN_RESULT"));
		config.setMaxLinesAllowedPerFile(rs.getInt("MAX_LINES_PER_FILE"));
		return config;
	}

}
