package com.qrcafe.service.impl;

import com.qrcafe.dto.CartItemRequestDTO;
import com.qrcafe.entity.Cart;
import com.qrcafe.entity.CartItem;
import com.qrcafe.entity.Combo;
import com.qrcafe.entity.Product;
import com.qrcafe.repository.CartRepository;
import com.qrcafe.service.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

  @Autowired
  CartRepository cartRepository;

  @Autowired
  UserService userService;

  @Autowired
  CartItemService cartItemService;

  @Autowired
  ProductService productService;

  @Autowired
  ComboService comboService;

  @Override
  public Cart getCartByUsername(String username) {
    return cartRepository.getCartByUsername(username);
  }

  @Transactional
  @Override
  public void addCartItemToCart(String username, CartItemRequestDTO cartItemRequest) {
    if (cartItemRequest.getProductId() != null && cartItemRequest.getComboId() == null) {
      Optional<Product> productOpt = productService.getProductById(cartItemRequest.getProductId());
      if (productOpt.isPresent()) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cartRepository.getCartByUsername(username));
        cartItem.setProduct(productOpt.get());
        cartItem.setQuantity(cartItemRequest.getQuantity());
        cartItemService.save(cartItem);
      }
      else {
        throw new EntityNotFoundException("product is not existed");
      }
    } else if (cartItemRequest.getProductId() == null && cartItemRequest.getComboId() != null) {
      Combo combo = comboService.getComboById(cartItemRequest.getComboId());
      if(combo == null){
        throw new EntityNotFoundException("combo is not existed");
      }
      CartItem cartItem = new CartItem();
      cartItem.setCart(cartRepository.getCartByUsername(username));
      cartItem.setCombo(combo);
      cartItem.setQuantity(cartItemRequest.getQuantity());
      cartItemService.save(cartItem);
    } else {
      throw new EntityNotFoundException("product's id or combo's id is not existed");
    }
  }

  @Override
  public void updateCartItemQuantity(String username, Long cartItemId, Integer newQuantity) {
    CartItem cartItem = cartItemService.getCartItemById(cartItemId);
      if (newQuantity > 0) {
        cartItem.setQuantity(newQuantity);
        cartItemService.save(cartItem);
      } else {
        cartItemService.delete(cartItem);
      }
  }

  @Override
  public List<CartItem> getCartItemsByUsername(String username) {
    return cartRepository.getCartItemsByUsername(username);
  }

  @Override
  public void createCartForUser(String username) {
    if (getCartByUsername(username) == null) {
      Cart cartToSave = new Cart();
      cartToSave.setUser(userService.getUserByUsername(username));
      cartRepository.save(cartToSave);
    }
  }

  @Transactional
  @Override
  public void handleAddCartItemToCart(String username, CartItemRequestDTO cartItemRequest) {
//create if user have no cart
    createCartForUser(username);

    if (cartItemRequest.getProductId() != null && cartItemRequest.getComboId() == null) {
      handleAddProductToCart(username, cartItemRequest);
    } else if (cartItemRequest.getProductId() == null && cartItemRequest.getComboId() != null) {
      handleAddComboToCart(username, cartItemRequest);
    } else {
      throw new EntityNotFoundException("product or combo not found");
    }

  }

  @Transactional
  @Override
  public void handleAddProductToCart(String username, CartItemRequestDTO cartItemRequest) {
    List<CartItem> cartItemsOfUser = getCartItemsByUsername(username);
    if (cartItemsOfUser.isEmpty()) {
      addCartItemToCart(username, cartItemRequest);
    } else {
      List<Long> idOfProductsCurrent = cartItemsOfUser.stream()
              .map(ci -> {
                Product product = ci.getProduct();
                return (product != null) ? product.getId() : null;
              })
              .toList();

      if (idOfProductsCurrent.contains(cartItemRequest.getProductId())) {
        Optional<CartItem> cartItemToUpdateOpt = cartItemsOfUser.stream()
                .filter(c -> {
                  Product product = c.getProduct();
                  return product != null && Objects.equals(product.getId(), cartItemRequest.getProductId());
                })
                .findFirst();

        if (cartItemToUpdateOpt.isPresent()) {
          CartItem cartItemToUpdate = cartItemToUpdateOpt.get();
          Integer newQuantity = cartItemToUpdate.getQuantity() + cartItemRequest.getQuantity();
          updateCartItemQuantity(username, cartItemToUpdate.getId(), newQuantity);
        }
      } else {
        addCartItemToCart(username, cartItemRequest);
      }
    }
  }


  @Transactional
  @Override
  public void handleAddComboToCart(String username, CartItemRequestDTO cartItemRequest) {
    List<CartItem> cartItemsOfUser = getCartItemsByUsername(username);
    if (cartItemsOfUser.isEmpty()) {
      addCartItemToCart(username, cartItemRequest);
    } else {
      List<Long> idOfCombosCurrent = cartItemsOfUser.stream()
              .map(ci -> {
                Combo combo = ci.getCombo();
                return (combo != null) ? combo.getId() : null;
              })
              .toList();

      if (idOfCombosCurrent.contains(cartItemRequest.getComboId())) {
        Optional<CartItem> cartItemToUpdateOpt = cartItemsOfUser.stream()
                .filter(c -> {
                  Combo combo = c.getCombo();
                  return combo != null && Objects.equals(combo.getId(), cartItemRequest.getComboId());
                })
                .findFirst();

        if (cartItemToUpdateOpt.isPresent()) {
          CartItem cartItemToUpdate = cartItemToUpdateOpt.get();
          Integer newQuantity = cartItemToUpdate.getQuantity() + cartItemRequest.getQuantity();
          updateCartItemQuantity(username, cartItemToUpdate.getId(), newQuantity);
        }
      } else {
        addCartItemToCart(username, cartItemRequest);
      }
    }
  }



  @Override
  public void deleteCartItem(Long id, String username) {
    if (cartItemService.existById(id) && getCartItemsByUsername(username).contains(cartItemService.getCartItemById(id))) {
      cartItemService.deleteById(id);
    } else {
      throw new EntityNotFoundException("This cart item is not existed");
    }
  }
}
