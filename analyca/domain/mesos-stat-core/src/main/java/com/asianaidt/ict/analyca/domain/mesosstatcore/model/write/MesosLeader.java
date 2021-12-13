package com.asianaidt.ict.analyca.domain.mesosstatcore.model.write;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Measurement(name = "leader")
public class MesosLeader {
    @Column(name = "id")
    private String id;
    @Column(name = "pid")
    private String pid;
    @Column(name = "port")
    private int port;
    @Column(name = "hostname", tag = true)
    private String hostname;
}
