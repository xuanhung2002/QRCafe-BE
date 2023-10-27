package com.qrcafe.service;

import com.qrcafe.dto.ComboProductRequestDTO;
import com.qrcafe.entity.Combo;
import com.qrcafe.entity.ComboProductDetails;

import java.util.List;
import java.util.Set;

public interface ComboProductDetailsService {
    ComboProductDetails save(ComboProductDetails comboProductDetails);

    void delete(ComboProductDetails comboProductDetails);

    ComboProductDetails createAndSaveComboProductDetails(Combo combo, ComboProductRequestDTO comboProductRequestDTO);

    List<ComboProductDetails> saveAll(Set<ComboProductDetails> comboProductDetails);

    void deleteByCombo(Combo combo);
}
