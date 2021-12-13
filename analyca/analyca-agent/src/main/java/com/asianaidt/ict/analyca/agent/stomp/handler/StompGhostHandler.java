package com.asianaidt.ict.analyca.agent.stomp.handler;

import com.asianaidt.ict.analyca.agent.ghost.GhostService;
import com.asianaidt.ict.analyca.system.dockercore.dto.DockerCommandStompRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@RequiredArgsConstructor
@Component
public class StompGhostHandler implements StompFrameHandler {
    private final GhostService ghostService;

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return DockerCommandStompRequest.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        ghostService.execute((DockerCommandStompRequest) payload);
    }
}
