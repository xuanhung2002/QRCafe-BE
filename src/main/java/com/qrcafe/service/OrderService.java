package com.qrcafe.service;

import com.qrcafe.dto.*;
import com.qrcafe.entity.Order;
import com.qrcafe.entity.OrderDetail;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    Order getOrderById(Long orderId);

    Order save(Order order);

    List<Order> getOfflineOrders();
    List<Order> getOnlineOrders();
    Order addOrderOffline(OrderOfflineRequestDTO orderOfflineRequestDTO);

    Order addOrderOfflineDetails(Order order, List<OrderDetailRequestDTO> orderDetailDTOS);

    Double calcTotalPrice(List<OrderDetailRequestDTO> orderDetailDTOS);
    Double calcTotalPriceOfCartItems(List<Long> cartItemIds);

    void addOrUpdateOrderDetail(Order order, OrderDetail newOrderDetail);

    Order addOrderOnline(OrderOnlineRequestDTO orderOnlineRequestDTO, String username);

    void cancelOrderOnline(Long id, String username);

    List<Order> getOrdersByUsername(String username);


    Order getCurrentOrderOfTable(UUID idTable);
    void confirmDomeOrderOfTable(Long idOrder, String paymentMethod);
    void comfirmDoneOnlineOrder(Long idOrder);

}
