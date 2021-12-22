package com.asianaidt.ict.analyca.service.schedulerservice.service;

import com.asianaidt.ict.analyca.domain.schedulercore.model.Schedule;
import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleStep;
import com.asianaidt.ict.analyca.domain.schedulercore.service.ScheduleService;
import com.asianaidt.ict.analyca.domain.schedulercore.service.ScheduleStepService;
import com.asianaidt.ict.analyca.service.schedulerservice.domain.CronJob;
import com.asianaidt.ict.analyca.service.schedulerservice.domain.ServiceJob;
import com.asianaidt.ict.analyca.service.schedulerservice.domain.WorkflowRunner;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service("jobSchedulerService")
public class JobSchedulerServiceImpl implements JobSchedulerService {

    @Autowired
    ScheduleService scheduleService;
    @Autowired
    ScheduleStepService scheduleStepService;
    @Autowired
    WorkflowRunner workflowRunner;

    @Autowired
    private ApplicationContext context;

    @Autowired
    @Lazy
    SchedulerFactoryBean schedulerFactoryBean;


    @Override
    public List<Schedule> findAllPossibleSchedule() {
//        return scheduleService.findAllPossibleSchedule();
        return scheduleService.findByDeleted(Schedule.deletedStatus.EXIST);
    }

    @Override
    public boolean startJobAll() {
        // clear all schedules exist
        try {
            schedulerFactoryBean.getScheduler().clear();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        List<Schedule> listSchedule = findAllPossibleSchedule();
        LocalDateTime now = LocalDateTime.now();
        for (Schedule schedule : listSchedule) {
//            if (now.isBefore(schedule.getDtEnd())) {    // 종료일이 지나지 않은 경우 실행
//                startJob(schedule);
//            }
            startJob(schedule);
        }
        return true;
    }

    @Override
    public boolean addJob(Schedule schedule) {
//        scheduleService.save(schedule);     // add new schedule
        return startJob(schedule);
    }

    @Override
    public boolean startJob(Schedule schedule) {
        System.out.println("##### JobSchedulerServiceImpl.startJob() : " + schedule.getId() + " / " + schedule.getScheduleName());
//        Date startDate = Date.from( schedule.getDtStart().atZone(ZoneId.of("Asia/Seoul")).toInstant() );
//        Date startDate = Date.from(schedule.getDtStart().atZone(ZoneId.systemDefault()).toInstant());
//        Date endDate = Date.from(schedule.getDtEnd().atZone(ZoneId.systemDefault()).toInstant());
        Date startDate = new Date();
        Date endDate = new Date();
        if (schedule.getType().equals("batch")) {
            scheduleCronJob(schedule, CronJob.class, startDate, endDate, schedule.getCronExpression());     // create job and update state
        } else if (schedule.getType().equals("service")) {
            scheduleServiceJob(schedule, ServiceJob.class, startDate);     // create job and update state
        }

        return true;
    }

    public boolean scheduleCronJob(Schedule schedule, Class<? extends QuartzJobBean> jobClass, Date startDate, Date endDate, String cronExpression) {
        System.out.println("##### JobSchedulerServiceImpl.scheduleCronJob()");

        String jobKey = String.valueOf(schedule.getId());                      // Schedule ID
        String groupKey = schedule.getScheduleName();                                  // Schedule Name
        String triggerKey = String.valueOf(schedule.getId());

        System.out.println("Creating trigger for ID / Name : " + jobKey + " / " + groupKey + " at start_date : " + startDate + " and end_date : " + endDate);

        JobDetail jobDetail = JobUtil.createJob(jobClass, false, context, jobKey, groupKey, endDate, schedule.getSequential());
        Trigger cronTriggerBean = JobUtil.createCronTrigger(triggerKey, startDate, cronExpression, SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW); // endtime 지원 안 함 -> job 내부 처리 -> date 처리 안 함

        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            Date dt = scheduler.scheduleJob(jobDetail, cronTriggerBean);    // 스케줄 등록

            // State check
            if (schedule.getUsed() == Schedule.usedStatus.STOP) {
                JobKey jkey = new JobKey(jobKey, groupKey);
                schedulerFactoryBean.getScheduler().pauseJob(jkey);
            }
//            updateStateAndDeleted(schedule, schedule.getUsed(), schedule.getDeleted());
            updateStateAndDeleted(schedule, schedule.getUsed(), Schedule.deletedStatus.EXIST);

            System.out.println("Job with key jobKey : " + jobKey + " / scheduleName : " + groupKey + " scheduled successfully for date : " + dt);
            return true;
        } catch (SchedulerException e) {
            System.out.println("SchedulerException while scheduling job with key :" + jobKey + " message : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean scheduleServiceJob(Schedule schedule, Class<? extends QuartzJobBean> jobClass, Date startDate) {
        System.out.println("##### JobSchedulerServiceImpl.scheduleSerivceJob()");

        String jobKey = String.valueOf(schedule.getId());                              // Schedule ID
        String groupKey = schedule.getScheduleName();                                  // Schedule Name
        String triggerKey = String.valueOf(schedule.getId());

        System.out.println("Creating trigger for ID / Name : " + jobKey + " / " + groupKey + " at start_date : " + startDate);

        JobDetail jobDetail = JobUtil.createJob(jobClass, false, context, jobKey, groupKey, new Date(), schedule.getSequential());
        Trigger cronTriggerBean = JobUtil.createSingleTrigger(triggerKey, startDate, SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);

        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            Date dt = scheduler.scheduleJob(jobDetail, cronTriggerBean);    // 스케줄 등록
            updateStateAndDeleted(schedule, Schedule.usedStatus.RUNNING, schedule.getDeleted());
            System.out.println("Job with key jobKey : " + jobKey + " / scheduleName : " + groupKey + " scheduled successfully for date : " + dt);
            return true;
        } catch (SchedulerException e) {
            System.out.println("SchedulerException while scheduling job with key :" + jobKey + " message : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean updateJob(Schedule newSchedule) {
        Schedule oldSchedule = scheduleService.findById(newSchedule.getId()).orElseGet(Schedule::new);

        String jobKey = String.valueOf(oldSchedule.getId());
        String groupKey = oldSchedule.getScheduleName();

        System.out.println("##### JobSchedulerServiceImpl.updateJob() : " + jobKey + " / " + groupKey);

        try {
            scheduleService.save(newSchedule);
            deleteJob(oldSchedule);
            startJob(newSchedule);
            System.out.println("Trigger associated with jobKey :" + jobKey + " rescheduled successfully.");

            return true;
        } catch (Exception e) {
            scheduleService.save(oldSchedule);

            System.out.println("SchedulerException while updating cron job with key : " + jobKey + " message : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean stopJob(Schedule schedule) {
        String jobKey = String.valueOf(schedule.getId());
        String groupKey = schedule.getScheduleName();
        System.out.println("##### JobSchedulerServiceImpl.stopJob() : " + jobKey + " / " + groupKey);

        JobKey jkey = new JobKey(jobKey, groupKey);
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.interrupt(jkey);
            updateStateAndDeleted(schedule, Schedule.usedStatus.STOP, schedule.getDeleted());
            return true;
        } catch (SchedulerException e) {
            System.out.println("SchedulerException while stopping job. error message :" + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean pauseJob(Schedule schedule) {
        String jobKey = String.valueOf(schedule.getId());
        String groupKey = schedule.getScheduleName();
        System.out.println("##### JobSchedulerServiceImpl.pauseJob() : " + jobKey + " / " + groupKey);

        JobKey jkey = new JobKey(jobKey, groupKey);
        try {
            schedulerFactoryBean.getScheduler().pauseJob(jkey);
            updateStateAndDeleted(schedule, Schedule.usedStatus.STOP, schedule.getDeleted());
            System.out.println("Job with jobKey : " + jobKey + " / " + groupKey + " paused succesfully.");
            return true;
        } catch (SchedulerException e) {
            System.out.println("SchedulerException while pausing job with key :" + jobKey + " / " + groupKey + " message :" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean resumeJob(Schedule schedule) {
        String jobKey = String.valueOf(schedule.getId());
        String groupKey = schedule.getScheduleName();
        System.out.println("##### JobSchedulerServiceImpl.resumeJob() : " + jobKey + " / " + groupKey);

        JobKey jKey = new JobKey(jobKey, groupKey);
        try {
            schedulerFactoryBean.getScheduler().resumeJob(jKey);
            updateStateAndDeleted(schedule, Schedule.usedStatus.RUNNING, schedule.getDeleted());
            System.out.println("Job with jobKey : " + jobKey + " / " + groupKey + " resumed succesfully.");
            return true;
        } catch (SchedulerException e) {
            System.out.println("SchedulerException while resuming job with key :" + jobKey + " / " + groupKey + " message :" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteJob(Schedule schedule) {
        String jobKey = String.valueOf(schedule.getId());
        String groupKey = schedule.getScheduleName();
        System.out.println("##### JobSchedulerServiceImpl.deleteJob() : " + jobKey + " / " + groupKey);

        JobKey jkey = new JobKey(jobKey, groupKey);
        try {
            boolean status = schedulerFactoryBean.getScheduler().deleteJob(jkey);
            updateStateAndDeleted(schedule, Schedule.usedStatus.STOP, Schedule.deletedStatus.DELETED);
            System.out.println("Job with jobKey : " + jobKey + " deleted with status : " + status);
            return status;
        } catch (SchedulerException e) {
            System.out.println("SchedulerException while deleting job with key :" + jobKey + " message :" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isJobWithNamePresent(Long scheduleId, String scheduleName) {

        try {
            String jobKey = String.valueOf(scheduleId);
            String groupKey = scheduleName;
            JobKey jKey = new JobKey(jobKey, groupKey);
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            if (scheduler.checkExists(jKey)) {
                return true;
            }
        } catch (SchedulerException e) {
            System.out.println("SchedulerException while checking job with name and group exist : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Schedule save(Schedule schedule) {
        return scheduleService.save(schedule);
    }

    @Override
    public Optional<Schedule> findById(Long id) {
        return scheduleService.findById(id);
    }

    @Override
    public Optional<ScheduleStep> findStepById(Long id) {
        return scheduleStepService.findById(id);
    }

    @Override
    public List<Schedule> findByDeleted(Schedule.deletedStatus deleted) {
        return scheduleService.findByDeleted(deleted);
    }

    @Override
    public Schedule updateUsed(Long id, Schedule.usedStatus used) {
        Schedule schedule = findById(id).orElse(new Schedule());
        if (schedule.getUsed() == Schedule.usedStatus.STOP && used == Schedule.usedStatus.READY) {
            // 기존에 stop 상태였으면 변경하지 않음
            return schedule;
        }
        return updateStateAndDeleted(schedule, used, schedule.getDeleted());
    }

    public Schedule updateStateAndDeleted(Schedule schedule, Schedule.usedStatus used, Schedule.deletedStatus deleted) {
        schedule.setUsed(used);
        schedule.setDeleted(deleted);
        return scheduleService.save(schedule);
    }

    @Override
    public boolean startJobOnce(Schedule schedule) {
        try {
            workflowRunner.requestRun("parallel", schedule.getId());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
