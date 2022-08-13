package com.log.server.data.db;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
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
import com.log.server.util.Utilities;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {
		"com.log.server.data.db" }, entityManagerFactoryRef = "dbEntityManager", transactionManagerRef = "dbTransactionManager")
public class DataSourceConfiguration{


	@Bean
	@Primary
	LocalContainerEntityManagerFactoryBean dbEntityManager() throws IOException {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(plutoDataSource());
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
	DataSource plutoDataSource() throws IOException {
		HikariDataSource dataSource = new HikariDataSource();
		
		String driverClass = LocalConstants.DATABASES.getDriverName();

		String url = driverClass.equals(LocalConstants.DATABASES.hsql.driverClass) ? "jdbc:hsqldb:file:"
				+ Utilities.getEmbededDbFilePath().toString() + ";shutdown=true;hsqldb.log_size=10"
				: Utilities.getProperty(LocalConstants.PROPERTIES.DB_URL);

		String userName = Utilities.getProperty(LocalConstants.PROPERTIES.DB_USERNAME);
		userName = userName == null ? "" : userName;

		String password = Utilities.getProperty(LocalConstants.PROPERTIES.DB_PASSWORD);
		password = password == null ? "" : password;

		dataSource.setDriverClassName(driverClass);
		dataSource.setJdbcUrl(url);
		dataSource.setUsername(userName);
		dataSource.setPassword(password);

		createDataBaseStructure(dataSource, driverClass);

		return dataSource;
	}

	/**
	 * in case of HSQLDB sessions are not saved in DB
	 * @param dataSource
	 * @param driverClass
	 * @throws IOException
	 */
	private void createDataBaseStructure(DataSource dataSource, String driverClass) throws IOException {
		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
		databasePopulator.setContinueOnError(true);
		
		if (driverClass.equals(LocalConstants.DATABASES.hsql.driverClass)) {
			databasePopulator.addScripts(new ClassPathResource("hsqldb.sql"), getModifiiedSessionTableSql());
		} else {
			databasePopulator.addScripts(new ClassPathResource("schema.sql"), getModifiiedSessionTableSql());
		}

		DatabasePopulatorUtils.execute(databasePopulator, dataSource);
	}
	
	private Resource getModifiiedSessionTableSql() throws IOException {
		Resource sessionTablesSqlFile  = new ClassPathResource(LocalConstants.DATABASES.getSessionSchema());
		BufferedInputStream bis = new BufferedInputStream(sessionTablesSqlFile.getInputStream());
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		for (int result = bis.read(); result != -1; result = bis.read()) {
		    buf.write((byte) result);
		}
		String data = buf.toString();
		if(LocalConstants.DATABASES.isHsqlDB()) {
			data = data.replace("CREATE TABLE", "CREATE TABLE IF NOT EXISTS")
			.replace("CREATE UNIQUE INDEX", "CREATE UNIQUE INDEX IF NOT EXISTS")
			.replace("CREATE INDEX", "CREATE INDEX IF NOT EXISTS");
		}else {
			data = data.replace("CREATE TABLE", "CREATE TABLE IF NOT EXISTS");
		}
		
		//System.out.println(data);
		
		return new ByteArrayResource(data.getBytes());
	}

	@Primary
	@Bean
	PlatformTransactionManager dbTransactionManager() throws IOException {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(dbEntityManager().getObject());
		return transactionManager;
	}
}
