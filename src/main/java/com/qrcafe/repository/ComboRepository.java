package com.qrcafe.repository;

import com.qrcafe.entity.Combo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComboRepository extends JpaRepository<Combo, Long> {
    boolean existsByName(String name);
}
