package com.log.server.data.db;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		basePackages = {"com.log.server.data.db" }, 
		entityManagerFactoryRef = "dbEntityManager", 
		transactionManagerRef = "dbTransactionManager"
		)
public class DataSourceConfiguration {

//	@Autowired
//	private Environment env;
	

    @Bean
    @Primary
    LocalContainerEntityManagerFactoryBean dbEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(hsdbDataSource());
        em.setPackagesToScan(new String[]{"com.log.server.data.db"});
        em.setPersistenceUnitName("dbEntityManager");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        HashMap<String, Object> properties = new HashMap<>();

        properties.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
        properties.put("hibernate.show-sql", true);
        properties.put("hbm2ddl.auto", "create");
 //       properties.put("hibernate.initialize-database", "always");

        em.setJpaPropertyMap(properties);
      
        return em;
    }

    @Primary
    @Bean
    DataSource hsdbDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//		dataSource.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));
//		dataSource.setUrl(env.getProperty("spring.datasource.url"));
//		dataSource.setUsername(env.getProperty("spring.datasource.username"));
//		dataSource.setPassword(env.getProperty("spring.datasource.password"));
        dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
        dataSource.setUrl("jdbc:hsqldb:file:~/.pluto/db/configuration;shutdown=true;hsqldb.log_size=10");
        dataSource.setUsername("");
        dataSource.setPassword("");
        return dataSource;
    }

    @Primary
    @Bean
    PlatformTransactionManager dbTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(dbEntityManager().getObject());
        return transactionManager;
    }
}
