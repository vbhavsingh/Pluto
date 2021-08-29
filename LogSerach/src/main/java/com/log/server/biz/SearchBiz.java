/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.server.biz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.log.analyzer.commons.Commons;
import com.log.analyzer.commons.Constants;
import com.log.analyzer.commons.model.AgentModel;
import com.log.analyzer.commons.model.FileLineClientResultModel;
import com.log.analyzer.commons.model.FileLineRequestModel;
import com.log.analyzer.commons.model.FileSearchResult;
import com.log.analyzer.commons.model.SearchModel;
import com.log.analyzer.commons.model.SearchResultModel;
import com.log.server.LocalConstants;
import com.log.server.comm.http.FileLineReader;
import com.log.server.concurrent.NodalSearch;
import com.log.server.concurrent.SaveUpdateSerachKeyword;
import com.log.server.concurrent.UserNodeCorrection;
import com.log.server.data.db.Dao;
import com.log.server.model.DaoSearchModel;
import com.log.server.model.FileLineServerResultModel;
import com.log.server.model.Group;
import com.log.server.model.LogRecordModel;
import com.log.server.model.SearchInput;
import com.log.server.model.ViewResultModel;
import com.log.server.model.graphics.GraphRowElement;
import com.log.server.model.graphics.GraphicsData;
import com.log.server.util.TagUtils;
import com.log.server.util.Utilities;

import net.rationalminds.LocalDateModel;
import net.rationalminds.Parser;
import net.rationalminds.es.EnvironmentalControl;

/**
 *
 * @author Vaibhav Pratap Singh
 */
@Service
public class SearchBiz {

	private final static Logger Log = LoggerFactory.getLogger(SearchBiz.class);
	private static final String pattern = "\"([^\"]*)\"";
	private static final String notContainPattern = "[\\s+]-[\\w]+";
	private static final Pattern patternContain;
	private static final Pattern patternRemove;
	private Parser parser = new Parser();
	
	@Autowired
	private Dao dao;

	static {
		patternContain = Pattern.compile(pattern);
		patternRemove = Pattern.compile(notContainPattern);
	}

	/**
	 * 
	 * @param input
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws Exception
	 */
	@Cacheable(value = "SearchResultCache", key = "#key")
	public ViewResultModel getSearchResult(SearchInput input,String key) throws InterruptedException, ExecutionException, Exception {
		return getSearchResult(input);
	}
	/**
	 * 
	 * @param input
	 * @param key
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws Exception
	 */
	public ViewResultModel getSearchResult(SearchInput input) throws InterruptedException, ExecutionException, Exception {
		long startTime= System.currentTimeMillis();
		List<String> logPathPatterns = Commons.getDelimatedList(input.getLogPathPatterns());
		List<String> logFileNamePatterns = Commons.getDelimatedList(input.getLogFileNamePtterns());
		List<String> searchOnLabels = Commons.getDelimatedList(input.getSearchOnLabels());

		DaoSearchModel daoIp = new DaoSearchModel();
		daoIp.setLogFileNamePatterns(logFileNamePatterns);
		daoIp.setLogPathPatterns(logPathPatterns);
		daoIp.setSearchOnLabels(searchOnLabels);
		List<AgentModel> nodes = dao.getClients(daoIp);

		if (nodes.isEmpty()) {
			Log.warn("no nodes found for search labels: {}", input.getSearchOnLabels());
			ViewResultModel model = new ViewResultModel(input);
			model.setExplicitErrorMessage("Label " + input.getSearchOnLabels() + " has no nodes associated, search can't be done.");
			return model;
		}

		SearchModel searchModel = new SearchModel();
		searchModel.setUserName(input.getUserName());
		searchModel.setSessionId(input.getSessionId());
		searchModel.setFileList(logFileNamePatterns);
		searchModel.setPathList(logPathPatterns);
		/*setting time period range. Time will be normalized in NodalSearch for requested timezone and target machine timezone */
		searchModel.setRequestedTz(input.getViewTz());
		searchModel.setStartDateTime(input.getFromDateTime());
		searchModel.setEndDateTime(input.getToDateTime());
		
		/*
		 * check if user belongs to super user group, these users can search all
		 * nodes irrespective of their id being present there
		 */
		List<Group> assignedGroups = dao.getAssignedGroupsForUser(input.getUserName());
		if (assignedGroups != null) {
			for (Group gp : assignedGroups) {
				if (LocalConstants.SUPEGROUP.equals(gp.getName())) {
					searchModel.setVip(true);
					break;
				}
			}
		}

		ViewResultModel model = executeParallelSearch(nodes, searchModel, input);
		model.setSearchKeyword(getContains(input.getSearch()));
		model.setMadatorySearchKeyword(getMustHave(input.getSearch()));
		model.setInput(input);
		/* save keywords used in the successful search and is not a rest web service call */
		if (model.getLinesFetched() > 0 && input.getMedium() != LocalConstants.REST_METHOD) {
			SaveUpdateSerachKeyword keywordSaver = new SaveUpdateSerachKeyword(input);
			Thread t = new Thread(keywordSaver);
			t.start();
		}
		model.setSearchTime(System.currentTimeMillis()-startTime);
		return model;
	}

