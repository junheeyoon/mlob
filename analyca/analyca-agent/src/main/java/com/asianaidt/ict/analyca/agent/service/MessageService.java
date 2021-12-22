package com.asianaidt.ict.analyca.agent.service;

import com.asianaidt.ict.analyca.system.websocketcore.dto.ScheduleForStompRequest;
import com.asianaidt.ict.analyca.system.websocketcore.dto.ScheduleServiceStateList;
import com.asianaidt.ict.analyca.system.websocketcore.dto.ScheduleServiceStateRequest;
import com.asianaidt.ict.analyca.system.websocketcore.type.ScheduleServiceState;

public interface MessageService {
    void startCommand(ScheduleForStompRequest request) throws Exception;

    void statusCommand(ScheduleServiceStateList list) throws Exception;
}
