package com.qrcafe.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.qrcafe.converter.Converter;
import com.qrcafe.dto.NewOrderDetailResponseDTO;
import com.qrcafe.dto.OrderDetailRequestDTO;
import com.qrcafe.dto.OrderDetailResponseDTO;
import com.qrcafe.dto.OrderOfflineRequestDTO;
import com.qrcafe.dto.OrderOnlineRequestDTO;
import com.qrcafe.dto.WsMessageDTO;
import com.qrcafe.entity.Order;
import com.qrcafe.enums.OrderStatus;
import com.qrcafe.service.ComboService;
import com.qrcafe.service.OrderService;
import com.qrcafe.service.ProductService;

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
    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @GetMapping("/offlineOrders")
    @PreAuthorize("hasAnyAuthority('STAFF', 'ADMIN')")
    public ResponseEntity<?> getOfflineOrders() {
        List<Order> orders = orderService.getOfflineOrders();
        if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No orders");
        } else {
            // orders.stream().map(converter::toOrderOfflineDTO).forEach(System.out::println);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(orders.stream().map(converter::toOrderOfflineResponseDTO).toList());
        }
    }

    @PostMapping("/addOfflineOrder")
    public ResponseEntity<?> addOfflineOrder(@RequestBody OrderOfflineRequestDTO orderOfflineRequestDTO) {
        try {
            Order savedOrder = orderService.addOrderOffline(orderOfflineRequestDTO);
            if (savedOrder == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("This table is UNEMPTY");
            }

            WsMessageDTO messageDTO = WsMessageDTO.builder()
                    .message("NEW_OFFLINE_ORDER")
                    .data(savedOrder.getTable())
                    .build();
            messagingTemplate.convertAndSend("/topic/notify", messageDTO);
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
                Order updatedOrder = orderService.addOrderOfflineDetails(order, orderDetailDTOs);

                // return to WebSocket to FE can see new update detail
                List<OrderDetailResponseDTO> orderDetailResponseDTOS = new ArrayList<>();
                for (OrderDetailRequestDTO orderDetailRequestDTO : orderDetailDTOs) {
                    if (orderDetailRequestDTO.getProductId() != null) {
                        orderDetailResponseDTOS.add(OrderDetailResponseDTO.builder()
                                .productDTO(converter.toProductDTO(
                                        productService.getProductById(orderDetailRequestDTO.getProductId()).get()))
                                .quantity(orderDetailRequestDTO.getQuantity())
                                .build());
                    }
                    if (orderDetailRequestDTO.getComboId() != null) {
                        orderDetailResponseDTOS.add(
                                OrderDetailResponseDTO.builder()
                                        .comboDTO(converter.toComboDTO(
                                                comboService.getComboById(orderDetailRequestDTO.getComboId())))
                                        .quantity(orderDetailRequestDTO.getQuantity())
                                        .build());
                    }
                }
                NewOrderDetailResponseDTO newOrderDetailResponseDTO = NewOrderDetailResponseDTO.builder()
                        .tableName(order.getTable().getName())
                        .tableId(order.getTable().getId())
                        .orderDetails(orderDetailResponseDTOS)
                        .build();
                WsMessageDTO messageDTO = WsMessageDTO.builder()
                        .message("ADD_ORDER_DETAIL")
                        .data(newOrderDetailResponseDTO)
                        .build();
                messagingTemplate.convertAndSend("/topic/notify", messageDTO);
                //

                return ResponseEntity.status(HttpStatus.OK).body(newOrderDetailResponseDTO);
            } catch (Exception e) {
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

    @GetMapping("onlineOrder/{id}")
    public ResponseEntity<?> getOnlineOrdersById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This order is not existed");
        }
        return ResponseEntity.status(HttpStatus.OK).body(converter.toOrderOnlineResponseDTO(order));
    }

    @PutMapping("/updateStatusOrder/{orderId}")
    @PreAuthorize("hasAnyAuthority('STAFF', 'ADMIN')")
    public ResponseEntity<?> updateStatusOrder(@PathVariable Long orderId,
            @RequestBody Map<String, String> requestBody) {

        String orderStatus = requestBody.get("orderStatus");
        if (orderStatus == null) {
            return ResponseEntity.badRequest().body("The 'orderStatus' field is missing in the JSON request.");
        }
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This order is not existed");
        } else {
            order.setStatus(OrderStatus.valueOf(orderStatus.toUpperCase()));
            orderService.save(order);
            return ResponseEntity.status(HttpStatus.OK).body("Update succesfully!!!");
        }
    }

    @GetMapping("/onlineOrders")
    @PreAuthorize("hasAnyAuthority('STAFF', 'ADMIN')")
    public ResponseEntity<?> getAllOnlineOrder() {
        List<Order> orders = orderService.getOnlineOrders();
        if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No orders");
        } else {
            // orders.stream().map(converter::toOrderOfflineDTO).forEach(System.out::println);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(orders.stream().map(converter::toOrderOnlineResponseDTO).toList());
        }
    }

    @GetMapping("/onlineOrderOfUser")
    public ResponseEntity<?> getOnlineOrdersOfUser(Authentication authentication){
        if(authentication == null || !authentication.isAuthenticated()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Let's loginn");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrdersByUsername(username).stream().map(converter::toOrderOnlineResponseDTO).toList());
    }

    @PostMapping("/addOnlineOrder")
    public ResponseEntity<?> addOnlineOrder(@RequestBody OrderOnlineRequestDTO orderOnlineRequestDTO,
            Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Let's loginn");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        try {
            Order savedOrder = orderService.addOrderOnline(orderOnlineRequestDTO, username);
            WsMessageDTO messageDTO = WsMessageDTO.builder()
                    .message("NEW_ONLINE_ORDER")
                    .data(converter.toOrderOnlineResponseDTO(savedOrder))
                    .build();
            messagingTemplate.convertAndSend("/topic/notify", messageDTO);
            return ResponseEntity.status(HttpStatus.OK).body(converter.toOrderOnlineResponseDTO(savedOrder));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/cancelOnlineOrder/{id}")
    public ResponseEntity<?> cancelOnlineOrder(@PathVariable Long id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Let's loginn");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        try {
            orderService.cancelOrderOnline(id, username);
            return ResponseEntity.status(HttpStatus.OK).body("cancel success!!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/getCurrentOrderOfTable/{idTable}")
    public ResponseEntity<?> getCurrentOrderOfTable(@PathVariable UUID idTable) {
        Order order = orderService.getCurrentOrderOfTable(idTable);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No order");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(converter.toOrderOfflineResponseDTO(order));
        }
    }

    @PutMapping("/confirmDoneOrderOfTable/{idOrder}")
    @PreAuthorize("hasAnyAuthority('STAFF', 'ADMIN')")
    public ResponseEntity<?> confirmDoneOrderOfTable(@PathVariable Long idOrder,
                                                     @RequestParam("paymentMethod") String paymentMethod) {
        try {
            orderService.confirmDomeOrderOfTable(idOrder, paymentMethod);
            return ResponseEntity.status(HttpStatus.OK).body("Success!!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/comfirmDoneOnlineOrder/{idOrder}")
    @PreAuthorize("hasAnyAuthority('STAFF', 'ADMIN')")
    public ResponseEntity<?> comfirmDoneOrderOnline(@PathVariable Long idOrder){
        try {
            orderService.comfirmDoneOnlineOrder(idOrder);
            return ResponseEntity.status(HttpStatus.OK).body("Success!!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/getAllOrder")
  //  @PreAuthorize("hasAnyAuthority('STAFF', 'ADMIN')")
    public ResponseEntity<?> getAllOrder() {
        List<Order> orders = orderService.getAllOrder();
        if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No orders");
        } else {
            // orders.stream().map(converter::toOrderOfflineDTO).forEach(System.out::println);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(orders.stream().map(converter::toOrderDTO).toList());
        }
    }

}
