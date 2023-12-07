package com.qrcafe.dto;

import com.qrcafe.entity.Category;
import com.qrcafe.entity.Image;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProductDTO {

    private Long id;

    private String name;

    private Double price;

    private String description;

    private Integer amount;

    private Category category;

    private List<Image> images;
}
