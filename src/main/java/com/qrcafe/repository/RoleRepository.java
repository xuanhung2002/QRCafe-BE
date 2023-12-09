package com.qrcafe.repository;

import com.qrcafe.entity.Role;
import com.qrcafe.enums.RolesEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RolesEnum name);
}
