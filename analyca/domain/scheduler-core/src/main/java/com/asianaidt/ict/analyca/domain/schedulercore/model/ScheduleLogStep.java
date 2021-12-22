package com.asianaidt.ict.analyca.domain.schedulercore.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Table(name = "schedule_log_step")
@Entity
public class ScheduleLogStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long idWorkflow;

    @Column(nullable = false)
    private long idStep;

    @Column(nullable = false)
    private long stepOrder;

    @Column(length = 50, nullable = true)
    private String scheduleName;

    @Column(length = 50, nullable = true)
    private String stepName;

    @Column(length = 500)
    private String containerId;

    private LocalDateTime dtCommand;

    private LocalDateTime dtStart;

    private LocalDateTime dtEnd;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private commandStatus status;

//    @Column(nullable = false)
    private long exitCode;

    @Column(length = 50, nullable = false)
    private String userId;

    @PrePersist
    protected void onCreate() {
        dtCommand = LocalDateTime.now();
    }

    public enum commandStatus {
        ORDERED,    // 요청됨
        READY,      // 시작된 workflow
        RUNNING,    // 실행 중
        DONE,       // 실행 및 정상 종료
        FAILED,     // 실행 실패 혹은 비정상 종료
        CANCELED;   // 서버 내 취소
    }
}
