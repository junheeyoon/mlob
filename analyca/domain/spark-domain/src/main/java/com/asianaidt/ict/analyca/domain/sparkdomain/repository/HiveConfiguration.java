package com.asianaidt.ict.analyca.domain.sparkdomain.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;


import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "spring.spark")
public class HiveConfiguration {

    private String home;
    private String hiveUrl;
    private String hiveUser;
    private String hivePassword;
    private String masterUri;
    private String driverClassName;
    private String appName;
    private String database;

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getHiveUrl() {
        return hiveUrl;
    }

    public void setHiveUrl(String hiveUrl) {
        this.hiveUrl = hiveUrl;
    }

    public String getHiveUser() {
        return hiveUser;
    }

    public void setHiveUser(String hiveUser) {
        this.hiveUser = hiveUser;
    }

    public String getHivePassword() {
        return hivePassword;
    }

    public void setHivePassword(String hivePassword) {
        this.hivePassword = hivePassword;
    }

    public String getMasterUri() {
        return masterUri;
    }

    public void setMasterUri(String masterUri) {
        this.masterUri = masterUri;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }
}
