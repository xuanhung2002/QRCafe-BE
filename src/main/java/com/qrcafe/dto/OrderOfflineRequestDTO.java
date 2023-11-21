package com.qrcafe.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderOfflineRequestDTO {
    private Long id;
    private UUID tableId;
    private String note;
    private List<OrderDetailRequestDTO> orderDetails;
}
