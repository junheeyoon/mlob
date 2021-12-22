package com.asianaidt.ict.analyca.service.schedulerservice.service;

import com.asianaidt.ict.analyca.domain.schedulercore.model.*;
import com.asianaidt.ict.analyca.domain.schedulercore.service.ScheduleLogStepService;
import com.asianaidt.ict.analyca.domain.schedulercore.service.ScheduleLogWorkflowService;
import com.asianaidt.ict.analyca.domain.schedulercore.service.ScheduleRunningService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobLogServiceImpl implements JobLogService{

    private final ScheduleLogWorkflowService scheduleLogWorkflowService;
    private final ScheduleLogStepService scheduleLogStepService;
    private final ScheduleRunningService scheduleRunningService;

    @Override
    public ScheduleLogWorkflow saveLogSchedule(Schedule schedule) {
        return scheduleLogWorkflowService.saveBySchedule(schedule);
    }

    @Override
    public List<ScheduleLogStep> saveLogStepByWorkflow(ScheduleLogWorkflow logWorkflow, List<ScheduleStep> stepList) {
        List<ScheduleLogStep> logStepList = new ArrayList<>();
        for(ScheduleStep step : stepList){
            ScheduleLogStep logStep = new ScheduleLogStep();
            logStep.setContainerId("");
            logStep.setIdStep(step.getId());
            logStep.setStepOrder(step.getStepOrder());
            logStep.setIdWorkflow(logWorkflow.getId());
            logStep.setScheduleName(logWorkflow.getScheduleName());
            logStep.setStepName(step.getStepName());
            logStep.setUserId(logWorkflow.getUserId());
//            logStep.setStatus(ScheduleLogStep.commandStatus.READY);
            logStep.setStatus(ScheduleLogStep.commandStatus.ORDERED);
            logStepList.add(logStep);
        }
        return scheduleLogStepService.saveAll(logStepList);
    }

    @Override
    public List<ScheduleRunning> saveRunningByStep(ScheduleLogWorkflow logWorkflow, List<ScheduleStep> stepList) {
        List<ScheduleRunning> runningList = new ArrayList<>();
        for(ScheduleStep step : stepList) {
            ScheduleRunning running = new ScheduleRunning();
            running.setScheduleName(logWorkflow.getScheduleName());
            running.setScheduleDesc(logWorkflow.getScheduleDesc());
            running.setStepName(step.getStepName());
            running.setStepOrder(step.getStepOrder());
            running.setIdWorkflow(logWorkflow.getId());
            running.setType(logWorkflow.getType());
            running.setUserId(logWorkflow.getUserId());
            if (logWorkflow.getSequential() == ScheduleLogWorkflow.sequentialStatus.SEQUENTIAL)
                running.setSequential(ScheduleRunning.sequentialStatus.SEQUENTIAL);
            else
//                running.setSequential(ScheduleRunning.sequentialStatus.CONCURRENT);
                running.setSequential(ScheduleRunning.sequentialStatus.PARALLEL);
            running.setStatus(ScheduleRunning.commandStatus.READY);
            running.setImage(step.getImage());

            runningList.add(running);
        }
        return scheduleRunningService.saveAll(runningList);
    }

    @Override
    public List<Long> saveRunning(Schedule schedule) {
        return null;
    }

    @Override
    public void updateContainerId(Long idLogStep, String containerId) {
        // ???????????????????????????????????????
        scheduleLogStepService.updateContainerId(idLogStep, containerId);
    }

    @Override
    public ScheduleLogStep findById(Long id) {
        return scheduleLogStepService.findById(id);
    }

    @Override
    public void updateLogStepDtStart(Long id) {
        scheduleLogStepService.updateLogStepDtStart(id);
    }
    @Override
    public void updateLogStepStatus(Long id, ScheduleLogStep.commandStatus status) {
        scheduleLogStepService.updateLogStepStatus(id, status);
    }

    @Override
    public void updateLogWorkflowStatus(Long id, ScheduleLogWorkflow.commandStatus status){
        scheduleLogWorkflowService.updateLogWorkflowStatus(id, status);
    }

    @Override
    public void updateAllLogStepStatusByIdWorkflow(Long idWorkflow, ScheduleLogStep.commandStatus status){
        scheduleLogStepService.updateAllLogStepStatusByIdWorkflow(idWorkflow, status);
    }

    @Override
    public void updateRunningStatus(Long idLogStep, ScheduleRunning.commandStatus status) {
        ScheduleLogStep logStep = scheduleLogStepService.findById(idLogStep);
        scheduleRunningService.updateRunningStatusByIdWorkflowAndStepOrder(logStep.getIdWorkflow(), logStep.getStepOrder(), status);
    }

    @Override
    public void deleteRunningByIdWorkflow(Long idWorkflow){
        scheduleRunningService.deleteByIdWorkflow(idWorkflow);
    }

    @Override
    public ScheduleLogWorkflow serialStart(Long idWorkflow){
        List status = new ArrayList<ScheduleLogWorkflow.commandStatus>();
        status.add(ScheduleLogWorkflow.commandStatus.READY);
        status.add(ScheduleLogWorkflow.commandStatus.RUNNING);
        status.add(ScheduleLogWorkflow.commandStatus.ORDERED);
        return scheduleLogWorkflowService.findTopByIdScheduleAndSequentialAndStatusInOrderById(idWorkflow, ScheduleLogWorkflow.sequentialStatus.SEQUENTIAL, status);
    }

    /**
     * 서버 부팅시 실행
     * - select 조건 : id_workflow & step_order
     * - select 수행 테이블 : schedule_log_step, schedule_running
     * - 수행 :
     *      - schedule_log_workflow, schedule_log_step 테이블에서 status == ( RUNNING || READY || ORDERED ) 인 행 -> CANCEL
     *      - schedule_running 테이블 전체 삭제
     **/
    @Override
    public void initLogStates(){
        scheduleLogWorkflowCancel();
        scheduleLogStepCancel();
        scheduleRunningService.deleteAll();
    }
    public void scheduleLogWorkflowCancel(){
        List status = new ArrayList<ScheduleLogWorkflow.commandStatus>();
        status.add(ScheduleLogWorkflow.commandStatus.READY);
        status.add(ScheduleLogWorkflow.commandStatus.RUNNING);
        status.add(ScheduleLogWorkflow.commandStatus.ORDERED);

        List<ScheduleLogWorkflow> workflowList = scheduleLogWorkflowService.findAllByStatusIn(status);
        for(ScheduleLogWorkflow workflow : workflowList){
//            workflow.setStatus(ScheduleLogWorkflow.commandStatus.FAILED);
            workflow.setStatus(ScheduleLogWorkflow.commandStatus.CANCELED);
        }
        // save all
        scheduleLogWorkflowService.saveAll(workflowList);
    }
    public void scheduleLogStepCancel(){
        List status = new ArrayList<ScheduleLogStep.commandStatus>();
        status.add(ScheduleLogStep.commandStatus.READY);
        status.add(ScheduleLogStep.commandStatus.RUNNING);
        status.add(ScheduleLogStep.commandStatus.ORDERED);

        List<ScheduleLogStep> stepList = scheduleLogStepService.findAllByStatusIn(status);
        for(ScheduleLogStep step : stepList){
//            step.setStatus(ScheduleLogStep.commandStatus.FAILED);
            step.setStatus(ScheduleLogStep.commandStatus.CANCELED);
        }
        // save all
        scheduleLogStepService.saveAll(stepList);
    }
}
