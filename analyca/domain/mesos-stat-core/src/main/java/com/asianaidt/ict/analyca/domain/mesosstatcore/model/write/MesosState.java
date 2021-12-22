package com.asianaidt.ict.analyca.domain.mesosstatcore.model.write;

import com.asianaidt.ict.analyca.domain.mesosstatcore.model.read.MesosResource;
import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Data
@Measurement(name = "state")
public class MesosState {
    @Column(name = "version")
    private String version;
    @Column(name = "id")
    private String id;
    @Column(name = "pid")
    private String pid;
    @Column(name = "hostname", tag = true)
    private String hostname;
    @Column(name = "activated_slaves")
    private int activatedSlaves;
    @Column(name = "deactivated_slaves")
    private int deactivatedSlaves;
    @Column(name = "unreachable_slaves")
    private int unreachableSlaves;

    public MesosState(final MesosResource mesosResource) {
        this.version = mesosResource.getVersion();
        this.id = mesosResource.getId();
        this.pid = mesosResource.getPid();
        this.hostname = mesosResource.getHostname();
        this.activatedSlaves = mesosResource.getActivatedSlaves();
        this.deactivatedSlaves = mesosResource.getDeactivatedSlaves();
        this.unreachableSlaves = mesosResource.getUnreachableSlaves();
    }
}
