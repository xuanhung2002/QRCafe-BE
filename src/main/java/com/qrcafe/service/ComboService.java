package com.qrcafe.service;

import com.qrcafe.dto.ComboRequestDTO;
import com.qrcafe.entity.Combo;

import java.util.List;

public interface ComboService {
    List<Combo> getAllCombos();

    Combo save(Combo combo);

    boolean existedByName(String name);

    Combo getComboById(Long id);

    void delete(Combo combo);

    void validateComboAddRequestDTO(ComboRequestDTO comboRequestDTO);

    void validateComboUpdateRequestDTO(ComboRequestDTO comboRequestDTO);

    Combo updateCombo(Combo oldCombo, ComboRequestDTO newCombo);

    Combo addCombo(ComboRequestDTO newCombo);

    void deleteCombosByIds(List<Long> comboIds);

}
