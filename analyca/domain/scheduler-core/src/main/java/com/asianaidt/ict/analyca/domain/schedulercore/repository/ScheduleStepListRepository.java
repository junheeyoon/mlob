package com.asianaidt.ict.analyca.domain.schedulercore.repository;

import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleStepList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScheduleStepListRepository extends JpaRepository<ScheduleStepList, Long> {

    ScheduleStepList save(ScheduleStepList scheduleStep);

    Optional<ScheduleStepList> findById(Long id);
}
