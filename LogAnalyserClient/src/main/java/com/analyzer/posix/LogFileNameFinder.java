/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.analyzer.posix;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.analyzer.LocalUtil;
import com.analyzer.commons.CustomConcurrentMap;
import com.analyzer.commons.FileIndexModel;
import com.analyzer.commons.FilePathListResult;
import com.analyzer.commons.LocalConstants;
import com.analyzer.commons.SystemStreamCapture;
import com.log.analyzer.commons.err.InputParsingException;
import com.log.analyzer.commons.model.SearchModel;

/**
 *
 * @author vs1953
 */
public class LogFileNameFinder {

    private static final Logger Log = Logger.getLogger(LogFileNameFinder.class);

    public final static CustomConcurrentMap filePathMap = new CustomConcurrentMap();

    /**
     * Periodic call will have information ready in the cache
     *
     * @return
     * @throws IOException
     */
    public static void findAllFile() throws IOException, InterruptedException {
        String command = null;
        if (LocalConstants.FIND_COMMAND == null) {
            Log.debug("no indexing command recieved from master, using default command");
            command = LocalConstants.DEFAULT_FIND_COMMAND;
        } else {
            Log.debug("using indexing command provided by master");
            command = LocalConstants.FIND_COMMAND;
        }
        String[] script = {"/bin/sh", "-c", command};
        Log.debug("started indexing of log files");
        Log.trace("executing log file search script : " + command);
        long statrtime = System.currentTimeMillis();
        Process p = Runtime.getRuntime().exec(script);

        SystemStreamCapture errorStream = new SystemStreamCapture(p.getErrorStream(), LocalConstants.GENERIC_TYPE);
        SystemStreamCapture outputStream = new SystemStreamCapture(p.getInputStream(), LocalConstants.GENERIC_TYPE);

        errorStream.start();
        outputStream.start();

        while (!outputStream.isDone()) {
            Thread.sleep(1000);
        }

        List<String> filepathList = outputStream.getData();
        
        Log.trace("search finished, starting to index the files searched ");
        Log.debug("executed command : " + command);

        CustomConcurrentMap newmap = new CustomConcurrentMap();
        for (String s : filepathList) {
            String pathParts[] = getPathSeprator(s);
            String dir = pathParts[0];
            String file = pathParts[1];
            if (newmap.containsKey(dir)) {
                List<FileIndexModel> existingFileList = newmap.get(dir);
                if (existingFileList == null) {
                    existingFileList = new ArrayList<FileIndexModel>();
                }
                if (!contains(existingFileList, file)) {
                    existingFileList.add(new FileIndexModel(dir, file));
                }
            } else {
                List<FileIndexModel> fileList = new ArrayList<FileIndexModel>();
                fileList.add(new FileIndexModel(dir,file));
                newmap.put(dir, fileList);
            }
        }
        filePathMap.setNewmap(newmap);
        long timeTaken= System.currentTimeMillis() - statrtime ;
        Log.debug("total " + filePathMap.getFileCount() + " files indexed in " + filePathMap.size() + " directories, in "+timeTaken+"ms");
    }

