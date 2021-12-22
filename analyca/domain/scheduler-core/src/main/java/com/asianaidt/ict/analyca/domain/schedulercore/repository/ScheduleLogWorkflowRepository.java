package com.asianaidt.ict.analyca.domain.schedulercore.repository;

import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleLog;
import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleLogWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ScheduleLogWorkflowRepository extends JpaRepository<ScheduleLogWorkflow, Long> {
    List<ScheduleLogWorkflow> findAllByOrderByIdDesc();

    List<ScheduleLogWorkflow> findByUserIdOrderByIdDesc(String userId);

    ScheduleLogWorkflow findTopByIdScheduleAndStatusOrderById(Long idSchedule, ScheduleLogWorkflow.commandStatus status);
    ScheduleLogWorkflow findTopByIdScheduleAndSequentialAndStatusInOrderById(Long idSchedule, ScheduleLogWorkflow.sequentialStatus seq, Collection<ScheduleLogWorkflow.commandStatus> status);
    List<ScheduleLogWorkflow> findAllByStatusIn(Collection<ScheduleLogWorkflow.commandStatus> status);
}
