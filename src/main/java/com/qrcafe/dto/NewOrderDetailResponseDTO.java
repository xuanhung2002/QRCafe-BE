package com.qrcafe.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NewOrderDetailResponseDTO{
    private UUID tableId;
    List<OrderDetailResponseDTO> orderDetails;
}
