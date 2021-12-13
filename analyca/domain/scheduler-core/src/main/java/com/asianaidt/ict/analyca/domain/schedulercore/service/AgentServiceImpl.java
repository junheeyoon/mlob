package com.asianaidt.ict.analyca.domain.schedulercore.service;

import com.asianaidt.ict.analyca.domain.schedulercore.model.Agent;
import com.asianaidt.ict.analyca.domain.schedulercore.repository.AgentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AgentServiceImpl implements AgentService {

    private final AgentRepository agentRepository;

    public AgentServiceImpl(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    @Override
    public void addAgent(String ip, String hostname, String userId, String sessionId) {
        Agent saved = agentRepository.save(
                Agent.builder()
                        .ip(ip)
                        .hostname(hostname)
                        .userId(userId)
                        .sessionId(sessionId).build()
        );
        System.out.println("saved = " + saved);
    }

    @Override
    @Transactional(value = "schedulerTransactionManager")
    public void deleteAgentByUserId(String userId) {
        agentRepository.deleteByUserId(userId);
        System.out.println("userId = " + userId);
    }

    @Override
    @Transactional(value = "schedulerTransactionManager")
    public void deleteAgentBySessionId(String sessionId) {
        agentRepository.deleteBySessionId(sessionId);
        System.out.println("sessionId = " + sessionId);
    }

    @Override
    public List<Agent> findAll() {
        return agentRepository.findAll();
    }

    @Override
    public Agent findByIp(String ip) {
        Agent agent = agentRepository.findByIp(ip);
        return agent != null ? agent : Agent.builder().hostname("temp").ip("127.0.0.1").build();
    }

    @Override
    public Agent findByIpAndHostname(String ip, String hostname) {
        Agent agent = agentRepository.findByIpAndHostname(ip, hostname);
        return agent;
    }

    @Override
    public Agent findByUserId(String userId) {
        return agentRepository.findByUserId(userId).orElseGet(() -> Agent.builder().hostname("temp").ip("127.0.0.1").build());
    }

    @Override
    public Agent findByHostname(String hostName) {
        return agentRepository.findByHostname(hostName);
    }

    @Override
    public void deleteAll() {
        agentRepository.deleteAll();
    }
}
