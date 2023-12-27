package com.qrcafe.repository;

import com.qrcafe.entity.Order;
import com.qrcafe.entity.Table;
import com.qrcafe.enums.OrderStatus;
import com.qrcafe.enums.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> getOrdersByOrderTypeEquals(OrderType orderType);

    List<Order> getOrdersByUserUsername(String user_username);


    Order getOrderByTableAndStatusNot(Table table, OrderStatus status);


}
