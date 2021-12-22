package com.asianaidt.ict.analyca.domain.dockerdomain.model;

import lombok.*;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Measurement(name = "metrics")
@Data
@AllArgsConstructor
@Builder
public class DockerMetrics {
    @Column(name = "container_name", tag = true)
    private String containerName;
    @Column(name = "container_id")
    private String containerId;
    @Column(name = "cpu")
    private double cpu;
    @Column(name = "total_memory")
    private double totalMemory;
    @Column(name = "used_memory")
    private double usedMemory;
    @Column(name = "net_in")
    private double netIn;
    @Column(name = "net_out")
    private double netOut;
    @Column(name = "block_in")
    private double blockIn;
    @Column(name = "block_out")
    private double blockOut;
    @Column(name = "host", tag = true)
    private String host;
    @Column(name = "ip")
    private String ip;
    @Column(name = "status")
    private String status;
    @Column(name = "time")
    private String time;

    public DockerMetrics() {

    }

}