	/**
	 * 
	 * @param nodes
	 * @param searchModel
	 * @param input
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws Exception
	 */
	private ViewResultModel executeParallelSearch(List<AgentModel> nodes, SearchModel searchModel, SearchInput input) throws InterruptedException, ExecutionException, Exception {
		long starttime = System.currentTimeMillis();

		String[] contains = getContains(input.getSearch());
		String[] mustContain = getMustHave(input.getSearch());
		String[] notContains = getNotContains(input.getSearch());

		searchModel.setTextCommand(getEgrepCommand(contains, mustContain, notContains));

		Log.trace("executing search on nodes with SearchModel : {}, on nodes: {}", searchModel, Utilities.listToString(nodes));
		CompletionService<SearchResultModel> pool = new ExecutorCompletionService<SearchResultModel>(LocalConstants.executor);

		/*
		 * parameter to record if max file search limit got violated anywhere on
		 * any node, this will help printing meaningful error on page in case 0
		 * records are recieved from all nodes. User need to be told that more
		 * files need to be searched.
		 */
		boolean maxFileLimitBreached = false;

		List<SearchResultModel> resultList = new ArrayList<SearchResultModel>();
		for (AgentModel node : nodes) {
			SearchModel newSearchModel = cloneSearchModelObject(searchModel);
			Log.trace("creating search job for node: {}", node);
			try {
				/* Linux and UNIX search commands differ - changed 06/30/2016 */

				newSearchModel.setArchiveCommand(getGzgrepCommand(node.getOsName(), contains, mustContain, notContains));
			} catch (Exception e) {
				throw new Exception("invalid search aguments");
			}
			Log.trace("created search criteria object : {}" + newSearchModel);
			Log.debug("Using archive log search command: {} on node :{}", newSearchModel.getArchiveCommand(), node.getClientNode());
			Log.debug("Using log search command: {} on onde: {}", newSearchModel.getArchiveCommand(), node.getClientNode());
			NodalSearch searchJob = new NodalSearch(node, newSearchModel);
			pool.submit(searchJob);
		}

		Map<String,String> faultNodeNames = new HashMap<String,String>();

		for (int i = 0; i < nodes.size(); i++) {
			SearchResultModel result = pool.take().get();
			if (result == null) {
				continue;
			}
			Log.debug("result recieved from node : {}, result is {}", result.getNodeName(), result);
			if (result.isFailed()) {
				/*
				 * if user is unauthorized to search on particular node, remove
				 * users access to from this application on that node
				 */
				if (LocalConstants.ERROR.UNAUTHORIZED.equals(result.getErrorCode())) {
					UserNodeCorrection correction = new UserNodeCorrection(searchModel.getUserName(), result.getNodeName());
					correction.run();
				}
				faultNodeNames.put(result.getNodeName(),Utilities.getFaultMessage(result, false));
			}
			resultList.add(result);
			/*
			 * if result in 0 and max file limit is breached set proper error
			 * notice
			 */
			if (result.getWarnings().isMaxFileLimitBreached()) {
				maxFileLimitBreached = true;
				int filesSearched = result.getResultList().size();
				int totalFilesFound = result.getFilesFoundCount();
				faultNodeNames.put(result.getNodeName(),Utilities.getMaxFileLimitViolationMsg(result, filesSearched));
			}
		}

		long endtime = System.currentTimeMillis();
		float totaltime = (endtime - starttime) / 1000;
		Log.info("Total time taken for search is {} secs, done over {} node/nodes, for search: {}", Commons.threeDecimal(totaltime), nodes.size(), searchModel.info());

		// executor.shutdown();
		ViewResultModel view = getPresentationResult(resultList, input);
		view.setServersSearched(nodes.size());
		view.setFaultNodeMessages(faultNodeNames);
		view.setMaxFileLimitBreached(maxFileLimitBreached);
		Log.trace("result compiled : {}", view);
		Log.info("returning result: {}, for search: {}", view.info(), searchModel.info());
		
		/*if there are no lines of data set appropriate error message*/
		if(view.getDatedTextLines() + view.getUnDatedTextLines() == 0){
			
		}

		return view;
	}
	
