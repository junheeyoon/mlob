package com.asianaidt.ict.analyca.webserver.service;

import com.asianaidt.ict.analyca.service.schedulerservice.domain.JobIssued;
import com.asianaidt.ict.analyca.service.schedulerservice.domain.JobRemoveIssued;
import com.asianaidt.ict.analyca.service.schedulerservice.domain.JobStepIssued;
import com.asianaidt.ict.analyca.service.schedulerservice.domain.JobWorkflowIssued;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service("jobEventListener")
public class JobEventListenerImpl implements JobEventListener {

    @Autowired
    ScheduleRunService scheduleRunService;

    @Override
    @EventListener
    public void onTaskSchedulerEvent(JobIssued issued) {
        System.out.println("##### JobEventListenerImpl.onTaskSchedulerEvent() : " + issued.getJobId());
        scheduleRunService.scheduleRun(issued.getJobId());
    }

    @Override
    @EventListener
    public void onTaskSchedulerEventWorkflow(JobWorkflowIssued issued) {
        //System.out.println("##### JobEventListenerImpl.onTaskSchedulerEvent() : " + issued.getJobId());
        //scheduleRunService.scheduleRun(issued.getJobId());
        System.out.println("onTaskSchedulerEventWorkflow : " + issued.getJobId());
    }

    @Override
    @EventListener
    public void onTaskSchedulerEventStep(JobStepIssued issued) {
//        System.out.println("##### JobEventListenerImpl.onTaskSchedulerEvent() : " + issued.getJobId());
//        scheduleRunService.scheduleRun(issued.getJobId());
        System.out.println("onTaskSchedulerEventStep : " + issued.getIdStep());
        scheduleRunService.scheduleStepRun(issued);
    }

    @Override
    @EventListener
    public void onTaskSchedulerEventRemove(JobRemoveIssued issued) {
//        System.out.println("##### JobEventListenerImpl.onTaskSchedulerEvent() : " + issued.getJobId());
//        scheduleRunService.scheduleRun(issued.getJobId());
        System.out.println("onTaskSchedulerEventRemove : " + issued.getIdStep());
        scheduleRunService.scheduleStepContainerRemove(issued);
    }
}
