package com.asianaidt.ict.analyca.webserver.service;

import com.asianaidt.ict.analyca.domain.dockerdomain.model.DockerMetrics;

import java.util.List;

public interface GhostService {

    List<DockerMetrics> getMetricsInfoAll();

    List<DockerMetrics> getCPUMetricsContainer(String containerName, String period);

    List<DockerMetrics> getMemMetricsContainer(String containerName, String period);
}
