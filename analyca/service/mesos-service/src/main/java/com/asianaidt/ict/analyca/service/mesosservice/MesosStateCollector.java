package com.asianaidt.ict.analyca.service.mesosservice;

import com.asianaidt.ict.analyca.domain.mesosstatcore.model.read.MesosResource;
import com.asianaidt.ict.analyca.domain.mesosstatcore.service.MesosCoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MesosStateCollector {
    private final MesosCoreService mesosCoreService;

    public MesosStateCollector(MesosCoreService mesosCoreService) {
        this.mesosCoreService = mesosCoreService;
    }

    public void collect(String url) {
        RestTemplate restTemplate = new RestTemplate();
        String mesosStateJson = restTemplate.getForObject(url, String.class);
        try {
            if (mesosStateJson != null)
                mesosCoreService.write(new ObjectMapper().readValue(mesosStateJson, MesosResource.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
