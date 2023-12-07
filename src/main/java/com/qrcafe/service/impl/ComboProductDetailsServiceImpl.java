package com.qrcafe.service.impl;

import com.qrcafe.dto.ComboProductRequestDTO;
import com.qrcafe.entity.Combo;
import com.qrcafe.entity.ComboProductDetails;
import com.qrcafe.entity.Product;
import com.qrcafe.repository.ComboProductDetailsRepository;
import com.qrcafe.service.ComboProductDetailsService;
import com.qrcafe.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ComboProductDetailsServiceImpl implements ComboProductDetailsService {
    @Autowired
    ComboProductDetailsRepository comboProductDetailsRepository;
    @Autowired
    ProductService productService;

    @Override
    public ComboProductDetails save(ComboProductDetails comboProductDetails) {
        return comboProductDetailsRepository.save(comboProductDetails);
    }

    @Override
    public void delete(ComboProductDetails comboProductDetails) {
        comboProductDetailsRepository.delete(comboProductDetails);
    }

    @Override
    public ComboProductDetails createAndSaveComboProductDetails(Combo combo, ComboProductRequestDTO comboProductRequestDTO) {
        Optional<Product> productOpt = productService.getProductById(comboProductRequestDTO.getProductId());
        if (productOpt.isPresent()) {
            ComboProductDetails comboProductDetails = new ComboProductDetails();
            comboProductDetails.setProduct(productOpt.get());
            comboProductDetails.setCombo(combo);
            comboProductDetails.setQuantity(comboProductRequestDTO.getQuantity());
            return comboProductDetailsRepository.save(comboProductDetails);
        } else {
            throw new IllegalArgumentException("One or more items is not existed!!");
        }
    }

    @Override
    public List<ComboProductDetails> saveAll(Set<ComboProductDetails> comboProductDetails) {
        return comboProductDetailsRepository.saveAll(comboProductDetails);
    }

    @Override
    public void deleteByCombo(Combo combo) {
        comboProductDetailsRepository.deleteByCombo(combo);
    }


}
