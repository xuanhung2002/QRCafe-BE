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
                .paymentMethod(order.getPaymentMethod() != null ? order.getPaymentMethod().toString() : null)
                .isPaid(order.isPaid())
                .totalPrice(order.getTotalPrice())
                .tableId(order.getTable().getId())
                .orderDetails(order.getOrderDetails().stream().map(this::toOrderDetailResponseDTO).collect(Collectors.toList()))
                .build();
    }

    public OrderOnlineResponseDTO toOrderOnlineResponseDTO(Order order){
        return OrderOnlineResponseDTO.builder()
                .id(order.getId())
                .username(order.getUser().getUsername())
                .orderType(order.getOrderType())
                .orderStatus(order.getStatus().name())
                .note(order.getNote())
                .orderTime(order.getOrderTime())
                .paymentTime(order.getPaymentTime())
                .paymentMethod(order.getPaymentMethod().toString())
                .isPaid(order.isPaid())
                .totalPrice(order.getTotalPrice())
                .location(order.getLocation())
                .phoneNumber(order.getPhoneNumber())
                .orderDetails(order.getOrderDetails().stream().map(this::toOrderDetailResponseDTO).collect(Collectors.toList()))
                .build();
    }

    public CartItemDTO toCartItemDTO(CartItem cartItem){
        if(cartItem.getProduct() != null){
            return CartItemDTO.builder()
                    .id(cartItem.getId())
                    .productId(cartItem.getProduct().getId())
                    .comboId(null)
                    .itemImages(cartItem.getProduct().getImages().stream().map(Image::getImageUrl).toList())
                    .nameItem(cartItem.getProduct().getName())
                    .unitPrice(cartItem.getProduct().getPrice())
                    .quantity(cartItem.getQuantity())
                    .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getPrice())
                    .build();
        }
        else {
            return CartItemDTO.builder()
                    .id(cartItem.getId())
                    .productId(null)
                    .comboId(cartItem.getCombo().getId())
                    .itemImages(cartItem.getCombo().getComboProductDetails()
                            .stream()
                            .map(t -> t.getProduct().getImages())
                            .flatMap(t -> t.stream().map(Image::getImageUrl)).toList())
                    .nameItem(cartItem.getCombo().getName())
                    .unitPrice(cartItem.getCombo().getPrice())
                    .quantity(cartItem.getQuantity())
                    .totalPrice(cartItem.getQuantity() * cartItem.getCombo().getPrice())
                    .build();
        }
    }

    public UserLocationDTO toUserLocationDTO(UserLocation userLocation) {
        return UserLocationDTO.builder()
                .id(userLocation.getId())
                .address(userLocation.getAddress())
                .fullName(userLocation.getFullName())
                .phoneNumber(userLocation.getPhoneNumber())
                .build();
    }

    public UserDTO toUserDTO(User user){
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth())
                .fullName(user.getFullName())
                .roles(user.getRoles().stream().map(Role::getName).toList())
                .build();
    }
}
