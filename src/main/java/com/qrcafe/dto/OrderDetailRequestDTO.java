package com.qrcafe.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderDetailRequestDTO {
    private Long productId;

    private Long comboId;

    private Integer quantity;

}
