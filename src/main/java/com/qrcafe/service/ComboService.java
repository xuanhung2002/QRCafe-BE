package com.qrcafe.service;

import com.qrcafe.dto.ComboDTO;
import com.qrcafe.entity.Combo;

import java.util.List;

public interface ComboService {
    List<Combo> getAllCombos();

    Combo save(Combo combo);

    boolean existedByName(String name);

    Combo getComboById(Long id);

    void delete(Combo combo);
}
