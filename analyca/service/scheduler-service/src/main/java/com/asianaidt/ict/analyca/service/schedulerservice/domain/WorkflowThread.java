package com.asianaidt.ict.analyca.service.schedulerservice.domain;

import com.asianaidt.ict.analyca.domain.containerdomain.service.ContainerStatusService;
import com.asianaidt.ict.analyca.domain.schedulercore.model.*;
import com.asianaidt.ict.analyca.service.schedulerservice.service.JobLogService;
import lombok.SneakyThrows;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import java.util.ArrayList;
import java.util.List;

public class WorkflowThread implements Runnable {
    private final JobLogService jobLogService;              // 로그
    private final JobPublisher publisher;
    private final ContainerStatusService containerStatusService;

    public Long idSchedule;
    public Schedule schedule;
    public List<ScheduleStep> steps;

    public ScheduleLogWorkflow logWorkflow;
    public List<ScheduleLogStep> logStepList;
    public Long idWorkflow;
    public String runType;

    public WorkflowThread(String runType, Long idSchedule, Schedule schedule, List<ScheduleStep> steps, JobLogService jobLogService, JobPublisher publisher, ContainerStatusService containerStatusService) {
        this.runType = runType;
        this.idSchedule = idSchedule;
        this.schedule = schedule;
        this.steps = steps;

        this.jobLogService = jobLogService;
        this.publisher = publisher;
        this.containerStatusService = containerStatusService;
    }

    public void logInitOrdered(){
        // Insert Workflow Execution Log
        // 시작 요청시 로그 삽입 (ORDERED)
        this.logWorkflow = jobLogService.saveLogSchedule(this.schedule);     // ORDERED 상태로 등록
        this.logStepList = jobLogService.saveLogStepByWorkflow(logWorkflow, this.steps);         // ORDERED 상태로 등록
        this.idWorkflow = logWorkflow.getId();
    }
    public void logInitStart(){
        // Update Workflow Execution Log
        // 실제 시작시 상태 변경
        jobLogService.updateLogWorkflowStatus(this.idWorkflow, ScheduleLogWorkflow.commandStatus.RUNNING);       // RUNNING 상태로 등록
        jobLogService.updateAllLogStepStatusByIdWorkflow(this.idWorkflow, ScheduleLogStep.commandStatus.READY);  // READY steps
        jobLogService.saveRunningByStep(this.logWorkflow, this.steps);
    }

    @SneakyThrows
    @Override
    public void run() {
        logInitOrdered();

        // 순차 실행인 경우, 동일 workflow 의 가장 오래된 ready 객체를 실행함
        if(runType.equalsIgnoreCase("sequential")){
            while(true){
                ScheduleLogWorkflow top = jobLogService.serialStart(idSchedule);        // 순차 실행 작업 -> 실행 여부 확인
                if(top != null && top.getId() == idWorkflow && (top.getStatus() == ScheduleLogWorkflow.commandStatus.ORDERED || top.getStatus() == ScheduleLogWorkflow.commandStatus.READY))
                    break;
                Thread.sleep(1000 * 10);
            }
        }

        logInitStart();

        for(ScheduleLogStep step : this.logStepList){
            // Set Start Time Now
            jobLogService.updateLogStepDtStart(step.getId());

            // LOCAL DEMO ####################################################################
//            if(!stepExitCheckRequestDemo(step.getId())){    // exited with non-zero exitcode
            // LOCAL DEMO ####################################################################

            // REAL ##########################################################################
            stepStartRequest(step.getIdStep(), step.getId());       // request server to start step
            if(!stepExitCheckRequest(step.getId())){    // exited with non-zero exitcode
            // REAL ##########################################################################

                jobLogService.updateLogStepStatus(step.getId(), ScheduleLogStep.commandStatus.FAILED);
                jobLogService.updateLogWorkflowStatus(this.idWorkflow, ScheduleLogWorkflow.commandStatus.FAILED);
                jobLogService.deleteRunningByIdWorkflow(this.idWorkflow);            // running 삭제
                return;
            } else {    // step done
                jobLogService.updateLogStepStatus(step.getId(), ScheduleLogStep.commandStatus.DONE);
                jobLogService.updateRunningStatus(step.getId(), ScheduleRunning.commandStatus.DONE);
            }
        }
        // all exited 0
        jobLogService.updateLogWorkflowStatus(this.idWorkflow, ScheduleLogWorkflow.commandStatus.DONE);
        jobLogService.deleteRunningByIdWorkflow(this.idWorkflow);            // running 삭제
    }

    public void stepStartRequest(Long idStep, Long idLogStep) {
        // 웹서버에 실행요청
        // JobEventListenerImpl.onTaskSchedulerEvent() 실행
        publisher.publishToServer(new JobStepIssued(idStep, idLogStep));
    }
    public void stepRemoveContainer(Long idStep, String contId) throws InterruptedException {
        List<String> st = findContainerStatus(contId);
        String container_name = st.get(2);
        System.out.println("########################################");
        System.out.println("##### WorkflowThread.stepRemoveContainer : container_id   - " + contId);
        System.out.println("##### WorkflowThread.stepRemoveContainer : container_name - " + container_name);
        publisher.publishToServer((new JobRemoveIssued(idStep, container_name)));
    }

