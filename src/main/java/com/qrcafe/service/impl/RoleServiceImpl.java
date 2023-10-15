package com.qrcafe.service.impl;

import com.qrcafe.entity.Role;
import com.qrcafe.enums.RolesEnum;
import com.qrcafe.repository.RoleRepository;
import com.qrcafe.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepository;
    @Override
    public Role getRoleByRoleName(RolesEnum roleName) {
        return roleRepository.findByName(roleName);
    }
}
