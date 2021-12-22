package com.asianaidt.ict.analyca.agent.stomp;

import com.asianaidt.ict.analyca.agent.stomp.handler.StompGhostHandler;
import com.asianaidt.ict.analyca.agent.stomp.handler.StompServiceMessageHandler;
import com.asianaidt.ict.analyca.agent.stomp.handler.StompServiceStateHandler;
import com.asianaidt.ict.analyca.system.dockercore.type.DockerCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.asianaidt.ict.analyca.system.dockercore.type.DockerCommand.WebSocket.ServerToAgent.COMMAND;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompSessionHandler extends StompSessionHandlerAdapter {
    private final StompServiceMessageHandler stompServiceMessageHandler;
    private final StompServiceStateHandler stompServiceStateHandler;
    private final StompGhostHandler ghostHandler;

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        session.subscribe("/user/service/message", stompServiceMessageHandler); // task 시작 요청 - chain 이후 docker run
        session.subscribe("/user/service/state", stompServiceStateHandler);     // task pid 확인 - chain 이후 미사용
        session.subscribe(COMMAND.getUriForUser(), ghostHandler);
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        Arrays.stream(exception.getStackTrace()).forEach(e -> log.error(e.toString()));
        log.error(exception.getStackTrace().toString());
        log.error("[WebSocket] handleException");
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        log.error("[WebSocket] handleTransportError");
    }
}
