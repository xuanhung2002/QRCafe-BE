package com.qrcafe.controller;

import com.qrcafe.converter.Converter;
import com.qrcafe.dto.CartItemRequestDTO;
import com.qrcafe.dto.UpdateCartItemRequest;
import com.qrcafe.entity.CartItem;
import com.qrcafe.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

  @Autowired
  CartService cartService;
  @Autowired
  Converter converter;

  @GetMapping("")
  public ResponseEntity<?> getCartItemByUsername(Authentication authentication){
    if (authentication == null || !authentication.isAuthenticated()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("let's login");
    }
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    String username = userDetails.getUsername();

    List<CartItem> cartItems = cartService.getCartItemsByUsername(username);
    if(cartItems == null){
      return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No cart item");
    }
    return ResponseEntity.status(HttpStatus.OK).body(cartItems.stream().map(converter::toCartItemDTO).toList());
  }


  @PostMapping("/add")
  public ResponseEntity<String> addCartItemToCart(@RequestBody CartItemRequestDTO cartItemRequest,
                                                  Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    String username = userDetails.getUsername();

    try {
      cartService.handleAddCartItemToCart(username, cartItemRequest);
      return ResponseEntity.status(HttpStatus.OK).body("Add cart item to cart success");
    }
    catch (Exception e){
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
  }

  @PutMapping("/update")
  public ResponseEntity<String> updateCartItemQuantityInCart(
          @RequestBody UpdateCartItemRequest updateCartItemRequest,
          Authentication authentication) {

    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    String username = userDetails.getUsername();

    try {
      cartService.updateCartItemQuantity(username, updateCartItemRequest.getCartItemId(),
              updateCartItemRequest.getNewQuantity());
      return new ResponseEntity<>("Update success", HttpStatus.OK);
    }
    catch (Exception e){
      return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> deleteCartItemById(@PathVariable Long id, Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    String username = userDetails.getUsername();

    try {
      cartService.deleteCartItem(id, username);
      return ResponseEntity.status(HttpStatus.OK).body("Delete cart item succesfully!!");
    } catch (Exception e){
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }
}
