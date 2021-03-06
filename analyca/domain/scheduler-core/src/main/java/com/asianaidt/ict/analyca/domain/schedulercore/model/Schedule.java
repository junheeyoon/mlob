package com.asianaidt.ict.analyca.domain.schedulercore.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Table(name = "schedule_list")
@Entity
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

//    ///////////////////////// temp
//    @Column(length = 500, nullable = false)
//    private String command;
//
//    @Column(length = 50, nullable = false)
//    private String agent;

    @Column(length = 50, nullable = true)
    private String scheduleName;

    @Column(length = 500, nullable = true)
    private String scheduleDesc;

    @Column(length = 10, nullable = false)
    private String type;

    @Column(length = 50)
    private String cronExpression;

    @Column(length = 200)
    private String cronExpressionStr;

    @Column(name = "sequential")
    @Enumerated(EnumType.STRING)
    private sequentialStatus sequential;

    @Column(name = "used")
    @Enumerated(EnumType.STRING)
    private usedStatus used;

    @Column(name = "deleted")
    @Enumerated(EnumType.STRING)
    private deletedStatus deleted;

    private LocalDateTime dtStart;
    private LocalDateTime dtEnd;

    private LocalDateTime dtRegister;
    private LocalDateTime dtUpdate;

    @Column(length = 50, nullable = false)
    private String userId;

    @PrePersist
    protected void onCreate() {
        dtRegister = LocalDateTime.now();
        dtUpdate = LocalDateTime.now();
    }


    @PreUpdate
    protected void onUpdate() {
        dtUpdate = LocalDateTime.now();
    }


    public enum usedStatus {
        RUNNING,    // 0
        READY,      // 1
        STOP;       // 2
    }

    public enum deletedStatus {
        EXIST, // 0
        DELETED; // 1
    }

    public enum sequentialStatus {
        //CONCURRENT, // 0
        PARALLEL,   // 0
        SEQUENTIAL; // 1
    }

}


