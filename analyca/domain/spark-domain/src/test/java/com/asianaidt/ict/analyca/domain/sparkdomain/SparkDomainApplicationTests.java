package com.asianaidt.ict.analyca.domain.sparkdomain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;



class SparkDomainApplicationTests {


    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Test
    void test() {
        System.out.println("Hello World");
        String sql ="show tables";
        System.out.println(jdbcTemplate.queryForList(sql));

    }

}
