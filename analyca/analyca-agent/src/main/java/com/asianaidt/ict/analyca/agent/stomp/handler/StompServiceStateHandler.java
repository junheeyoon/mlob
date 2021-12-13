package com.asianaidt.ict.analyca.agent.stomp.handler;

import com.asianaidt.ict.analyca.agent.service.MessageService;
import com.asianaidt.ict.analyca.system.websocketcore.dto.ScheduleServiceStateList;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class StompServiceStateHandler implements StompFrameHandler {

    @Autowired
    MessageService messageService;

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return ScheduleServiceStateList.class;
    }

    @SneakyThrows
    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        System.out.println("payload = " + payload);
        ScheduleServiceStateList states = (ScheduleServiceStateList) payload;
        messageService.statusCommand(states);

    }

}
