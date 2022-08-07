/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.analyzer.posix;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.analyzer.LocalUtil;
import com.analyzer.commons.ClientFileModel;
import com.analyzer.commons.FileIndexModel;
import com.analyzer.commons.FilePathListResult;
import com.analyzer.commons.LocalConstants;
import com.analyzer.posix.concurrent.SearchFileTask;
import com.log.analyzer.commons.Constants;
import com.log.analyzer.commons.err.InputParsingException;
import com.log.analyzer.commons.model.FileSearchResult;
import com.log.analyzer.commons.model.SearchModel;
import com.log.analyzer.commons.model.SearchResultModel;
import com.log.analyzer.commons.model.WarningFlags;

import net.rationalminds.util.RegexPatternMaker;

/**
 *
 * @author Vaibhav Pratap Singh
 */
public class Search {

	private final static Logger Log = LogManager.getLogger(Search.class);
	private static final ExecutorService executor = Executors.newFixedThreadPool(100);
	private static int timeout = 20;

	private int linesIncluded = 0;
	private int validSearchedFiles = 0;
	private AtomicLong resultSizeOfData = new AtomicLong(0);

	public SearchResultModel executeSearch(SearchModel model) throws InterruptedException, ExecutionException, InputParsingException, ParseException {
		Log.debug("starting search for request from " + model.getUserName());
		Log.trace("received search request for criterias : " + model);
		LogFileNameFinder finder = new LogFileNameFinder();
		FilePathListResult filePathListResult = finder.getRelevantLogFileList(model);
		List<FileIndexModel> fileList = filePathListResult.getFilepathList();
		if (fileList == null || fileList.isEmpty()) {
			return null;
		}

		SearchResultModel resultModel = new SearchResultModel();
		WarningFlags flags = resultModel.getWarnings();
		resultModel.setWarnings(flags);

		if (filePathListResult.isLimitBreached()) {
			flags.setMaxFileLimitBreached(true);
		}

		resultModel.setTimeZone(LocalUtil.getTimeZone());
		resultModel.setNodeName(LocalUtil.getAgentCustomName());
		resultModel.setFilesFoundCount(filePathListResult.getFilesMatchedCount());
		resultModel.setFilesSelectedCount(fileList.size());

		List<FileSearchResult> resultList = new ArrayList<FileSearchResult>();

		CompletionService<ClientFileModel> pool = new ExecutorCompletionService<ClientFileModel>(executor);

		for (FileIndexModel file : fileList) {
			String command = "";
			if (file.getAbsoluteFileName().matches(Constants.ARCHIVE_LOG_PATTERN)) {
				command = model.getArchiveCommand();
			} else {
				command = model.getTextCommand();
			}
			command = command.replace(Constants.FILE_NAME_PLACEHOLDER, file.getAbsoluteFileName());
			if(model.isTimedSearch() && file.getDateFormat() != null) {
				String pattern = RegexPatternMaker.getPattern(model.getStartDateTime(), model.getEndDateTime(), file.getDateFormat());
				pattern = pattern==null ?"":pattern;
				command = command.replace(Constants.DATETIME_PATTERN_PLACEHOLDER, "'"+pattern+"'");
			}else {
				command = command.replace(Constants.DATETIME_PATTERN_PLACEHOLDER, "''");
			}
			SearchFileTask searchTask = new SearchFileTask(command, this.resultSizeOfData, file.getAbsoluteFileName());
			pool.submit(searchTask);
		}

		for (int i = 0; i < fileList.size(); i++) {
			Log.trace("fetching task : " + i);
			ClientFileModel fileModel = pool.take().get();
			Log.trace(fileModel.getTotalLines() + " lines found in file :" + fileModel.getFileName());
			if (fileModel.getTextLineList().size() > 0) {
				validSearchedFiles++;
				FileSearchResult resultText = new FileSearchResult(fileModel.getFileName(), fileModel.getTextLineList());
				resultList.add(resultText);
				linesIncluded += fileModel.getTotalLines();
				if (fileModel.isMaxdataLimitBreached()) {
					flags.setMaxDataLimitBreached(true);
				}
				if (fileModel.isMaxLineLimitBreached()) {
					flags.setMaxLinesPerFileLimitBreached(true);
					flags.getMessages().add("Max read limit for search per file violated while reading " + fileModel.getFileName());
					Log.trace("Max read limit for search per file violated while reading " + fileModel.getFileName());
				}
			}
		}
		/*
		 * If the final resultset is large beyond permitted threshold, delete
		 * line in individual file result set to meet the target
		 */
		if (linesIncluded > LocalConstants.MAX_LINES_ALLOWED_IN_RESULT) {
			Log.trace("total lines in result exceeded allowed limit of " + LocalConstants.MAX_LINES_ALLOWED_IN_RESULT);
			/* sort the list for removing excess data */
			flags.setMaxLineLimitBreached(true);
			Collections.sort(resultList);
			int tentaiveMaxLinesPerFile = (int) Math.ceil(LocalConstants.MAX_LINES_ALLOWED_IN_RESULT / (double) validSearchedFiles);
			Log.trace("tentaive max lines allowed per file : " + tentaiveMaxLinesPerFile + ", where valid searched file count is :" + validSearchedFiles);
			int newLineCount = 0;
			int filesConsidered = validSearchedFiles;
			Iterator<FileSearchResult> itr = resultList.iterator();
			while (itr.hasNext()) {
				--filesConsidered;
				FileSearchResult rs = itr.next();
				if (filesConsidered == 0) {
					if (rs.getCount() > tentaiveMaxLinesPerFile) {
						rs = trimmer(rs, tentaiveMaxLinesPerFile);
					}
					break;
				}
				if (rs.getCount() > tentaiveMaxLinesPerFile) {
					rs = trimmer(rs, tentaiveMaxLinesPerFile);
					newLineCount += rs.getTextLineList().size();
					tentaiveMaxLinesPerFile = (int) Math.ceil((LocalConstants.MAX_LINES_ALLOWED_IN_RESULT - newLineCount) / filesConsidered);
				} else {
					newLineCount += rs.getTextLineList().size();
					tentaiveMaxLinesPerFile = (int) Math.ceil((LocalConstants.MAX_LINES_ALLOWED_IN_RESULT - newLineCount) / filesConsidered);
					Log.trace("using all lines from file: " + rs.getFileName());
				}
			}
		}

		resultModel.setResultList(resultList);
		return resultModel;
	}

	/**
	 *
	 * @param rs
	 * @param tentaiveMaxLinesPerFile
	 * @return
	 */
	private FileSearchResult trimmer(FileSearchResult rs, int tentaiveMaxLinesPerFile) {
		int size = rs.getTextLineList().size();
		List sublist = rs.getTextLineList().subList(0, tentaiveMaxLinesPerFile);
		int trimmedCount = size - tentaiveMaxLinesPerFile;
		rs.setTrimmedCount(trimmedCount);
		rs.setTextLineList(sublist);
		Log.debug("removing " + trimmedCount + " lines out of " + size + " lines from file: " + rs.getFileName() + " tentaive limit is " + tentaiveMaxLinesPerFile);
		return rs;
	}
}
