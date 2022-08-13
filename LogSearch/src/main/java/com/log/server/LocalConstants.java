/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.log.server.util.Utilities;

/**
 *
 * @author Vaibhav Pratap Singh
 */
public class LocalConstants {

	public static final String PASSWORD_PREFIX = "{bcrypt}";

	public static final Logger CLIENT_ACCESS_LOG = LoggerFactory.getLogger("nodeAccess");

	public static final Logger USER_ACCESS_LOG = LoggerFactory.getLogger("userAccess");

	public static final ExecutorService executor = Executors.newFixedThreadPool(600);

	public static final int DEFAULT_PAGE_LENGTH = 100;

	public static final String PRESENTATION_DATE_FORMAT = "MM/dd/yy HH:mm:ss.SSS";

	public static final SimpleDateFormat BARCHART_DATE_FORMATTER = new SimpleDateFormat("MM/dd/yy");

	public static final String FIND_COMMAND = "find -L $HOME /var/log -type f \\( -name \"*.log\" -o -name \"*.log.gz\" -o -name \"*.log.tar\" \\)";

	public static final String SUCCESS = "SUCCESS";

	public static final String FAILED = "FAILED";

	public static final String TRUE = "true";

	public static final String FALSE = "false";

	public static final String DATA_EXISTS = "EXISTS";

	public static final String SUPEGROUP = "super-group";

	public static final int AGENT_DEAD_MINUTES = 30;

	public static final String FIELD_LOGPATH = "PATH";

	public static final String FIELD_FILENAME = "FILE";

	public static final String FIELD_KEYTEXT = "KEYTEXT";

	public static final String REST_METHOD = "REST";

	public static final String BROWSER_METHOD = "BROWSER";

	public static final String SUPERGROUP_DESC = "super group allows users to search across all machines, irrespective whether they have account on those machines";

	public static final class KEYS {
		public static final String NEW_INSTALL = "fob.check";

		public static final String APPLY_PATCH = "apply.patch";
	}

	public static final class ERROR {
		public static final String UNDEFINED = "UNDEFINED";
		public static final String UNAUTHORIZED = "NO-AUTH";
		public static final String UNREACHABLE = "NO-NETWORK";
		public static final String BAD_REQUEST = "BAD-REQUEST";
		public static final String BAD_SEARCH_PARAMETERS = "BAD-SEARCH-PARAMS";
		public static final String SECURE_COMM_FAILED = "FAILED-SECURITY-VERIFICATION";
	}

	public static final class MESSAGES {

		public static final String UNAUTHORIZED = "NO-AUTH";
		public static final String UNREACHABLE = "NO-NETWORK";
		public static final String BAD_REQUEST = "BAD-REQUEST";
		public static final String BAD_SEARCH_PARAMETERS = "BAD-SEARCH-PARAMS";
	}

	public static final class SQL {

		public static final String ROLE_INSERT_PATCH_JULY2018 = "INSERT INTO CREDENTIALS_ROLE "
				+ "(ROLENAME,DESCRIPTION,VISIBLE) " + "VALUES (?,?,?)";

	}

	public static final class ROLE {

		public static final String BOT = "ROBOT";

		public static enum ROLES {
			ADMIN(ROLES.ADMIN_VALUE, "System admin, can create groups, group members and assign roles"),
			GROUP_ADMIN(ROLES.GROUP_ADMIN_VALUE, "Group admin, can create group members and assign roles"),
			GROUP_MEMBER(ROLES.GROUP_MEMBER_VALUE, "Group members just can use the search and have no admin privilege");

			public static final String ADMIN_VALUE = "ADMIN";
			public static final String GROUP_ADMIN_VALUE = "GROUP-ADMIN";
			public static final String GROUP_MEMBER_VALUE = "GROUP-MEMBER";

			private static final Map<String, String> DESCRIPTION_MAP = new HashMap<>();
			private static final List<String> ROLE_LIST = new ArrayList<>();

