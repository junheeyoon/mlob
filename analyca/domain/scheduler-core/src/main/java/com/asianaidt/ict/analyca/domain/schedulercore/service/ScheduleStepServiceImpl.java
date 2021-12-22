package com.asianaidt.ict.analyca.domain.schedulercore.service;

import com.asianaidt.ict.analyca.domain.schedulercore.model.Schedule;
import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleStep;
import com.asianaidt.ict.analyca.domain.schedulercore.repository.ScheduleStepRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleStepServiceImpl implements ScheduleStepService {
    private ScheduleStepRepository scheduleStepRepository;

    public ScheduleStepServiceImpl(ScheduleStepRepository scheduleStepRepository) {
        this.scheduleStepRepository = scheduleStepRepository;
    }

    @Override
    public ScheduleStep save(ScheduleStep scheduleStep) {
        return scheduleStepRepository.save(scheduleStep);
    }

    @Override
    public void saveAll(List<ScheduleStep> steps) {
        scheduleStepRepository.saveAll(steps);
    }

    @Override
    public Optional<ScheduleStep> findById(Long id) {
        return scheduleStepRepository.findById(id);
    }

    @Override
    public List<ScheduleStep> findByIdSchedule(Schedule i) {
        return scheduleStepRepository.findByIdSchedule(i);
    }

    @Override
    public void deleteAllByIdSchedule(Schedule i) {
        scheduleStepRepository.deleteAllByIdSchedule(i);
    }


}
