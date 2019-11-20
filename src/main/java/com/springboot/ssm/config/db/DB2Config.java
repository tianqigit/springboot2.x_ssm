package com.springboot.ssm.config.db;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

@Configuration
@MapperScan(
        basePackages = {"com.springboot.ssm.mapper.server"}, // 1. 扫描mapper层所在的包
        sqlSessionTemplateRef = "db2SqlSessionTemplate")
public class DB2Config {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.server")
    public DataSourceProperties db2DataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.server.configuration")
    public DataSource db2DataSource() {
        return db2DataSourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class) // 可以显示指定连接池，也可以不显示指定；即此行代码可以注释掉
                .build();
    }

    @Bean
    public SqlSessionFactory db2SqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(db2DataSource());
        factoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver()
                        .getResources("classpath:mapper/server/*.xml")); // xml 所在路径
        factoryBean.setTypeAliasesPackage("com.springboot.ssm.domain"); // 设置扫描别名包路径
        return factoryBean.getObject();
    }

    @Bean
    public DataSourceTransactionManager db2TransactionManager() {
        return new DataSourceTransactionManager(db2DataSource());
    }

    @Bean
    public SqlSessionTemplate db2SqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(db2SqlSessionFactory());
    }

}