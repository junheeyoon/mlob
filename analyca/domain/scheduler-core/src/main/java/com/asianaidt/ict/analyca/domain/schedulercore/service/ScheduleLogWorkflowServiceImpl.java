package com.asianaidt.ict.analyca.domain.schedulercore.service;

import com.asianaidt.ict.analyca.domain.schedulercore.model.Schedule;
import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleLogWorkflow;
import com.asianaidt.ict.analyca.domain.schedulercore.repository.ScheduleLogRepository;
import com.asianaidt.ict.analyca.domain.schedulercore.repository.ScheduleLogWorkflowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleLogWorkflowServiceImpl implements ScheduleLogWorkflowService{

    private final ScheduleLogWorkflowRepository scheduleLogWorkflowRepository;

    public ScheduleLogWorkflowServiceImpl(ScheduleLogWorkflowRepository scheduleLogWorkflowRepository) {
        this.scheduleLogWorkflowRepository = scheduleLogWorkflowRepository;
    }

    @Override
    public List<ScheduleLogWorkflow> findByUserIdOrderByIdDesc(String UserId) {
        return scheduleLogWorkflowRepository.findByUserIdOrderByIdDesc(UserId);
    }

    @Override
    public List<ScheduleLogWorkflow> findAllByOrderByIdDesc() {
        return scheduleLogWorkflowRepository.findAllByOrderByIdDesc();
    }

    @Override
    public ScheduleLogWorkflow saveBySchedule(Schedule schedule){
        ScheduleLogWorkflow log = new ScheduleLogWorkflow();
        log.setIdSchedule(schedule.getId());
        log.setScheduleName(schedule.getScheduleName());
        log.setScheduleDesc(schedule.getScheduleDesc());
        log.setType(schedule.getType());
        log.setUserId(schedule.getUserId());

//        log.setStatus(ScheduleLogWorkflow.commandStatus.READY);
        log.setStatus(ScheduleLogWorkflow.commandStatus.ORDERED);

        if(schedule.getSequential().toString().equalsIgnoreCase("sequential"))
            log.setSequential(ScheduleLogWorkflow.sequentialStatus.SEQUENTIAL);
        else
//            log.setSequential(ScheduleLogWorkflow.sequentialStatus.CONCURRENT);
            log.setSequential(ScheduleLogWorkflow.sequentialStatus.PARALLEL);

        return scheduleLogWorkflowRepository.save(log);
    }


    @Override
    public void updateLogWorkflowStatus(Long id, ScheduleLogWorkflow.commandStatus status){
        Optional<ScheduleLogWorkflow> ifLogWorkflow = scheduleLogWorkflowRepository.findById(id);
        if(ifLogWorkflow.isPresent()){
            ScheduleLogWorkflow logWorkflow = ifLogWorkflow.get();
            logWorkflow.setStatus(status);
            if(status == ScheduleLogWorkflow.commandStatus.FAILED){
                logWorkflow.setDtStart(LocalDateTime.now());
                logWorkflow.setDtEnd(LocalDateTime.now());
            }
            else if(status == ScheduleLogWorkflow.commandStatus.RUNNING)
                logWorkflow.setDtStart(LocalDateTime.now());
            else if(status == ScheduleLogWorkflow.commandStatus.DONE)
                logWorkflow.setDtEnd(LocalDateTime.now());

            scheduleLogWorkflowRepository.save(logWorkflow);
        }
    }

    @Override
    public ScheduleLogWorkflow findTopByIdScheduleAndStatusOrderById(Long idSchedule, ScheduleLogWorkflow.commandStatus status){
        return scheduleLogWorkflowRepository.findTopByIdScheduleAndStatusOrderById(idSchedule, status);
    }

    @Override
    public ScheduleLogWorkflow findTopByIdScheduleAndSequentialAndStatusInOrderById(Long idSchedule,
                                                                       ScheduleLogWorkflow.sequentialStatus seq,
                                                                       Collection<ScheduleLogWorkflow.commandStatus> status){
        return scheduleLogWorkflowRepository.findTopByIdScheduleAndSequentialAndStatusInOrderById(idSchedule, seq, status);
    }

    @Override
    public List<ScheduleLogWorkflow> findAllByStatusIn(Collection<ScheduleLogWorkflow.commandStatus> status){
        return scheduleLogWorkflowRepository.findAllByStatusIn(status);
    }

    @Override
    public List<ScheduleLogWorkflow> saveAll(List<ScheduleLogWorkflow> workflowList){
        return scheduleLogWorkflowRepository.saveAll(workflowList);
    }
}
