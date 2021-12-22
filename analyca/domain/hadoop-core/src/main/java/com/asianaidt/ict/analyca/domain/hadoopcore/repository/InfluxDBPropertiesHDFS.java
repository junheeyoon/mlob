package com.asianaidt.ict.analyca.domain.hadoopcore.repository;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.influxdb.InfluxDBProperties;

@ConfigurationProperties("spring.hdfs-influxdb")
public class InfluxDBPropertiesHDFS extends InfluxDBProperties {
}
