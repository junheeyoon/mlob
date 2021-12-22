package com.asianaidt.ict.analyca.domain.schedulercore.model;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Table(name = "schedule_running")
@Entity
public class ScheduleRunning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long idWorkflow;

    @Column(nullable = false)
    private long stepOrder;

    @Column(length = 50, nullable = false)
    private String stepName;

    @Column(length = 50, nullable = false)
    private String image;

    @Column(name = "sequential")
    @Enumerated(EnumType.STRING)
    private sequentialStatus sequential;

    @Column(length = 50, nullable = false)
    private String userId;

    @Column(length = 10, nullable = false)
    private String type;

    @Column(length = 50)
    private String scheduleName;

    @Column(length = 500)
    private String scheduleDesc;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private commandStatus status;

    private LocalDateTime dtCommand;

    @PrePersist
    protected void onCreate() {
        dtCommand = LocalDateTime.now();
    }

    public enum commandStatus {
        READY, // 0
        RUNNING, // 1
        DONE,
    }
    public enum sequentialStatus {
        //CONCURRENT, // 0
        PARALLEL, // 0
        SEQUENTIAL; // 1
    }
}
