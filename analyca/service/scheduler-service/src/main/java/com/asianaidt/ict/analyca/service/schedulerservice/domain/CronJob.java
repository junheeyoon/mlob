package com.asianaidt.ict.analyca.service.schedulerservice.domain;

import org.quartz.*;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class CronJob extends QuartzJobBean implements InterruptableJob {

    private volatile boolean toStopFlag = true;

    public final JobPublisher publisher;
    public final WorkflowRunner workflowRunner;

    public CronJob(JobPublisher publisher, WorkflowRunner workflowRunner) {
        this.publisher = publisher;
        this.workflowRunner = workflowRunner;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        JobKey key = jobExecutionContext.getJobDetail().getKey();
//		Long id = (Long) jobDataMap.get("scheduleId");
//		publisher.publishToServer(new JobIssued(Long.parseLong(key.getName())));	// name : scd id / group : scd name

        String type = (String) jobDataMap.get("sequential");
        if (type.equals("sequential")) {
            workflowRunner.requestRun("sequential", Long.parseLong(key.getName()));
        } else {
            workflowRunner.requestRun("parallel", Long.parseLong(key.getName()));
        }

//		Date endDate = (Date) jobDataMap.get("endDate");
//		Timer timer = new Timer();
//		timer.schedule(new EndTimeTask(), endDate);
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        System.out.println("Stopping thread... ");
        toStopFlag = false;
    }
}