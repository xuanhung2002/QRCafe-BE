package com.qrcafe.service.impl;

import com.qrcafe.dto.ComboProductRequestDTO;
import com.qrcafe.dto.ComboRequestDTO;
import com.qrcafe.entity.Combo;
import com.qrcafe.repository.ComboRepository;
import com.qrcafe.service.ComboProductDetailsService;
import com.qrcafe.service.ComboService;
import com.qrcafe.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ComboServiceImpl implements ComboService {

    @Autowired
    ComboRepository comboRepository;

    @Autowired
    ComboProductDetailsService comboProductDetailsService;

    @Autowired
    ProductService productService;

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

    @Transactional
    @Override
    public void deleteCombosByIds(List<Long> comboIds) {
        if (comboIds.isEmpty()) {
            throw new IllegalArgumentException("Combo ids must not null");
        }
        for (Long id : comboIds
        ) {
            if (!comboRepository.existsById(id)) {
                throw new IllegalArgumentException("Id: '" + id + "' is not existed");
            }
        }
        comboRepository.deleteAllById(comboIds);
    }

    @Override
    public boolean existedById(Long id) {
        return comboRepository.existsById(id);
    }

    @Override
    public void validateComboAddRequestDTO(ComboRequestDTO comboRequestDTO) {
        if (comboRequestDTO.getName() == null || comboRequestDTO.getPrice() == null || comboRequestDTO.getDetailsProducts().isEmpty()) {
            throw new IllegalArgumentException("name and price and details combo must not be null");
        }
        if (comboRepository.existsByName(comboRequestDTO.getName())) {
            throw new IllegalArgumentException("This combo name has already existed!!!");
        }
    }

    @Override
    public void validateComboUpdateRequestDTO(ComboRequestDTO comboRequestDTO) {
        if (comboRequestDTO.getName() == null || comboRequestDTO.getPrice() == null || comboRequestDTO.getDetailsProducts().isEmpty()) {
            throw new IllegalArgumentException("name and price and details combo must not be null");
        }
    }


    @Transactional
    @Override
    public Combo updateCombo(Combo oldCombo, ComboRequestDTO newCombo) {
        try {
            validateComboUpdateRequestDTO(newCombo);
            oldCombo.setName(newCombo.getName());
            oldCombo.setPrice(newCombo.getPrice());
            oldCombo.setDescription(newCombo.getDescription());

            //delete old combo product details
            comboProductDetailsService.deleteByCombo(oldCombo);

            for (ComboProductRequestDTO comboProductRequestDTO : newCombo.getDetailsProducts()) {
                //add new combo product details
                comboProductDetailsService.createAndSaveComboProductDetails(oldCombo, comboProductRequestDTO);
            }
            return comboRepository.save(oldCombo);
        } catch (Exception e) {
            throw new RuntimeException("Update failed: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public Combo addCombo(ComboRequestDTO newCombo) {
        try {
            validateComboAddRequestDTO(newCombo);
            Combo savedCombo = comboRepository.save(Combo.builder()
                    .name(newCombo.getName())
                    .price(newCombo.getPrice())
                    .description(newCombo.getDescription()).build());

            for (ComboProductRequestDTO comboProductRequestDTO : newCombo.getDetailsProducts()) {
                //add new combo product details
                comboProductDetailsService.createAndSaveComboProductDetails(savedCombo, comboProductRequestDTO);
            }
        } catch (Exception e) {
            throw new RuntimeException("Add failed: " + e.getMessage());
        }
        return null;
    }


}
