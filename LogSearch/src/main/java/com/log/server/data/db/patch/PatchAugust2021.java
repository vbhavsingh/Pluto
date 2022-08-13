package com.log.server.data.db.patch;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.log.server.LocalConstants;
import com.log.server.data.db.service.UserDataService;
import com.log.server.model.UserCredentialsModel;

/**
 * This patch is to encrypt existing passwords. 
 * @author Vaibhav Singh
 *
 */
@Component
@ConditionalOnProperty(name = LocalConstants.KEYS.APPLY_PATCH, havingValue = "true")
public class PatchAugust2021 {

private static final Logger Log = LoggerFactory.getLogger(PatchAugust2021.class);
	
	
	@Autowired
	private PasswordEncoder encoder;
	
//	@Autowired
//	Dao dao;
	
	@Autowired
	private UserDataService userDataService;

//	@Resource(name="hsqlDataSource")
//	public void setDataSource(DataSource hsqlDataSource) {
//		Log.debug("creating data source assignement");
//	}
//	
	public void applyPatch() throws Exception {
		Log.info("Applying patch version 5.0, release August 2021");
		//List<UserCredentialsModel> users= jdbcTemplate.query(LocalConstants.SQL.GET_ALL_USER_PROFILES, new UserCredentialsMapper(true));
		List<UserCredentialsModel> users= userDataService.getAllUsers();
		boolean patchUsed = false;
		for(UserCredentialsModel user : users) {
			if(!user.getPassword().startsWith(LocalConstants.PASSWORD_PREFIX)) {
				Log.info("Encrypting password for user: {} using patch 5.0, August 2021", user.getUsername());
				user.setPassword(LocalConstants.PASSWORD_PREFIX + encoder.encode(user.getPassword()));
				userDataService.saveUser(user);	
				patchUsed = true;
			}
		}
		
		if(!patchUsed) {
			Log.info("No open passwords found, patch is not required.");
		}
		
	}
}
