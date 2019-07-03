package org.suresec.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * 相当于之前的ApplicationContext-Dao.xml
 * 配置数据源,使用阿里Druid数据源
 * @author xiyan
 *
 */

@Configuration
public class DataSourceConfig {
	/**
     * 数据源驱动类型
     */
	@Value("${cas.authn.jdbc.query[0].driverClass}")
	private String driver;

    /**
     * 连接地址
     */
	@Value("${cas.authn.jdbc.query[0].url}")
	private String url;

    /**
     * 用户名
     */
	@Value("${cas.authn.jdbc.query[0].user}")
	private String username;

    /**
     * 密码
     */
	@Value("${cas.authn.jdbc.query[0].password}")
	private String password;
	
	@Bean(name="jdbcDataSource")
	public DataSource primaryDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(driver);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
	}

	@Bean
	public JdbcTemplate jdbcTemplate(@Qualifier("jdbcDataSource") DataSource dataSource){
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(dataSource);
		return jdbcTemplate;
	}


}
