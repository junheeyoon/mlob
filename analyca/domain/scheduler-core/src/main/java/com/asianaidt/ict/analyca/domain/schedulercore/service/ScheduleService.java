package com.asianaidt.ict.analyca.domain.schedulercore.service;

import com.asianaidt.ict.analyca.domain.schedulercore.model.Schedule;

import java.util.List;
import java.util.Optional;

public interface ScheduleService {

    Schedule save(Schedule schedule);

    List<Schedule> findAll();

    Optional<Schedule> findById(Long id);

    Schedule findByScheduleName(String s);

    List<Schedule> findByDeleted(Schedule.deletedStatus deleted);

    List<Schedule> findAllPossibleSchedule();
}
