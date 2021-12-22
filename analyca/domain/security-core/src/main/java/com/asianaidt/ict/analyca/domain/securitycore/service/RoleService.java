package com.asianaidt.ict.analyca.domain.securitycore.service;

import com.asianaidt.ict.analyca.domain.securitycore.model.Role;

import java.util.List;

public interface RoleService {
    Role findByName(String name);

    List<Role> findAll();
}
