package com.qrcafe.repository;

import com.qrcafe.entity.Category;
import com.qrcafe.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category, Pageable pageable);
}
