package com.asianaidt.ict.analyca.service.schedulerservice.domain;

import lombok.Data;

@Data
public class JobRemoveIssued {
    public Long idStep;  // matched with ID of Step
    public String containerId;

    public JobRemoveIssued() {
    }

    public JobRemoveIssued(Long jobId, String containerId) {
        this.idStep = jobId;
        this.containerId = containerId;     // container name 값이 옴
    }
}
