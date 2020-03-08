package com.log.server.data.db.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.log.server.model.UserCredentials;

public class UserCredentialsMapper implements RowMapper<UserCredentials> {

	private boolean setpassword = false;

	public void setSetpassword(boolean setpassword) {
		this.setpassword = setpassword;
	}

	public UserCredentialsMapper(boolean setpassword) {
		super();
		this.setpassword = setpassword;
	}

	@Override
	public UserCredentials mapRow(ResultSet rs, int rownum) throws SQLException {
		UserCredentials u = new UserCredentials();
		u.setUsername(rs.getString("USERNAME"));
		if (setpassword) {
			u.setPassword(rs.getString("PASSWORD"));
		}
		u.setEmail(rs.getString("EMAIL"));
		u.setFirstname(rs.getString("FIRST_NAME"));
		u.setLastname(rs.getString("LAST_NAME"));
		u.setCreatedBy(rs.getString("CREATED_BY"));
		u.setCreatedDate(rs.getDate("CREATED_TIME"));
		u.setModifiedDate(rs.getDate("LAST_MODIFIED"));
		return u;
	}

}
