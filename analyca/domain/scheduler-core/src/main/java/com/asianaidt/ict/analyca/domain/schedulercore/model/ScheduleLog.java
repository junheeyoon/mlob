package com.asianaidt.ict.analyca.domain.schedulercore.model;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Table(name = "schedule_log")
@Entity
public class ScheduleLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long idSchedule;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private commandStatus status;

    @Column(length = 50, nullable = false)
    private String userId;

    @Column(length = 50, nullable = false)
    private String hostName;

    @Column(length = 50, nullable = false)
    private String hostIp;

    @Column(length = 10, nullable = false)
    private String type;

    @Column(length = 50, nullable = true)
    private String scheduleName;

    @Column(length = 500, nullable = true)
    private String scheduleDesc;

//    @Column(length = 5000)
//    private String log;

    private LocalDateTime dtCommand;

    @PrePersist
    protected void onCreate() {
        dtCommand = LocalDateTime.now();
    }

    public enum commandStatus {
        WAIT, // 0
        START, // 1
        DONE,
        FAILED;
    }
}
