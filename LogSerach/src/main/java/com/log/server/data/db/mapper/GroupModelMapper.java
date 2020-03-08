package com.log.server.data.db.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.log.server.model.Group;


public class GroupModelMapper implements RowMapper<Group>{

	@Override
	public Group mapRow(ResultSet rs, int rownum) throws SQLException {
		Group gp =new Group();
		gp.setName(rs.getString("GROUPNAME"));
		gp.setDescription(rs.getString("DESCRIPTION"));
		gp.setCreatedBy(rs.getString("CREATED_BY"));
		gp.setCreatedDate(rs.getDate("CREATED_TIME"));
		gp.setModifiedBy(rs.getString("MODIFIED_BY"));
		gp.setModifiedDate(rs.getDate("LAST_MODIFIED"));
		return gp;
	}

}
