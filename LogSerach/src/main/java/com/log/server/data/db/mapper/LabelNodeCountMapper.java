package com.log.server.data.db.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.log.server.model.LabelCounter;

public class LabelNodeCountMapper implements RowMapper<LabelCounter> {

	@Override
	public LabelCounter mapRow(ResultSet rs, int arg1) throws SQLException {
		LabelCounter labelCounter = new LabelCounter();
		labelCounter.setLabelName(rs.getString(1));
		labelCounter.setNodeCount(rs.getInt(2));
		return labelCounter;
	}

}
