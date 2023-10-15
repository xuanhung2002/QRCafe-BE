package com.qrcafe.service;

import com.qrcafe.entity.Role;
import com.qrcafe.enums.RolesEnum;

public interface RoleService {
    Role getRoleByRoleName(RolesEnum roleName);
}
