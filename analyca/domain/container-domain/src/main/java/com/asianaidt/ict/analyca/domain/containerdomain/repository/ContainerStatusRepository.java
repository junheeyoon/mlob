package com.asianaidt.ict.analyca.domain.containerdomain.repository;

import com.asianaidt.ict.analyca.domain.containerdomain.model.ContainerLog;
import com.asianaidt.ict.analyca.domain.containerdomain.model.ContainerStatus;
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
public class ContainerStatusRepository {

    private final InfluxDBTemplate<Point> influxDBTemplate;

    public ContainerStatusRepository(@Qualifier("influxDBTemplateContainer") InfluxDBTemplate<Point> influxDBTemplate) {
        this.influxDBTemplate = influxDBTemplate;
    }
    private ConcurrentHashMap<String, ConcurrentHashMap<String, ContainerStatus>> ContainerStatusMapByHost = new ConcurrentHashMap<>();

    public void update(String host, List<ContainerStatus> containers) {
        if (!ContainerStatusMapByHost.containsKey(host)) {
            ContainerStatusMapByHost.put(host, new ConcurrentHashMap<>());
        }
        ConcurrentHashMap<String, ContainerStatus> ContainerStatusMap = ContainerStatusMapByHost.get(host);
        ContainerStatusMap.clear();
        containers.forEach(container -> ContainerStatusMap.put(container.getContainerName(), container));
    }

    public List<ContainerStatus> findAll() {
        List<ContainerStatus> containers = new ArrayList<>();
        ContainerStatusMapByHost.keySet().forEach(host -> containers.addAll(findByHost(host)));
        return containers;
    }

    public List<ContainerStatus> findByHost(String host) {
        return new ArrayList<>(ContainerStatusMapByHost.getOrDefault(host, new ConcurrentHashMap<>()).values());
    }
    public QueryResult queryAction(String queryString) {
        Query query = new Query(queryString);
        return influxDBTemplate.getConnection().setDatabase(influxDBTemplate.getDatabase()).query(query);
    }
}
