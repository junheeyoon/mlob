package com.asianaidt.ict.analyca.domain.schedulercore.service;

import com.asianaidt.ict.analyca.domain.schedulercore.model.Schedule;
import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleLogWorkflow;

import java.util.Collection;
import java.util.List;

public interface ScheduleLogWorkflowService {
    List<ScheduleLogWorkflow> findByUserIdOrderByIdDesc(String UserId);

    List<ScheduleLogWorkflow> findAllByOrderByIdDesc();

    ScheduleLogWorkflow saveBySchedule(Schedule schedule);
    //ScheduleLogWorkflow findById(String );

    void updateLogWorkflowStatus(Long id, ScheduleLogWorkflow.commandStatus status);

    ScheduleLogWorkflow findTopByIdScheduleAndStatusOrderById(Long idSchedule, ScheduleLogWorkflow.commandStatus status);
    ScheduleLogWorkflow findTopByIdScheduleAndSequentialAndStatusInOrderById(Long idSchedule, ScheduleLogWorkflow.sequentialStatus seq, Collection<ScheduleLogWorkflow.commandStatus> status);
    List<ScheduleLogWorkflow> findAllByStatusIn(Collection<ScheduleLogWorkflow.commandStatus> status);
    List<ScheduleLogWorkflow> saveAll(List<ScheduleLogWorkflow> workflowList);
}
