package com.asianaidt.ict.analyca.service.hadoopservice;

import com.asianaidt.ict.analyca.domain.hadoopcore.model.read.HdfsDataNodeResource;
import com.asianaidt.ict.analyca.domain.hadoopcore.model.read.HdfsNameNodeFSNameInfoResource;
import com.asianaidt.ict.analyca.domain.hadoopcore.model.read.HdfsNameNodeFSSystemResource;
import com.asianaidt.ict.analyca.domain.hadoopcore.service.HdfsCoreService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class HdfsStateCollector {
    private final HdfsCoreService hdfsCoreService;

    public HdfsStateCollector(HdfsCoreService hdfsCoreService) {
        this.hdfsCoreService = hdfsCoreService;
    }

    public void collect(String url) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectNode hdfsStatJson = restTemplate.getForObject(url, ObjectNode.class);

        if (hdfsStatJson != null) {
            JsonNode hdfsStatJsonBeans = hdfsStatJson.get("beans");
            String liveNodes = hdfsStatJsonBeans.get(27).get("LiveNodes").toString().replaceAll("\\\\", "");
            liveNodes = liveNodes.substring(1, liveNodes.length() - 1);

            List<HdfsDataNodeResource> listHdfsDataNodeResource = new ArrayList<>();

            try {
                Map<String, Map<String, String>> liveNodeMaps =
                        new ObjectMapper().readValue(liveNodes, new TypeReference<Map<String, Map<String, String>>>() {
                        });

                for (String nodeKey : liveNodeMaps.keySet()) {
                    HdfsDataNodeResource hdfsDataNodeResource =
                            new ObjectMapper().convertValue(liveNodeMaps.get(nodeKey), HdfsDataNodeResource.class);
                    hdfsDataNodeResource.setHostname(nodeKey);
                    listHdfsDataNodeResource.add(hdfsDataNodeResource);
                }

                HdfsNameNodeFSNameInfoResource hdfsNameNodeFSNameInfoResource =
                        new ObjectMapper().readValue(hdfsStatJsonBeans.get(27).toString(), HdfsNameNodeFSNameInfoResource.class);
                HdfsNameNodeFSSystemResource hdfsNameNodeFSSystemResource =
                        new ObjectMapper().readValue(hdfsStatJsonBeans.get(33).toString(), HdfsNameNodeFSSystemResource.class);

                hdfsCoreService.write(hdfsNameNodeFSNameInfoResource, hdfsNameNodeFSSystemResource, listHdfsDataNodeResource);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
