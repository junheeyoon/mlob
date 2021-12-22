package com.asianaidt.ict.analyca.domain.mesosstatcore.service;

import com.asianaidt.ict.analyca.domain.mesosstatcore.model.read.MesosResource;
import com.asianaidt.ict.analyca.domain.mesosstatcore.model.write.MesosLeader;
import com.asianaidt.ict.analyca.domain.mesosstatcore.model.write.MesosSlave;
import com.asianaidt.ict.analyca.domain.mesosstatcore.model.write.MesosState;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.stream.Collectors;

@Service
public class MesosCoreServiceImpl implements MesosCoreService {
    private final InfluxDBTemplate<Point> influxDBTemplate;

    public MesosCoreServiceImpl(InfluxDBTemplate<Point> influxDBTemplate) {
        this.influxDBTemplate = influxDBTemplate;
    }

    @PostConstruct
    public void init() {
        influxDBTemplate.createDatabase();
    }

    @Override
    public void write(MesosResource mesosResource) {
        influxDBTemplate.write(
                BatchPoints.builder().points(
                        Point.measurementByPOJO(MesosState.class).addFieldsFromPOJO(new MesosState(mesosResource)).build(),
                        Point.measurementByPOJO(MesosLeader.class).addFieldsFromPOJO(mesosResource.getLeaderInfo()).build()
                ).points(
                        mesosResource.getSlaves().stream()
                                .map(s -> Point.measurementByPOJO(MesosSlave.class).addFieldsFromPOJO(new MesosSlave(s)).build()).collect(Collectors.toList())
                ).build().getPoints()
        );
    }
}
