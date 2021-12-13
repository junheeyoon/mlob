package com.asianaidt.ict.analyca.webserver.controller;

import com.asianaidt.ict.analyca.domain.dockerdomain.model.ContainerPS;
import com.asianaidt.ict.analyca.domain.dockerdomain.service.DockerService;
import com.asianaidt.ict.analyca.domain.hostdomain.model.HostUsageResource;
import com.asianaidt.ict.analyca.domain.hostdomain.service.HostService;
import com.asianaidt.ict.analyca.domain.schedulercore.model.Agent;
import com.asianaidt.ict.analyca.domain.schedulercore.service.AgentService;
import com.asianaidt.ict.analyca.system.dockercore.dto.command.*;
import com.asianaidt.ict.analyca.system.dockercore.dto.registry.Catalog;
import com.asianaidt.ict.analyca.system.dockercore.dto.registry.ImageTag;
import com.asianaidt.ict.analyca.webserver.service.GhostService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.asianaidt.ict.analyca.system.dockercore.type.DockerCommand.WebSocket.ServerToAgent.COMMAND;

@RequiredArgsConstructor
@Controller
@RequestMapping("/ghost")
public class GhostController {
    private final DockerService dockerService;
    private final AgentService agentService;
    private final SimpMessagingTemplate messagingTemplate;
    private final HostService hostService;
    private final GhostService ghostService;

    @GetMapping("/runnerview")
    public String runnerViewer() {
        return "ghost/ghost-dashboard";
    }

    @GetMapping("/containers")
    public String containersViewer() {
        return "ghost/ghost-containers";
    }

    @RequestMapping("/node/status/all")
    public @ResponseBody
    List<HostUsageResource.HostStatus> getNodeInfoAll() {
        List<Agent> agents = agentService.findAll();
        List<HostUsageResource.HostStatus> hostStatusList = new ArrayList<>();
        for (Agent agent : agents) {
            HostUsageResource.HostStatus hostStatus = hostService.getNodesStatus(agent.getHostname());
            hostStatusList.add(hostStatus);
        }

        return hostStatusList;
    }

    @RequestMapping("/node/status/cpu")
    public @ResponseBody
    String getNodeCpu(@RequestParam(value = "hostname") String hostname,
                      @RequestParam(value = "period") String period) {

        return new Gson().toJson(hostService.cpuList(hostname, period));
    }

    @RequestMapping("/node/status/mem")
    public @ResponseBody
    String getNodeMem(@RequestParam(value = "hostname") String hostname,
                      @RequestParam(value = "period") String period) {
        return new Gson().toJson(hostService.memoryList(hostname, period));
    }

    @RequestMapping("/node/status/disk")
    public @ResponseBody
    String getNodeDisk(@RequestParam(value = "hostname") String hostname,
                       @RequestParam(value = "period") String period) {
        return new Gson().toJson(hostService.diskList(hostname, period));
    }

    @GetMapping("/test")
    @ResponseStatus(value = HttpStatus.OK)
    public void test() {
        runContainer(new DockerRunCommand(
                "ai-big-node04", "freight", "latest", "freight", "4906:80", "", "", "", "", false
        ));
    }

    @PostMapping("/run")
    @ResponseStatus(value = HttpStatus.OK)
    public void runContainer(@RequestBody DockerRunCommand runCommand) {
        containerCommandSendToUser(agentService.findByHostname(runCommand.getHostName()).getUserId(), runCommand);
    }

    @PostMapping("/start")
    @ResponseStatus(value = HttpStatus.OK)
    public void startContainer(@RequestBody DockerStartCommand startCommand) {
        containerCommandSendToUser(agentService.findByHostname(startCommand.getHostName()).getUserId(), startCommand);
    }

    @PostMapping("/stop")
    @ResponseStatus(value = HttpStatus.OK)
    public void stopContainer(@RequestBody DockerStopCommand stopCommand) {
        containerCommandSendToUser(agentService.findByHostname(stopCommand.getHostName()).getUserId(), stopCommand);
    }

    @PostMapping("/rm")
    @ResponseStatus(value = HttpStatus.OK)
    public void rmContainer(@RequestBody DockerRMCommand rmCommand) {
        containerCommandSendToUser(agentService.findByHostname(rmCommand.getHostName()).getUserId(), rmCommand);
    }

    @PostMapping("/images")
    public @ResponseBody
    List<ImageTag> images() {
        final String REGISTRY_HOST = "http://10.33.194.14:4506/v2";
        RestTemplate registry = new RestTemplate();
        ResponseEntity<Catalog> catalog = registry.getForEntity(REGISTRY_HOST + "/_catalog", Catalog.class);
        if (catalog.getStatusCode() != HttpStatus.OK) {
            System.out.println("catalog error");
            return Collections.emptyList();
        }
        return Objects.requireNonNull(catalog.getBody()).getRepositories().stream().map(image -> {
            ResponseEntity<ImageTag> imageTag = registry.getForEntity(REGISTRY_HOST + "/" + image + "/tags/list", ImageTag.class);
            System.out.println("imageTag.getBody() = " + imageTag.getBody());
            if (imageTag.getStatusCode() != HttpStatus.OK) return null;
            return imageTag.getBody();
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @PostMapping({"/ps", "/ps/{hostname}"})
    public @ResponseBody
    List<ContainerPS> ps(@PathVariable(required = false) String hostname) {
        if (hostname != null) return dockerService.findContainerPS(hostname);
        return dockerService.findAllContainerPS();
    }

    @PostMapping({"/metrics", "/metrics/{containerName}/{period}/{type}"})
    public @ResponseBody
    String metrics(@PathVariable(required = false) String containerName,
                   @PathVariable(required = false) String period,
                   @PathVariable(required = false) String type) {
        if (containerName == null) {
            return new Gson().toJson(ghostService.getMetricsInfoAll());
        } else {
            switch (type) {
                case "cpu":
                    return new Gson().toJson(ghostService.getCPUMetricsContainer(containerName, period));
                case "mem":
                    return new Gson().toJson(ghostService.getMemMetricsContainer(containerName, period));
                default:
                    return null;
            }
        }
    }

    public void containerCommandSendToUser(String userId, DockerCommandContainer commandContainer) {
        messagingTemplate.convertAndSendToUser(userId, COMMAND.getUri(), commandContainer.getCommandStompRequest());
    }
}
