package com.qrcafe.service;

import com.qrcafe.entity.ComboProductDetails;

public interface ComboProductDetailsService {
    ComboProductDetails save(ComboProductDetails comboProductDetails);

    void delete(ComboProductDetails comboProductDetails);
}
