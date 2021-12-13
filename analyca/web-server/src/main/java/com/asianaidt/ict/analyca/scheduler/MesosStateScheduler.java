package com.asianaidt.ict.analyca.scheduler;

import com.asianaidt.ict.analyca.service.mesosservice.MesosStateCollector;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MesosStateScheduler {
    private final MesosStateCollector mesosStateCollector;

    public MesosStateScheduler(MesosStateCollector mesosStateCollector) {
        this.mesosStateCollector = mesosStateCollector;
    }

    @Scheduled(cron = "0 * * * * *")
    public void collectMesosState() {
        mesosStateCollector.collect("http://10.33.194.25:8778/state");
    }
}
