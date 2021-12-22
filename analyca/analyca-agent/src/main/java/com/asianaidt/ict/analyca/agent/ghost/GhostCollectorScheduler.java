package com.asianaidt.ict.analyca.agent.ghost;

import com.asianaidt.ict.analyca.agent.stomp.StompClient;
import com.asianaidt.ict.analyca.service.dockerservice.DockerCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.asianaidt.ict.analyca.system.dockercore.type.DockerCommand.WebSocket.AgentToServer.METRICS;

@Slf4j
@RequiredArgsConstructor
@Component
public class GhostCollectorScheduler {
    private final DockerCommandService dockerCommandService;
    private final StompClient stompClient;

    @Scheduled(cron = "*/10 * * * * *")
    public void collect() {
        log.debug("collect start");
        stompClient.send(METRICS.getUri(), dockerCommandService.metrics());
        log.debug("collect end");
    }
}
