package com.qrcafe.repository;

import com.qrcafe.entity.Table;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TableRepository extends JpaRepository<Table, UUID> {
    boolean existsByName(String name);
}
