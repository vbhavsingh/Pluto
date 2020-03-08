/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.analyzer.commons;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.analyzer.LocalUtil;

/**
 *
 * @author Vaibhav Pratap Singh
 * 
 */
public class CustomConcurrentMap extends ConcurrentHashMap<String, List<FileIndexModel>> {
    
	private static final long serialVersionUID = 1L;
	
	private static Logger Log = Logger.getLogger(CustomConcurrentMap.class);

	public Map<String, List<FileIndexModel>> getMatchingPathMap(String path) {
        Pattern pattern = Pattern.compile(path.toLowerCase());
        Map<String, List<FileIndexModel>> resultMap = new HashMap<String, List<FileIndexModel>>();
        for (Map.Entry<String, List<FileIndexModel>> resultEntry : super.entrySet()) {
            String entry = resultEntry.getKey().toLowerCase();
            Matcher matcher = pattern.matcher(entry.toLowerCase());
            if (matcher.find()) {
                resultMap.put(entry, super.get(entry));
            }
        }
        if (resultMap.size() == 0) {
            return null;
        }
        return resultMap;
    }
	
	
    public int getFileCount() {
        int i = 0;
        for (Map.Entry<String, List<FileIndexModel>> resultEntry : super.entrySet()) {
            i += resultEntry.getValue().size();
        }
        return i;
    }
    
    public void setNewmap(CustomConcurrentMap newMap) {
    	Log.debug("starting time indexing for log files");
    	long starttime = System.currentTimeMillis();
        for(Map.Entry<String, List<FileIndexModel>> indices:newMap.entrySet()) {
        	if(super.get(indices.getKey()) != null) {
        		List<FileIndexModel> oldIndexes = super.get(indices.getKey());
        		List<FileIndexModel> newIndexes = indices.getValue();
        		for(FileIndexModel oIndex : oldIndexes) {
        			findAndUpdate(oIndex, newIndexes);
        		}
        	}
        }
        long timeTaken= System.currentTimeMillis() - starttime ;
        Log.debug("Deep indexing completed, time taken : "+timeTaken+"ms");
        super.clear();
        super.putAll(newMap);
    }
    /**
     * 
     * @param index
     * @param indicies
     * @return
     */
    private boolean findAndUpdate(FileIndexModel oIndex,List<FileIndexModel> indicies) {
    	for(FileIndexModel nIndex:indicies) {
    		if(nIndex.getFileName().equals(oIndex.getFileName())){
    			nIndex.setDateFormat(oIndex.getDateFormat());
    			nIndex = LocalUtil.refreshFileIndex(nIndex);
    			return true;
    		}
    	}
    	return false;
    }  

}
