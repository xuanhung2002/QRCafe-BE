package com.qrcafe.repository;

import com.qrcafe.entity.Role;
import com.qrcafe.entity.User;
import com.qrcafe.enums.RolesEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    boolean existsByUsername(String username);

    User findUserByEmail(String email);

    @Query("SELECT u.roles FROM User u WHERE u.username = :username")
    List<Role> findRolesByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :role")
    List<User> findUsersByRole(@Param("role") RolesEnum role);

}
