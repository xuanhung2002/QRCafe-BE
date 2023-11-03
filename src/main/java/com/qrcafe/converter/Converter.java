package com.qrcafe.converter;

import com.qrcafe.dto.*;
import com.qrcafe.entity.*;
import com.qrcafe.service.ComboService;
import com.qrcafe.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class Converter {
    @Autowired
    ProductService productService;
    @Autowired
    ComboService comboService;

    public ProductDTO toProductDTO(Product product){
        return ProductDTO.builder().
                id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .amount(product.getAmount())
                .category(product.getCategory())
                .images(product.getImages()).build();
    }

    public CommentResponseDTO toCommentResponseDTO(Comment comment){
        return CommentResponseDTO.builder()
                    .id(comment.getId())
                    .username(comment.getUser().getUsername())
                    .description(comment.getDescription()).build();
    }

    public ComboProductDetailsDTO toComboProductDetailsDTO(ComboProductDetails comboProductDetails){
        return ComboProductDetailsDTO.builder()
                .id(comboProductDetails.getId())
                .quantity(comboProductDetails.getQuantity())
                .product(toProductDTO(comboProductDetails.getProduct())).build();
    }
    public ComboDTO toComboDTO(Combo combo){
        Set<ComboProductDetailsDTO> ComboProductDetailsDTOs = combo.getComboProductDetails().stream().map(this::toComboProductDetailsDTO).collect(Collectors.toSet());
        return ComboDTO.builder()
                .id(combo.getId())
                .name(combo.getName())
                .price(combo.getPrice())
                .description(combo.getDescription())
                .detailsProducts(ComboProductDetailsDTOs).build();
    }

    public OrderDetail toOrderDetailEntity(OrderDetailRequestDTO orderDetailRequestDTO){
        if(orderDetailRequestDTO.getProductId() == null && comboService.existedById(orderDetailRequestDTO.getComboId())){
            return OrderDetail.builder()
                    .combo(comboService.getComboById(orderDetailRequestDTO.getComboId()))
                    .quantity(orderDetailRequestDTO.getQuantity())
                    .build();
        }
        else if(orderDetailRequestDTO.getComboId() == null && productService.existedById(orderDetailRequestDTO.getProductId())){
            return OrderDetail.builder()
                    .product(productService.getProductById(orderDetailRequestDTO.getProductId()).orElse(null))
                    .quantity(orderDetailRequestDTO.getQuantity())
                    .build();
        }
        else {
            throw new IllegalArgumentException("can not convert OrderDetailDTO to OrderDetailEntity because dont have any product or combo");
        }

    }

    public OrderDetailResponseDTO toOrderDetailResponseDTO(OrderDetail orderDetail){
        if(orderDetail.getCombo() == null && orderDetail.getProduct() != null){
            return OrderDetailResponseDTO.builder()
                    .productDTO(toProductDTO(orderDetail.getProduct()))
                    .quantity(orderDetail.getQuantity())
                    .build();
        } else if (orderDetail.getProduct() == null && orderDetail.getCombo() != null) {
            return OrderDetailResponseDTO.builder()
                    .comboDTO(toComboDTO(orderDetail.getCombo()))
                    .quantity(orderDetail.getQuantity())
                    .build();
        }
        else {
            throw new EntityNotFoundException("product or combo is not existed");
        }
    }


    public OrderOfflineResponseDTO toOrderOfflineResponseDTO(Order order){
        return OrderOfflineResponseDTO.builder()
                .id(order.getId())
                .orderType(order.getOrderType())
                .orderStatus(order.getStatus().name())
                .note(order.getNote())
                .orderTime(order.getOrderTime())
                .paymentTime(order.getPaymentTime())
                .paymentMethod(order.getPaymentMethod())
                .isPaid(order.isPaid())
                .totalPrice(order.getTotalPrice())
                .tableId(order.getTable().getId())
                .orderDetails(order.getOrderDetails().stream().map(this::toOrderDetailResponseDTO).collect(Collectors.toList()))
                .build();
    }
}
