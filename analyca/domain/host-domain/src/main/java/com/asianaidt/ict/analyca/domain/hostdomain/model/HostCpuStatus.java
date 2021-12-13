package com.asianaidt.ict.analyca.domain.hostdomain.model;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Data
@Measurement(name = "cpu")
public class HostCpuStatus {
    @Column(name = "usage_user")
    private double usage_user;
    @Column(name = "time")
    private String time;


}
