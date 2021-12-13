package com.asianaidt.ict.analyca.domain.mesosstatcore.model.write;

import com.asianaidt.ict.analyca.domain.mesosstatcore.model.read.Slave;
import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;
import org.influxdb.annotation.TimeColumn;

import java.time.Instant;

@Data
@Measurement(name = "slave")
public class MesosSlave {
    @TimeColumn
    @Column(name = "tiem")
    private Instant time;
    @Column(name = "id")
    private String id;
    @Column(name = "hostname", tag = true)
    private String hostname;
    @Column(name = "port")
    private int port;
    @Column(name = "pid")
    private String pid;
    @Column(name = "used_disk")
    private int usedDisk;
    @Column(name = "used_mem")
    private int usedMem;
    @Column(name = "used_gpus")
    private int usedGpus;
    @Column(name = "used_cpus")
    private int usedCpus;
    @Column(name = "unreserved_disk")
    private int unreservedDisk;
    @Column(name = "unreserved_mem")
    private int unreservedMem;
    @Column(name = "unreserved_gpus")
    private int unreservedGpus;
    @Column(name = "unreserved_cpus")
    private int unreservedCpus;

    public MesosSlave(final Slave slave) {
        this.id = slave.getId();
        this.hostname = slave.getHostname();
        this.port = slave.getPort();
        this.pid = slave.getPid();
        this.usedDisk = slave.getUsedRresources().getDisk();
        this.usedMem = slave.getUsedRresources().getMem();
        this.usedGpus = slave.getUsedRresources().getGpus();
        this.usedCpus = slave.getUsedRresources().getCpus();
        this.unreservedDisk = slave.getUnreservedResources().getDisk();
        this.unreservedMem = slave.getUnreservedResources().getMem();
        this.unreservedGpus = slave.getUnreservedResources().getGpus();
        this.unreservedCpus = slave.getUnreservedResources().getCpus();
    }
}
