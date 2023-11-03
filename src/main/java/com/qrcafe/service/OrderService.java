package com.qrcafe.service;

import com.qrcafe.dto.OrderDetailRequestDTO;
import com.qrcafe.dto.OrderOfflineRequestDTO;
import com.qrcafe.entity.Order;
import com.qrcafe.entity.OrderDetail;

import java.util.List;

public interface OrderService {

    Order getOrderById(Long orderId);

    List<Order> getOfflineOrders();
    Order addOrderOffline(OrderOfflineRequestDTO orderOfflineRequestDTO);

    Order addOrderOfflineDetails(Order order, List<OrderDetailRequestDTO> orderDetailDTOS);

    Double calcTotalPrice(List<OrderDetailRequestDTO> orderDetailDTOS);

    void addOrUpdateOrderDetail(Order order, OrderDetail newOrderDetail);

}
