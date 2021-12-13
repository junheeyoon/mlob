package com.asianaidt.ict.analyca.webserver.service;

import com.asianaidt.ict.analyca.domain.schedulercore.model.*;
import com.asianaidt.ict.analyca.domain.schedulercore.service.*;
import com.asianaidt.ict.analyca.service.schedulerservice.domain.JobRemoveIssued;
import com.asianaidt.ict.analyca.service.schedulerservice.domain.JobStepIssued;
import com.asianaidt.ict.analyca.service.schedulerservice.service.JobSchedulerService;
import com.asianaidt.ict.analyca.system.dockercore.dto.command.DockerCommandContainer;
import com.asianaidt.ict.analyca.system.dockercore.dto.command.DockerRMCommand;
import com.asianaidt.ict.analyca.system.dockercore.dto.command.DockerRunCommand;
import com.asianaidt.ict.analyca.system.websocketcore.dto.ScheduleForStompRequest;
import com.asianaidt.ict.analyca.system.websocketcore.dto.ScheduleServiceStateList;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static com.asianaidt.ict.analyca.system.dockercore.type.DockerCommand.WebSocket.ServerToAgent.COMMAND;

@RequiredArgsConstructor
@Service("scheduleRunService")
@Slf4j
public class ScheduleRunServiceImpl implements ScheduleRunService {

    private final ScheduleService scheduleService;
    private final SimpMessagingTemplate messagingTemplate;
    private final JobSchedulerService jobSchedulerService;

    private final AgentService agentService;
    private final ScheduleLogService scheduleLogService;            // DB
    private final ScheduleRunningService scheduleRunningService;    // DB
    private final ScheduleStepService scheduleStepService;          // DB
    private final ScheduleStepListService stepListService;
    private final GhostService ghostService;

    @Value("${temp.path}") private String tempPath;

    @Override
    public Schedule save(Schedule schedule) {
        return jobSchedulerService.save(schedule);
    }

    @Override
    public List<Schedule> findByDeleted(Schedule.deletedStatus deleted) {
        return jobSchedulerService.findByDeleted(deleted);
    }

