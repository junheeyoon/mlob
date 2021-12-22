package com.asianaidt.ict.analyca.scheduler;


import com.asianaidt.ict.analyca.service.hadoopservice.HdfsStateCollector;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HdfsStateScheduler {
    private final HdfsStateCollector hdfsStateCollector;

    public HdfsStateScheduler(HdfsStateCollector hdfsStateCollector) {
        this.hdfsStateCollector = hdfsStateCollector;
    }

    /////////////////////////////////////////////////////
//    @Scheduled(cron = "0 * * * * *")
//    public void collectMesosState() {
//        hdfsStateCollector.collect("http://10.33.194.25:8083/jmx");
//    }

}
