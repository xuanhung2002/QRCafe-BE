package com.qrcafe.service;

import com.qrcafe.entity.CartItem;
import com.qrcafe.entity.Product;

public interface CartItemService {

  CartItem getCartItemById(Long id);

  boolean existById(Long id);
  void deleteById(Long id);

  void save(CartItem cartItem);
  void delete(CartItem cartItem);

}
