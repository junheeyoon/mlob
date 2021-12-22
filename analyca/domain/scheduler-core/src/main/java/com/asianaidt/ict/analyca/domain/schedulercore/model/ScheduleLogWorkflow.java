package com.asianaidt.ict.analyca.domain.schedulercore.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Table(name = "schedule_log_workflow")
@Entity
public class ScheduleLogWorkflow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long idSchedule;

    @Column(length = 10, nullable = false)
    private String type;

    @Column(name = "sequential")
    @Enumerated(EnumType.STRING)
    private sequentialStatus sequential;

    @Column(length = 50, nullable = true)
    private String scheduleName;

    @Column(length = 500)
    private String scheduleDesc;

    private LocalDateTime dtCommand;

    private LocalDateTime dtStart;

    private LocalDateTime dtEnd;

    @Column(length = 50, nullable = false)
    private String userId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private commandStatus status;

    @PrePersist
    protected void onCreate() {
        dtCommand = LocalDateTime.now();
    }

    public enum sequentialStatus {
//        CONCURRENT, // 0
        PARALLEL, // 0
        SEQUENTIAL; // 1
    }
    public enum commandStatus {
        ORDERED,    // 요청됨
        READY,      // 시작된 workflow -> ORDERED로 대체
        RUNNING,    //
        DONE,       // 실행 및 정상 종료
        FAILED,     // 실행 실패 혹은 비정상 종료
        CANCELED;   // 서버 내 취소
    }
}
