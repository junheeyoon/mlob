package com.asianaidt.ict.analyca.agent.stomp;

import com.asianaidt.ict.analyca.agent.service.MessageService;
import com.asianaidt.ict.analyca.system.websocketcore.dto.ScheduleForStompRequest;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class StompServiceScheduleHandler implements StompFrameHandler {
    private final MessageService messageService;

    public StompServiceScheduleHandler(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return ScheduleForStompRequest.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        System.out.println("payload = " + payload);
        try {
            messageService.startCommand((ScheduleForStompRequest) payload);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
