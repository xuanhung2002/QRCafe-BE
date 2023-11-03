package com.qrcafe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderDetailResponseDTO {
    @JsonProperty("product")
    private ProductDTO productDTO;
    @JsonProperty("combo")
    private ComboDTO comboDTO;
    private Integer quantity;

}
