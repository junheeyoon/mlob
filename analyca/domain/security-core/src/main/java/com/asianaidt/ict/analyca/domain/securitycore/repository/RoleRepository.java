package com.asianaidt.ict.analyca.domain.securitycore.repository;

import com.asianaidt.ict.analyca.domain.securitycore.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
