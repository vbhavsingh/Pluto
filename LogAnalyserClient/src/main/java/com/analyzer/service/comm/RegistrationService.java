/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.analyzer.service.comm;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.analyzer.LocalUtil;
import com.google.gson.Gson;
import com.log.analyzer.commons.CipherService;
import com.log.analyzer.commons.Constants;
import com.log.analyzer.commons.Util;
import com.log.analyzer.commons.model.AgentCommands;
import com.log.analyzer.commons.model.AgentRegistrationForm;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

/**
 *
 * @author vs1953
 */
public class RegistrationService {

    public final static Logger Log = LogManager.getLogger(RegistrationService.class);

    public String registerAgent() {
        Client c = Client.create();
        WebResource resource = c.resource(LocalUtil.getMasterURL() + Constants.REGISTER_URI_SUFFIX);
        AgentRegistrationForm agent = LocalUtil.getRegistrationObject();
        MultivaluedMap query = Util.toQueryParams(agent);
        Log.info("registering agent with master at " + LocalUtil.getMasterURL());
        String result=resource.queryParams(query).get(String.class);
        if(Constants.FAILURE.equals(result)){
        	Log.error("Registration failed with master");
        	return Constants.FAILURE;
        }else{
        	try {
				CipherService.setCipherKey(result);
			} catch (Exception e) {
				Log.fatal("communication channel is not regsitered, agent will not be able to decipher search requests",e);
			} 
        }
        return Constants.SUCCESS;
    }

    public String sendHeartbeat() {
        Client c = Client.create();
        String url = LocalUtil.getMasterURL();
        Log.info("sending heatbeat to master at " + url);
        WebResource resource = c.resource(url + Constants.HEARTBEAT_URI_SUFFIX);
        return resource.post(String.class, LocalUtil.getNodeFQDN());
    }

    public AgentCommands getMasterCommands() {
        Client c = Client.create();
        String url = LocalUtil.getMasterURL();
        Log.info("fetching latest indexing command from master at " + url);
        WebResource resource = c.resource(url + Constants.COMMAND_URI_SUFFIX);
        String cmd = resource.post(String.class, LocalUtil.getNodeFQDN());
        Gson gson = new Gson();
        AgentCommands commands = gson.fromJson(cmd,AgentCommands.class);
        Log.debug("recieved indexing command from master : " + cmd);
        return commands;
    }

}
