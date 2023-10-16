package com.qrcafe.repository;

import com.qrcafe.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findFirstByName(String categoryName);

    boolean existsByName(String name);

}
