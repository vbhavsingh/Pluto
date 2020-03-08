package com.analyzer.posix;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.analyzer.commons.CustomConcurrentMap;
import com.analyzer.commons.FileIndexModel;
import com.analyzer.commons.FilePathListResult;
import com.log.analyzer.commons.Constants;
import com.log.analyzer.commons.err.InputParsingException;
import com.log.analyzer.commons.model.SearchModel;

public class LogFileNameFinderTest {

  @Test
  public void getRelevantLogFileList() throws InputParsingException, ParseException {
	  Calendar cal = Calendar.getInstance();
	  cal.setTime(new Date());
	  cal.add(Calendar.DAY_OF_MONTH, -2);
	  
	  final CustomConcurrentMap filePathMap = new CustomConcurrentMap();
		for (int i = 0; i < (Math.random() + 1) * 10; i++) {
			List<FileIndexModel> indexes = new ArrayList<FileIndexModel>();
			for (int j = 0; j < (Math.random() + 1) * 10; j++) {
				FileIndexModel f = new FileIndexModel(String.valueOf(j)+".log");
				f.setDateFormat(Constants.SEARCH_DATE_FORMAT.toPattern());
				f.setLastModifiedDate(cal.getTime());
				indexes.add(f);
			}
			filePathMap.put("path/"+String.valueOf(i)+"/", indexes);
		}
		LogFileNameFinder.filePathMap.clear();
		LogFileNameFinder.filePathMap.putAll(filePathMap);
		
		SearchModel model = new SearchModel();
		List<String> files = new ArrayList<String>(); 
		files.add("10.log");
		files.add("12.log");
		
		model.setFileList(files);
		model.setArchiveCommand("egrep "+Constants.DATETIME_PATTERN_PLACEHOLDER+" "+Constants.FILE_NAME_PLACEHOLDER);
		model.setTextCommand("grep "+Constants.DATETIME_PATTERN_PLACEHOLDER+" "+Constants.FILE_NAME_PLACEHOLDER);
		
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, -5);
		
		model.setStartDateTime(Constants.SEARCH_DATE_FORMAT.format(cal.getTime()));
		model.setEndDateTime(Constants.SEARCH_DATE_FORMAT.format(new Date()));
		//model.setTimedSearch(true);
		
		LogFileNameFinder finder = new LogFileNameFinder();
		FilePathListResult result = finder.getRelevantLogFileList(model);
		
		Assert.assertTrue(result.getFilepathList().size()>0);
  }
}
