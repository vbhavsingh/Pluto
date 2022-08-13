package com.security.common;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import org.springframework.transaction.PlatformTransactionManager;

import com.log.server.util.NotHsqlBackendCondition;

@Configuration
@EnableJdbcHttpSession
@Conditional(NotHsqlBackendCondition.class)
public class SessionStore extends AbstractHttpSessionApplicationInitializer {
	
	@Bean
	@Resource(name = "plutoDataSource")
    public PlatformTransactionManager transactionManager(DataSource plutoDataSource) {
        return new DataSourceTransactionManager(plutoDataSource);
    }
	
}
