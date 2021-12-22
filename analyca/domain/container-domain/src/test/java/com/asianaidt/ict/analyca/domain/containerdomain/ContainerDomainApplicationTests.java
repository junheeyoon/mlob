package com.asianaidt.ict.analyca.domain.containerdomain;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
class ContainerDomainApplicationTests {

    @Test
    void contextLoads() throws InterruptedException {
//        String i = "de36f1ea3393";
        String i = "b630b4aec6f8";
//        String i = "aaa";

        String influxHost = "http://10.33.194.25:4006";
        InfluxDB influxDB = InfluxDBFactory.connect(influxHost, "admin", "admin123");
        influxDB.setDatabase("telegraf");
//        Query _queryString = new Query("SELECT * FROM docker_container_status where source = '" + i + "' order by time desc limit 1");
//        Query _queryString = new Query("SELECT * FROM docker_container_status where source = '" + i + "' order by time desc limit 1");
        Query _queryString = new Query("SELECT container_status, exitcode, container_name, container_image FROM docker_container_status where source = '" + i + "' order by time desc limit 1");
        for(int j = 0 ; j < 10; j++) {
            QueryResult queryResultMem = influxDB.query(_queryString);
            System.out.println(queryResultMem.getResults());
            if(queryResultMem.getResults().get(0).getSeries() == null){
                System.out.println("null");
                Thread.sleep(1000 *5);
            }
        }

//        System.out.println(queryResultMem);
//        System.out.println("####################################");
//        System.out.println(queryResultMem.getResults());
//        System.out.println("####################################");
    }
}
