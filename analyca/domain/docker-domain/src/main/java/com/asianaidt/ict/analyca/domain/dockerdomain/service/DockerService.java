package com.asianaidt.ict.analyca.domain.dockerdomain.service;

import com.asianaidt.ict.analyca.domain.dockerdomain.model.ContainerPS;
import com.asianaidt.ict.analyca.domain.dockerdomain.model.DockerMetrics;

import java.util.List;

public interface DockerService {
    void createContainerMetrics(List<DockerMetrics> metricsList);

    void updateContainerPS(String hostname, List<ContainerPS> containerPSList);

    List<ContainerPS> findAllContainerPS();

    List<ContainerPS> findContainerPS(String hostName);

    List<DockerMetrics> getContainerMetricQueryAction(String query);
}
