package com.asianaidt.ict.analyca.domain.schedulercore.service;

import com.asianaidt.ict.analyca.domain.schedulercore.model.Agent;
import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleLog;
import com.asianaidt.ict.analyca.domain.schedulercore.repository.ScheduleLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleLogServiceImpl implements ScheduleLogService {
    @Autowired
    private ScheduleLogRepository scheduleLogRepository;

    public ScheduleLogServiceImpl(ScheduleLogRepository scheduleLogRepository) {
        this.scheduleLogRepository = scheduleLogRepository;
    }

    @Override
    public List<ScheduleLog> findAll() {
        return scheduleLogRepository.findAll();
    }

    @Override
    public ScheduleLog save(ScheduleLog scheduleLog) {
        return scheduleLogRepository.save(scheduleLog);
    }

    @Override
    public List<ScheduleLog> findByUserIdOrderByIdDesc(String UserId) {
        return scheduleLogRepository.findByUserIdOrderByIdDesc(UserId);
    }

    @Override
    public List<ScheduleLog> findAllByOrderByIdDesc() {
        return scheduleLogRepository.findAllByOrderByIdDesc();
    }


//    public void saveFromMessageRes(MessageRes msg){
//        ScheduleLog log = scheduleLogRepository.findScheduleLogById(msg.getId());
//
//        scheduleLogRepository.save(log);
//    }

}
