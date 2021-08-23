/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.log.analyzer.commons.Constants;
import com.log.analyzer.commons.model.AgentCommands;
import com.log.analyzer.commons.model.AgentConfigurationModel;
import com.log.analyzer.commons.model.AgentRegistrationForm;
import com.log.server.LocalConstants;
import com.log.server.biz.AdminServices;

/**
 *
 * @author Vaibhav Pratap Singh
 */
@Controller
public class Configuration {

    private static final Logger Log = LoggerFactory.getLogger(Configuration.class);
    
    @Autowired
    private AdminServices svc;

    /**
     * Registration of new nodes, this will also return communication key
     *
     * @param node
     */
    @RequestMapping(Constants.REGISTER_URI_SUFFIX)
    public @ResponseBody
    String registerAgent(AgentRegistrationForm node) {
        try {
            Log.debug("Request to register node : " + node);
            LocalConstants.CLIENT_ACCESS_LOG.info("{} : request to register", node.getClientNode());
            svc.registerAgent(node);
            return svc.getRegisteredAgentdetail(node).getCommKey();
        } catch (Exception e) {
            Log.error("failed to register node: {} ",node.getClientNode(),e);
            LocalConstants.CLIENT_ACCESS_LOG.error("{} : failed to register",node.getClientNode(),e);
        }
        return Constants.FAILURE;
    }

    @RequestMapping(Constants.HEARTBEAT_URI_SUFFIX)
    public @ResponseBody
    String registerAgent(@RequestBody String node) {
        try {
            Log.debug("recieved heartbeat from node: {}",node);
            LocalConstants.CLIENT_ACCESS_LOG.info("{} : recieved heartbeat",node);
            AgentConfigurationModel conf=svc.recordHeartBeat(node);
            conf.setCurrentAgentVersion(Constants.CURRENT_AGENT_VERSION);
            Gson gson=new Gson();
            String response= gson.toJson(conf);
            Log.debug("sucessfully recorded heartbeat for node: {}",node);
            LocalConstants.CLIENT_ACCESS_LOG.info("sucessfully recorded heartbeat for node: {}",node);
            return response;
        } catch (Exception e) {
            Log.error("failed to record heartbeat for node: {}",node,e);
            LocalConstants.CLIENT_ACCESS_LOG.error("{} : failed to record heartbeat",node,e);
            return Constants.FAILURE;
        }
    }

    @RequestMapping(Constants.COMMAND_URI_SUFFIX)
    public @ResponseBody
    String getIndexCommand(@RequestBody String node) {
    	AgentCommands commands = svc.getCommands(node);
        Gson gson=new Gson();
        String response= gson.toJson(commands);
        Log.debug("command: {}  sent to node: {}",commands,node);
        LocalConstants.CLIENT_ACCESS_LOG.info("{} : request to get commands",node);
        return response;
    }
}
