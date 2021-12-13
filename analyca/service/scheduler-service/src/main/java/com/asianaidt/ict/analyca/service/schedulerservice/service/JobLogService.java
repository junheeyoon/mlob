package com.asianaidt.ict.analyca.service.schedulerservice.service;

import com.asianaidt.ict.analyca.domain.schedulercore.model.*;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface JobLogService {
    ScheduleLogWorkflow saveLogSchedule(Schedule schedule);
    List<ScheduleLogStep> saveLogStepByWorkflow(ScheduleLogWorkflow logWorkflow, List<ScheduleStep> stepList);
    List<ScheduleRunning> saveRunningByStep(ScheduleLogWorkflow logWorkflow, List<ScheduleStep> stepList);
    List<Long> saveRunning(Schedule schedule);
    void updateContainerId(Long idLogStep, String containerId);
    ScheduleLogStep findById(Long id);
    void updateLogStepDtStart(Long id);
    void updateLogStepStatus(Long id, ScheduleLogStep.commandStatus status);
    void updateLogWorkflowStatus(Long id, ScheduleLogWorkflow.commandStatus status);
    void updateAllLogStepStatusByIdWorkflow(Long idWorkflow, ScheduleLogStep.commandStatus status);
    void updateRunningStatus(Long idLogStep, ScheduleRunning.commandStatus status);
    void deleteRunningByIdWorkflow(Long idWorkflow);
    ScheduleLogWorkflow serialStart(Long idWorkflow);

    void initLogStates();
}
