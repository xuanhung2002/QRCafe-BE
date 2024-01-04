package com.qrcafe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qrcafe.enums.OrderType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderOfflineResponseDTO {
    private Long id;
    private OrderType orderType = OrderType.OFFLINE;
    private String orderStatus;
    private UUID tableId;
    private String tableName;
    private Double totalPrice;
    private String note;
    private String paymentMethod;
    private LocalDateTime orderTime;
    private LocalDateTime paymentTime;
    @JsonProperty("isPaid")
    private boolean isPaid;
    private List<OrderDetailResponseDTO> orderDetails;
}