    /**
     *
     * @param model
     * @return
     * @throws InputParsingException 
     * @throws ParseException 
     */
    public FilePathListResult getRelevantLogFileList(SearchModel model) throws InputParsingException, ParseException {
        if (filePathMap == null || filePathMap.isEmpty()) {
            Log.debug("no files on disk to be searched, may be indexing is not complete");
            return null;
        }
        boolean filePathsPresent = (model.getPathList() != null && model.getPathList().size() > 0) ? true : false;
        boolean fileNamesPresent = (model.getFileList() != null && model.getFileList().size() > 0) ? true : false;
        
        /*variable to hold the list of files that will be searched*/
        List<FileIndexModel> fileList = new ArrayList<FileIndexModel>();
        
        Log.debug("search options requested : " + model);
        
        /*Wrapper variable to contain file list and flag indicators*/
        FilePathListResult result = new FilePathListResult();
        result.setFilepathList(fileList);
        
        int totalFiles = 0;
        
        /*If file names and file path both are present in search criteria, the file will be searched only in given path locations
         * only. 
         * */        
        if (filePathsPresent && fileNamesPresent) {
            for (String path : model.getPathList()) {
            	/*path map has file path as key and list of files at that path as value*/
                Map<String, List<FileIndexModel>> matchingPathMap = filePathMap.getMatchingPathMap(path);
                /* set the count of files that qualifies for search before applying node limits of file counts*/
                result.setFilesMatchedCount(LocalUtil.getTotalCount(model, matchingPathMap));
                /*if no matching path is found*/
                if(matchingPathMap==null){
                	Log.trace("no matching file found for given options : "+model);
                	break;
                }
                for (Map.Entry<String, List<FileIndexModel>> resultEntry : matchingPathMap.entrySet()) {
                    String pathPrefix = resultEntry.getKey();
                    /*get the list of matching files with full qualified path at this directory*/
                    if(resultEntry.getValue()==null){
                    	break;
                    }
                    FilePathListResult tempResult = getMatchingFileName(model, resultEntry.getValue(), pathPrefix);
                    
                    fileList.addAll(tempResult.getFilepathList());
                    totalFiles = fileList.size();
                    Log.debug("total files that can be searched in directory "+pathPrefix+" are: "+totalFiles);
                    if (totalFiles >= LocalConstants.MAX_FILES_TO_BE_SEARCHED) {
                        Log.debug("max file limit reached while searching " + pathPrefix);
                        result.setLimitBreached(true);
                        break;
                    }
                    if (tempResult.isLimitBreached()) {
                        Log.debug("max file limit reached while searching " + pathPrefix);
                        result.setLimitBreached(true);
                        break;
                    }
                }
            }
            //if only filePath is present in search Criteria
        } else if (filePathsPresent) {
        	Log.debug("file path is present instead of filename");
            for (String path : model.getPathList()) {
            	/*path map has file path as key and list of files at that path as value*/
                Map<String, List<FileIndexModel>> matchingPathMap = filePathMap.getMatchingPathMap(path); 
                
                /*if no matching path is found*/
                if(matchingPathMap==null){
                	Log.trace("no matching file found for provided options : "+model);
                	break;
                }
                
                for (Map.Entry<String, List<FileIndexModel>> resultEntry : matchingPathMap.entrySet()) {
                	Iterator<FileIndexModel> itr = resultEntry.getValue().iterator();
                	while(itr.hasNext()) {
                		FileIndexModel index =itr.next();
                		if(LocalUtil.isFileinTimeInterval(index, model) == false) {
                			itr.remove();
                		}
                	}
                }
                
                Log.debug("directories found : "+matchingPathMap.size());
                /* set the count of files that qualifies for search before applying node limits of file counts*/
                result.setFilesMatchedCount(LocalUtil.getTotalCount(matchingPathMap));
                for (Map.Entry<String, List<FileIndexModel>> resultEntry : matchingPathMap.entrySet()) {
                    String pathPrefix = resultEntry.getKey();
                    if (totalFiles >= LocalConstants.MAX_FILES_TO_BE_SEARCHED) {
                        Log.debug("max file limit reached while searching " + pathPrefix);
                        result.setLimitBreached(true);
                        break;
                    }
                    if(resultEntry.getValue()==null){
                    	continue;
                    }
                    for (FileIndexModel index : resultEntry.getValue()) {
                        if (totalFiles >= LocalConstants.MAX_FILES_TO_BE_SEARCHED) {
                            Log.debug("max file limit reached while searching " + pathPrefix);
                            result.setLimitBreached(true);
                            break;
                        }
                        fileList.add(index);
                        totalFiles++;
                    }
                }
            }
        } else if (fileNamesPresent) { 
        	 /* set the count of files that qualifies for search before applying node limits of file counts*/
            result.setFilesMatchedCount(LocalUtil.getTotalCount(model, filePathMap));
            for (Map.Entry<String, List<FileIndexModel>> resultEntry : filePathMap.entrySet()) {
                String pathPrefix = resultEntry.getKey();
                FilePathListResult tempResult = getMatchingFileName(model, resultEntry.getValue(), pathPrefix);
                fileList.addAll(tempResult.getFilepathList());
                totalFiles = fileList.size();
                if (totalFiles >= LocalConstants.MAX_FILES_TO_BE_SEARCHED) {
                    Log.debug("max file limit reached while searching " + pathPrefix);
                    result.setLimitBreached(true);
                    break;
                }
            }
        } else {
            return null;
        }
        if (result.getFilepathList().size() > LocalConstants.MAX_FILES_TO_BE_SEARCHED) {
            result.setLimitBreached(true);
            List<FileIndexModel> newList = result.getFilepathList().subList(0, LocalConstants.MAX_FILES_TO_BE_SEARCHED);
            result.setFilepathList(newList);
        }
        Log.debug("total " + fileList.size() + " files to be searched");
        return result;
    }

    /**
     *
     * @param model
     * @param mappedFileList
     * @param pathPrefix
     * @return
     * @throws ParseException 
     */
    private FilePathListResult getMatchingFileName(SearchModel model, List<FileIndexModel> mappedFileList, String pathPrefix) throws ParseException {
        Log.trace("searching matching files in directory " + pathPrefix);
        int i = 0;
        List<FileIndexModel> fileList = new ArrayList<FileIndexModel>();
        FilePathListResult result = new FilePathListResult();
        result.setFilepathList(fileList);
        for (String fileKey : model.getFileList()) {
            Pattern pattern = Pattern.compile(fileKey.toLowerCase());
            if (i >= LocalConstants.MAX_FILES_TO_BE_SEARCHED) {
                result.setLimitBreached(true);
                Log.trace("max file limit reached while searching " + pathPrefix);
                break;
            }
            for (FileIndexModel index : mappedFileList) {
                Matcher matcher = pattern.matcher(index.getFileName().toLowerCase());
                if (i >= LocalConstants.MAX_FILES_TO_BE_SEARCHED) {
                    result.setLimitBreached(true);
                    Log.trace("max file limit reached while searching " + pathPrefix);
                    break;
                }
                if (matcher.find() && LocalUtil.isFileinTimeInterval(index, model)) {
                    fileList.add(index);
                    i++;
                }
            }
        }
        Log.trace("total " + fileList.size() + " files selected for search in dir " + pathPrefix);
        return result;
    }

    /**
     *
     * @param fPath
     * @return
     */
    private static String[] getPathSeprator(String fPath) {
        if (fPath == null) {
            return null;
        }
        String s[] = new String[2];
        int idx = 0;
        idx = fPath.lastIndexOf(File.separator);
        if (idx <= 0) {
            s[0] = "~/";
            s[1] = fPath;
        } else {
            s[0] = fPath.substring(0, idx + 1);
            s[1] = fPath.substring(idx + 1);
        }
        return s;
    }
    
    /**
     * 
     * @param list
     * @param fileName
     * @return
     */
    private static boolean contains(List<FileIndexModel> list, String fileName) {
    	for(FileIndexModel index : list) {
    		if(index.getFileName().equals(fileName)) {
    			return true;
    		}
    	}
    	return false;
    }
}
