package com.qrcafe.service.impl;

import com.qrcafe.entity.ComboProductDetails;
import com.qrcafe.repository.ComboProductDetailsRepository;
import com.qrcafe.service.ComboProductDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComboProductDetailsServiceImpl implements ComboProductDetailsService {
    @Autowired
    ComboProductDetailsRepository comboProductDetailsRepository;

    @Override
    public ComboProductDetails save(ComboProductDetails comboProductDetails) {
        return comboProductDetailsRepository.save(comboProductDetails);
    }

    @Override
    public void delete(ComboProductDetails comboProductDetails) {
        comboProductDetailsRepository.delete(comboProductDetails);
    }
}
