package com.asianaidt.ict.analyca.domain.schedulercore.repository;

import com.asianaidt.ict.analyca.domain.schedulercore.model.Schedule;
import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleStep;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChainScheduleStepRepository extends JpaRepository<ScheduleStep, Long> {
    List<ScheduleStep> findByIdSchedule(Schedule i);
}
