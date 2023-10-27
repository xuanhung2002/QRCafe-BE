package com.qrcafe.service.impl;

import com.qrcafe.dto.ComboDTO;
import com.qrcafe.entity.Combo;
import com.qrcafe.repository.ComboRepository;
import com.qrcafe.service.ComboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComboServiceImpl implements ComboService {

    @Autowired
    ComboRepository comboRepository;

    @Override
    public List<Combo> getAllCombos() {
        return comboRepository.findAll();
    }

    @Override
    public Combo save(Combo combo) {
        return comboRepository.save(combo);
    }

    @Override
    public boolean existedByName(String name) {
        return comboRepository.existsByName(name);
    }

    @Override
    public Combo getComboById(Long id) {
        Optional<Combo> comboOpt = comboRepository.findById(id);
        return comboOpt.orElse(null);
    }

    @Override
    public void delete(Combo combo) {
        comboRepository.delete(combo);
    }
}
