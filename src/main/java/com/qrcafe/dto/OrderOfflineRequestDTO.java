package com.qrcafe.dto;

import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderOfflineRequestDTO {
    private Long id;
    private Long tableId;
    private Double totalPrice;
    private String note;
    private List<OrderDetailRequestDTO> orderDetails;
}
