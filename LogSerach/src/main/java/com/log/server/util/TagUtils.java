/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.server.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.web.util.HtmlUtils;

import com.log.server.biz.CommonServices;
import com.log.server.model.Group;
import com.log.server.model.LabelCounter;
import com.log.server.model.ViewResultModel;

/**
 *
 * @author Vaibhav Pratap Singh
 */
public class TagUtils extends TagSupport {

    /**
     *
     * @param i
     * @param text
     * @return
     */
    public static String spCheck(int i, String text) {
        if (i == 0) {
            return "no " + text + "s";
        }
        if (i == 1) {
            return "one " + text;
        }
        return i + " " + text + "s";
    }

    /**
     *
     * @param model
     * @return
     */
    public static String resultMessage(ViewResultModel model) {
        StringBuilder message = new StringBuilder();
        if (model.getServersSearched() == 1) {
            message.append("one node searched,");
        } else {
            message.append(model.getServersSearched());
            message.append(" nodes were searched,");
        }
        if (model.getLinesFetched() == 1) {
            message.append(" only one line found");
            if (model.getFilesWithMatchCount() == 1) {
                message.append(" in one file");
            } else {
                message.append(" in ");
                message.append(model.getFilesWithMatchCount());
                message.append(" files.");
            }
            if (model.getDatedTextLines() == 0) {
                message.append(" and that is not with date");
            }
        } else {
            message.append(model.getLinesFetched());
            message.append(" lines are in result");
            if (model.getFilesWithMatchCount() == 1) {
                message.append(" from one file");
            } else {
                message.append(" from ");
                message.append(model.getFilesWithMatchCount());
                message.append(" files.");
            }
            if (model.getUnDatedTextLines() > 0) {
                message.append(". ");
                if (model.getUnDatedTextLines() == 1) {
                    message.append("One line is wothout date in result.");
                } else {
                    message.append(model.getUnDatedTextLines());
                    message.append(" lines are without date.");
                }
            }
        }
        return message.toString();
    }
    
    /**
    *
    * @return
    */
   public static String previousSearchCriterias(String fieldName) {
       List<String> suggestions = CommonServices.getPreviousSearchCriterias(fieldName);
       if (suggestions == null || suggestions.size() == 0) {
           return null;
       }
       String list = "";
       int length = suggestions.size();
       int i = 0;
       for (String s : suggestions) {
           list=list+s;
           ++i;
           if (length - i > 0) {
               list += ",";
           }
       }
       return list;
   }
    

    /**
     *
     * @return
     */
    public static String labels() {
        List<LabelCounter> labelList = CommonServices.getLabelListWithNodeCounter();
        if (labelList == null || labelList.size() == 0) {
            return null;
        }
        return labelList.stream().map(e -> e.getLabelName()+" : "+getNodeLiteal(e.getNodeCount())).collect(Collectors.joining(","));
        
    /*    int length = labelList.size();
        int i = 0;
        for (String s : labelList) {
            list=list+s;
            ++i;
            if (length - i > 0) {
                list += ",";
            }
        }*/
    }
    /**
    *
    * @return
    */
   public static String labelsArray() {
       List<LabelCounter> labelList = CommonServices.getLabelListWithNodeCounter();
       if (labelList == null || labelList.size() == 0) {
           return null;
       }
       
       return labelList.stream().map(e -> e.getLabelName()).collect(Collectors.joining(","));
       /*String list = null;
       int length = labelList.size();
       int i = 0;
       for (String s : labelList) {
           if (list == null) {
               list = "'" + s + "'";
           } else {
               list = list + "'" + s + "'";
           }
           ++i;
           if (length - i > 0) {
               list += ",";
           }
       }*/
   }
    /**
     * 
     * @param text
     * @param model
     * @return
     */
    public static String highlightSearch(String text, List<String> patterns) {
        if (text != null) {
            text = HtmlUtils.htmlEscape(text);
        }
        if(patterns == null){
        	return text;
        }
        for (String key:patterns){
        	if (text != null && key != null && !"".equals(text)) {
                text = highlight(key, text);
            }
        }
 /*       if (model.getSearchKeyword() != null) {
            for (String key : model.getSearchKeyword()) {
                if (text != null) {
                    text = highlight(key, text);
                }
            }
        }
        if (model.getMadatorySearchKeyword() != null) {
            for (String key : model.getMadatorySearchKeyword()) {
                if (text != null && key != null && !"".equals(text)) {
                    text = highlight(key, text);
                }
            }
        }*/
        
        return text;
    }
    /**
     * 
     * @param node
     * @return
     */
    public static String shortNodeName(String node) {
        if (node == null) {
            return node;
        }
        if (node.contains(".")) {
            int pos = node.indexOf(".");
            return (node.substring(0, pos));
        }
        return node;
    }
    /**
     * 
     * @param fPath
     * @return
     */
    public static String shortFilePathName(String fPath) {
        if (fPath == null) {
            return null;
        }
        if (fPath.contains("/")) {
            int pos = fPath.lastIndexOf("/");
            return fPath.substring(pos + 1);
        }
        return fPath;

    }
    /**
     * 
     * @param group
     * @param groupNameList
     * @return
     */
    public static boolean groupMatched(String group, List<Group> groupNameList){
    	for(Group gp:groupNameList){
    		if(group.equals(gp.getName())){
    			return true;
    		}
    	}
    	return false;
    }
    /**
     * 
     * @param cssIfy
     * @param plain
     * @return
     */
    private static String highlight(String cssIfy, String plain) {
        Pattern p = Pattern.compile(cssIfy, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(plain);
        StringBuffer buff = new StringBuffer();
        while (m.find()) {
            String replacement = m.group().concat("</span>");
            replacement = "<span class=\"_highlight\">" + replacement;
            m.appendReplacement(buff, Matcher.quoteReplacement(replacement));
        }
        m.appendTail(buff);
        return buff.toString();
    }
    
    
    private static String getNodeLiteal(int count) {
       return count==1?"1 Node":count+" Nodes";
    }
    

}
