/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.analyzer.commons;

import java.util.List;

/**
 *
 * @author Vaibhav Singh
 */
public class FilePathListResult {

    List<FileIndexModel> filepathList;

    boolean limitBreached = false;
    
    /* number of files that can be searched provided given input by user*/
    int filesMatchedCount = 0;

    public List<FileIndexModel> getFilepathList() {
        return filepathList;
    }

    public void setFilepathList(List<FileIndexModel> filepathList) {
        this.filepathList = filepathList;
    }

    public boolean isLimitBreached() {
        return limitBreached;
    }

    public void setLimitBreached(boolean limitBreached) {
        this.limitBreached = limitBreached;
    }

	public int getFilesMatchedCount() {
		return filesMatchedCount;
	}

	public void setFilesMatchedCount(int filesMatchedCount) {
		this.filesMatchedCount = filesMatchedCount;
	}

	@Override
	public String toString() {
		return "FilePathListResult [filepathList=" + filepathList + ", limitBreached=" + limitBreached + ", filesMatchedCount=" + filesMatchedCount + "]";
	}

}
