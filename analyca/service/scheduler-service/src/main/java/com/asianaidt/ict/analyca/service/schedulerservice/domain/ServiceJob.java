package com.asianaidt.ict.analyca.service.schedulerservice.domain;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;
import java.util.Map;

public class ServiceJob extends QuartzJobBean implements InterruptableJob {

    private volatile boolean toStopFlag = true;

    public final JobPublisher publisher;

    public ServiceJob(JobPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        JobKey key = jobExecutionContext.getJobDetail().getKey();

        publisher.publishToServer(new JobIssued(Long.parseLong(key.getName())));    // name : scd id / group : scd name

        while (toStopFlag) {
            try {
//				System.out.println("Service Job Running... Thread Name :"+ Thread.currentThread().getName());
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Thread : " + Thread.currentThread().getName() + " stopped.");
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        System.out.println("Stopping thread... ");
        toStopFlag = false;
    }
}