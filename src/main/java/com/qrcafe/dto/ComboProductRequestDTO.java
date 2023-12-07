package com.qrcafe.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ComboProductRequestDTO {
    private Integer quantity;
    private Long productId;
}
