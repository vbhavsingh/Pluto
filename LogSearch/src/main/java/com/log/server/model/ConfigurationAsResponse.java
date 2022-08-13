/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.server.model;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * file name has top precedence, file path has second and filetype has least
 * precedence
 *
 * @author Vaibhav Pratap Singh
 */
public class ConfigurationAsResponse {

    private List<String> fileTypesToSearch;

    private List<String> filePathToSearch;

    private List<String> fileNameToSearch;

    private List<String> clientLabels;

    private int searchThreadCount;
    

    public List<String> getFileTypesToSearch() {
        return fileTypesToSearch;
    }

    public void setFileTypesToSearch(List<String> fileTypesToSearch) {
        this.fileTypesToSearch = fileTypesToSearch;
    }

    public List<String> getFilePathToSearch() {
        return filePathToSearch;
    }

    public void setFilePathToSearch(List<String> filePathToSearch) {
        this.filePathToSearch = filePathToSearch;
    }

    public List<String> getFileNameToSearch() {
        return fileNameToSearch;
    }

    public void setFileNameToSearch(List<String> fileNameToSearch) {
        this.fileNameToSearch = fileNameToSearch;
    }

    public List<String> getClientLabels() {
        return clientLabels;
    }

    public void setClientLabels(List<String> clientLabels) {
        this.clientLabels = clientLabels;
    }

    public int getSearchThreadCount() {
        return searchThreadCount;
    }

    public void setSearchThreadCount(int searchThreadCount) {
        this.searchThreadCount = searchThreadCount;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
    
}
