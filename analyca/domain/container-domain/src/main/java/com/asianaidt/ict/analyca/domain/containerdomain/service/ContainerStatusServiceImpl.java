package com.asianaidt.ict.analyca.domain.containerdomain.service;

import com.asianaidt.ict.analyca.domain.containerdomain.model.ContainerLog;
import com.asianaidt.ict.analyca.domain.containerdomain.model.ContainerStatus;
import com.asianaidt.ict.analyca.domain.containerdomain.repository.ContainerStatusRepository;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContainerStatusServiceImpl implements ContainerStatusService{

    private final ContainerStatusRepository containerStatusRepository;

    public ContainerStatusServiceImpl(ContainerStatusRepository containerStatusRepository) {
        this.containerStatusRepository = containerStatusRepository;
    }

//    @Override
//    public List<ContainerStatus> findContainerStatus(String i) {
//        System.out.println("##### ContainerStatusServiceImpl.findContainerStatus - " + i);
//        String queryString = "SELECT * FROM docker_container_status where source = '" + i + "' order by time desc limit 1";
//        System.out.println("##### " + queryString);
//        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
//        System.out.println(resultMapper.toPOJO(containerStatusRepository.queryAction(queryString), ContainerStatus.class));
//        return resultMapper.toPOJO(containerStatusRepository.queryAction(queryString), ContainerStatus.class);
//    }

    @Override
    public List<String> findContainerStatus(String i) {
        System.out.println("####################################");
        System.out.println("ContainerStatusServiceImpl.findContainerStatus : " + i);

        String influxHost = "http://10.33.194.25:4006";
        InfluxDB influxDB = InfluxDBFactory.connect(influxHost, "admin", "admin123");
        influxDB.setDatabase("telegraf");
//        Query _queryString = new Query("SELECT * FROM docker_container_status where source = '" + i + "' order by time desc limit 1");
        Query _queryString = new Query("SELECT container_status, exitcode, container_name FROM docker_container_status where source = '" + i + "' order by time desc limit 1");
        System.out.println("query : " + _queryString.getCommand());
        QueryResult queryResultMem = influxDB.query(_queryString);

        System.out.println("####################################");
        System.out.println(queryResultMem);
        System.out.println("####################################");

        String container_status = queryResultMem.getResults().get(0).getSeries().get(0).getValues().get(0).get(1).toString();
        String exitcode = queryResultMem.getResults().get(0).getSeries().get(0).getValues().get(0).get(2).toString();


        List<String> result = new ArrayList<>();
        result.add(container_status);
        result.add(exitcode);

        return result;
    }

}