    /*
     * send message to agent
     * and make schedule_log
     */
//    @Override
//    public void scheduleRun(Long id) {
////        Schedule vo = scheduleService.findById(id).orElse(new Schedule());
//        log.info("##### ScheduleRunServiceImpl.scheduleRun() : " + id);
//
//        Optional<Schedule> ifSchedule = jobSchedulerService.findById(id);
//        if(ifSchedule.isPresent()) {
//            Schedule vo = ifSchedule.get();
//            Agent agent = agentService.findByUserId(vo.getAgent());
//            if (agent.getHostname().equals("temp") && agent.getIp().equals("127.0.0.1")){       // no agent found
//                saveScheduleLog(vo, null, ScheduleLog.commandStatus.FAILED);
//            } else {
//                try {
//                    String filePath = tempPath + File.separator + vo.getCommand();
//                    byte[] readFile = Files.readAllBytes(Paths.get(filePath));
//                    runServiceByScheduler(vo.getAgent(), new ScheduleForStompRequest(vo.getId(), vo.getCommand(), readFile));
//                    jobSchedulerService.updateUsed(vo.getId(), Schedule.usedStatus.RUNNING);
//                    saveScheduleLog(vo, agent, ScheduleLog.commandStatus.START);               // run successfully
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    saveScheduleLog(vo, agent, ScheduleLog.commandStatus.FAILED);              // error
//                }
//            }
//        } else {
//            saveScheduleLog(null, null, ScheduleLog.commandStatus.FAILED);      // no schedule found by id
//        }
//    }
    @Override
    public void scheduleRun(Long id) {
//        Schedule vo = scheduleService.findById(id).orElse(new Schedule());
        log.info("##### ScheduleRunServiceImpl.scheduleRun() : " + id);

        Optional<Schedule> ifSchedule = jobSchedulerService.findById(id);
        if(ifSchedule.isPresent()) {
            Schedule vo = ifSchedule.get();
            try {
                jobSchedulerService.startJobOnce(vo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Optional<Schedule> findById(Long id) {
        return jobSchedulerService.findById(id);
    }

    @Override
    public void scheduleAdd(Schedule schedule) {
        jobSchedulerService.addJob(schedule);
    }

    @Override
    public void scheduleUpdate(Schedule schedule){
        log.info("##### ScheduleRunServiceImpl.scheduleUpdate()");
        jobSchedulerService.updateJob(schedule);
    }

    @Override
    public void scheduleDelete(Schedule schedule){
        log.info("##### ScheduleRunServiceImpl.scheduleDelete()");
        jobSchedulerService.deleteJob(schedule);
    }
    @Override
    public void schedulePause(Schedule schedule){
        log.info("##### ScheduleRunServiceImpl.schedulePause()");
        jobSchedulerService.pauseJob(schedule);
    }
    @Override
    public void scheduleResume(Schedule schedule){
        log.info("##### ScheduleRunServiceImpl.scheduleResume()");
        jobSchedulerService.resumeJob(schedule);
    }

    @Override
    public void collectServiceState() {
        /*
        log.info("##### ScheduleRunServiceImpl.collectServiceState()");
        /* 수정 필요
        List<ScheduleRunning.pidGroup> pidsGroupByAgent = scheduleRunningService.findPidsGroupByAgent();
        List<GroupList> res = groupingPids(pidsGroupByAgent);
        for (GroupList group : res){
            requestServiceStateByScheduler(group.getAgent(), group.getServiceList());
        }
        */
    }

    @Override
    public Boolean saveSteps(Schedule scd, String steps) {
        try {
            // 특정 schedule의 step을 저장할 땐 기존 schedule의 step 들은 삭제함
            scheduleStepService.deleteAllByIdSchedule(scd);

            JSONArray arr = new JSONArray(steps);
            List<ScheduleStep> stepList = new ArrayList<>();
            for (int i=0; i<arr.length(); i++) {
                JSONObject step = arr.optJSONObject(i);
                ScheduleStep vo = new ScheduleStep();
                vo.setIdSchedule(scd);
                vo.setStepName(step.get("stepName").toString());
                vo.setStepDesc(step.get("stepDesc").toString());
                vo.setStepOrder(i+1);
                vo.setHost(step.get("host").toString());
                String[] hostSplit = step.get("host").toString().split("\\(|\\)");
                Agent agent = agentService.findByIpAndHostname(hostSplit[1], hostSplit[0]);
                vo.setAgent(agent.getUserId());
                vo.setImage(step.get("image").toString());
                vo.setIdStepList(Long.parseLong(step.get("idStepList").toString()));
                stepList.add(vo);
            }
            scheduleStepService.saveAll(stepList);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<ScheduleStep> findStepsByIdSchedule(Schedule i) {
        return scheduleStepService.findByIdSchedule(i);
    }

    @Override
    public void scheduleStepRun(JobStepIssued issued) {
        Optional<ScheduleStep> ifStep = jobSchedulerService.findStepById(issued.getIdStep());
        if(ifStep.isPresent()) {
            ScheduleStep vo = ifStep.get();
            Agent agent = agentService.findByUserId(vo.getAgent());
            if (agent.getHostname().equals("temp") && agent.getIp().equals("127.0.0.1")){       // no agent found
//                saveScheduleLog(vo, null, ScheduleLog.commandStatus.FAILED);
            } else {
                try {
                    Optional<ScheduleStepList> ifStepReg = stepListService.findById(vo.getIdStepList());
                    if(ifStepReg.isPresent()) {
                        ScheduleStepList stepReg = ifStepReg.get();

                        DockerRunCommand runCommand = new DockerRunCommand(vo.getHost().split("[(]")[0],
                                stepReg.getImage(),
                                stepReg.getTag(),
                                //stepReg.getContainerName(),
                                "",
                                stepReg.getPort(),
                                stepReg.getVolume(),
                                "",
                                stepReg.getMemory(),
                                stepReg.getCpu(),
                                stepReg.getGpu(),
                                stepReg.getOptions(),
                                stepReg.getCommands(),
                                true,
                                issued.getIdLogScheduleStep()
                        );
                        containerCommandSendToUser(agentService.findByHostname(runCommand.getHostName()).getUserId(), runCommand);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
//            saveScheduleLog(null, null, ScheduleLog.commandStatus.FAILED);      // no schedule found by id
        }
    }

    @Override
    public void scheduleStepContainerRemove(JobRemoveIssued issued) {
        System.out.println("##### ScheduleRunServiceImpl.scheduleStepContainerRemove");
        Optional<ScheduleStep> ifStep = jobSchedulerService.findStepById(issued.getIdStep());
        if(ifStep.isPresent()) {
            ScheduleStep vo = ifStep.get();
            Agent agent = agentService.findByUserId(vo.getAgent());
            if (agent.getHostname().equals("temp") && agent.getIp().equals("127.0.0.1")){       // no agent found
//                saveScheduleLog(vo, null, ScheduleLog.commandStatus.FAILED);
                System.out.println("No Agent Found");
            } else {
                try {
                    System.out.println("##### ScheduleRunServiceImpl.scheduleStepContainerRemove 111");
                    System.out.println(agent.getHostname() + " / " + issued.getContainerId());
//                    DockerRMCommand dockerRMCommand = DockerRMCommand.builder().hostName(agent.getHostname())/*.containerId(issued.getContainerId())*/
//                            .containerName(issued.getContainerId()).build();
                    DockerRMCommand dockerRMCommand = DockerRMCommand.builder().hostName(agent.getHostname())
                            .containerName(issued.getContainerId()).build();
                    System.out.println(agentService.findByHostname(dockerRMCommand.getHostName()).getUserId() + " / " + dockerRMCommand);
                    containerCommandSendToUser(agentService.findByHostname(dockerRMCommand.getHostName()).getUserId(), dockerRMCommand);
                } catch (Exception e) {
                    e.printStackTrace();
//                    saveScheduleLog(vo, agent, ScheduleLog.commandStatus.FAILED);              // error
                }
            }
        }
    }

    private ScheduleLog saveScheduleLog(Schedule schedule, Agent agent, ScheduleLog.commandStatus status){
        ScheduleLog scheduleLog = new ScheduleLog();
        String failMessage = "";

        if(schedule != null && agent != null){
//            Schedule schedule = ifSchedule.get();
//            scheduleLog.setStatus(success ? ScheduleLog.commandStatus.START : ScheduleLog.commandStatus.FAILED);
            scheduleLog.setStatus(status);
            scheduleLog.setUserId(schedule.getUserId());
            scheduleLog.setType(schedule.getType());
            scheduleLog.setScheduleName(schedule.getScheduleName());
            scheduleLog.setScheduleDesc(schedule.getScheduleDesc());
            scheduleLog.setIdSchedule(schedule.getId());

            scheduleLog.setHostIp(agent.getIp());
            scheduleLog.setHostName(agent.getHostname());
        } else {
            if(schedule == null) {
                failMessage = "Failed -- Schedule Not Found";
                scheduleLog.setHostIp("");
                scheduleLog.setHostName("");

                scheduleLog.setUserId("");
                scheduleLog.setType("");
                scheduleLog.setScheduleName("");
                scheduleLog.setScheduleDesc(failMessage);
                scheduleLog.setIdSchedule(-1);
            } else {
                failMessage = "Failed -- Agent Not Found";
                scheduleLog.setHostIp("");
                scheduleLog.setHostName(failMessage);

                scheduleLog.setUserId(schedule.getUserId());
                scheduleLog.setType(schedule.getType());
                scheduleLog.setScheduleName(schedule.getScheduleName());
                scheduleLog.setScheduleDesc(schedule.getScheduleDesc());
                scheduleLog.setIdSchedule(schedule.getId());
            }
            scheduleLog.setStatus(status);
//            scheduleLog.setStatus(ScheduleLog.commandStatus.FAILED);
        }
        return scheduleLogService.save(scheduleLog);
    }

    private MessageHeaders createHeaders(@Nullable String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        if (sessionId != null) headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }

    public void runServiceByScheduler(@NonNull String userId, @NonNull ScheduleForStompRequest request) {   //ScheduleForStompRequest
        log.info("##### ScheduleRunServiceImpl.runServiceByScheduler() : " + userId);
        messagingTemplate.convertAndSendToUser(userId, "/service/message", request, createHeaders(null));
    }

    public void requestServiceStateByScheduler(@NonNull String userId, @NonNull ScheduleServiceStateList stateList) {
        log.info("##### ScheduleRunServiceImpl.requestServiceStateByScheduler() : " + userId);
        messagingTemplate.convertAndSendToUser(userId, "/service/state", stateList, createHeaders(null));
    }

    public void containerCommandSendToUser(String userId, DockerCommandContainer commandContainer) {
        messagingTemplate.convertAndSendToUser(userId, COMMAND.getUri(), commandContainer.getCommandStompRequest());
    }

    @Data
    public class GroupList{
        public String agent;
        public ScheduleServiceStateList serviceList;

        public GroupList(String agent, ScheduleServiceStateList scheduleServiceStateList) {
            this.agent = agent;
            this.serviceList = scheduleServiceStateList;
        }
    }
}
