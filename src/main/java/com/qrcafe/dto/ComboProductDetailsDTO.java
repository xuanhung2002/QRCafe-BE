package com.qrcafe.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ComboProductDetailsDTO {
    private Long id;

    private Integer quantity;

    private ProductDTO product;
}
