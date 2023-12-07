package com.qrcafe.repository;

import com.qrcafe.entity.CartItem;
import com.qrcafe.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
  List<CartItem> findByCartId(Long cartId);

  @Query("SELECT ci.product FROM CartItem ci WHERE ci.id = :id")
  Product getProductByCartItemId(@Param("id") Long id);
  @Query("SELECT ci.combo FROM CartItem ci WHERE ci.id = :id")
  Product getComboByCartItemId(@Param("id") Long id);

}
