package com.asianaidt.ict.analyca.system.websocketcore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleForStompRequest {
    private long scheduleId;
    private String filename;
    private byte[] file;
}