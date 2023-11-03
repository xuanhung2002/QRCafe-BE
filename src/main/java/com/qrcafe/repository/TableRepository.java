package com.qrcafe.repository;

import com.qrcafe.entity.Table;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<Table, Long> {
    boolean existsByName(String name);
}
