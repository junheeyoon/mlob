package com.asianaidt.ict.analyca.webserver.stomp;

import com.asianaidt.ict.analyca.system.websocketcore.type.WebSocketHeader;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

public class AssignPrincipalHandshakeHandler extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String name = request.getHeaders().getFirst(WebSocketHeader.USER.getName());
        return new Principal() {
            @Override
            public String getName() {
                return name;
            }
        };
    }
}
