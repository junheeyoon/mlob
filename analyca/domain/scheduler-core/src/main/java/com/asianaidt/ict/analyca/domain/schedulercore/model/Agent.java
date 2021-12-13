package com.asianaidt.ict.analyca.domain.schedulercore.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class Agent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long agentId;

    private String ip;
    private String hostname;
    private String userId;
    private String sessionId;

    @Builder
    public Agent(String ip, String hostname, String userId, String sessionId) {
        this.ip = ip;
        this.hostname = hostname;
        this.userId = userId;
        this.sessionId = sessionId;
    }

    private LocalDateTime registered;
    private LocalDateTime updated;

    @PrePersist
    protected void onCreate() {
        registered = LocalDateTime.now();
        updated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updated = LocalDateTime.now();
    }
}
