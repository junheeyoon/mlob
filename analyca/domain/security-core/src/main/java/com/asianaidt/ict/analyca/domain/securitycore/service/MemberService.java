package com.asianaidt.ict.analyca.domain.securitycore.service;

import com.asianaidt.ict.analyca.domain.securitycore.model.Member;
import com.asianaidt.ict.analyca.domain.securitycore.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberService {
    Member findByUsername(String username);

    void save(Member member);

    Page<Member> findAll(Pageable pageable);

    List<Member> findAll();

    void deleteByUsername(String username);
}
