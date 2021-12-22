package com.asianaidt.ict.analyca.domain.securitycore.repository;

import com.asianaidt.ict.analyca.domain.securitycore.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUsername(String username);

    @Transactional(value = "securityTransactionManager")
    void deleteByUsername(String username);
}
