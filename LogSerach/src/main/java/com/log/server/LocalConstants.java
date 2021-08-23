/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.server;

import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Vaibhav Pratap Singh
 */
public class LocalConstants {
	
	public static final Logger CLIENT_ACCESS_LOG = LoggerFactory.getLogger("nodeAccess");
	
	public static final Logger USER_ACCESS_LOG = LoggerFactory.getLogger("userAccess");

    public static final ExecutorService executor = Executors.newFixedThreadPool(600);
    
    public static final int DEFAULT_PAGE_LENGTH=100;
    
    public static final String PRESENTATION_DATE_FORMAT ="MM/dd/yy HH:mm:ss.SSS";
    
    public static final SimpleDateFormat BARCHART_DATE_FORMATTER = new SimpleDateFormat("MM/dd/yy");

    public static final String FIND_COMMAND = "find -L $HOME /var/log -type f \\( -name \"*.log\" -o -name \"*.log.gz\" -o -name \"*.log.tar\" \\)";
    
    public static final String SUCCESS="SUCCESS";
    
    public static final String FAILED="FAILED";
    
    public static final String TRUE="true";
    
    public static final String FALSE="false";
    
    public static final String DATA_EXISTS="EXISTS";
    
    public static final String SUPEGROUP="super-group";
    
    public static final int AGENT_DEAD_MINUTES = 30;
    
    public static final String FIELD_LOGPATH = "PATH";
    
    public static final String FIELD_FILENAME = "FILE";
    
    public static final String FIELD_KEYTEXT = "KEYTEXT";
    
    public static final String REST_METHOD = "REST";
    
    public static final String BROWSER_METHOD = "BROWSER";
    
    public static final String SUPERGROUP_DESC="super group allows users to search across all machines, irrespective whether they have account on those machines";
    
    public static final class KEYS{
    	public static final String NEW_INSTALL="fob.check";
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
    

        public static final String UPDATE_AGENTS = "UPDATE AGENTS "
                + "SET NODE_PORT=?, "
                + "NODE_TZ=?, "
                + "LAST_MODIFIED=?, "
                + "AGENT_VERSION=? "
                + "WHERE NODE_NAME=?";
        
        public static final String INSERT_AGENT = "INSERT INTO AGENTS "
                + "(NODE_NAME,AGENT_NAME,COMM_KEY,OS_TYPE,AGENT_VERSION,NODE_PORT,PARRALEL,NODE_TZ,MAX_FILES_TO_SEARCH,"
                + "MAX_LINES_PER_FILE,MAX_LINES_IN_RESULT,RESULT_SIZE_KB,"
                + "LAST_MODIFIED,LAST_HEARTBEAT) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        public static final String UPDATE_NODE_AGENT="UPDATE AGENTS SET AGENT_NAME=?,"
        		+ "PARRALEL=?,"
        		+ "MAX_FILES_TO_SEARCH=?,"
        		+ "MAX_LINES_PER_FILE=?,"
        		+ "MAX_LINES_IN_RESULT=?,"
        		+ "RESULT_SIZE_KB=?,"
        		+ "LAST_MODIFIED=? "
        		+ "WHERE NODE_NAME=?";

        public static final String HEARTBEAT = "UPDATE AGENTS "
                + "SET LAST_HEARTBEAT=? "
                + "WHERE NODE_NAME=?";

        public static final String LABEL_INSERT = "INSERT INTO LABELS "
                + "(NODE_NAME,LABEL_NAME) "
                + "VALUES (?,?)";
        
        public static final String LABEL_DELETE = "DELETE FROM LABELS "
                + "WHERE NODE_NAME=? AND "
                + "LABEL_NAME=?";

        public static final String USER_INSERT = "INSERT INTO CREDENTIALS_USER "
                + "(USERNAME,PASSWORD,EMAIL,FIRST_NAME,LAST_NAME,CREATED_BY,CREATED_TIME,LAST_MODIFIED) "
                + "VALUES (?,?,?,?,?,?,?,?)";
        
        public static final String USER_PROFILE_AND_PASSWORD = "UPDATE CREDENTIALS_USER SET "
        		+ "PASSWORD=?,"
        		+ "EMAIL=?,"
        		+ "FIRST_NAME=?,"
        		+ "LAST_NAME=?,"
        		+ "LAST_MODIFIED=? "
        		+ "WHERE USERNAME=?";
        
        public static final String USER_PROFILE = "UPDATE CREDENTIALS_USER SET "
        		+ "EMAIL=?,"
        		+ "FIRST_NAME=?,"
        		+ "LAST_NAME=?,"
        		+ "LAST_MODIFIED=? "
        		+ "WHERE USERNAME=?";

        public static final String ROLE_INSERT = "INSERT INTO CREDENTIALS_ROLE "
                + "(ROLENAME,DESCRIPTION) "
                + "VALUES (?,?)";
        
        public static final String ROLE_INSERT_PATCH_JULY2018 = "INSERT INTO CREDENTIALS_ROLE "
                + "(ROLENAME,DESCRIPTION,VISIBLE) "
                + "VALUES (?,?,?)";

        public static final String GROUP_INSERT = "INSERT INTO CREDENTIALS_GROUP "
                + "(GROUPNAME,DESCRIPTION,CREATED_BY,CREATED_TIME) "
                + "VALUES (?,?,?,?)";

        public static final String USER_ROLE_MAP_INSERT = "INSERT INTO USER_ROLE_MAP "
                + "(USERNAME,ROLENAME,CREATED_BY,CREATED_TIME) "
                + "VALUES (?,?,?,?)";

