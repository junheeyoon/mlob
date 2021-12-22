package com.asianaidt.ict.analyca.domain.schedulercore.service;

import com.asianaidt.ict.analyca.domain.schedulercore.model.Agent;
import com.asianaidt.ict.analyca.domain.schedulercore.model.Schedule;
import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleRunning;
import com.asianaidt.ict.analyca.domain.schedulercore.repository.ScheduleRunningRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("scheduleRunningService")
public class ScheduleRunningServiceImpl implements ScheduleRunningService {

    private ScheduleRunningRepository scheduleRunningRepository;

    public ScheduleRunningServiceImpl(ScheduleRunningRepository scheduleRunningRepository) {
        this.scheduleRunningRepository = scheduleRunningRepository;
    }

    @Override
    public ScheduleRunning save(ScheduleRunning scheduleRunning) {
        return scheduleRunningRepository.save(scheduleRunning);
    }

    @Override
    public void deleteById(Long id) {
        scheduleRunningRepository.deleteById(id);
    }

    @Override
    public Optional<ScheduleRunning> findById(Long id) {
        return scheduleRunningRepository.findById(id);
    }

    @Override
    public List<ScheduleRunning> findAll() {
        return scheduleRunningRepository.findAll();
    }

    @Override
    public List<ScheduleRunning> findByUserId(String UserId) {
        return scheduleRunningRepository.findByUserId(UserId);
    }

    @Override
    public List<ScheduleRunning> saveAll(List<ScheduleRunning> runningList) {
        return scheduleRunningRepository.saveAll(runningList);
    }

    @Override
    public void updateRunningStatusByIdWorkflowAndStepOrder(Long idWorkflow, Long stepOrder, ScheduleRunning.commandStatus status) {
        ScheduleRunning running = scheduleRunningRepository.findByIdWorkflowAndStepOrder(idWorkflow, stepOrder);
        running.setStatus(status);
        scheduleRunningRepository.save(running);
    }

    @Override
    public void deleteByIdWorkflow(Long idWorkflow){
        scheduleRunningRepository.deleteByIdWorkflow(idWorkflow);
    }

    @Override
    public void deleteAll(){
        scheduleRunningRepository.deleteAll();
    }

}
