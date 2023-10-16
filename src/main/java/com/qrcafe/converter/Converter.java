package com.qrcafe.converter;

import com.qrcafe.dto.ProductDTO;
import com.qrcafe.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class Converter {

    public ProductDTO toProductDTO(Product product){
        ProductDTO productDTO = ProductDTO.builder().
                id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .amount(product.getAmount())
                .category(product.getCategory())
                .images(product.getImages()).build();
    return productDTO;
    }
}
