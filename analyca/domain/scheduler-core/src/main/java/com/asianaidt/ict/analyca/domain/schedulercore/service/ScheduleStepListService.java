package com.asianaidt.ict.analyca.domain.schedulercore.service;

import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleStepList;

import java.util.List;
import java.util.Optional;

public interface ScheduleStepListService {

    List<ScheduleStepList> findAll();

    ScheduleStepList save(ScheduleStepList scheduleStepList);

    Optional<ScheduleStepList> findById(Long id);

}
