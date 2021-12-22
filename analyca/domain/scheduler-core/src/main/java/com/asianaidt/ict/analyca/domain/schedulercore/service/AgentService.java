package com.asianaidt.ict.analyca.domain.schedulercore.service;

import com.asianaidt.ict.analyca.domain.schedulercore.model.Agent;

import java.util.List;

public interface AgentService {
    void addAgent(String ip, String hostname, String userId, String sessionId);

    void deleteAgentByUserId(String userId);

    void deleteAgentBySessionId(String sessionId);

    List<Agent> findAll();

    /***
     * 엔티티 검색은 IP로 한다.
     * Hostname은 같을 수 있기 때문이다.
     *
     * @param ip        검색을 위한 Agent의 아이피
     * @return
     */
    Agent findByIp(String ip);

    Agent findByIpAndHostname(String ip, String hostname);

    Agent findByUserId(String userId);

    /**
     * 호스트 명으로 검색한다.
     *
     * @param hostName
     * @return
     */
    Agent findByHostname(String hostName);

    void deleteAll();
}
