package com.asianaidt.ict.analyca.webserver.service;

import com.asianaidt.ict.analyca.service.schedulerservice.domain.JobIssued;
import com.asianaidt.ict.analyca.service.schedulerservice.domain.JobRemoveIssued;
import com.asianaidt.ict.analyca.service.schedulerservice.domain.JobStepIssued;
import com.asianaidt.ict.analyca.service.schedulerservice.domain.JobWorkflowIssued;

public interface JobEventListener {
    void onTaskSchedulerEvent(JobIssued event);
    void onTaskSchedulerEventWorkflow(JobWorkflowIssued event);
    void onTaskSchedulerEventStep(JobStepIssued event);
    void onTaskSchedulerEventRemove(JobRemoveIssued event);
}
