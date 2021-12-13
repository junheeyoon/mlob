package com.asianaidt.ict.analyca.domain.schedulercore.repository;

import com.asianaidt.ict.analyca.domain.schedulercore.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleLogStepRepository extends JpaRepository<ScheduleLogStep, Long> {
    //List<ScheduleLogStep> findByIdSchedule(ScheduleLogWorkflow i);
    List<ScheduleLogStep> findByIdWorkflow(Long i);
    List<ScheduleLogStep> findAllByOrderByIdDesc();
    Optional<ScheduleLogStep> findById(Long id);
    List<ScheduleLogStep> findByUserIdOrderByIdDesc(String userId);
    List<ScheduleLogStep> findAllByStatusIn(Collection<ScheduleLogStep.commandStatus> status);
    List<ScheduleLogStep> findAllByIdWorkflow(Long idWorkflow);
}
