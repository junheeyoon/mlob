package com.asianaidt.ict.analyca.domain.hostdomain.model;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Data
@Measurement(name = "mem")
public class HostMemStatus {

    @Column(name = "total")
    private long total;
    @Column(name = "used")
    private long used;
    @Column(name = "time")
    private String time;

}
