package com.qrcafe.repository;

import com.qrcafe.entity.Combo;
import com.qrcafe.entity.ComboProductDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComboProductDetailsRepository extends JpaRepository<ComboProductDetails, Long> {
    void deleteByCombo(Combo combo);
}