        public static final String USER_NODE_MAP_INSERT = "INSERT INTO USER_NODE_MAP "
                + "(USERNAME,NODE_NAME,CREATED_BY,CREATED_TIME) "
                + "VALUES (?,?,?,?)";

        public static final String USER_GROUP_MAP_INSERT = "INSERT INTO USER_GROUP_MAP "
                + "(USERNAME,GROUPNAME,CREATED_BY,CREATED_TIME) "
                + "VALUES (?,?,?,?)";

        public static final String GET_ROLE_FOR_USER = "SELECT ROLENAME FROM USER_ROLE_MAP WHERE USERNAME=?";

        public static final String GET_NODE_FOR_USER = "SELECT A.NODE_NAME,A.AGENT_NAME,"
                + "A.NODE_PORT,A.PARRALEL,A.NODE_TZ,A.MAX_FILES_TO_SEARCH,"
                + "A.MAX_LINES_PER_FILE,A.MAX_LINES_IN_RESULT,A.RESULT_SIZE_KB,"
                + "A.LAST_MODIFIED,A.LAST_HEARTBEAT "
                + "FROM AGENTS A, USER_NODE_MAP M  "
                + "WHERE A.AGENT_NAME = M.NODE_NAME "
                + "AND M.USERNAME=?";
        
        public static final String GET_NODE_FOR_MULTIPLE_USERS = "SELECT A.NODE_NAME,A.AGENT_NAME,"
                + "A.NODE_PORT,A.PARRALEL,A.NODE_TZ,A.MAX_FILES_TO_SEARCH,"
                + "A.MAX_LINES_PER_FILE,A.MAX_LINES_IN_RESULT,A.RESULT_SIZE_KB,"
                + "A.LAST_MODIFIED,A.LAST_HEARTBEAT "
                + "FROM AGENTS A, USER_NODE_MAP M  "
                + "WHERE A.AGENT_NAME = M.NODE_NAME "
                + "AND M.USERNAME IN ";
        
        public static final String ASSIGNED_ROLE_FOR_USER="SELECT R.ROLENAME,R.DESCRIPTION,R.VISIBLE "
        		+ "FROM CREDENTIALS_ROLE R, USER_ROLE_MAP U "
        		+ "WHERE U.ROLENAME=R.ROLENAME "
        		+ "AND U.USERNAME=?";
        
        public static final String ASSIGNED_GROUPS_FOR_USER="SELECT G.GROUPNAME, G.DESCRIPTION "
        		+ "G.CREATED_BY, G.CREATED_TIME, G.MODIFIED_BY, G.LAST_MODIFIED "
        		+ "FROM USER_GROUP_MAP U,CREDENTIALS_GROUP G "
        		+ "WHERE U.GROUPNAME = G.GROUPNAME "
        		+ "AND U.USERNAME=?";
        
        public static final String GET_USER_PROFILE="SELECT * FROM CREDENTIALS_USER WHERE USERNAME=?";
        
        public static final String UPDATE_CREDENTIALS_GROUP="UPDATE CREDENTIALS_GROUP SET GROUPNAME=?,"
        		+ "DESCRIPTION=?,"
        		+ "MODIFIED_BY=?,"
        		+ "LAST_MODIFIED=? "
        		+ "WHERE GROUPNAME=?";
        
        public static final String CREATE_CONFIG= "INSERT INTO CONFIG (CFGID,VAL) VALUES (?,?)";
        
        public static final String UPDATE_CONFIG= "UPDATE CONFIG SET VAL=? WHERE CFGID=?";
        
        public static final String INSERT_AUTO_POPULATE_KEYWORD="INSERT INTO SEARCH_ASSISTANT(FIELD,KEYTEXT,FREQUENCY,USERNAME,CREATED_TIME) VALUES (?,?,?,?,?)";
        
        public static final String UPDATE_FRQUENCY_OF_AUTO_POPULATE="UPDATE SEARCH_ASSISTANT SET FREQUENCY=FREQUENCY+1 WHERE LOWER(FIELD)=LOWER(?) AND LOWER(KEYTEXT)=LOWER(?)";
        
        public static final String GET_AUTOPOPULATE_KWS="SELECT FIELD,KEYTEXT,FREQUENCY FROM SEARCH_ASSISTANT WHERE FIELD=?";
        
        public static final String AUTOPOPULATE_EXISTS="SELECT COUNT(*) FROM SEARCH_ASSISTANT WHERE LOWER(FIELD)=LOWER(?) AND LOWER(KEYTEXT)=LOWER(?)";

    }

    public static final class ROLE {

        public static final String ADMIN = "ADMIN";
        public static final String GROUP_ADMIN = "GROUP-ADMIN";
        public static final String GROUP_MEMBER = "GROUP-MEMBER";
        public static final String BOT="ROBOT";

        public static final String ADMIN_DESC = "System admin, can create groups, group members and assign roles";
        public static final String GROUP_ADMIN_DESC = "Group admin, can create group members and assign roles";
        public static final String GROUP_MEMBER_DESC = "Group members just can use the search and have no admin privilege";
        public static final String BOT_DESC="user with bot role can use rest services";
    }
    
    public static final class USER_DEFAULTS{
    	public static final String USERNAME="admin";
    	public static final String PASSWORD="$2a$12$Kqu8z4rEM15vQCjGxIb3ke37Wh35RCbfecN1ZdPTy7BUB66bVKLpK";
    	public static final String CREATED_BY="system";
    	public static final String EMAIL="admin@delete.me";
    	public static final String NAME="Admin";
    }
}
