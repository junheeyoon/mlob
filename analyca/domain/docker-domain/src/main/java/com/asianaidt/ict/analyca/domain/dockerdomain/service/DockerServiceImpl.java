package com.asianaidt.ict.analyca.domain.dockerdomain.service;

import com.asianaidt.ict.analyca.domain.dockerdomain.model.ContainerPS;
import com.asianaidt.ict.analyca.domain.dockerdomain.model.DockerMetrics;
import com.asianaidt.ict.analyca.domain.dockerdomain.repository.ContainerPSRepository;
import com.asianaidt.ict.analyca.domain.dockerdomain.repository.DockerStatInfluxRepository;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DockerServiceImpl implements DockerService {
    private final InfluxDBTemplate<Point> influxDBTemplate;
    private final ContainerPSRepository containerPSRepository;
    private final DockerStatInfluxRepository dockerStatInfluxRepository;

    public DockerServiceImpl(
            @Qualifier("influxDBTemplateDocker") InfluxDBTemplate<Point> influxDBTemplate,
            ContainerPSRepository containerPSRepository, DockerStatInfluxRepository dockerStatInfluxRepository) {
        this.influxDBTemplate = influxDBTemplate;
        this.containerPSRepository = containerPSRepository;
        this.dockerStatInfluxRepository = dockerStatInfluxRepository;
    }

    @PostConstruct
    public void init() {
        influxDBTemplate.createDatabase();
    }

    @Override
    public void createContainerMetrics(List<DockerMetrics> metricsList) {
        influxDBTemplate.write(
                BatchPoints.builder().points(
                        metricsList.stream().map(d ->
                                Point.measurementByPOJO(DockerMetrics.class).addFieldsFromPOJO(d).build())
                                .collect(Collectors.toList())
                ).build().getPoints()
        );
    }

    @Override
    public void updateContainerPS(String hostname, List<ContainerPS> containerPSList) {
        containerPSRepository.update(hostname, containerPSList);
    }

    @Override
    public List<ContainerPS> findAllContainerPS() {
        return containerPSRepository.findAll();
    }

    @Override
    public List<ContainerPS> findContainerPS(String hostName) {
        return containerPSRepository.findByHostName(hostName);
    }

    @Override
    public List<DockerMetrics> getContainerMetricQueryAction(String query) {
        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        return resultMapper.toPOJO(dockerStatInfluxRepository.queryAction(query), DockerMetrics.class);
    }

//    @Override
//    public List<DockerMetrics> findAllContainerStat() {
//        String queryString = "select last(container_name) as container_name, last(container_id) as container_id,last(cpu) as cpu," +
//                "last(total_memory) as total_memory, last(used_memory) as used_memory,last(net_in) as net_in," +
//                "last(net_out) as net_out, last(block_in) as block_in,last(block_out) as block_out, last(host) as host, last(ip) as ip, last(status) as status " +
//                "from metrics where time>now()-5m group by host, container_name";
//
//
//    }
//
//    @Override
//    public List<DockerMetrics> findContainerStat(String hostname) {
//        String queryString = "select last(container_name) as container_name, last(container_id) as container_id,last(cpu) as cpu," +
//                "last(total_memory) as total_memory, last(used_memory) as used_memory,last(net_in) as net_in," +
//                "last(net_out) as net_out, last(block_in) as block_in,last(block_out) as block_out, last(host) as host, last(ip) as ip, last(status) as status  " +
//                "from metrics where time>now()-5m and hostname='" + hostname +"' group by host, container_name";
//
//        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
//        return resultMapper.toPOJO(dockerStatInfluxRepository.queryAction(queryString), DockerMetrics.class);
//    }
}