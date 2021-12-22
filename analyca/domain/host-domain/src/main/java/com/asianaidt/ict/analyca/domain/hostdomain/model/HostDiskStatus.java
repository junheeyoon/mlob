package com.asianaidt.ict.analyca.domain.hostdomain.model;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Data
@Measurement(name = "disk")
public class HostDiskStatus {

    @Column(name = "disktotal")
    private double total;
    @Column(name = "diskused")
    private double used;
    @Column(name = "time")
    private String time;


}
