package com.asianaidt.ict.analyca.domain.containerdomain.model;

import lombok.*;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Measurement(name = "docker_log")
@Data
@AllArgsConstructor
@Builder
public class ContainerLog {

    @Column(name = "container_name", tag = true)
    private String containerName;
    @Column(name = "container_image")
    private String containerImage;
    @Column(name = "container_id")
    private String containerId;
    @Column(name = "container_version")
    private String containerVersion;
    @Column(name = "message")
    private String message;
    @Column(name = "org.label-schema.build-date")
    private String buildDate;
    @Column(name = "org.label-schema.license")
    private String license;
    @Column(name = "org.label-schema.name")
    private String name;
    @Column(name = "org.label-schema.schema-version")
    private String schemaVersion;
    @Column(name = "org.label-schema.vendor")
    private String vendor;
    @Column(name = "stream")
    private String stream;
    @Column(name = "host", tag = true)
    private String host;
    @Column(name = "time")
    private String time;
    public ContainerLog() {

    }
}
