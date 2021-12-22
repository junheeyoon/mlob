package com.asianaidt.ict.analyca.service.schedulerservice.domain;

import com.asianaidt.ict.analyca.domain.containerdomain.service.ContainerStatusService;
import com.asianaidt.ict.analyca.domain.schedulercore.model.Schedule;
import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleLogStep;
import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleLogWorkflow;
import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleStep;
import com.asianaidt.ict.analyca.domain.schedulercore.service.ScheduleService;
import com.asianaidt.ict.analyca.domain.schedulercore.service.ScheduleStepService;
import com.asianaidt.ict.analyca.service.schedulerservice.service.JobLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkflowRunner {

    private final ScheduleService scheduleService;          // id로 스케줄 검색
    private final ScheduleStepService scheduleStepService;  // 해당 스케줄의 step 목록 가져오기
    private final JobLogService jobLogService;              // 로그
    private final JobPublisher publisher;
    private final ContainerStatusService containerStatusService;    // 컨테이너 ID query

//    public void requestRun(String runType, Long idSchedule) {
//
//        Long idLogSchedule;
//
//        Schedule scd = scheduleService.findById(idSchedule).orElse(new Schedule());
//        List<ScheduleStep> steps = scheduleStepService.findByIdSchedule(scd);
//
//        // Insert Workflow Execution Log
//        ScheduleLogWorkflow logWorkflow = jobLogService.saveLogSchedule(scd);       // ORDERED 상태로 등록
//        List<ScheduleLogStep> logStep = jobLogService.saveLogStepByWorkflow(logWorkflow, steps);    // ORDERED 상태로 등록
//        jobLogService.saveRunningByStep(logWorkflow, steps);
//
//        idLogSchedule = logWorkflow.getId();
//
//        Thread t = new Thread(new WorkflowThread(runType, idSchedule, logStep, idLogSchedule, jobLogService, publisher, containerStatusService));
//        t.start();
//    }
    public void requestRun(String runType, Long idSchedule) {

        Schedule scd = scheduleService.findById(idSchedule).orElse(new Schedule());
        List<ScheduleStep> steps = scheduleStepService.findByIdSchedule(scd);

        Thread t = new Thread(new WorkflowThread(runType, idSchedule, scd, steps, jobLogService, publisher, containerStatusService));
        t.start();
    }
}
