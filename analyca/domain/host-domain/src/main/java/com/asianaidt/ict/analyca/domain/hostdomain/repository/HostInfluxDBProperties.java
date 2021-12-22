package com.asianaidt.ict.analyca.domain.hostdomain.repository;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.influxdb.InfluxDBProperties;

@ConfigurationProperties("spring.host-influxdb")
public class HostInfluxDBProperties extends InfluxDBProperties {
}
