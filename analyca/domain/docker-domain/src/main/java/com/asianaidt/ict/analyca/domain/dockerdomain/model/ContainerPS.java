package com.asianaidt.ict.analyca.domain.dockerdomain.model;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class ContainerPS {
    private String hostName;
    private String hostIp;
    private String containerName;
    private String creator;
    private String cpu;
    private String mem;
    private String gpu;
    private String createdAt;
    private String status;
    private String imageName;
    private String imageTag;
    private String binds;
    private String ports;
}
