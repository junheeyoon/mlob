package com.asianaidt.ict.analyca.domain.schedulercore.service;

import com.asianaidt.ict.analyca.domain.schedulercore.model.Schedule;
import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleStep;

import java.util.List;
import java.util.Optional;

public interface ScheduleStepService {

    ScheduleStep save(ScheduleStep scheduleStep);
    void saveAll(List<ScheduleStep> steps);

    Optional<ScheduleStep> findById(Long id);

    List<ScheduleStep> findByIdSchedule(Schedule i);

    void deleteAllByIdSchedule(Schedule i);
}
