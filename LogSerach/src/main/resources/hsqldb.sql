SET DATABASE EVENT LOG SQL LEVEL 0;
CREATE TABLE  IF NOT EXISTS PUBLIC.AGENTS
(
NODE_NAME VARCHAR(50) NOT NULL,
AGENT_NAME VARCHAR(30) NOT NULL,
COMM_KEY VARCHAR(50) NOT NULL,
OS_TYPE VARCHAR(50) NOT NULL,
AGENT_VERSION VARCHAR(20) NOT NULL,
NODE_PORT INTEGER NOT NULL,
PARRALEL INTEGER DEFAULT 20 NOT NULL,
NODE_TZ VARCHAR(10),
MAX_FILES_TO_SEARCH INTEGER DEFAULT 50 NOT NULL,
MAX_LINES_PER_FILE INTEGER DEFAULT 50 NOT NULL,
MAX_LINES_IN_RESULT INTEGER DEFAULT 200 NOT NULL,
RESULT_SIZE_KB INTEGER DEFAULT 2048 NOT NULL,
LAST_MODIFIED TIMESTAMP,
LAST_HEARTBEAT TIMESTAMP,
PRIMARY KEY (NODE_NAME));

CREATE  TABLE IF NOT EXISTS PUBLIC.LABELS 
(NODE_NAME VARCHAR(50) NOT NULL,
LABEL_NAME VARCHAR(100) NOT NULL);

CREATE TABLE IF NOT EXISTS  PUBLIC.FILE_NAME_PATTERN 
(NODE_NAME VARCHAR(50) NOT NULL,
PATTERN VARCHAR(1000) NOT NULL);

CREATE TABLE IF NOT EXISTS  PUBLIC.CREDENTIALS_USER
(USERNAME VARCHAR(50) NOT NULL,
PASSWORD VARCHAR(50) NOT NULL,
EMAIL VARCHAR(50) NOT NULL,
FIRST_NAME VARCHAR(50),
LAST_NAME VARCHAR(50),
CREATED_BY VARCHAR(50),
CREATED_TIME TIMESTAMP,
LAST_MODIFIED TIMESTAMP,
PRIMARY KEY (USERNAME));

CREATE TABLE IF NOT EXISTS  PUBLIC.CREDENTIALS_ROLE
(ROLENAME VARCHAR(50) NOT NULL,
DESCRIPTION VARCHAR(500),
PRIMARY KEY (ROLENAME));

CREATE TABLE IF NOT EXISTS  PUBLIC.CREDENTIALS_GROUP
(GROUPNAME VARCHAR(50) NOT NULL,
DESCRIPTION VARCHAR(500),
CREATED_BY VARCHAR(50),
MODIFIED_BY VARCHAR(50),
CREATED_TIME TIMESTAMP,
LAST_MODIFIED TIMESTAMP,
PRIMARY KEY (GROUPNAME));

CREATE TABLE IF NOT EXISTS  PUBLIC.USER_ROLE_MAP
(USERNAME VARCHAR(50) NOT NULL,
ROLENAME VARCHAR(50) NOT NULL,
CREATED_BY VARCHAR(50),
CREATED_TIME TIMESTAMP,
PRIMARY KEY (USERNAME,ROLENAME));

CREATE TABLE IF NOT EXISTS  PUBLIC.USER_GROUP_MAP
(USERNAME VARCHAR(50) NOT NULL,
GROUPNAME VARCHAR(50) NOT NULL,
CREATED_BY VARCHAR(50),
CREATED_TIME TIMESTAMP,
PRIMARY KEY (USERNAME,GROUPNAME));

CREATE TABLE IF NOT EXISTS  PUBLIC.USER_NODE_MAP
(USERNAME VARCHAR(50) NOT NULL,
NODE_NAME VARCHAR(50) NOT NULL,
CREATED_BY VARCHAR(50),
CREATED_TIME TIMESTAMP,
PRIMARY KEY (USERNAME,NODE_NAME));

CREATE TABLE IF NOT EXISTS  PUBLIC.CONFIG
(CFGID VARCHAR(50) NOT NULL,
VAL VARCHAR(500) NOT NULL,
PRIMARY KEY (CFGID));

CREATE TABLE IF NOT EXISTS  PUBLIC.SEARCH_ASSISTANT
(FIELD VARCHAR(50) NOT NULL,
KEYTEXT VARCHAR(500) NOT NULL,
FREQUENCY INTEGER NOT NULL,
USERNAME VARCHAR(50) NOT NULL,
CREATED_TIME TIMESTAMP,
PRIMARY KEY (FIELD,KEYTEXT));
