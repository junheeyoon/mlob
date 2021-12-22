package com.asianaidt.ict.analyca.system.websocketcore.dto;

import com.asianaidt.ict.analyca.system.websocketcore.type.ScheduleServiceState;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleServiceStateRequest {
    private long id;
    private long pid;
    private ScheduleServiceState state;

    public ScheduleServiceStateRequest(long id, long pid) {
        this.id = id;
        this.pid = pid;
    }
}
