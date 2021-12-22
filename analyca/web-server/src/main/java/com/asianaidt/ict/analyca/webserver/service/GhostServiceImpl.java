package com.asianaidt.ict.analyca.webserver.service;

import com.asianaidt.ict.analyca.domain.dockerdomain.model.DockerMetrics;
import com.asianaidt.ict.analyca.domain.dockerdomain.service.DockerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GhostServiceImpl implements GhostService {

    final DockerService dockerService;

    @Override
    public List<DockerMetrics> getMetricsInfoAll() {
        String query = "select last(container_name) as container_name, last(container_id) as container_id,last(cpu) as cpu," +
                "last(total_memory) as total_memory, last(used_memory) as used_memory,last(net_in) as net_in," +
                "last(net_out) as net_out, last(block_in) as block_in,last(block_out) as block_out, last(host) as host, last(ip) as ip, last(status) as status " +
                "from metrics where time>now()-5m group by host, container_name";
        return dockerService.getContainerMetricQueryAction(query);
    }

    @Override
    public List<DockerMetrics> getCPUMetricsContainer(String containerName, String period) {
        String query = "select container_name, cpu, time from metrics where container_name='" + containerName + "' and time>now()-" + period + " tz('Asia/Seoul')";
        return dockerService.getContainerMetricQueryAction(query);
    }

    @Override
    public List<DockerMetrics> getMemMetricsContainer(String containerName, String period) {
        String query = "select container_name, (total_memory/1000/1000/1000) as total_memory,(used_memory/1000/1000/1000) as used_memory, time from metrics where container_name='" + containerName + "' and time>now()-" + period + " tz('Asia/Seoul')";
        return dockerService.getContainerMetricQueryAction(query);
    }
}
