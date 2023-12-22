package com.qrcafe.dto;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
