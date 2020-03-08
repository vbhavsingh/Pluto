/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.server;

import com.log.server.biz.SearchBiz;
import com.log.server.data.db.Dao;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Vaibhav Pratap Singh
 */
public class SpringHelper {

    private static final Logger Log = Logger.getLogger(SpringHelper.class);

    private static ApplicationContext context;

    private static SpringHelper springHelper;

    private SpringHelper() {
    }

    public static SpringHelper getHelper() {
        if (springHelper == null) {
            springHelper = new SpringHelper();
        }
        return springHelper;
    }

    static {
        try {
            context = new ClassPathXmlApplicationContext(new String[]{
                "database-spring.xml",
                "future-jobs.xml",
                "spring-config.xml"
            });
            Log.debug("loaded all spring configurations successfully.");
        } catch (Exception e) {
            Log.error("Error Loading files");
            Log.error(e);
        }
    }

    public static Object getBean(String bean) {
        Log.trace("Creating bean " + bean);
        return (Object) context.getBean(bean);
    }

    public static Dao getDao() {
        Log.trace("Creating data access bean ");
        return (Dao) context.getBean("Dao");
    }
    
	public static SearchBiz searchBean() {
		return (SearchBiz) context.getBean("SearchBiz");
	}

}
