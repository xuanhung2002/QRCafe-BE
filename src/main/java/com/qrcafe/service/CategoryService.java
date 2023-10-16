package com.qrcafe.service;

import com.qrcafe.entity.Category;

import java.util.List;

public interface CategoryService {

    Category getCategoryById(Long id);
    Category getCategoryByCategoryName(String categoryName);

    List<Category> getAllCategory();

    Category save(Category category);

    void deleteCategory(Category category);
}
