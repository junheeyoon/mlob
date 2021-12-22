package com.asianaidt.ict.analyca.domain.sparkdomain;


import com.asianaidt.ict.analyca.domain.sparkdomain.repository.HiveConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@SpringBootApplication
public class SparkDomainApplication implements CommandLineRunner {

    @Autowired
    private HiveConfiguration hiveConfiguration;

    public static void main(String[] args) {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setUrl("jdbc:hive2://10.33.194.25:9200/default");
//        dataSource.setDriverClassName("org.apache.hive.jdbc.HiveDriver");
//        dataSource.setUsername("hive");
//        dataSource.setPassword("hive");
//
//        System.out.println("TEST");
//
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//        System.out.println("Hello World");
//        String sql ="show databases";
//        System.out.println(jdbcTemplate.queryForList(sql));
//        sql ="use default";
//        jdbcTemplate.execute(sql);
//        sql = "describe mytest";
//        System.out.println(jdbcTemplate.queryForList(sql));
//        SpringApplication.run(SparkDomainApplication.class, args);
//        SpringApplication app = new SpringApplication(SparkDomainApplication.class);
//        app.run();
    }

    public void run(String... args) throws Exception {
//        System.out.println("Hellow world");
//        System.out.println("Hive" +hiveConfiguration.getHivePassword());
    }

}
