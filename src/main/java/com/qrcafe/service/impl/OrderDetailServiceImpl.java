package com.qrcafe.service.impl;

import com.qrcafe.entity.OrderDetail;
import com.qrcafe.repository.OrderDetailRepository;
import com.qrcafe.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Override
    public List<OrderDetail> saveAll(List<OrderDetail> orderDetails) {
        return orderDetailRepository.saveAll(orderDetails);
    }
}
