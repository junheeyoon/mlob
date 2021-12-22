package com.asianaidt.ict.analyca.domain.schedulercore.service;

import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleStepList;
import com.asianaidt.ict.analyca.domain.schedulercore.repository.ScheduleStepListRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleStepListServiceImpl implements ScheduleStepListService {
    private ScheduleStepListRepository scheduleStepListRepository;

    public ScheduleStepListServiceImpl(ScheduleStepListRepository scheduleStepListRepository) {
        this.scheduleStepListRepository = scheduleStepListRepository;
    }

    @Override
    public List<ScheduleStepList> findAll() {
        return this.scheduleStepListRepository.findAll();
    }

    @Override
    public ScheduleStepList save(ScheduleStepList scheduleStep) {
        return scheduleStepListRepository.save(scheduleStep);
    }

    @Override
    public Optional<ScheduleStepList> findById(Long id) {
        return scheduleStepListRepository.findById(id);
    }

}
