package com.qrcafe.controller;

import com.qrcafe.converter.Converter;
import com.qrcafe.dto.NewOrderDetailResponseDTO;
import com.qrcafe.dto.OrderDetailRequestDTO;
import com.qrcafe.dto.OrderDetailResponseDTO;
import com.qrcafe.dto.OrderOfflineRequestDTO;
import com.qrcafe.entity.Order;
import com.qrcafe.service.ComboService;
import com.qrcafe.service.OrderService;
import com.qrcafe.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    Converter converter;
    @Autowired
    ProductService productService;
    @Autowired
    ComboService comboService;

    @GetMapping("/offlineOrders")
    public ResponseEntity<?> getOfflineOrders() {
        List<Order> orders = orderService.getOfflineOrders();
        if (orders == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No orders");
        } else {
//            orders.stream().map(converter::toOrderOfflineDTO).forEach(System.out::println);
            return ResponseEntity.status(HttpStatus.OK).body(orders.stream().map(converter::toOrderOfflineResponseDTO).toList());
        }
    }

    @PostMapping("/addOfflineOrder")
    public ResponseEntity<?> addOfflineOrder(@RequestBody OrderOfflineRequestDTO orderOfflineRequestDTO) {
        try {
            Order savedOrder = orderService.addOrderOffline(orderOfflineRequestDTO);
            if (savedOrder == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("This table is UNEMPTY");
            }
            return ResponseEntity.status(HttpStatus.OK).body(converter.toOrderOfflineResponseDTO(savedOrder));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/addOrderOfflineDetails/{orderId}")
    public ResponseEntity<?> addOrderOfflineDetails(@PathVariable Long orderId,
                                                    @RequestBody List<OrderDetailRequestDTO> orderDetailDTOs) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This order is not existed");
        } else {
            try {
                Order updatedOrder =  orderService.addOrderOfflineDetails(order, orderDetailDTOs);

                //return to WebSocket to FE can see new update detail
                List<OrderDetailResponseDTO> orderDetailResponseDTOS = new ArrayList<>();
                for (OrderDetailRequestDTO orderDetailRequestDTO : orderDetailDTOs){
                    if(orderDetailRequestDTO.getProductId() != null){
                      orderDetailResponseDTOS.add(OrderDetailResponseDTO.builder()
                              .productDTO(converter.toProductDTO(productService.getProductById(orderDetailRequestDTO.getProductId()).get()))
                              .quantity(orderDetailRequestDTO.getQuantity())
                              .build()
                      );
                    }
                    if(orderDetailRequestDTO.getComboId() != null){
                      orderDetailResponseDTOS.add(
                              OrderDetailResponseDTO.builder()
                                  .comboDTO(converter.toComboDTO(comboService.getComboById(orderDetailRequestDTO.getComboId())))
                                  .quantity(orderDetailRequestDTO.getQuantity())
                                  .build()
                        );
                    }
                }
                NewOrderDetailResponseDTO newOrderDetailResponseDTO = NewOrderDetailResponseDTO.builder()
                        .tableId(order.getTable().getId())
                        .orderDetails(orderDetailResponseDTOS)
                        .build();
                return ResponseEntity.status(HttpStatus.OK).body(newOrderDetailResponseDTO);
            }
            catch (Exception e){
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrdersById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This order is not existed");
        }
        return ResponseEntity.status(HttpStatus.OK).body(converter.toOrderOfflineResponseDTO(order));
    }
}
