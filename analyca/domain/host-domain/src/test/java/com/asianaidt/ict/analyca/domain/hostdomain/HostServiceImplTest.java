package com.asianaidt.ict.analyca.domain.hostdomain;

import com.asianaidt.ict.analyca.domain.hostdomain.model.HostCpuStatus;
import com.asianaidt.ict.analyca.domain.hostdomain.model.HostDiskStatus;
import com.asianaidt.ict.analyca.domain.hostdomain.model.HostMemStatus;
import com.asianaidt.ict.analyca.domain.hostdomain.model.HostUsageResource;
import com.asianaidt.ict.analyca.domain.hostdomain.service.HostService;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HostServiceImplTest {


    @Test
    @DisplayName("Get Nodes List with Resource Info")
    public void getNodesResource() {
        String influxHost = "http://10.33.194.25:4006";
        InfluxDB influxDB = InfluxDBFactory.connect(influxHost, "admin", "admin123");
        influxDB.setDatabase("telegraf");

        Query query = new Query(
                "select mean(used_percent) as used_percent from mem where host = 'ai-big-node03' and time>now()-30s group by time(10m)");
//                        "select mean(used_percent) as used_percent from mem where host = 'ai-big-node04' and time>now()-30s group by time(10m)");
        QueryResult queryResultMem = influxDB.query(query);

        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        List<HostUsageResource.MemUsage> memoryPointList = resultMapper.toPOJO(queryResultMem, HostUsageResource.MemUsage.class);

        HostUsageResource.HostStatus hostStatus = new HostUsageResource.HostStatus();


        System.out.println(memoryPointList.get(0).getUser_percent());
//        HostUsageResource.MemUsage memUsage = memoryPointList.get(0);

    }

    @Test
    @DisplayName("InFluxDB Select Query Test")
    public void getCPUData() {
        String influxHost = "http://10.33.194.25:4006";
        InfluxDB influxDB = InfluxDBFactory.connect(influxHost, "admin", "admin123");
        influxDB.setDatabase("telegraf");

        /**
         * Get Memory Query
         */
//        Query queryMem = new Query("select total, used, time from mem where host='ai-big-node04' and time>now()-6h");
//        QueryResult queryResultMem = influxDB.query(queryMem);
//
//        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
//        List<HostMemStatus> memoryPointList = resultMapper.toPOJO(queryResultMem, HostMemStatus.class);
//
//        System.out.println(memoryPointList);

        /**
         * Get Cpu Query
         * d : day
         * m : minute
         * h : hour
         */
//        Query queryCpu = new Query("select usage_user, time from cpu where host='ai-big-node04' and time>now()-1m tz('Asia/Seoul')");
//        QueryResult queryResultCpu = influxDB.query(queryCpu);
//
//        InfluxDBResultMapper resultMapperCpu = new InfluxDBResultMapper();
//        List<HostCpuStatus> cpuPointList = resultMapperCpu.toPOJO(queryResultCpu, HostCpuStatus.class);
//
//        System.out.println(cpuPointList);

        /**
         * Get Disk Query
         */
        Query queryDisk = new Query("select (sum(total)/1024/1024/1024) as disktotal, (sum(used)/1024/1024/1024) as diskused, time from (select total, used, time, device from disk where host='ai-big-node04' and time>now()-6h group by total, used, device) group by total, used,time(10s) tz('Asia/Seoul')");
        QueryResult queryResultDisk = influxDB.query(queryDisk);

        System.out.println(queryResultDisk);

        InfluxDBResultMapper resultMapperDisk = new InfluxDBResultMapper();
        List<HostDiskStatus> diskPointList = resultMapperDisk.toPOJO(queryResultDisk, HostDiskStatus.class);
        System.out.println(diskPointList);
//
//        influxDB.close();

    }

    @Mock
    HostService hostService;

    @Test
    @DisplayName("InFluxDB Select Service Test")
    public void getResourceInfo() {
        System.out.println(hostService.cpuList("ai-big-node04", "10m"));
    }
}
