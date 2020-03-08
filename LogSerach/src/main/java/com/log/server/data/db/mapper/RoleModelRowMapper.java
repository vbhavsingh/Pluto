package com.log.server.data.db.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.log.server.model.Role;

public class RoleModelRowMapper implements RowMapper<Role>{

	@Override
	public Role mapRow(ResultSet rs, int rownum) throws SQLException {
		Role role=new Role();
		role.setRolename(rs.getString("ROLENAME"));
		role.setDescription(rs.getString("DESCRIPTION"));
		role.setVisible(rs.getBoolean("VISIBLE"));
		return role;
	}

}
