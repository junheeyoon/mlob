package com.asianaidt.ict.analyca.domain.containerdomain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Measurement(name = "docker_container_status")
@Data
@AllArgsConstructor
@Builder
public class ContainerStatus {
    @Column(name = "container_name", tag = true)
    private String containerName;
    @Column(name = "container_image")
    private String containerImage;
    @Column(name = "container_id")
    private String containerId;
    @Column(name = "container_status")
    private String containerStatus;
    @Column(name = "exitcode")
    private String exitcode;
    @Column(name = "time")
    private String time;
    public ContainerStatus() {
    }
}
