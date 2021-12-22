package com.asianaidt.ict.analyca.webserver.stomp;

import com.asianaidt.ict.analyca.domain.schedulercore.service.AgentService;
import com.asianaidt.ict.analyca.system.websocketcore.type.WebSocketHeader;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class SessionEventListener {
    private final AgentService agentService;

    public SessionEventListener(AgentService agentService) {
        this.agentService = agentService;
//        this.agentService.deleteAll();
    }

    @EventListener
    private void handleSessionConnected(SessionConnectedEvent event) {
        StompHeaderAccessor accessor
                = MessageHeaderAccessor.getAccessor(
                (GenericMessage) event.getMessage().getHeaders().get("simpConnectMessage"), StompHeaderAccessor.class);

        String ip = accessor.getFirstNativeHeader(WebSocketHeader.IP.getName());
        String hostname = accessor.getFirstNativeHeader(WebSocketHeader.HOSTNAME.getName());
        String userId = accessor.getUser().getName();
        String sessionId = accessor.getSessionId();
        System.out.println("userId = " + userId);
        agentService.addAgent(ip, hostname, userId, sessionId);
    }

    @EventListener
    private void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String userId = accessor.getUser().getName();
        System.out.println("userId = " + userId);
        if (userId != null) agentService.deleteAgentByUserId(userId);
    }


    @EventListener
    private void handlerSessionSubscribe(SessionSubscribeEvent event) {
        StompHeaderAccessor accessorHeader = StompHeaderAccessor.wrap(event.getMessage());
        System.out.println("accessorHeader = " + accessorHeader);
    }
}
