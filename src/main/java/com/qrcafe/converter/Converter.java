package com.qrcafe.converter;

import com.qrcafe.dto.CommentResponseDTO;
import com.qrcafe.dto.ProductDTO;
import com.qrcafe.entity.Comment;
import com.qrcafe.entity.Product;
import com.qrcafe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    public CommentResponseDTO toCommentResponseDTO(Comment comment){
            CommentResponseDTO commentResponseDTO = CommentResponseDTO.builder()
                    .id(comment.getId())
                    .username(comment.getUser().getUsername())
                    .description(comment.getDescription()).build();
            return commentResponseDTO;
    }
}
