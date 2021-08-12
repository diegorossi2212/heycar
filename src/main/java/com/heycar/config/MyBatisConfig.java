package com.heycar.config;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.heycar.mybatis.CarMapper;
import com.heycar.mybatis.DealerMapper;

@Configuration
@EnableTransactionManagement
public class MyBatisConfig {

	private static final String PATH_MYBATIS_CONFIG_XML = "com/heycar/mybatis/mybatis-config.xml";
	
	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
	    return new PersistenceExceptionTranslationPostProcessor();
	}

	@Bean("sqlSessionFactory")
	public SqlSessionFactoryBean sqlSessionFactory(@Autowired DataSource dataSource) {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setConfigLocation(new ClassPathResource(PATH_MYBATIS_CONFIG_XML));
		return sqlSessionFactoryBean;
	}

	@Bean
	public MapperFactoryBean<CarMapper> carMapper(
			@Qualifier("sqlSessionFactory") SqlSessionFactoryBean sqlSessionFactoryBean) throws Exception {
		MapperFactoryBean<CarMapper> factoryBean = new MapperFactoryBean<>(CarMapper.class);
		factoryBean.setSqlSessionFactory(sqlSessionFactoryBean.getObject());
		return factoryBean;
	}

	@Bean
	public MapperFactoryBean<DealerMapper> dealerMapper(
			@Qualifier("sqlSessionFactory") SqlSessionFactoryBean sqlSessionFactoryBean) throws Exception {
		MapperFactoryBean<DealerMapper> factoryBean = new MapperFactoryBean<>(DealerMapper.class);
		factoryBean.setSqlSessionFactory(sqlSessionFactoryBean.getObject());
		return factoryBean;
	}

}
