package com.asianaidt.ict.analyca.domain.schedulercore.repository;

import com.asianaidt.ict.analyca.domain.schedulercore.model.Schedule;
import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleRunning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ScheduleRunningRepository extends JpaRepository<ScheduleRunning, Long> {

    @Transactional
        //void deleteScheduleRunningByAgentAndPid(String agent, Long pid);

        //    @Query(value = "SELECT agent, GROUP_CONCAT(id SEPARATOR ',' ) ids, GROUP_CONCAT(pid SEPARATOR ',' ) pids AS pids FROM schedule_running GROUP BY agent", nativeQuery = true)
        //    @Query(value = "SELECT id, agent, pid FROM schedule_running GROUP BY agent", nativeQuery = true)
    /*
    @Query(value = "SELECT agent, GROUP_CONCAT( CONCAT (id,':',pid) SEPARATOR ',' ) AS pids FROM schedule_running GROUP BY agent", nativeQuery = true)
    List<ScheduleRunning.pidGroup> findPidsGroupByAgent();
    */
    List<ScheduleRunning> findByUserId(String userId);

    //List<ScheduleRunning> findByIdSchedule(Long id);

    ScheduleRunning findByIdWorkflowAndStepOrder(Long idWorkflow, Long stepOrder);

    List<ScheduleRunning> findAll();

    @Transactional
    @Modifying
    void deleteByIdWorkflow(Long idWorkflow);

    @Transactional
    @Modifying
    void deleteAll();

}
