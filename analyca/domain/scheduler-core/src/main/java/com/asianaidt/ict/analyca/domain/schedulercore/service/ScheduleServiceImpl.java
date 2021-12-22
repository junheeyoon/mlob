package com.asianaidt.ict.analyca.domain.schedulercore.service;

import com.asianaidt.ict.analyca.domain.schedulercore.model.Schedule;
import com.asianaidt.ict.analyca.domain.schedulercore.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    private ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public Schedule save(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    @Override
    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    @Override
    public Optional<Schedule> findById(Long id) {
        return scheduleRepository.findById(id);
    }

    @Override
    public Schedule findByScheduleName(String s) {
        return scheduleRepository.findByScheduleName(s);
    }

    @Override
    public List<Schedule> findByDeleted(Schedule.deletedStatus deleted) {
        return scheduleRepository.findByDeleted(deleted);
    }

    @Override
    public List<Schedule> findAllPossibleSchedule() {
        LocalDateTime now = LocalDateTime.now();
        return scheduleRepository.findByDtEndAfterAndDeleted(now, Schedule.deletedStatus.EXIST);
    }
}
