package com.asianaidt.ict.analyca.webserver.service;

import com.asianaidt.ict.analyca.service.schedulerservice.domain.JobPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class JobPublisherEvent implements JobPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public JobPublisherEvent(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publishToServer(Object event) {
        applicationEventPublisher.publishEvent(event);
    }
}
