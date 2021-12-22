package com.asianaidt.ict.analyca.domain.dockerdomain.repository;

import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DockerStatInfluxRepository {

    private final InfluxDBTemplate<Point> influxDBTemplate;

    public DockerStatInfluxRepository(@Qualifier("influxDBTemplateDocker") InfluxDBTemplate<Point> influxDBTemplate) {
        this.influxDBTemplate = influxDBTemplate;
    }

    public QueryResult queryAction(String queryString) {
        Query query = new Query(queryString);
        return influxDBTemplate.getConnection().setDatabase(influxDBTemplate.getDatabase()).query(query);
    }
}
