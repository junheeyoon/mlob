package com.asianaidt.ict.analyca.domain.hadoopcore.service;

import com.asianaidt.ict.analyca.domain.hadoopcore.model.read.HdfsDataNodeResource;
import com.asianaidt.ict.analyca.domain.hadoopcore.model.read.HdfsNameNodeFSNameInfoResource;
import com.asianaidt.ict.analyca.domain.hadoopcore.model.read.HdfsNameNodeFSSystemResource;
import com.asianaidt.ict.analyca.domain.hadoopcore.model.write.HdfsDataNodeState;
import com.asianaidt.ict.analyca.domain.hadoopcore.model.write.HdfsNameNodeState;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HdfsCoreServiceImpl implements HdfsCoreService {
    private final InfluxDBTemplate<Point> influxDBTemplate;

    public HdfsCoreServiceImpl(@Qualifier("influxDBTemplateHDFS") InfluxDBTemplate<Point> influxDBTemplate) {
        this.influxDBTemplate = influxDBTemplate;
    }

    @PostConstruct
    public void init() {
        influxDBTemplate.createDatabase();
    }

    @Override
    public void write(HdfsNameNodeFSNameInfoResource hdfsNameNodeFSNameInfoResource,
                      HdfsNameNodeFSSystemResource hdfsNameNodeFSSystemResource,
                      List<HdfsDataNodeResource> listHdfsDataNodeResource) {
        influxDBTemplate.write(
                BatchPoints.builder().points(
                        Point.measurementByPOJO(HdfsNameNodeState.class).
                                addFieldsFromPOJO(new HdfsNameNodeState(hdfsNameNodeFSSystemResource, hdfsNameNodeFSNameInfoResource)).build()
                ).points(
                        listHdfsDataNodeResource.stream().
                                map(s -> Point.measurementByPOJO(HdfsDataNodeState.class).
                                        addFieldsFromPOJO(new HdfsDataNodeState(s)).build()).collect(Collectors.toList())
                ).build().getPoints()
        );
    }
}
