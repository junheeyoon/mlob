package com.asianaidt.ict.analyca.service.schedulerservice.domain;

import lombok.Data;

@Data
public class JobWorkflowIssued {
    public Long jobId;  // matched with ID of SCHEDULE_LIST
    public Long idLogSchedule;
    public Long idLogStep;

    public JobWorkflowIssued() {
    }

    public JobWorkflowIssued(Long jobId, Long idLogSchedule, Long idLogStep) {
        this.jobId = jobId;
        this.idLogSchedule = idLogSchedule;
        this.idLogStep = idLogStep;
    }
}
