package com.asianaidt.ict.analyca.domain.containerdomain.repository;

import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.influxdb.DefaultInfluxDBTemplate;
import org.springframework.data.influxdb.InfluxDBConnectionFactory;
import org.springframework.data.influxdb.InfluxDBProperties;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.data.influxdb.converter.PointConverter;

@Configuration
@EnableConfigurationProperties(ContainerInfluxDBProperties.class)
public class ContainerInfluxDBConfiguration {
    @Bean(name = "connectionFactoryContainer")
    public InfluxDBConnectionFactory connectionFactory(final ContainerInfluxDBProperties properties) {
        return new InfluxDBConnectionFactory(properties);
    }

    @Bean(name = "influxDBTemplateContainer")
    public InfluxDBTemplate<Point> influxDBTemplate(@Qualifier("connectionFactoryContainer") final InfluxDBConnectionFactory connectionFactory) {
        /*
         * You can use your own 'PointCollectionConverter' implementation, e.g. in case
         * you want to use your own custom measurement object.
         */
        return new InfluxDBTemplate<>(connectionFactory, new PointConverter());
    }

    @Bean(name = "defaultTemplateContainer")
    public DefaultInfluxDBTemplate defaultTemplate(@Qualifier("connectionFactoryContainer") final InfluxDBConnectionFactory connectionFactory) {
        /*
         * If you are just dealing with Point objects from 'influxdb-java' you could
         * also use an instance of class DefaultInfluxDBTemplate.
         */
        return new DefaultInfluxDBTemplate(connectionFactory);
    }

}