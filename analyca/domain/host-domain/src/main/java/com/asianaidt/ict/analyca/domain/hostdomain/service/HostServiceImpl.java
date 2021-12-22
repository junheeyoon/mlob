package com.asianaidt.ict.analyca.domain.hostdomain.service;

import com.asianaidt.ict.analyca.domain.hostdomain.model.HostCpuStatus;
import com.asianaidt.ict.analyca.domain.hostdomain.model.HostDiskStatus;
import com.asianaidt.ict.analyca.domain.hostdomain.model.HostMemStatus;
import com.asianaidt.ict.analyca.domain.hostdomain.model.HostUsageResource;
import com.asianaidt.ict.analyca.domain.hostdomain.repository.HostInfluxRepository;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HostServiceImpl implements HostService {

    private final HostInfluxRepository hostInfluxRepository;

    public HostServiceImpl(HostInfluxRepository hostInfluxRepository) {
        this.hostInfluxRepository = hostInfluxRepository;
    }

    /**
     * 9 시간 + 필요
     * Period : h(시간) , d(날짜), m(분) 만 입력 받음
     */
    @Override
    public List<HostMemStatus> memoryList(String hostname, String period) {
        String queryString = "select (total/1024/1024/1024) as total, (used/1024/1024/1024) as used, time from mem where host='" + hostname + "' and time>now()- " + period + " tz('Asia/Seoul')";

        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        return resultMapper.toPOJO(hostInfluxRepository.queryAction(queryString), HostMemStatus.class);
    }

    @Override
    public List<HostCpuStatus> cpuList(String hostname, String period) {
        String queryString = "select usage_user, time from cpu where host='" + hostname + "' and time>now()-" + period + " tz('Asia/Seoul')";

        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        return resultMapper.toPOJO(hostInfluxRepository.queryAction(queryString), HostCpuStatus.class);
    }

    @Override
    public List<HostDiskStatus> diskList(String hostname, String period) {
//        Query query = new Query("select (total/1024/1024/1024) as total, (used/1024/1024/1024) as used, time from disk where host='" + hostname +"' and time>now()-" + period + " tz('Asia/Seoul')");
        String queryString = "select (sum(total)/1024/1024/1024/1024) as disktotal, (sum(used)/1024/1024/1024/1024) as diskused, time from (select total, used, time, device from disk where host='" + hostname + "' and time>now()-" + period + " group by total, used, device) group by total, used,time(1m) tz('Asia/Seoul')";

        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        return resultMapper.toPOJO(hostInfluxRepository.queryAction(queryString), HostDiskStatus.class);
    }

    @Override
    public HostUsageResource.HostStatus getNodesStatus(String hostname) {
        String queryMem = "select mean(used_percent) as used_percent from mem where host = '" + hostname + "' and time>now()-10m tz('Asia/Seoul')";
        String queryCpu = "select mean(usage_user) as usage_user from cpu where host = '" + hostname + "' and time>now()-10m tz('Asia/Seoul')";
        String queryDisk = "select mean(used_percent) as used_percent from disk where host = '" + hostname + "' and time>now()-10m tz('Asia/Seoul')";

        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        List<HostUsageResource.MemUsage> memoryPointList = resultMapper.toPOJO(hostInfluxRepository.queryAction(queryMem), HostUsageResource.MemUsage.class);

        List<HostUsageResource.CpuUsage> cpuPointList = resultMapper.toPOJO(hostInfluxRepository.queryAction(queryCpu), HostUsageResource.CpuUsage.class);

        List<HostUsageResource.DiskUsage> diskPointList = resultMapper.toPOJO(hostInfluxRepository.queryAction(queryDisk), HostUsageResource.DiskUsage.class);

        HostUsageResource.HostStatus hostStatus = new HostUsageResource.HostStatus();

        if (memoryPointList.size() > 0) {
            long memPer = memoryPointList.get(0).getUser_percent();
            long cpuPer = cpuPointList.get(0).getUsage_user();
            long diskPer = diskPointList.get(0).getUsed_percent();

            hostStatus.setHostname(hostname);
            hostStatus.setStatus(getStat(memPer, cpuPer, diskPer));
        }

        return hostStatus;
    }

    private String getStat(long memPer, long cpuPer, long diskPer) {
        if (memPer > 80 || cpuPer > 80 || diskPer > 80) {
            return "CRITICAL";
        }
        if (memPer > 60 || cpuPer > 60 || diskPer > 60) {
            return "WARN";
        } else {
            return "GOOD";
        }
    }
}
