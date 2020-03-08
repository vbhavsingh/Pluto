package com.log.analyzer.commons.model;

import com.log.analyzer.commons.Constants;


public class AgentTerminatorRequestModel implements SecureInterface{

	private String nodeName;

	private String user;

	private String session;

	private String originIp;
	
	private String securityTag = Constants.REQUEST_SANITY;


	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public String getOriginIp() {
		return originIp;
	}

	public void setOriginIp(String originIp) {
		this.originIp = originIp;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	
	
	@Override
	public String toString() {
		return "AgentTerminatorRequestModel [nodeName=" + nodeName + ", user=" + user + ", session=" + session + ", originIp=" + originIp + "]";
	}

	@Override
	public boolean validateObject(String criteria) {
		return this.securityTag.equalsIgnoreCase(criteria);
	}

	@Override
	public String getSecurityTag() {
		return this.securityTag;
	}

	@Override
	public void setSecurityTag(String securityTag) {
		this.securityTag = securityTag;
	}

}
