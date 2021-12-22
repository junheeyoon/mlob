package com.asianaidt.ict.analyca.domain.hostdomain.service;

import com.asianaidt.ict.analyca.domain.hostdomain.model.HostCpuStatus;
import com.asianaidt.ict.analyca.domain.hostdomain.model.HostDiskStatus;
import com.asianaidt.ict.analyca.domain.hostdomain.model.HostMemStatus;
import com.asianaidt.ict.analyca.domain.hostdomain.model.HostUsageResource;

import java.util.List;

public interface HostService {

    List<HostMemStatus> memoryList(String hostname, String period);

    List<HostCpuStatus> cpuList(String hostname, String period);

    List<HostDiskStatus> diskList(String hostname, String period);

    HostUsageResource.HostStatus getNodesStatus(String hostname);
}
