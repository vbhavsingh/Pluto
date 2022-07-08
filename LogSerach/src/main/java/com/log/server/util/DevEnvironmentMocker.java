package com.log.server.util;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.log.analyzer.commons.Constants;
import com.log.analyzer.commons.model.AgentModel;
import com.log.analyzer.commons.model.FileLineRequestModel;
import com.log.analyzer.commons.model.FileSearchResult;
import com.log.analyzer.commons.model.SearchModel;
import com.log.analyzer.commons.model.SearchResultModel;
import com.log.server.LocalConstants;
import com.log.server.concurrent.NodalSearch;
import com.log.server.model.FileLineServerResultModel;
import com.log.server.model.LogRecordModel;
import com.log.server.model.NodeAgentViewModel;

public class DevEnvironmentMocker {

	public Object getClients() {
		
		int randomNodeCount = (int) (Math.random()*10+5);
		List<AgentModel> node = new ArrayList<AgentModel>();
		for (int i=0;i<randomNodeCount;i++) {
			AgentModel a = new AgentModel();
			a.setClientConnectPort(1234);
			a.setClientLabels(Arrays.asList("test"));
			a.setClientName("pluto"+i+1+".test.net");
			a.setClientNode("pluto"+i+1+".test.net");
			node.add(a);
		}
		return node;
	}

	public Object call(Object object) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		SearchResultModel resultModel = new SearchResultModel();
		resultModel.setFilesFoundCount(10);
		resultModel.setFilesSelectedCount(10);

		Field fSearchModel = object.getClass().getDeclaredField("searchModel");
		fSearchModel.setAccessible(true);	
		SearchModel model = (SearchModel) fSearchModel.get(object);
		
		Field fNode = object.getClass().getDeclaredField("node");
		fNode.setAccessible(true);	
		AgentModel agent = (AgentModel) fNode.get(object);

		List<FileSearchResult> dummyResults = new ArrayList<FileSearchResult>();
		Calendar cal = Calendar.getInstance();
		int randomFileCount=(int) (Math.random()*10+2);
		for (int i = 0; i <= randomFileCount; i++) {
			List<String> lines = new ArrayList<String>();
			int randomLineCount=(int) (Math.random()*100+10);
			for (int j = 0; j <= randomLineCount; j++) {
				cal.setTime(new Date());
				cal.add(Calendar.DATE, -(int)((Math.random()*100)));
				lines.add(Constants.SEARCH_DATE_FORMAT.format(cal.getTime())+" this is line for test_" + i +" "+ model.getTextCommand());
			}
			FileSearchResult f = new FileSearchResult("test_file_" + i + ".log", lines);
			dummyResults.add(f);
		}
		if(StringUtils.isEmpty(agent.getClientNode())){
			resultModel.setNodeName("pluto.test.net");
		}else {
			resultModel.setNodeName(agent.getClientNode());
		}
		
		resultModel.setResultList(dummyResults);
		resultModel.setTimeZone(Calendar.getInstance().getTimeZone().getID());
		return resultModel;
	}
	
	public Object getLines(FileLineRequestModel ip) {
		/*FileLineRequestModel ip = (FileLineRequestModel)args[0];*/
		int start = ip.getStartIndex();
		int end = ip.getEndIndex();
		SimpleDateFormat sdf = new SimpleDateFormat(LocalConstants.PRESENTATION_DATE_FORMAT);
		List<LogRecordModel> logResult = new ArrayList<LogRecordModel>();
		for(int i=start;i<=end;i++) {
			LogRecordModel logRecord = new LogRecordModel();
			logRecord.setClientNode(ip.getNodeName());
			logRecord.setDisplayDate(sdf.format(new Date()));
			logRecord.setLogFileName(ip.getFileName());
			logRecord.setLogLineNumber(i);
			logRecord.setLogText("This is dummy text. The date is put as current date for demo.");
			logResult.add(logRecord);
		}
		FileLineServerResultModel result = new FileLineServerResultModel();
		result.setLogLines(logResult);
		result.setCreatedDate(sdf.format(new Date()));
		result.setLastModifiedDate(sdf.format(new Date()));
		return result;
	} 
	
	
	public Object giveMeDummyNodes() {
		List<NodeAgentViewModel> nodes = new ArrayList<NodeAgentViewModel>();
		int randomNodeCount = (int) (Math.random()*10+5);
		for (int i=0;i<randomNodeCount;i++) {
			NodeAgentViewModel node = new NodeAgentViewModel();
			node.setAgentName("pluto"+i+1+".test.net");
			node.setAlive(true);
			node.setLabels(Arrays.asList("test"));
			node.setMaxFilesToSearch(Constants.PROPERTIES.MAX_FILES_TO_SEARCH);
			node.setMaxLinesInresult(Constants.PROPERTIES.MAX_LINES_IN_RESULT);
			node.setMaxLinesPerFile(Constants.PROPERTIES.MAX_LINES_PER_FILE);
			node.setNodeName("pluto"+i+1+".test.net");
			node.setOsName("Linux");
			node.setParallelism(Constants.PROPERTIES.MAX_THREADS);
			node.setPort(51000);
			node.setVersion(Constants.CURRENT_AGENT_VERSION);
			nodes.add(node);
		}
		return nodes;
	}  
}
