package com.asianaidt.ict.analyca.system.websocketcore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleServiceStateList {
    List<ScheduleServiceStateRequest> requestList;
}