			static {
				for (ROLES role : values()) {
					DESCRIPTION_MAP.put(role.roleName, role.description);
					ROLE_LIST.add(role.roleName);
				}
			}

			ROLES(String roleName, String description) {
				this.roleName = roleName;
				this.description = description;
			}

			public final String roleName;
			public final String description;

			public static boolean isValidRole(String roleName) {
				return ROLE_LIST.contains(roleName);
			}
		}

		public static final String BOT_DESC = "user with bot role can use rest services";
	}

	public static final class USER_DEFAULTS {
		public static final String USERNAME = "admin";
		public static final String PASSWORD = "{bcrypt}$2a$12$Kqu8z4rEM15vQCjGxIb3ke37Wh35RCbfecN1ZdPTy7BUB66bVKLpK";
		public static final String CREATED_BY = "system";
		public static final String EMAIL = "admin@delete.me";
		public static final String NAME = "Admin";
	}

	public static final class PROPERTIES {
		public static final String EMBEDED_DB_FILE_PATH = "db.file";
		public static final String DB_CONNECTION_STRING = "db.connection";
		public static final String DB_USERNAME = "db.user";
		public static final String DB_PASSWORD = "db.password";
		public static final String DB_TYPE = "db.type";
		public static final String DB_URL = "db.url";
	}

	public static enum DATABASES {
		oracle("org.hibernate.dialect.OracleDialect", "oracle.jdbc.driver.OracleDriver", "schema-oracle.sql"),
		mysql("org.hibernate.dialect.MySQLDialect", "com.mysql.jdbc.Driver", "schema-mysql.sql"),
		mssql("org.hibernate.dialect.SQLServerDialect", "com.microsoft.sqlserver.jdbc.SQLServerDriver",
				"schema-sqlserver.sql"),
		postgres("org.hibernate.dialect.PostgreSQLDialect", "org.postgresql.Driver", "schema-postgresql.sql"),
		mariadb("org.hibernate.dialect.MariaDBDialect", "org.mariadb.jdbc.Driver", "schema-mysql.sql"),
		hsql("org.hibernate.dialect.HSQLDialect", "org.hsqldb.jdbcDriver", "schema-hsqldb.sql");

		private static final Map<String, DATABASES> MAP = new HashMap<>();

		static {
			for (DATABASES db : values()) {
				MAP.put(db.name(), db);
			}
		}

		DATABASES(String hibernateDialect, String driverClass, String sessionSchema) {
			this.hibernateDialect = hibernateDialect;
			this.driverClass = driverClass;
			this.sessionSchema = sessionSchema;
		}

		public final String hibernateDialect;
		public final String driverClass;
		public final String sessionSchema;

		public static String getDialect() {
			String dbName = Utilities.getProperty(PROPERTIES.DB_TYPE);
			dbName = dbName == null ? hsql.name() : dbName;
			DATABASES db = MAP.get(dbName);
			if (db != null) {
				return db.hibernateDialect;
			}
			return null;
		}

		public static String getDriverName() {
			String dbName = Utilities.getProperty(PROPERTIES.DB_TYPE);
			dbName = dbName == null ? hsql.name() : dbName;
			DATABASES db = MAP.get(dbName);
			if (db != null) {
				return db.driverClass;
			}
			return null;
		}

		public static String getSessionSchema() {
			String dbName = Utilities.getProperty(PROPERTIES.DB_TYPE);
			dbName = dbName == null ? hsql.name() : dbName;
			DATABASES db = MAP.get(dbName);
			if (db != null) {
				return "org/springframework/session/jdbc/" + db.sessionSchema;
			}
			return null;
		}

		public static boolean isHsqlDB() {
			String dbName = Utilities.getProperty(PROPERTIES.DB_TYPE);
			dbName = dbName == null ? hsql.name() : dbName;
			return dbName.equals(hsql.name());
		}
	}

}
