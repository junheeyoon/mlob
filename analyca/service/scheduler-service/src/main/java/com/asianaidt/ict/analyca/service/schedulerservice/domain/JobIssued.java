package com.asianaidt.ict.analyca.service.schedulerservice.domain;

import lombok.Data;

// Event Class
@Data
public class JobIssued {
    public Long jobId;  // matched with ID of SCHEDULE_LIST

    public JobIssued() {
    }

    public JobIssued(Long jobId) {
        this.jobId = jobId;
    }
}
