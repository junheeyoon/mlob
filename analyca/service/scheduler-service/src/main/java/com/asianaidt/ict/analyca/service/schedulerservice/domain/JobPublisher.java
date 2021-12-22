package com.asianaidt.ict.analyca.service.schedulerservice.domain;

public interface JobPublisher {
    void publishToServer(Object event);
}