	/**
	 * 
	 * @param result
	 * @param viewTimeZone
	 * @return
	 */
	private ViewResultModel getPresentationResult(List<SearchResultModel> result, SearchInput input) {
		long starttime = System.currentTimeMillis();
		List<LogRecordModel> datedList = new ArrayList<LogRecordModel>();
		List<LogRecordModel> unDatedList = new ArrayList<LogRecordModel>();

		ViewResultModel viewModel = new ViewResultModel(datedList, unDatedList);
		
		List<String> searchKeyWords = new ArrayList<String>();
		searchKeyWords.addAll(Arrays.asList(ArrayUtils.nullToEmpty(getContains(input.getSearch()))));
		searchKeyWords.addAll(Arrays.asList(ArrayUtils.nullToEmpty(getMustHave(input.getSearch()))));
		
		List<GraphRowElement> dayFrequencyChart = new ArrayList<GraphRowElement>();
		List<GraphRowElement> nodeFrequencyChart = new ArrayList<GraphRowElement>();
		List<GraphRowElement> fileFrequencyChart = new ArrayList<GraphRowElement>();

		int fileCount = 0;
		int lineCount = 0;
		int datedLineCnt = 0;
		int undatedLineCnt = 0;
		int totalSearchableFiles = 0;

		Iterator<SearchResultModel> nodeRresultItr = result.iterator();
		while (nodeRresultItr.hasNext()) {
			SearchResultModel nodeResult = nodeRresultItr.next();

			setProblemsInPresentationModel(nodeResult, viewModel);
			/*
			 * if it is failed request, there will be not data in result, so
			 * skip any further analysis
			 */
			if (nodeResult.isFailed()) {
				Log.info("node {} returned no data ", nodeResult.getNodeName());
				continue;
			}

			totalSearchableFiles += nodeResult.getFilesFoundCount();
			List<FileSearchResult> fileSearchResultList = nodeResult.getResultList();
			Iterator i = fileSearchResultList.iterator();
			while (i.hasNext()) {
				fileCount++;
				FileSearchResult st = (FileSearchResult) i.next();
				List<String> textLineList = st.getTextLineList();
				int trimCount = st.getTrimmedCount() == 0 ? 0 : st.getCount() - st.getTrimmedCount();
				Iterator<String> textLineIterator = textLineList.iterator();
				while (textLineIterator.hasNext()) {
					lineCount++;
					String textLine = textLineIterator.next();
					int lineNumDelimeterPos = textLine.indexOf(":");
					LogRecordModel view = new LogRecordModel();
					
					/* Identify and extract line number information*/
					if(lineNumDelimeterPos>0 && lineNumDelimeterPos <10){
						try{
							int lineNumber=Integer.parseInt(textLine.substring(0,lineNumDelimeterPos));
							textLine=textLine.substring(lineNumDelimeterPos+1);
							view.setLogLineNumber(lineNumber);
						}catch (Exception e){
							Log.error("cannot parse line number from log record : {}", e.getMessage());
						}
					}
					
					view.setClientNode(nodeResult.getNodeName());
					view.setLogFileName(st.getFileName());
					view.setLogText(TagUtils.highlightSearch(textLine, searchKeyWords));
					if (trimCount > 0) {
						view.setMessage("trimmed down the result by " + trimCount + " lines due to search size restrictions");
						Log.trace("trimmed down the result by {} lines due to search size restrictions", trimCount);
					}

					Log.trace("Parsing text: {}", textLine);
					List<LocalDateModel> groups = null;
					try {
						groups = parser.parse(textLine);
					} catch (Exception e) {
						Log.error("error while parsing text to get date :{}", textLine, e);
						textLine = textLine + "--PARSE--ERROR--";
					}
					if (groups == null || groups.size() == 0) {
						unDatedList.add(view);
						undatedLineCnt++;
					} else {				
						//if search is for time interval, continue in case date lies beyond time interval
						if(input.isTimedSearch()) {
							LocalDateModel localdate = groups.get(0);
							if(isEventInDateRange(localdate, nodeResult.getTimeZone(), input) == false) {
								// Dated search and date is not in timezone, skip and continue x
								continue;
							}
							
						}
						LogRecordModel logRecord = getDateFormattedView(groups, view, nodeResult.getTimeZone(), input.getViewTz());
						datedList.add(logRecord);
						datedLineCnt++;
						String datePart = logRecord.getDisplayDate().split("\\s+")[0];
						dayFrequencyChart = updateChart(datePart,dayFrequencyChart);
					}
					nodeFrequencyChart = updateChart(nodeResult.getNodeName(), nodeFrequencyChart);
					fileFrequencyChart = updateChart(st.getFileName()+":"+nodeResult.getNodeName(), fileFrequencyChart);
				}

			}
		}
		
		GraphicsData graphicData = new GraphicsData();
		graphicData.setDateFrequencyChart(dayFrequencyChart);
		graphicData.setNodeFrequencyChart(nodeFrequencyChart);
		graphicData.setFileFrequencyChart(fileFrequencyChart);
		
		viewModel.setGraphicsData(graphicData);
		viewModel.setFilesWithMatchCount(fileCount);
		/*changing it to be sum of dated and updated as pure linecount become misleading in dated searches*/
		//viewModel.setLinesFetched(lineCount);
		viewModel.setLinesFetched(datedLineCnt+undatedLineCnt);
		viewModel.setDatedTextLines(datedLineCnt);
		viewModel.setUnDatedTextLines(undatedLineCnt);
		viewModel.setSearchedFilesCount(totalSearchableFiles);
		Collections.sort(datedList);
		long clockedtime = System.currentTimeMillis() - starttime;
		Log.info("presentation view, time taken {} secs", clockedtime / 1000);
		return viewModel;
	}
	/**
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@EnvironmentalControl(devMethod="com.log.server.util.DevEnvironmentMocker.getLines(args)")
	public FileLineServerResultModel  getLinesInRangeFromFile(FileLineRequestModel model) throws Exception{
		String command = getLineRangeCommand(model);
		if(command == null){
			throw new Exception("file name not present");
		}
		model.setCommand(command);
		AgentModel agent = dao.getRegisteredAgent(model.getNodeName());
		FileLineReader reader = new FileLineReader();
		FileLineClientResultModel result = reader.readLines(model, agent);
		List<String> logLines = result.getTextInRange();
		
		/*
		 * check if user belongs to super user group, these users can search all
		 * nodes irrespective of their id being present there
		 */
		List<Group> assignedGroups = dao.getAssignedGroupsForUser(model.getUserName());
		if (assignedGroups != null) {
			for (Group gp : assignedGroups) {
				if (LocalConstants.SUPEGROUP.equals(gp.getName())) {
					model.setVip(true);
					break;
				}
			}
		}
		
