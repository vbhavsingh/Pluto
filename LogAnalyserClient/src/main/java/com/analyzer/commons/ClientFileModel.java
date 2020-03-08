/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.analyzer.commons;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 *
 * @author vs1953
 */
public class ClientFileModel {

    private List<String> textLineList;
    private int totalLines;
    private String fileName;
    private boolean maxdataLimitBreached = false;
    private boolean maxLineLimitBreached = false;

    public ClientFileModel(List<String> textLineList, int totalLines) {
        this.textLineList = textLineList;
        this.totalLines = totalLines;
    }

    public List<String> getTextLineList() {
        return textLineList;
    }

    public void setTextLineList(List<String> textLineList) {
        this.textLineList = textLineList;
    }

    public int getTotalLines() {
        return totalLines;
    }

    public void setTotalLines(int totalLines) {
        this.totalLines = totalLines;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isMaxdataLimitBreached() {
        return maxdataLimitBreached;
    }

    public void setMaxdataLimitBreached(boolean maxdataLimitBreached) {
        this.maxdataLimitBreached = maxdataLimitBreached;
    }

    public boolean isMaxLineLimitBreached() {
        return maxLineLimitBreached;
    }

    public void setMaxLineLimitBreached(boolean maxLineLimitBreached) {
        this.maxLineLimitBreached = maxLineLimitBreached;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
