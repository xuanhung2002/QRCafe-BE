package com.qrcafe.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NewOrderDetailResponseDTO{
    private Long tableId;
    List<OrderDetailResponseDTO> orderDetails;
}
