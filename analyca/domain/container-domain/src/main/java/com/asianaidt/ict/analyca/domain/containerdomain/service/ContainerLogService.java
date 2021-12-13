package com.asianaidt.ict.analyca.domain.containerdomain.service;

import com.asianaidt.ict.analyca.domain.containerdomain.model.ContainerLog;
import com.asianaidt.ict.analyca.domain.containerdomain.model.ContainerStatus;

import java.util.List;

public interface ContainerLogService {
    List<ContainerLog> h2o_list(String s);

    List<ContainerLog> findAllContainerLog();
    List<ContainerLog> findContainerLog(String host);
}
