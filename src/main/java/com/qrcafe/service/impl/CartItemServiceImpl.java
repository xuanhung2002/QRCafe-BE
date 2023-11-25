package com.qrcafe.service.impl;

import com.qrcafe.entity.CartItem;
import com.qrcafe.entity.Product;
import com.qrcafe.repository.CartItemRepository;
import com.qrcafe.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartItemServiceImpl implements CartItemService {
  @Autowired
  CartItemRepository cartItemRepository;

  @Override
  public CartItem getCartItemById(Long id) {
    return cartItemRepository.findById(id).orElse(null);
  }

  @Override
  public boolean existById(Long id) {
    return cartItemRepository.existsById((id));
  }

  @Override
  public void deleteById(Long id) {
    cartItemRepository.deleteById(id);
  }

  @Override
  public Object getItem(Long id) {
    Product product = cartItemRepository.getProductByCartItemId(id);
    if (product != null) {
      return product;
    } else {
      return cartItemRepository.getComboByCartItemId(id);
    }
  }

  @Override
  public void save(CartItem cartItem) {
    cartItemRepository.save(cartItem);
  }

  @Override
  public void delete(CartItem cartItem) {
    cartItemRepository.delete(cartItem);
  }

}
