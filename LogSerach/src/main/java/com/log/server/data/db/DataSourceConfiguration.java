package com.log.server.data.db;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.log.server.LocalConstants;
import com.log.server.LocalConstants.DATABASES;
import com.log.server.util.Utilities;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {
		"com.log.server.data.db" }, entityManagerFactoryRef = "dbEntityManager", transactionManagerRef = "dbTransactionManager")
public class DataSourceConfiguration {

//	@Autowired
//	private Environment env;

	@Bean
	@Primary
	LocalContainerEntityManagerFactoryBean dbEntityManager() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(hsdbDataSource());
		em.setPackagesToScan(new String[] { "com.log.server.data.db" });
		em.setPersistenceUnitName("dbEntityManager");
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);

		HashMap<String, Object> properties = new HashMap<>();

		String dialect = LocalConstants.DATABASES.getDialect();

		properties.put("hibernate.dialect", dialect);
		properties.put("hibernate.show-sql", false);
		properties.put("hbm2ddl.auto", "create");
		// properties.put("hibernate.initialize-database", "always");

		em.setJpaPropertyMap(properties);

		return em;
	}

	@Primary
	@Bean(name = "plutoDataSource")
	DataSource hsdbDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		
		String driverClass = LocalConstants.DATABASES.getDriverName();

		String url = driverClass.equals(LocalConstants.DATABASES.hsql.driverClass) ? "jdbc:hsqldb:file:"
				+ Utilities.getEmbededDbFilePath().toString() + ";shutdown=true;hsqldb.log_size=10"
				: Utilities.getProperty(LocalConstants.PROPERTIES.DB_URL);

		String userName = Utilities.getProperty(LocalConstants.PROPERTIES.DB_USERNAME);
		userName = userName == null ? "" : userName;

		String password = Utilities.getProperty(LocalConstants.PROPERTIES.DB_PASSWORD);
		password = password == null ? "" : password;

		dataSource.setDriverClassName(driverClass);
		dataSource.setUrl(url);
		dataSource.setUsername(userName);
		dataSource.setPassword(password);

		createDataBaseStructure(dataSource, driverClass);

		return dataSource;
	}

	private void createDataBaseStructure(DriverManagerDataSource dataSource, String driverClass) {
		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
		databasePopulator.setContinueOnError(false);
		if (driverClass.equals(LocalConstants.DATABASES.hsql.name())) {
			databasePopulator.addScript(new ClassPathResource("hsqldb.sql"));
		} else {
			databasePopulator.addScript(new ClassPathResource("schema.sql"));
		}

		DatabasePopulatorUtils.execute(databasePopulator, dataSource);
	}

	@Primary
	@Bean
	PlatformTransactionManager dbTransactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(dbEntityManager().getObject());
		return transactionManager;
	}
}