		Log.debug("Agent : " + agent);
		
		List<LogRecordModel> logResult = new ArrayList<LogRecordModel>();
		int i = model.getStartIndex();
		for(String textLine : logLines){
			LogRecordModel logRecord = new LogRecordModel();
			List<LocalDateModel> groups = null;
			try {
				groups = parser.parse(textLine);
			} catch (Exception e) {
				Log.error("error while parsing text to get date :{}", textLine, e);
				textLine = textLine + "--PARSE--ERROR--";
			}
			logRecord.setLogText(textLine);
			if(groups == null || groups.size() == 0){
				logResult.add(logRecord);
			}else{
				logRecord = getDateFormattedView(groups, logRecord, agent.getTimeZone(), model.getViewTz());
				logResult.add(logRecord);
			}
			logRecord.setLogLineNumber(i++);
			
		}
		FileLineServerResultModel serverResponse = new FileLineServerResultModel();
		serverResponse.setLogLines(logResult);
		serverResponse.setReadableFileSize(Utilities.fromatSizeFromBytes(result.getFileSize()));
		serverResponse.setLastModifiedDate(Utilities.millisecondsToDate(result.getLastModifiedTime(), model.getViewTz()));
		serverResponse.setCreatedDate(Utilities.millisecondsToDate(result.getCreationTime(), model.getViewTz()));
		return serverResponse;
	}

	
	/**
	 *
	 * @param contains
	 * @param mustContain
	 * @param notContains
	 * @return
	 * @throws Exception
	 */
	public String getEgrepCommand(String[] contains, String[] mustContain, String[] notContains) throws Exception {
		String command = "egrep -i -n ";
		String containPiped = concantatedPipedString(contains);
		String mustPiped = concantatedPipedString(mustContain);
		String notPiped = concantatedPipedString(notContains);
		String firstArg = concantatedPipedString(new String[] { containPiped, mustPiped });

		if (firstArg != null && firstArg.length() > 1) {
			if (containPiped != null && containPiped.length() > 1) {
				/* contains search keywords with OR */
				command = command + "'" + containPiped + "' " + Constants.FILE_NAME_PLACEHOLDER + " ";
			} else {
				/* contains search keywords with AND */
				command = concantatedMustKeyWords(mustContain, "egrep -i -n", true);
			}
			if (mustPiped != null && mustPiped.length() > 1 && containPiped != null && containPiped.length() > 1) {
				command = command + " | " + concantatedMustKeyWords(mustContain, "egrep -i -n", false);
			}
		}

		if (notPiped != null && notPiped.length() > 1) {
			if (firstArg == null || firstArg.length() == 0) {
				throw new Exception("invalid search aguments");
			}
			command = command + "| egrep -v '" + notPiped + "'";
		}
		command = command + " | grep -P "+ Constants.DATETIME_PATTERN_PLACEHOLDER;
		return command;
	}

	/**
	 *
	 * @param contains
	 * @param mustContain
	 * @param notContains
	 * @return
	 * @throws Exception
	 */
	public String getGzgrepCommand(String osName, String[] contains, String[] mustContain, String[] notContains) throws Exception {
		String archiveSearchCommand = "gzgrep -i -n -e  ";
		if ("sunos".equalsIgnoreCase(osName)) {
			archiveSearchCommand = "gzgrep -i -n -e  ";
		} else if ("linux".equalsIgnoreCase(osName)) {
			archiveSearchCommand = "zgrep -i -n -E  ";
		}
		String command = archiveSearchCommand;
		String containPiped = concantatedPipedString(contains);
		String mustPiped = concantatedPipedString(mustContain);
		String notPiped = concantatedPipedString(notContains);
		String firstArg = concantatedPipedString(new String[] { containPiped, mustPiped });

		if (firstArg != null && firstArg.length() > 1) {
			if (containPiped != null && containPiped.length() > 1) {
				command = command + "'" + containPiped + "' " + Constants.FILE_NAME_PLACEHOLDER + " ";
			} else {
				command = concantatedMustKeyWords(mustContain, "egrep -i -n", true);
				command = "gzgrep -i -n -e " + command.substring(8);
			}
			if (mustPiped != null && mustPiped.length() > 1 && containPiped != null && containPiped.length() > 1) {
				command = command + " | " + concantatedMustKeyWords(mustContain, "egrep -i -n", false);
			}
		}

		if (notPiped != null && notPiped.length() > 1) {
			if (firstArg == null || firstArg.length() == 0) {
				throw new Exception("invalid search aguments");
			}
			command = command + "| egrep -v '" + notPiped + "'";
		}
		command = command + " | grep -P "+ Constants.DATETIME_PATTERN_PLACEHOLDER;
		return command;
	}
	/**
	 * command to fetch lines in given range from file
	 * @param model
	 * @return
	 */
	public static String getLineRangeCommand(FileLineRequestModel model){
		if(model.getFileName() == null || "".equals(model.getFileName().trim())){
			return null;
		}
		int endIndex =  model.getEndIndex() - model.getStartIndex();
		String fileType = model.getFileName().substring(model.getFileName().lastIndexOf(".") + 1).toLowerCase();
		if("gz".equals(fileType)){
			String command = "gunzip -c " + model.getFileName()+ " | head -n "+model.getEndIndex()+" | " + "tail -"+ endIndex;
			return command;
		}
		String command = "head -n " + model.getEndIndex() + " " +  model.getFileName() +" | "+ "tail -"+ endIndex;
		return command;
	}

	/**
	 * This method clones {@link SearchModel} into a new objects. Archive
	 * command is not set as it differs by os type of remote node.
	 * 
	 * @param model
	 * @return
	 * @throws ParseException 
	 */
	public SearchModel cloneSearchModelObject(SearchModel model) throws ParseException {
		SearchModel clone = new SearchModel();
		clone.setEndDateTime(model.getEndDateTime());
		clone.setFileList(model.getFileList());
		clone.setPathList(model.getPathList());
		clone.setSessionId(model.getSessionId());
		clone.setStartDateTime(model.getStartDateTime());
		clone.setTextCommand(model.getTextCommand());
		clone.setUserName(model.getUserName());
		clone.setVip(model.isVip());
		clone.setRequestedTz(model.getRequestedTz());
		return clone;
	}
	
	/**
	 * 
	 * @param localdate
	 * @param boxTz
	 * @param input
	 * @return
	 */
	private boolean isEventInDateRange(LocalDateModel localdate,String boxTz,SearchInput input) {
		String format = Constants.SEARCH_DATE_FORMAT.toPattern();
		Date startDate = Utilities.convertStringToTimeZonedDate(input.getFromDateTime(), format, input.getViewTz());
		Date endDate = Utilities.convertStringToTimeZonedDate(input.getToDateTime(), format, input.getViewTz());
		Date date = Utilities.StringToDate(localdate.getConDateFormat(), localdate.getDateTimeString());
		DateTime dt = Utilities.convertTimeZonesinJoda(date,boxTz, input.getViewTz());
		Date eventDate = dt.toDate();
		return Utilities.isEventDateTimeInRange(eventDate, startDate, endDate);
	}

	/**
	 *
	 * @param arg
	 * @return
	 */
	private String concantatedPipedString(String[] arg) {
		String str = "";
		if (arg == null || arg.length < 1) {
			return null;
		}
		for (String s : arg) {
			if (s != null && !"".equals(s.trim())) {
				str = str + s + "|";
			}
		}
		if (str.length() > 1) {
			str = str.substring(0, str.length() - 1);
		}
		return str.trim();
	}

	/**
	 * 
	 * @param arg
	 * @return
	 */
	private String concantatedMustKeyWords(String[] arg, String cmd, boolean putPlaceholder) {
		String str = "";
		if (arg == null || arg.length < 1) {
			return null;
		}
		for (int i = 0; i < arg.length; i++) {
			if (arg[i] != null && !"".equals(arg[i].trim())) {
				if (i == 0 && putPlaceholder) {
					str = cmd + " '" + arg[i] + "' " + Constants.FILE_NAME_PLACEHOLDER + " |";
				} else {
					str = str + " " + cmd + " '" + arg[i] + "'|";
				}
			}
		}
		if (str.length() > 1) {
			str = str.substring(0, str.length() - 1);
		}
		return str.trim();
	}

	/**
	 *
	 * @param searchQuery
	 * @return
	 */
	public String[] getContains(String searchQuery) {
		String parts[] = null;
		if (searchQuery != null && !searchQuery.trim().equals("")) {
			searchQuery = searchQuery.replaceAll(notContainPattern, " ").trim();
			searchQuery = searchQuery.replaceAll(pattern, " ").trim();
			if ("".equals(searchQuery)) {
				return null;
			}
			parts = searchQuery.split("\\s+");
			for (int i = 0; i < parts.length; i++) {
				parts[i] = Utilities.posixSanitizer(parts[i]);
			}
		}
		return parts;
	}

	/**
	 *
	 * @param searchQuery
	 * @return
	 */
	public String[] getNotContains(String searchQuery) {
		String parts[] = null;
		if (searchQuery != null && !searchQuery.trim().equals("")) {
			Matcher match = patternRemove.matcher(searchQuery);
			List temp = new ArrayList();
			while (match.find()) {
				String s = match.group();
				s = s.replace("-", "").trim();
				temp.add(s);
			}
			parts = Arrays.copyOf(temp.toArray(), temp.toArray().length, String[].class);
			for (int i = 0; i < parts.length; i++) {
				parts[i] = Utilities.posixSanitizer(parts[i]);
			}
		}
		return parts;
	}

	/**
	 *
	 * @param searchQuery
	 * @return
	 */
	public String[] getMustHave(String searchQuery) {
		String parts[] = null;
		if (searchQuery != null && !searchQuery.trim().equals("")) {
			Matcher match = patternContain.matcher(searchQuery);
			List temp = new ArrayList();
			int c = match.groupCount();
			while (match.find()) {
				String s = match.group();
				s = s.replace("\"", "").trim();
				if ("".equals(s)) {
					continue;
				}
				temp.add(s);
			}
			parts = Arrays.copyOf(temp.toArray(), temp.toArray().length, String[].class);
			for (int i = 0; i < parts.length; i++) {
				parts[i] = Utilities.posixSanitizer(parts[i]);
			}
		}
		return parts;
	}

	/**
	 * Set elaborate errors and other issues into presentation model as messages
	 * 
	 * @param nodeResult
	 * @param viewModel
	 */
	private void setProblemsInPresentationModel(SearchResultModel nodeResult, ViewResultModel viewModel) {
		/* request rejected by client */
		if (nodeResult.isFailed()) {
			nodeResult.getWarnings().getMessages().add(Utilities.getFaultMessage(nodeResult, false));
		}
		if (nodeResult.hasWarnings()) {
			/* if more files remain to be searched include that warning too */
			if (nodeResult.getWarnings().isMaxFileLimitBreached()) {
				int filesSearched = nodeResult.getResultList().size();
				nodeResult.getWarnings().getMessages().add(Utilities.getMaxFileLimitViolationMsg(filesSearched, nodeResult));
			}
			viewModel.getNodeMessages().put(nodeResult.getNodeName(), nodeResult.getWarnings().getMessages());
		}

	}
	/**
	 * 
	 * @param groups
	 * @param view
	 * @param boxTz
	 * @param viewTz
	 * @return
	 */
	private LogRecordModel getDateFormattedView(List<LocalDateModel> groups, LogRecordModel view, String boxTz, String viewTz) {
		LocalDateModel localdate = groups.get(0);
		int start = localdate.getStart();
		int end = localdate.getEnd();
		String lineTxt = view.getLogText();

		String startText = (start ==1)?"":lineTxt.substring(0, start);
		String endText = lineTxt.substring(end);
		endText = (endText ==null)?"":endText.trim();
		endText=endText.replaceAll("^[(:)(-)(.)(=)(,)]+", "").trim();

		view.setLogText(startText + endText);

		Date date = Utilities.StringToDate(localdate.getConDateFormat(), localdate.getDateTimeString());
		Log.trace("Groups: "+groups+", view: "+view+", boxTz: "+boxTz+", viewTz: "+viewTz);
		DateTimeLocal dt = convertTimeZones(date, boxTz, viewTz);
		Log.trace("Identified date : '{}' converted into '{}'", localdate.getDateTimeString(), dt.viewDate);
		view.setDisplayDate(dt.viewDate);
		view.setSequence(dt.timeinMs);
		return view;
	}
	/**
	 * 
	 * @param fromDate
	 * @param frmTz
	 * @param toTz
	 * @return
	 */
	public DateTimeLocal convertTimeZones(Date fromDate, String frmTz, String toTz) {
		DateTimeFormatter fmt = DateTimeFormat.forPattern(LocalConstants.PRESENTATION_DATE_FORMAT);
		DateTime dtToTz = Utilities.convertTimeZonesinJoda(fromDate,frmTz,toTz);

		DateTimeLocal dtObj = new DateTimeLocal();
		dtObj.viewDate = dtToTz.toString(fmt);
		dtObj.timeinMs = dtToTz.getMillis();

		return dtObj;
	}
	
	
	public DateTimeLocal convertTimeZones(String dateText, String frmTz, String toTz) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

		/*
		 * Convert the time string into a string with valid timezone appeneded
		 * to it
		 */
		frmTz = Utilities.dayLightNormalizer(frmTz);
		toTz = Utilities.dayLightNormalizer(toTz);

		String fromDateTime = dateText + " " + DateTimeZone.forTimeZone(TimeZone.getTimeZone(frmTz));

		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss.SSS ZZZ");
		DateTimeFormatter fmt = DateTimeFormat.forPattern(LocalConstants.PRESENTATION_DATE_FORMAT);
		DateTime origDate = new DateTime(dtf.parseDateTime(fromDateTime));
		DateTime dtToTz = origDate.withZone(DateTimeZone.forTimeZone(TimeZone.getTimeZone(toTz)));

		DateTimeLocal dtObj = new DateTimeLocal();
		dtObj.viewDate = dtToTz.toString(fmt);
		dtObj.timeinMs = dtToTz.getMillis();

		return dtObj;
	}
	/**
	 * 
	 * @param label
	 * @param chartData
	 * @return
	 */
	private  List<GraphRowElement> updateChart(String label, List<GraphRowElement> chartData) {
		for(GraphRowElement row:chartData) {
			if(label.equals(row.getLabel())) {
				row.increment();
				return chartData;
			}
		}
		GraphRowElement row = new GraphRowElement(label);
		chartData.add(row);
		return chartData;
	}
	/**
	 * 
	 * @author Vaibhav Pratap Singh 
	 *
	 */
	private class DateTimeLocal {

		String viewDate;
		long timeinMs;

		@Override
		public String toString() {
			return ReflectionToStringBuilder.toString(this);
		}
	}

}
