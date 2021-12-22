package com.asianaidt.ict.analyca.domain.schedulercore.service;

import com.asianaidt.ict.analyca.domain.schedulercore.model.Agent;
import com.asianaidt.ict.analyca.domain.schedulercore.model.Schedule;
import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleRunning;

import java.util.List;
import java.util.Optional;

public interface ScheduleRunningService {
    ScheduleRunning save(ScheduleRunning scheduleRunning);

    //    ScheduleRunning saveByPid(Long pid, Schedule schedule, Agent agent);
//    void deleteByAgentAndPid(String agent, Long Pid);
    void deleteById(Long id);

    Optional<ScheduleRunning> findById(Long id);

    //    List<ScheduleRunning.pidGroup> findPidsGroupByAgent();
    List<ScheduleRunning> findAll();

    List<ScheduleRunning> findByUserId(String UserId);

    List<ScheduleRunning> saveAll(List<ScheduleRunning> runningList);

    void updateRunningStatusByIdWorkflowAndStepOrder(Long idWorkflow, Long stepOrder, ScheduleRunning.commandStatus status);

    void deleteByIdWorkflow(Long idWorkflow);

    void deleteAll();
}

