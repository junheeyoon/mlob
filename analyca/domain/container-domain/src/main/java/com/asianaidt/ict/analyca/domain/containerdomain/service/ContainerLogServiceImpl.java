package com.asianaidt.ict.analyca.domain.containerdomain.service;

import com.asianaidt.ict.analyca.domain.containerdomain.model.ContainerLog;
import com.asianaidt.ict.analyca.domain.containerdomain.model.ContainerStatus;
import com.asianaidt.ict.analyca.domain.containerdomain.repository.ContainerLogRepository;
import com.asianaidt.ict.analyca.domain.containerdomain.repository.ContainerStatusRepository;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContainerLogServiceImpl implements ContainerLogService{

    private final ContainerLogRepository containerLogRepository;
    private final ContainerStatusRepository containerStatusRepository;
    public ContainerLogServiceImpl(ContainerLogRepository containerLogRepository, ContainerStatusRepository containerStatusRepository) {
        this.containerLogRepository = containerLogRepository;
        this.containerStatusRepository = containerStatusRepository;
    }

    @Override
    public List<ContainerLog> h2o_list(String s) {
        String queryString = "SELECT * FROM docker_log WHERE source = '"+s+"'";

        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        return resultMapper.toPOJO(containerLogRepository.queryAction(queryString), ContainerLog.class);

    }


    @Override
    public List<ContainerLog> findAllContainerLog() {
        return containerLogRepository.findAll();
    }

    @Override
    public List<ContainerLog> findContainerLog(String host) {
        return containerLogRepository.findByHost(host);
    }
}
