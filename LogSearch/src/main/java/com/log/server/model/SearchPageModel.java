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
public class SearchPageModel {

    List<String> labelList;

    public List<String> getLabelList() {
        return labelList;
    }

    public void setLabelList(List<String> labelList) {
        this.labelList = labelList;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
