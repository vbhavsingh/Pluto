/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.analyzer.commons;

import com.sun.jersey.api.representation.Form;

import java.lang.reflect.Field;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.log4j.Logger;

/**
 *
 * @author Vaibhav Pratap Singh
 */
public class Util {

    private static final Logger Log = Logger.getLogger(Util.class);
    
    public static String getLinesFromArchiveFilePosixCmd(String osName) {
    	String archiveReadCmd = "";
		if ("sunos".equalsIgnoreCase(osName)) {
			archiveReadCmd = "gzgrep -P '"+Constants.UNIVERSAL_DATE_FORMAT+"' "+Constants.FILE_NAME_PLACEHOLDER;
		} else if ("linux".equalsIgnoreCase(osName)) {
			archiveReadCmd = "zgrep -aP '"+Constants.UNIVERSAL_DATE_FORMAT+"' "+Constants.FILE_NAME_PLACEHOLDER;
		}else {
			archiveReadCmd = "grep -aP '"+Constants.UNIVERSAL_DATE_FORMAT+"' "+Constants.FILE_NAME_PLACEHOLDER;
		}
		archiveReadCmd = archiveReadCmd +" | head -n 500";
		return archiveReadCmd;
    }

    /**
     *
     * @param obj
     * @return
     */
    public static MultivaluedMap<String, String> toQueryParams(Object obj) {
        if (obj == null) {
            return null;
        }
        MultivaluedMap<String, String> queryParams = new Form();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            boolean accessible = field.isAccessible();
            try {
                field.setAccessible(true);
                Object value = field.get(obj);
                if (value != null) {
                    final String name = field.getName();
                    queryParams.add(name, value.toString());
                }
            } catch (IllegalAccessException e) {
                Log.error("Error accessing a field", e);
            } finally {
                field.setAccessible(accessible);
            }
        }
        return queryParams;
    }

	/**
	 * 
	 * @param level
	 * @return
	 */
	public static boolean isValidLogLevel(String level) {
		if (level == null) {
			return false;
		}
		if ("trace".equals(level.toLowerCase())) {
			return true;
		}
		if ("debug".equals(level.toLowerCase())) {
			return true;
		}
		if ("info".equals(level.toLowerCase())) {
			return true;
		}
		if ("error".equals(level.toLowerCase())) {
			return true;
		}
		if ("fatal".equals(level.toLowerCase())) {
			return true;
		}
		if ("all".equals(level.toLowerCase())) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param console
	 * @return
	 */
	public static boolean doConsoleLogging(String console) {
		if (console == null) {
			return false;
		}
		if ("true".equals(console.toLowerCase())) {
			return true;
		}
		return false;
	}
}
