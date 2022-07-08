package com.log.server.data.db.patch;

import java.sql.Types;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.log.server.LocalConstants;
import com.log.server.data.db.service.UserRoleService;
import com.log.server.model.UserCredentialsModel;

@Component
public class Patch2018 {

	private static final Logger Log = LoggerFactory.getLogger(Patch2018.class);
	
	private JdbcTemplate jdbcTemplate;
	
//	@Autowired
//	Dao dao;
	
	@Autowired
	private UserRoleService roleService;

	@Resource(name="hsqlDataSource")
	public void setDataSource(DataSource hsqlDataSource) {
		Log.debug("creating data source assignement");
		this.jdbcTemplate = new JdbcTemplate(hsqlDataSource);
	}
	
	public void applyPatch(UserCredentialsModel user) throws Exception {
		Log.info("Applying patch version 4.0, release July 2018");
		try {
		Log.info("Trying to add column in table ");
		String alterRoleTable = "ALTER TABLE CREDENTIALS_ROLE add visible BOOLEAN";
		try {
			jdbcTemplate.execute(alterRoleTable);
		}catch (BadSqlGrammarException e) {
			Log.info("Tried adding column 'visible' in table 'CREDENTIALS_ROLE', but it already exists.");
		}
		
		Log.info("new column sucessfully added to roles table");
		String update = "UPDATE CREDENTIALS_ROLE SET VISIBLE = TRUE";
		jdbcTemplate.update(update);
		Log.info("updated all roles with default value, for new column");		
		}catch(Exception e) {
			Log.error("cannot add column for patch",e);
			throw e;
		}
		try {
			Object values[] = { LocalConstants.ROLE.BOT,  LocalConstants.ROLE.BOT_DESC,false};
			int types[] = { Types.VARCHAR, Types.VARCHAR, Types.BOOLEAN};
			jdbcTemplate.update(LocalConstants.SQL.ROLE_INSERT_PATCH_JULY2018, values, types);
			Log.info("added new role 'robot', in non primary mode");	
			roleService.createUserRoleMapping(user.getUsername(), LocalConstants.ROLE.BOT);
			//dao.createUserRoleMapping(user, LocalConstants.ROLE.BOT);
			Log.info("mapped defualt user to new role 'robot'");
		} catch (Exception e) {
			Log.error("error while creating robot role, patching has failed", e);
			throw e;
		}
	}
	
}
