package com.asianaidt.ict.analyca.scheduler;

import com.asianaidt.ict.analyca.service.schedulerservice.service.JobLogService;
import com.asianaidt.ict.analyca.service.schedulerservice.service.JobSchedulerService;
import com.asianaidt.ict.analyca.webserver.service.ScheduleRunService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.text.ParseException;

@Component
public class TaskScheduler {
    private final JobSchedulerService jobSchedulerService;
    private final JobLogService jobLogService;
    private final ScheduleRunService scheduleRunService;

    @Value("${scheduler.used}")
    private String schedulerUsed;

    public TaskScheduler(JobSchedulerService jobSchedulerService, JobLogService jobLogService, ScheduleRunService scheduleRunService) {
        this.jobSchedulerService = jobSchedulerService;
        this.jobLogService = jobLogService;
        this.scheduleRunService = scheduleRunService;
    }

    /**
     * 웹서버 실행시
     * - RUNNING 테이블 전체 삭제
     * - SCHEDULE_LOG_WORKFLOW, SCHEDULE_LOG_STEP 의 READY, RUNNING state 를 모두 CANCEL 처리
     * - SCHEDULE_LIST 의 EXIST 스케줄 전체 실행
     */
    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) throws ParseException {
        System.out.println("##### On ContextRefreshedEvent ");

        // application.yml -> ${scheduler.used} 값으로 설정
        if(!schedulerUsed.equalsIgnoreCase("true"))
            return;
        jobLogService.initLogStates();
        jobSchedulerService.startJobAll();
    }
}
