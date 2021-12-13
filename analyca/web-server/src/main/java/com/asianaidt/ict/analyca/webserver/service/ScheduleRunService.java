package com.asianaidt.ict.analyca.webserver.service;

import com.asianaidt.ict.analyca.domain.schedulercore.model.Schedule;
import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleRunning;
import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleStep;
import com.asianaidt.ict.analyca.service.schedulerservice.domain.JobRemoveIssued;
import com.asianaidt.ict.analyca.service.schedulerservice.domain.JobStepIssued;
import com.asianaidt.ict.analyca.system.websocketcore.dto.ScheduleForStompResponse;
import com.asianaidt.ict.analyca.system.websocketcore.dto.ScheduleServiceStateList;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface ScheduleRunService {
    Schedule save(Schedule schedule);
    List<Schedule> findByDeleted(Schedule.deletedStatus deleted);
    Optional<Schedule> findById(Long id);
    void scheduleAdd(@NonNull Schedule schedule);
    void scheduleRun(@NonNull Long id);
    void scheduleUpdate(@NonNull Schedule schedule);
    void scheduleDelete(@NonNull Schedule schedule);
    void schedulePause(@NonNull Schedule schedule);
    void scheduleResume(@NonNull Schedule schedule);

    void collectServiceState();
//    ScheduleRunning saveProcess(ScheduleForStompResponse response);
//    void updateStateList(ScheduleServiceStateList stateList);

    Boolean saveSteps(Schedule scd, String steps);

//    List<ScheduleStep> findStepsByIdSchedule(Long id);
    List<ScheduleStep> findStepsByIdSchedule(Schedule i);

     void scheduleStepRun(JobStepIssued issued);

     void scheduleStepContainerRemove(JobRemoveIssued issued);
}
