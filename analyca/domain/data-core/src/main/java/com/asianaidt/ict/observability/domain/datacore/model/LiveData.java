package com.asianaidt.ict.observability.domain.datacore.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Table(name = "live_data")
@Entity
public class LiveData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long id_sub_model;

    @Column(length = 50, nullable = true)
    private String groundTruth;

    @Column(length = 50, nullable = true)
    private String prediction;

    @Column(length = 50, nullable = true)
    private String confidenceScore;

    @Column(length = 50, nullable = true)
    private String processingTime;

    @Column(columnDefinition = "LONGTEXT")
    private String liveData;

    private LocalDateTime dtStart;
    private LocalDateTime dtEnd;


    @Column(length = 50, nullable = false)
    private String userId;

    @PrePersist
    protected void onCreate() {
        dtStart = LocalDateTime.now();
        dtEnd = LocalDateTime.now();
    }
}


