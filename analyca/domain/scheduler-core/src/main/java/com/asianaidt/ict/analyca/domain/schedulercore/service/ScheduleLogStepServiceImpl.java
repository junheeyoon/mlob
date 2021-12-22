package com.asianaidt.ict.analyca.domain.schedulercore.service;

import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleLogStep;
import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleLogWorkflow;
import com.asianaidt.ict.analyca.domain.schedulercore.repository.ScheduleLogStepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleLogStepServiceImpl implements ScheduleLogStepService{

    private final ScheduleLogStepRepository scheduleLogStepRepository;

    public ScheduleLogStepServiceImpl(ScheduleLogStepRepository scheduleLogStepRepository) {
        this.scheduleLogStepRepository = scheduleLogStepRepository;
    }


    @Override
    public List<ScheduleLogStep> findByIdWorkflow(Long i) {
        return scheduleLogStepRepository.findByIdWorkflow(i);
    }


    @Override
    public List<ScheduleLogStep> findByUserIdOrderByIdDesc(String UserId) {
        return scheduleLogStepRepository.findByUserIdOrderByIdDesc(UserId);
    }

    @Override
    public List<ScheduleLogStep> findAllByOrderByIdDesc() {
        return scheduleLogStepRepository.findAllByOrderByIdDesc();
    }

    @Override
    public List<ScheduleLogStep> saveAll(List<ScheduleLogStep> logStepList){
        return scheduleLogStepRepository.saveAll(logStepList);
    };

    @Override
    public ScheduleLogStep findById(Long id){
        return scheduleLogStepRepository.findById(id).orElse(new ScheduleLogStep());
    }

    @Override
    public void updateLogStepDtStart(Long id) {
        System.out.println("ScheduleLogStepServiceImpl.updateLogStepDtStart");
        Optional<ScheduleLogStep> ifStep = scheduleLogStepRepository.findById(id);
        if(ifStep.isPresent()) {
            ScheduleLogStep step = ifStep.get();
            step.setDtStart(LocalDateTime.now());
            scheduleLogStepRepository.save(step);
        }
    }

    @Override
    public void updateLogStepStatus(Long id, ScheduleLogStep.commandStatus status) {
        System.out.println("ScheduleLogStepServiceImpl.updateLogStepStatus");
        Optional<ScheduleLogStep> ifStep = scheduleLogStepRepository.findById(id);
        if(ifStep.isPresent()) {
            ScheduleLogStep step = ifStep.get();
            if (step.getContainerId() == null)
                step.setContainerId("");

            if (status == ScheduleLogStep.commandStatus.FAILED) {
                step.setDtEnd(LocalDateTime.now());
            }
//            else if(status == ScheduleLogStep.commandStatus.RUNNING) {
//                step.setDtCommand(LocalDateTime.now());
//            }
            else if(status == ScheduleLogStep.commandStatus.DONE) {
                step.setDtEnd(LocalDateTime.now());
            }
            step.setStatus(status);
            scheduleLogStepRepository.save(step);
        }
    }

    @Override
    public void updateAllLogStepStatusByIdWorkflow(Long idWorkflow, ScheduleLogStep.commandStatus status) {
        System.out.println("ScheduleLogStepServiceImpl.updateAllLogStepStatusByIdWorkflow");
        List<ScheduleLogStep> stepList = scheduleLogStepRepository.findAllByIdWorkflow(idWorkflow);
        for(ScheduleLogStep step : stepList){
            step.setStatus(status);
        }
        scheduleLogStepRepository.saveAll(stepList);
    }


    @Override
    public void updateContainerId(Long id, String contId){
        System.out.println("##### ScheduleLogStepServiceImpl.updateContainerId");
        Optional<ScheduleLogStep> ifStep = scheduleLogStepRepository.findById(id);
        if(ifStep.isPresent()){
            ScheduleLogStep step = ifStep.get();
            if(contId.length() > 12)
                step.setContainerId(contId.substring(0, 12));
            else
                step.setContainerId(contId);
            step.setStatus(ScheduleLogStep.commandStatus.RUNNING);
            scheduleLogStepRepository.save(step);
        }
    }

    @Override
    public List<ScheduleLogStep> findAllByStatusIn(Collection<ScheduleLogStep.commandStatus> status){
        return scheduleLogStepRepository.findAllByStatusIn(status);
    }

}

