package com.qrcafe.repository;

import com.qrcafe.entity.Category;
import com.qrcafe.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) like LOWER(CONCAT('%', :searchKey, '%'))")
    List<Product> searchProduct(@Param("searchKey") String searchKey);
}
