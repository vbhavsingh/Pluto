/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.server.data.db.mapper;

import com.log.server.LocalConstants;
import com.log.server.model.NodeAgentViewModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Vaibhav Singh
 */
public class AgentViewModelDbMapper implements RowMapper<NodeAgentViewModel> {

	private static final Logger Log = Logger.getLogger(AgentViewModelDbMapper.class);

	@Override
	public NodeAgentViewModel mapRow(ResultSet rs, int i) throws SQLException {

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z");

		NodeAgentViewModel model = new NodeAgentViewModel();
		model.setAgentName(rs.getString("AGENT_NAME"));
		model.setOsName(rs.getString("OS_TYPE"));
		model.setLastHeartbeat(sdf.format(rs.getTimestamp("LAST_HEARTBEAT")));
		model.setLastModifed(rs.getDate("LAST_MODIFIED"));
		model.setMaxFilesToSearch(rs.getInt("MAX_FILES_TO_SEARCH"));
		model.setMaxLinesInresult(rs.getInt("MAX_LINES_IN_RESULT"));
		model.setMaxLinesPerFile(rs.getInt("MAX_LINES_PER_FILE"));
		model.setNodeName(rs.getString("NODE_NAME"));
		model.setParallelism(rs.getInt("PARRALEL"));
		model.setPort(rs.getInt("NODE_PORT"));
		model.setResultInKb(rs.getInt("RESULT_SIZE_KB"));
		model.setVersion(rs.getString("AGENT_VERSION"));

		try {
			long diff = (new Date()).getTime() - sdf.parse(model.getLastHeartbeat()).getTime();
			long minutes = TimeUnit.MILLISECONDS.toMinutes(diff); 
			if(minutes > LocalConstants.AGENT_DEAD_MINUTES){
				model.setAlive(false);
			}
			if(minutes < 60){
				String literal = minutes==1?" minute":" minutes";
				model.setDeadSince(minutes + literal);
			}
			if(minutes >= 60 && minutes <24*60){
				minutes = (int)(Math.ceil(minutes/60));
				String literal = minutes==1?" hour":" hours";
				model.setDeadSince(minutes+literal);
			}
			if(minutes >= 24*60){
				minutes = (int)Math.ceil(minutes/(24*60));
				String literal = minutes==1?" day":" days";
				model.setDeadSince(minutes+literal);
			}
		} catch (ParseException e) {
			Log.error("error while determining whether client is alive, by checking its last heartbeat pulse",e);
		}

		return model;
	}

}
