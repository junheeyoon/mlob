package com.asianaidt.ict.analyca.domain.dockerdomain;

import com.asianaidt.ict.analyca.domain.dockerdomain.model.DockerMetrics;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class DockerDomainApplicationTests {

    @Test
    @DisplayName("Get Nodes List with Resource Info")
    public void getDockerStat() {
        String influxHost = "http://10.33.194.25:4006";
        InfluxDB influxDB = InfluxDBFactory.connect(influxHost, "admin", "admin123");
        influxDB.setDatabase("docker");

        Query query = new Query(
                "select last(container_name) as container_name, last(container_id) as container_id,last(cpu) as cpu," +
                        "last(total_memory) as total_memory, last(used_memory) as used_memory,last(net_in) as net_in," +
                        "last(net_out) as net_out, last(block_in) as block_in,last(block_out) as block_out, last(host) as host, last(ip) as ip " +
                        "from metrics where time>now()-5m group by host,container_name");
//                        "select mean(used_percent) as used_percent from mem where host = 'ai-big-node04' and time>now()-30s group by time(10m)");
        QueryResult queryResultMem = influxDB.query(query);

        System.out.println(queryResultMem);

        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();

        DockerMetrics dockerMetrics = new DockerMetrics();

        System.out.println(resultMapper.toPOJO(queryResultMem, DockerMetrics.class));

    }
}
