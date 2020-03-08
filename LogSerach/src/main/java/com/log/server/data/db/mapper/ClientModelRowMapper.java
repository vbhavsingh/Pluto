/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.server.data.db.mapper;

import com.log.analyzer.commons.model.AgentModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Vaibhav Pratap Singh
 */
public class ClientModelRowMapper implements RowMapper<AgentModel>{
    
    public AgentModel mapRow(ResultSet rs,int rowNum) throws SQLException{
        AgentModel model=new AgentModel();
        model.setClientName(rs.getString("AGENT_NAME"));
        model.setClientNode(rs.getString("NODE_NAME"));
        model.setCommKey(rs.getString("COMM_KEY"));
        model.setOsName(rs.getString("OS_TYPE"));
        model.setClientConnectPort(rs.getInt("NODE_PORT"));
        model.setThreads(rs.getInt("PARRALEL"));
        model.setTimeZone(rs.getString("NODE_TZ"));
        return model;
    }
    
}
