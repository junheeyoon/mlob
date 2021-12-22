package com.asianaidt.ict.analyca.domain.schedulercore.service;

import com.asianaidt.ict.analyca.domain.schedulercore.model.Agent;
import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleLog;
import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleRunning;

import java.util.List;

public interface ScheduleLogService {
    List<ScheduleLog> findAll();

    //    void saveFromMessageRes(MessageRes msg);
    ScheduleLog save(ScheduleLog scheduleLog);

    List<ScheduleLog> findByUserIdOrderByIdDesc(String UserId);

    List<ScheduleLog> findAllByOrderByIdDesc();
}
