package com.asianaidt.ict.analyca.domain.dockerdomain.repository;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.influxdb.InfluxDBProperties;

@ConfigurationProperties("spring.docker-influxdb")
public class DockerInfluxDBProperties extends InfluxDBProperties {
}
