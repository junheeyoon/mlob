package com.asianaidt.ict.analyca.service.schedulerservice.service;

import com.asianaidt.ict.analyca.domain.schedulercore.model.Schedule;
import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleStep;

import java.util.List;
import java.util.Optional;

public interface JobSchedulerService {
    List<Schedule> findAllPossibleSchedule();

    boolean startJobAll();

    boolean addJob(Schedule schedule);

    boolean startJob(final Schedule schedule);

    boolean stopJob(final Schedule schedule);

    boolean updateJob(final Schedule schedule);

    boolean pauseJob(final Schedule schedule);

    boolean resumeJob(final Schedule schedule);

    boolean deleteJob(final Schedule schedule);

    boolean isJobWithNamePresent(final Long scheduleId, final String scheduleName);

    Schedule save(Schedule schedule);

    Optional<Schedule> findById(Long id);

    Optional<ScheduleStep> findStepById(Long id);

    List<Schedule> findByDeleted(Schedule.deletedStatus deleted);

    Schedule updateUsed(Long id, Schedule.usedStatus status);

    boolean startJobOnce(final Schedule schedule);
}
