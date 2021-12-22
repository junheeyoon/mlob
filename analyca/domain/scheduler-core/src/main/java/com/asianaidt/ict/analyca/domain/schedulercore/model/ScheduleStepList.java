package com.asianaidt.ict.analyca.domain.schedulercore.model;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Data
@Table(name = "schedule_step_list")
@Entity
public class ScheduleStepList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 200, nullable = false)
    private String image;

    @Column(length = 200, nullable = false)
    private String tag;

    @Column(length = 200, nullable = false)
    private String containerName;

    @Column(length = 200)
    @ColumnDefault("")
    private String port;

    @Column(length = 200)
    @ColumnDefault("")
    private String volume;

    @Column(length = 200)
    @ColumnDefault("")
    private String memory;

    @Column(length = 200)
    @ColumnDefault("")
    private String cpu;

    @Column(length = 200)
    @ColumnDefault("false")
    private Boolean gpu;

    @Column(length = 200)
    @ColumnDefault("")
    private String options;

    @Column(length = 200)
    @ColumnDefault("")
    private String commands;

    public void setGpu(Boolean gpu) {
        this.gpu = gpu;
    }

    public void setGpu(String gpu) {
        if (gpu.equalsIgnoreCase("true"))
            this.gpu = true;
        else
            this.gpu = false;
    }
}

