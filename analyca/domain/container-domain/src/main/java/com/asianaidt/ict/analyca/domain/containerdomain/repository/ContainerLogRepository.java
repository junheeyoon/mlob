package com.asianaidt.ict.analyca.domain.containerdomain.repository;

import com.asianaidt.ict.analyca.domain.containerdomain.model.ContainerLog;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ContainerLogRepository {

    private final InfluxDBTemplate<Point> influxDBTemplate;

    public ContainerLogRepository(@Qualifier("influxDBTemplateContainer") InfluxDBTemplate<Point> influxDBTemplate) {
        this.influxDBTemplate = influxDBTemplate;
    }
    private ConcurrentHashMap<String, ConcurrentHashMap<String, ContainerLog>> containerLogMapByHost = new ConcurrentHashMap<>();

    public void update(String host, List<ContainerLog> containers) {
        if (!containerLogMapByHost.containsKey(host)) {
            containerLogMapByHost.put(host, new ConcurrentHashMap<>());
        }
        ConcurrentHashMap<String, ContainerLog> containerLogMap = containerLogMapByHost.get(host);
        containerLogMap.clear();
        containers.forEach(container -> containerLogMap.put(container.getContainerName(), container));
    }

    public List<ContainerLog> findAll() {
        List<ContainerLog> containers = new ArrayList<>();
        containerLogMapByHost.keySet().forEach(host -> containers.addAll(findByHost(host)));
        return containers;
    }

    public List<ContainerLog> findByHost(String host) {
        return new ArrayList<>(containerLogMapByHost.getOrDefault(host, new ConcurrentHashMap<>()).values());
    }
    public QueryResult queryAction(String queryString) {
        Query query = new Query(queryString);
        return influxDBTemplate.getConnection().setDatabase(influxDBTemplate.getDatabase()).query(query);
    }
}
