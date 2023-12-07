package com.qrcafe.service;

import com.qrcafe.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetail> saveAll(List<OrderDetail> orderDetails);
}
