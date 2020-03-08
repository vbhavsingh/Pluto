/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.analyzer.commons.model;

import java.util.List;

/**
 *
 * @author Vaibhav Pratap Singh
 */
public class FileSearchResult implements Comparable<FileSearchResult> {

    String fileName;
    int trimmedCount = 0;
    int count = 0;
    List<String> textLineList;

    public FileSearchResult(String fileName, List<String> textLineList) {
        this.fileName = fileName;
        this.textLineList = textLineList;
        if (textLineList != null) {
            this.count = textLineList.size();
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<String> getTextLineList() {
        return textLineList;
    }

    public void setTextLineList(List<String> textLineList) {
        this.textLineList = textLineList;
        if (textLineList != null) {
            this.count = textLineList.size();
        }
    }

    public int getTrimmedCount() {
        return trimmedCount;
    }

    public void setTrimmedCount(int trimmedCount) {
        this.trimmedCount = trimmedCount;
    }

    public int getCount() {
        return count;
    }


    @Override
	public String toString() {
		return "FileSearchResult [fileName=" + fileName + ", trimmedCount=" + trimmedCount + ", count=" + count + "]";
	}

    @Override
    public int compareTo(FileSearchResult o) {
        return count < o.getCount() ? -1 : count > o.getCount() ? 1 : doSecodaryOrderSort(o);
    }

    public int doSecodaryOrderSort(FileSearchResult o) {
        return count < o.getCount() ? -1 : count > o.getCount() ? 1 : 0;
    }
    
    

}
