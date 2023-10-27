package com.qrcafe.converter;

import com.qrcafe.dto.ComboDTO;
import com.qrcafe.dto.ComboProductDetailsDTO;
import com.qrcafe.dto.CommentResponseDTO;
import com.qrcafe.dto.ProductDTO;
import com.qrcafe.entity.Combo;
import com.qrcafe.entity.ComboProductDetails;
import com.qrcafe.entity.Comment;
import com.qrcafe.entity.Product;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

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

    public CommentResponseDTO toCommentResponseDTO(Comment comment){
            CommentResponseDTO commentResponseDTO = CommentResponseDTO.builder()
                    .id(comment.getId())
                    .username(comment.getUser().getUsername())
                    .description(comment.getDescription()).build();
            return commentResponseDTO;
    }

    public ComboProductDetailsDTO toComboProductDetailsDTO(ComboProductDetails comboProductDetails){
        ComboProductDetailsDTO comboProductDetailsDTO = ComboProductDetailsDTO.builder()
                .id(comboProductDetails.getId())
                .quantity(comboProductDetails.getQuantity())
                .product(toProductDTO(comboProductDetails.getProduct())).build();
        return comboProductDetailsDTO;
    }
    public ComboDTO toComboDTO(Combo combo){
        Set<ComboProductDetailsDTO> ComboProductDetailsDTOs = combo.getComboProductDetails().stream().map(this::toComboProductDetailsDTO).collect(Collectors.toSet());
        ComboDTO comboDTO = ComboDTO.builder()
                .id(combo.getId())
                .name(combo.getName())
                .price(combo.getPrice())
                .description(combo.getDescription())
                .detailsProducts(ComboProductDetailsDTOs).build();
        return comboDTO;
    }
}
