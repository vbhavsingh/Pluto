package com.log.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.log.server.biz.SearchBiz;

public class SpringUtil {

	private final static Logger Log = LoggerFactory.getLogger(SpringUtil.class);

	/** The context. */
	private static ApplicationContext context;

	private static SpringUtil springUtil;

	private SpringUtil() {
	}

	public static SpringUtil getHelper() {
		if (springUtil == null) {
			springUtil = new SpringUtil();
		}
		return springUtil;
	}

	static {
		try {
			context = new ClassPathXmlApplicationContext(new String[] { "spring-config.xml" });
			Log.info("loaded all beans");
		} catch (Exception e) {
			Log.error("Error Loading files",e);
		}
	}

	public static SearchBiz search() {
		return (SearchBiz) context.getBean("SearchBiz");
	}

}
