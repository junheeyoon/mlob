package com.asianaidt.ict.analyca.domain.containerdomain.repository;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.influxdb.InfluxDBProperties;

@ConfigurationProperties("spring.dockerlog-influxdb")
public class ContainerInfluxDBProperties extends InfluxDBProperties {
}
