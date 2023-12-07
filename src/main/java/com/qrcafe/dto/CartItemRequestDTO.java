package com.qrcafe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemRequestDTO {
    private Long id;
    private Long productId;
    private Long comboId;
    private int quantity;
}
