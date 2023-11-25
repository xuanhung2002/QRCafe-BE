package com.qrcafe.service;

import com.qrcafe.dto.CartItemRequest;
import com.qrcafe.entity.Cart;
import com.qrcafe.entity.CartItem;

import java.util.List;

public interface CartService {
  Cart getCartByUsername(String username);
  void addCartItemToCart(String username, CartItemRequest cartItemRequest);
  void updateCartItemQuantity(String username, Long cartItemId, Integer quantity);

  List<CartItem> getCartItemsByUsername(String username);
  void createCartForUser(String username);
  void handleAddCartItemToCart(String username, CartItemRequest cartItemRequest);
  void handleAddProductToCart(String username, CartItemRequest cartItemRequest);
  void handleAddComboToCart(String username, CartItemRequest cartItemRequest);
  void deleteCartItem(Long id, String username);
}
