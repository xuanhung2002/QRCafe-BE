package com.qrcafe.service;

import com.qrcafe.dto.CartItemRequestDTO;
import com.qrcafe.entity.Cart;
import com.qrcafe.entity.CartItem;

import java.util.List;

public interface CartService {
  Cart getCartByUsername(String username);
  void addCartItemToCart(String username, CartItemRequestDTO cartItemRequest);
  void updateCartItemQuantity(String username, Long cartItemId, Integer quantity);

  List<CartItem> getCartItemsByUsername(String username);
  void createCartForUser(String username);
  void handleAddCartItemToCart(String username, CartItemRequestDTO cartItemRequest);
  void handleAddProductToCart(String username, CartItemRequestDTO cartItemRequest);
  void handleAddComboToCart(String username, CartItemRequestDTO cartItemRequest);

  void deleteCartItem(Long id, String username);
}
