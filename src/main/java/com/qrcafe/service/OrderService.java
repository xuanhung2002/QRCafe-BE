package com.qrcafe.service;

import com.qrcafe.dto.OrderDetailRequestDTO;
import com.qrcafe.dto.OrderOfflineRequestDTO;
import com.qrcafe.dto.OrderOnlineRequestDTO;
import com.qrcafe.entity.Order;
import com.qrcafe.entity.OrderDetail;

import java.util.List;

public interface OrderService {

    Order getOrderById(Long orderId);

    Order save(Order order);

    List<Order> getOfflineOrders();
    List<Order> getOnlineOrders();
    Order addOrderOffline(OrderOfflineRequestDTO orderOfflineRequestDTO);

    Order addOrderOfflineDetails(Order order, List<OrderDetailRequestDTO> orderDetailDTOS);

    Double calcTotalPrice(List<OrderDetailRequestDTO> orderDetailDTOS);

    void addOrUpdateOrderDetail(Order order, OrderDetail newOrderDetail);

    Order addOrderOnline(OrderOnlineRequestDTO orderOnlineRequestDTO, String username);

    void cancelOrderOnline(Long id, String username);

    List<Order> getOrdersByUsername(String username);


    Order getCurrentOrderOfTable(Long idTable);
    void confirmDomeOrderOfTable(Long idOrder);
}
