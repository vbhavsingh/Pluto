package com.log.server.data.db.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.log.server.model.SearchHelpKeyword;

public class SearchHelpKeywordMapper implements RowMapper<SearchHelpKeyword> {

	@Override
	public SearchHelpKeyword mapRow(ResultSet rs, int arg1) throws SQLException {
		SearchHelpKeyword word = new SearchHelpKeyword();
		word.setField(rs.getString("FIELD"));
		word.setKeyWord("KEYTEXT");
		word.setFrequency(rs.getInt("FREQUENCY"));
		word.setUser(rs.getString("USERNAME"));
		word.setCreateDate(rs.getDate("CREATED_TIME"));
		return word;
	}

}
