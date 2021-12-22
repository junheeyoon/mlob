package com.asianaidt.ict.analyca.domain.schedulercore.repository;

import com.asianaidt.ict.analyca.domain.schedulercore.model.Schedule;
import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleLog;
import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleRunning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleLogRepository extends JpaRepository<ScheduleLog, Long> {
    ScheduleLog findScheduleLogById(Long id);

    ScheduleLog save(ScheduleLog scheduleLog);

    List<ScheduleLog> findAllByOrderByIdDesc();

    List<ScheduleLog> findByUserIdOrderByIdDesc(String userId);
}
