package com.qrcafe.service.impl;

import com.qrcafe.converter.Converter;
import com.qrcafe.dto.OrderDetailRequestDTO;
import com.qrcafe.dto.OrderOfflineRequestDTO;
import com.qrcafe.dto.OrderOnlineRequestDTO;
import com.qrcafe.entity.Order;
import com.qrcafe.entity.OrderDetail;
import com.qrcafe.entity.Table;
import com.qrcafe.enums.OrderStatus;
import com.qrcafe.enums.OrderType;
import com.qrcafe.enums.PaymentMethod;
import com.qrcafe.enums.TableStatus;
import com.qrcafe.repository.OrderRepository;
import com.qrcafe.service.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
  @Autowired
  OrderRepository orderRepository;

  @Autowired
  TableService tableService;

  @Autowired
  Converter converter;

  @Autowired
  OrderDetailService orderDetailService;

  @Autowired
  ComboService comboService;

  @Autowired
  ProductService productService;

  @Autowired
  UserService userService;

  @Override
  public Order getOrderById(Long orderId) {
    Optional<Order> orderOpt = orderRepository.findById(orderId);
    return orderOpt.orElse(null);
  }

  @Override
  public Order save(Order order) {
    return orderRepository.save(order);
  }

  @Override
  public List<Order> getOfflineOrders() {
    return orderRepository.getOrdersByOrderTypeEquals(OrderType.OFFLINE);
  }

  @Override
  public List<Order> getOnlineOrders() {
    return orderRepository.getOrdersByOrderTypeEquals(OrderType.ONLINE);
  }

  @Transactional
  @Override
  public Order addOrderOffline(OrderOfflineRequestDTO orderOfflineRequestDTO) {
    if (tableService.getTableById(orderOfflineRequestDTO.getTableId()).getStatus().equals(TableStatus.EMPTY)) {
      Order order = Order.builder()
              .orderType(OrderType.OFFLINE)
              .status(OrderStatus.PENDING)
              .table(tableService.getTableById(orderOfflineRequestDTO.getTableId()))
              .orderTime(LocalDateTime.now())
              .note(orderOfflineRequestDTO.getNote())
              .totalPrice(calcTotalPrice(orderOfflineRequestDTO.getOrderDetails()))
              .isPaid(false)
              .build();
      order.getTable().setStatus(TableStatus.UNEMPTY);

      Order savedOrder = orderRepository.save(order);
      List<OrderDetail> orderDetails = orderOfflineRequestDTO.getOrderDetails().stream().map(t -> converter.toOrderDetailEntity(t)).toList();
      for (OrderDetail orderDetail : orderDetails) {
        orderDetail.setOrder(savedOrder);
      }
      orderDetailService.saveAll(orderDetails);
      savedOrder.setOrderDetails(orderDetails); //to show in API response (it doesn't affect to db)

      return savedOrder;
    } else {
      return null;
    }
  }

  @Transactional
  @Override
  public Order addOrderOfflineDetails(Order order, List<OrderDetailRequestDTO> orderDetailDTOS) {

    //validate
    for (OrderDetailRequestDTO orderDetailDTO : orderDetailDTOS) {
      if (orderDetailDTO.getProductId() == null) {
        if (!comboService.existedById(orderDetailDTO.getComboId())) {
          throw new EntityNotFoundException("this combo is not existedd!!");
        }
      } else if (orderDetailDTO.getComboId() == null) {
        if (!productService.existedById(orderDetailDTO.getProductId())) {
          throw new EntityNotFoundException("this product is not existedd!!");
        }
      }
    }
    //
    List<OrderDetail> newOrderDetails = orderDetailDTOS.stream().map(converter::toOrderDetailEntity).toList();
    for (OrderDetail newOrderDetail : newOrderDetails) {
      addOrUpdateOrderDetail(order, newOrderDetail);
    }
    order.setTotalPrice(order.getTotalPrice() + calcTotalPrice(orderDetailDTOS));
    return orderRepository.save(order);

  }

  @Override
  public void addOrUpdateOrderDetail(Order order, OrderDetail newOrderDetail) {
    boolean foundMatch = false;
    List<OrderDetail> tempOrderDetails = new ArrayList<>();
    if (newOrderDetail.getProduct() != null) {
      for (OrderDetail orderDetail : order.getOrderDetails()) {
        if (orderDetail.getProduct() != null) {
          if (orderDetail.getProduct().equals(newOrderDetail.getProduct())) {
            orderDetail.setQuantity(orderDetail.getQuantity() + newOrderDetail.getQuantity());
            foundMatch = true;
            break;
          }
        }
      }
      if (!foundMatch) {
        OrderDetail addOrderDetail = OrderDetail.builder()
                .order(order)
                .product(newOrderDetail.getProduct())
                .quantity(newOrderDetail.getQuantity()).build();
        tempOrderDetails.add(addOrderDetail);
      }
    } else {
      for (OrderDetail orderDetail : order.getOrderDetails()) {
        if (orderDetail.getCombo() != null) {
          if (orderDetail.getCombo().equals(newOrderDetail.getCombo())) {
            orderDetail.setQuantity(orderDetail.getQuantity() + newOrderDetail.getQuantity());
            foundMatch = true;
            break;
          }
        }
      }
      if (!foundMatch) {
        OrderDetail addOrderDetail = OrderDetail.builder()
                .order(order)
                .combo(newOrderDetail.getCombo())
                .quantity(newOrderDetail.getQuantity()).build();
        tempOrderDetails.add(addOrderDetail);
      }
    }
    order.getOrderDetails().addAll(tempOrderDetails);
  }


  @Override
  public Double calcTotalPrice(List<OrderDetailRequestDTO> orderDetailDTOS) {
    double totalPrice = 0d;
    List<OrderDetail> orderDetails = orderDetailDTOS.stream().map(converter::toOrderDetailEntity).toList();
    for (OrderDetail orderDetail : orderDetails) {
      if (orderDetail.getProduct() != null) {
        totalPrice += orderDetail.getProduct().getPrice() * orderDetail.getQuantity();
      }
      if (orderDetail.getCombo() != null) {
        totalPrice += orderDetail.getCombo().getPrice() * orderDetail.getQuantity();
      }
    }
    return totalPrice;
  }

  @Transactional
  @Override
  public Order addOrderOnline(OrderOnlineRequestDTO orderOnlineRequestDTO, String username) {
    Order order = Order.builder()
            .user(userService.getUserByUsername(username))
            .location(orderOnlineRequestDTO.getLocation())
            .note(orderOnlineRequestDTO.getNote())
            .paymentMethod(PaymentMethod.valueOf(orderOnlineRequestDTO.getPaymentMethod().toUpperCase()))
            .isPaid(orderOnlineRequestDTO.isPaid())
            .orderTime(LocalDateTime.now())
            .paymentTime(orderOnlineRequestDTO.isPaid() ? LocalDateTime.now() : null)
            .status(OrderStatus.PENDING)
            .orderType(OrderType.ONLINE)
            .phoneNumber(orderOnlineRequestDTO.getPhoneNumber())
            .totalPrice(calcTotalPrice(orderOnlineRequestDTO.getOrderDetails()))
            .build();

    Order savedOrder = orderRepository.save(order);
    List<OrderDetail> orderDetails = orderOnlineRequestDTO.getOrderDetails().stream().map(t -> converter.toOrderDetailEntity(t)).toList();
    for (OrderDetail orderDetail : orderDetails) {
      orderDetail.setOrder(savedOrder);
    }
    orderDetailService.saveAll(orderDetails);
    savedOrder.setOrderDetails(orderDetails); //to show in API response (it doesn't affect to db)
    return savedOrder;
  }

  @Transactional
  @Override
  public void cancelOrderOnline(Long id, String username) {
    Optional<Order> orderOpt = orderRepository.findById(id);

    if (orderOpt.isPresent()) {
      Order order = orderOpt.get();
      if (getOrdersByUsername(username).contains(order) && order.getStatus() == OrderStatus.PENDING) {
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
      } else {
        throw new SecurityException("You can not cancel this order");
      }

    } else {
      throw new EntityNotFoundException("This order is not existed!");
    }
  }

  @Override
  public List<Order> getOrdersByUsername(String username) {
    return orderRepository.getOrdersByUserUsername(username);
  }

  @Override
  public Order getCurrentOrderOfTable(UUID idTable) {
    Table table = tableService.getTableById(idTable);
    return orderRepository.getOrderByTableAndStatusNot(table, OrderStatus.DONE);
  }

  @Transactional
  @Override
  public void confirmDomeOrderOfTable(Long idOrder) {
    Order order = getOrderById(idOrder);
    Table table = order.getTable();
    order.setStatus(OrderStatus.DONE);
    table.setStatus(TableStatus.EMPTY);
  }
}
