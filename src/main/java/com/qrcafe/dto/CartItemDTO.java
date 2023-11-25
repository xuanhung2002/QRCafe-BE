package com.qrcafe.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CartItemDTO {
  private Long id;

  private Long productId;

  private Long comboId;

  private List<String> itemImages;

  private String nameItem;

  private Double unitPrice;

  private Integer quantity;

  private Double totalPrice;
}
