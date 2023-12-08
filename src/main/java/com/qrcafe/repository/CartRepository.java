package com.qrcafe.repository;

import com.qrcafe.entity.Cart;
import com.qrcafe.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
  @Query("Select c.cartItems from Cart c where c.user.username = :username")
  List<CartItem> getCartItemsByUsername(@Param("username") String username);

  @Query("Select c FROM Cart c WHERE c.user.username = :username")
  Cart getCartByUsername(@Param("username") String username);
}
