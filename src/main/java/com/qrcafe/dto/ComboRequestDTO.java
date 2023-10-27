package com.qrcafe.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ComboRequestDTO {
    private String name;
    private Double price;
    private String description;
    Set<ComboProductRequestDTO> detailsProducts;
}
