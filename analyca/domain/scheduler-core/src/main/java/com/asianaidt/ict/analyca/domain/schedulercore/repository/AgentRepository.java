package com.asianaidt.ict.analyca.domain.schedulercore.repository;

import com.asianaidt.ict.analyca.domain.schedulercore.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
    Agent findByIp(String ip);

    Agent findByHostname(String hostName);

    Agent findByIpAndHostname(String ip, String hostname);

    Optional<Agent> findByUserId(String userId);

    Agent save(Agent agent);
    //List<Agent> findAll();

    void deleteBySessionId(String sessionId);

    void deleteByUserId(String userId);

    @Transactional(value = "schedulerTransactionManager")
    void delete(Agent agent);
}
