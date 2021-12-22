package com.asianaidt.ict.analyca.domain.schedulercore.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "schedule_step")
@Entity
public class ScheduleStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_schedule")
    private Schedule idSchedule;

    @Column(length = 50, nullable = false)
    private long stepOrder;

    @Column(length = 50, nullable = false)
    private String stepName;

//    @Column(length = 500, nullable = false)
//    private String command;

    @Column(length = 1000)
    private String stepDesc;

    @Column(length = 200, nullable = false)
    private String host;

    @Column(length = 200, nullable = false)
    private String agent;

    @Column(length = 200, nullable = false)
    private String image;

    @Column(length = 10, nullable = false)
    private Long idStepList;

}