    public Boolean stepExitCheckRequest(Long idLogStep) throws InterruptedException {
        // step container id check query
        String contId = "";
        int cnt = 0;
        ScheduleLogStep logStep = null;
        while(true) {       // check if start
            logStep = jobLogService.findById(idLogStep);
            contId = logStep.getContainerId();
            if(contId.length() > 12) {
                contId = contId.substring(0, 12);
                System.out.println("##### ? " + contId);
            }
            if(contId != null && contId.length() > 1) {
                System.out.println("WorkflowThread.stepExitCheckRequest : container id = " + contId);
                jobLogService.updateLogStepStatus(idLogStep, ScheduleLogStep.commandStatus.RUNNING);
                jobLogService.updateRunningStatus(idLogStep, ScheduleRunning.commandStatus.RUNNING);
                break;
            }
            else if(contId.equals("0")){    // 실행 오류
                System.out.println("WorkflowThread.stepExitCheckRequest : container id = 0");
                return false;
            } else {    // 이것도 실행 오류
//                System.out.println("##### WorkflowThread.stepExitCheckRequest : sleep 2 s");
                cnt++;
                if(cnt > 5) {
                    return false;
                }
                Thread.sleep(1000 * 2);
            }
        }

        while(true){
            List<String> status = findContainerStatus(contId);
            //queryResultMem.getResults().get(0).getSeries()

//            String container_status = status.get(0).getContainerStatus();
//            String exitcode = status.get(0).getExitcode();
            String container_status = status.get(0);
            String exitcode = status.get(1);
            String container_name = status.get(2);

            System.out.println("##### WorkflowThread.stepExitCheckRequest");
            System.out.println(container_status + " / " + exitcode);

            System.out.println("###### status = " + status);
            if(status.size() == 0){
                System.out.println("##### RM : " + logStep.getIdStep() + " / " + contId);
                stepRemoveContainer(logStep.getIdStep(), contId);
                return false;
            }

            if(container_status.equalsIgnoreCase("running")) {
                System.out.println("WorkflowThread.stepExitCheckRequest : status running");
                Thread.sleep(1000 * 5);
            }
            else if(container_status.equalsIgnoreCase("exited")){
                System.out.println("WorkflowThread.stepExitCheckRequest : status exited");
                if(exitcode.equals("0.0")) { // 정상종료
                    System.out.println("WorkflowThread.stepExitCheckRequest : exit code 0");
                    stepRemoveContainer(logStep.getIdStep(), contId);
                    return true;
                }
                else{                                        // 그 외 (에러)
                    System.out.println("WorkflowThread.stepExitCheckRequest : exit code non-zero");
                    stepRemoveContainer(logStep.getIdStep(), contId);
                    return false;
                }
            }
        }
    }


    /**
     * Local test 용
     * */
    public Boolean stepExitCheckRequestDemo(Long idLogStep) throws InterruptedException {
        jobLogService.updateLogStepStatus(idLogStep, ScheduleLogStep.commandStatus.RUNNING);
        jobLogService.updateRunningStatus(idLogStep, ScheduleRunning.commandStatus.RUNNING);
        Thread.sleep(1000 * 60);
        return (int) (Math.random() * 10) > 1;
    }

    public List<String> findContainerStatus(String i) throws InterruptedException { // return container id
        System.out.println("ContainerStatusServiceImpl.findContainerStatus : " + i);

        String influxHost = "http://10.33.194.25:4006";
        InfluxDB influxDB = InfluxDBFactory.connect(influxHost, "admin", "admin123");
        influxDB.setDatabase("telegraf");
//        Query _queryString = new Query("SELECT * FROM docker_container_status where source = '" + i + "' order by time desc limit 1");
        Query _queryString = new Query("SELECT container_status, exitcode, container_name, container_image FROM docker_container_status where source = '" + i + "' order by time desc limit 1");
        System.out.println("query : " + _queryString.getCommand());

        QueryResult queryResultMem = null;
        for(int k=0; k < 10; k++) {
            queryResultMem = influxDB.query(_queryString);
            if(queryResultMem.getResults().get(0).getSeries() == null){
                Thread.sleep(1000 * 5);
            } else{
                break;
            }
        }

//        System.out.println("####################################");
//        System.out.println(queryResultMem);
//        System.out.println("####################################");

        String container_status = queryResultMem.getResults().get(0).getSeries().get(0).getValues().get(0).get(1).toString();
        String exitcode = queryResultMem.getResults().get(0).getSeries().get(0).getValues().get(0).get(2).toString();
        String container_name = queryResultMem.getResults().get(0).getSeries().get(0).getValues().get(0).get(3).toString();


        List<String> result = new ArrayList<>();
        result.add(container_status);
        result.add(exitcode);
        result.add(container_name);

        return result;
    }
}