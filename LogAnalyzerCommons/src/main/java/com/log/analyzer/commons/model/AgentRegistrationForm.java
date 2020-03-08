/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.analyzer.commons.model;

import java.util.List;

/**
 *
 * @author Vaibhav Pratap Singh
 */
public class AgentRegistrationForm {

    private String clientName;
    
    private String agentVersion;

    private List<String> clientLabels;

    private String clientNode;

    private String clientConnectPort;

    private String timeZone;

    private String commKey;
    
    private String osName;
    
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public List<String> getClientLabels() {
        return clientLabels;
    }

    public void setClientLabels(List<String> clientLabels) {
        this.clientLabels = clientLabels;
    }

    public String getClientNode() {
        return clientNode;
    }

    public void setClientNode(String clientNode) {
        this.clientNode = clientNode;
    }

    public String getClientConnectPort() {
        return clientConnectPort;
    }

    public void setClientConnectPort(String clientConnectPort) {
        this.clientConnectPort = clientConnectPort;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
    

	public String getCommKey() {
		return commKey;
	}

	public void setCommKey(String commKey) {
		this.commKey = commKey;
	}
	
	

	public String getAgentVersion() {
		return agentVersion;
	}

	public void setAgentVersion(String clientCurrentVersion) {
		this.agentVersion = clientCurrentVersion;
	}
	

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	@Override
	public String toString() {
		return "AgentRegistrationForm [clientName=" + clientName + ", agentVersion=" + agentVersion + ", clientLabels=" + clientLabels + ", clientNode=" + clientNode
				+ ", clientConnectPort=" + clientConnectPort + ", timeZone=" + timeZone + ", osName=" + osName + "]";
	}

}
