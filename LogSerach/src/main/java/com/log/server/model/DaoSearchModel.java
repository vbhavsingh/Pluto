/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.server.model;

import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 *
 * @author Vaibhav Pratap Singh
 */
public class DaoSearchModel {

    List<String> logPathPatterns;
    List<String> logFileNamePatterns;
    List<String> searchOnLabels;

    public List<String> getLogPathPatterns() {
        return logPathPatterns;
    }

    public void setLogPathPatterns(List<String> logPathPatterns) {
        this.logPathPatterns = logPathPatterns;
    }

    public List<String> getLogFileNamePatterns() {
        return logFileNamePatterns;
    }

    public void setLogFileNamePatterns(List<String> logFileNamePatterns) {
        this.logFileNamePatterns = logFileNamePatterns;
    }

    public List<String> getSearchOnLabels() {
        return searchOnLabels;
    }

    public void setSearchOnLabels(List<String> searchOnLabels) {
        this.searchOnLabels = searchOnLabels;
    }
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
