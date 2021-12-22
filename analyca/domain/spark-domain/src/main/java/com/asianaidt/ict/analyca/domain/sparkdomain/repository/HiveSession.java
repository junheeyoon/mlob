package com.asianaidt.ict.analyca.domain.sparkdomain.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class HiveSession {

    private final HiveConfiguration hiveConfiguration;

    public HiveSession(HiveConfiguration hiveConfiguration) {
        this.hiveConfiguration = hiveConfiguration;
    }

    @Bean(name = "hiveJdbcDataSource")
    @Qualifier("hiveJdbcDataSource")
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setUrl(hiveConfiguration.getHiveUrl());
//        dataSource.setDriverClassName("org.apache.hive.jdbc.HiveDriver");
//        dataSource.setUsername(hiveConfiguration.getHiveUser());
//        dataSource.setPassword(hiveConfiguration.getHivePassword());
        dataSource.setUrl("jdbc:hive2://10.33.194.25:9200/default");
        dataSource.setDriverClassName("org.apache.hive.jdbc.HiveDriver");
        dataSource.setUsername("hive");
        dataSource.setPassword("hive");
        return dataSource;
    }

    @Bean(name = "hiveJdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("hiveJdbcDataSource") DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }
}
