package com.qrcafe.dto;

import com.qrcafe.entity.User;
import com.qrcafe.enums.OrderStatus;
import com.qrcafe.enums.OrderType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderOnlineDTO {
    private Long id;
    private OrderType orderType = OrderType.ONLINE;
    private OrderStatus orderStatus;
    private User user;
    private String location;
    private Double totalPrice;
    private String note;
    private LocalDateTime orderTime;
    private LocalDateTime paymentTime;
    private String paymentMethod;
    private boolean isPaid;
    private List<OrderDetailRequestDTO> orderDetail;
}
