package com.asianaidt.ict.analyca.webserver.controller;

import com.asianaidt.ict.analyca.domain.dockerdomain.model.ContainerPS;
import com.asianaidt.ict.analyca.domain.dockerdomain.model.DockerMetrics;
import com.asianaidt.ict.analyca.domain.dockerdomain.service.DockerService;
import com.asianaidt.ict.analyca.domain.schedulercore.model.Agent;
import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleLogStep;
import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleRunning;
import com.asianaidt.ict.analyca.domain.schedulercore.service.AgentService;
import com.asianaidt.ict.analyca.service.schedulerservice.service.JobLogService;
import com.asianaidt.ict.analyca.system.dockercore.dto.DockerCommandStompResponse;
import com.asianaidt.ict.analyca.system.dockercore.dto.DockerContainerResources;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class GhostMessageController {
    private final AgentService agentService;
    private final DockerService dockerService;
    private final JobLogService jobLogService;

    @MessageMapping("/ghost/collect")
    public void collectGhost(DockerContainerResources containerResources, Principal principal) {
        Agent agent = agentService.findByUserId(principal.getName());
        dockerService.createContainerMetrics(containerResources.values().stream()
                .filter(containerResource -> containerResource.getStatus().equals("running"))
                .map(containerResource -> DockerMetrics.builder()
                        .containerName(containerResource.getContainerName())
                        .containerId(containerResource.getContainerId())
                        .cpu(containerResource.getCpu())
                        .totalMemory(containerResource.getMem()[0])
                        .usedMemory(containerResource.getMem()[1])
                        .netIn(containerResource.getNetIO()[0])
                        .netOut(containerResource.getNetIO()[1])
                        .blockIn(containerResource.getBlockIO()[0])
                        .blockOut(containerResource.getBlockIO()[1])
                        .host(agent.getHostname())
                        .ip(agent.getIp())
                        .status(containerResource.getStatus())
                        .build())
                .collect(Collectors.toList()));

        dockerService.updateContainerPS(agent.getHostname(), containerResources.values().stream().map(containerResource ->
                ContainerPS.builder()
                        .hostName(agent.getHostname())
                        .hostIp(agent.getIp())
                        .containerName(containerResource.getContainerName())
                        .creator(containerResource.getCreator())
                        .cpu(containerResource.getCpuCnt())
                        .mem(containerResource.getMemCnt())
                        .gpu(containerResource.getGpuCnt())
                        .createdAt(containerResource.getCreatedAt())
                        .status(containerResource.getStatus())
                        .imageName(containerResource.getImageName())
                        .imageTag(containerResource.getImageTag())
                        .binds(containerResource.getBinds())
                        .ports(containerResource.getPorts())
                        .build()
        ).collect(Collectors.toList()));
    }

    @MessageMapping("/ghost/command")
    public void onCommand(DockerCommandStompResponse response) {
        System.out.println("response = " + response);
        switch (response.getType()) {
            case RUN:
                log.debug("response = " + response);

                jobLogService.updateContainerId(response.getIdLogStep(), response.getResponse().get(0));
                jobLogService.updateLogStepStatus(response.getIdLogStep(), ScheduleLogStep.commandStatus.RUNNING);
                jobLogService.updateRunningStatus(response.getIdLogStep(), ScheduleRunning.commandStatus.RUNNING);

                break;
            default:
                log.debug("response type is error = " + response);
        }
    }
}
