package com.asianaidt.ict.analyca.domain.schedulercore.repository;

import com.asianaidt.ict.analyca.domain.schedulercore.model.Schedule;
import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleStepRepository extends JpaRepository<ScheduleStep, Long> {

    ScheduleStep save(ScheduleStep scheduleStep);

    Optional<ScheduleStep> findById(Long id);

    List<ScheduleStep> findByIdSchedule(Schedule i);

    @Transactional
    @Modifying
    void deleteAllByIdSchedule(Schedule i);
}
