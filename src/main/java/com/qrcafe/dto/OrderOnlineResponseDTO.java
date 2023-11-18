package com.qrcafe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class OrderOnlineResponseDTO {
    private Long id;
    private OrderType orderType = OrderType.ONLINE;
    private String orderStatus;
    private String username;
    private String phoneNumber;
    private String location;
    private Double totalPrice;
    private String note;
    private LocalDateTime orderTime;
    private LocalDateTime paymentTime;
    private String paymentMethod;
    @JsonProperty("isPaid")
    private boolean isPaid;
    private List<OrderDetailResponseDTO> orderDetails;
}
