package com.asianaidt.ict.analyca.domain.dockerdomain.repository;

import com.asianaidt.ict.analyca.domain.dockerdomain.model.ContainerPS;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ContainerPSRepository {
    private ConcurrentHashMap<String, ConcurrentHashMap<String, ContainerPS>> containerPSMapByHostname = new ConcurrentHashMap<>();

    public void update(String hostname, List<ContainerPS> containers) {
        if (!containerPSMapByHostname.containsKey(hostname)) {
            containerPSMapByHostname.put(hostname, new ConcurrentHashMap<>());
        }
        ConcurrentHashMap<String, ContainerPS> containerPSMap = containerPSMapByHostname.get(hostname);
        containerPSMap.clear();
        containers.forEach(container -> containerPSMap.put(container.getContainerName(), container));
    }

    public List<ContainerPS> findAll() {
        List<ContainerPS> containers = new ArrayList<>();
        containerPSMapByHostname.keySet().forEach(hostname -> containers.addAll(findByHostName(hostname)));
        return containers;
    }

    public List<ContainerPS> findByHostName(String hostname) {
        return new ArrayList<>(containerPSMapByHostname.getOrDefault(hostname, new ConcurrentHashMap<>()).values());
    }
}