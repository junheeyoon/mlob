package com.asianaidt.ict.analyca.service.schedulerservice.domain;

import lombok.Data;

@Data
public class JobStepIssued {
    public Long idStep;  // matched with ID of Step
    public Long idLogScheduleStep;

    public JobStepIssued() {
    }

    public JobStepIssued(Long jobId, Long idLogScheduleStep) {
        this.idStep = jobId;
        this.idLogScheduleStep = idLogScheduleStep;
    }
}
