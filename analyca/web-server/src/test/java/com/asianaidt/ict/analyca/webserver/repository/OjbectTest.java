package com.asianaidt.ict.analyca.webserver.repository;

import com.asianaidt.ict.analyca.domain.schedulercore.model.Agent;
import com.asianaidt.ict.analyca.domain.schedulercore.model.Schedule;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class OjbectTest {

    @Test
    public void newObjTestAgent() {
        Agent a = new Agent();
        System.out.println(a.getUserId());
        System.out.println(a.getIp());
        System.out.println(a.getIp() != null ? a.getIp() : "0.0.0.0");

    }

    @Test
    public void newObjTestSchedule() {
        Schedule a = new Schedule();
        System.out.println(a.getUserId());
        System.out.println(a.getId());
        System.out.println(a.getScheduleDesc());
    }
}
