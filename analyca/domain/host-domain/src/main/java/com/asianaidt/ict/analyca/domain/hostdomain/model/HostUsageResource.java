package com.asianaidt.ict.analyca.domain.hostdomain.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

public class HostUsageResource {

    @Getter
    @Setter
    public static class HostStatus {
        private String hostname;
        private String status;
    }

    @Data
    @Measurement(name = "mem")
    public static class MemUsage {
        @Column(name = "used_percent")
        private long user_percent;
    }

    @Data
    @Measurement(name = "cpu")
    public static class CpuUsage {
        @Column(name = "usage_user")
        private long usage_user;
    }

    @Data
    @Measurement(name = "disk")
    public static class DiskUsage {
        @Column(name = "used_percent")
        private long used_percent;
    }

}
