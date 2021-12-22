package com.asianaidt.ict.analyca.domain.schedulercore.service;

import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleLog;
import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleLogStep;
import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleLogWorkflow;
import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleRunning;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ScheduleLogStepService {
    //List<ScheduleLogStep> findByIdSchedule(ScheduleLogWorkflow i);
    List<ScheduleLogStep> findByIdWorkflow(Long i);

    List<ScheduleLogStep> findByUserIdOrderByIdDesc(String UserId);

    List<ScheduleLogStep> findAllByOrderByIdDesc();

    List<ScheduleLogStep> saveAll(List<ScheduleLogStep> logStepList);

    ScheduleLogStep findById(Long id);

    void updateLogStepDtStart(Long id);

    void updateLogStepStatus(Long id, ScheduleLogStep.commandStatus status);

    void updateAllLogStepStatusByIdWorkflow(Long idWorkflow, ScheduleLogStep.commandStatus status);

    void updateContainerId(Long id, String contId);

    List<ScheduleLogStep> findAllByStatusIn(Collection<ScheduleLogStep.commandStatus> status);
}
