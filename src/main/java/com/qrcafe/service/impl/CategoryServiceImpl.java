package com.qrcafe.service.impl;

import com.qrcafe.entity.Category;
import com.qrcafe.repository.CategoryRepository;
import com.qrcafe.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(Long id) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        return categoryOpt.orElse(null);
    }

    @Override
    public Category getCategoryByCategoryName(String categoryName) {
        return categoryRepository.findFirstByName(categoryName);
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Category save(Category category) {

        if(categoryRepository.existsByName(category.getName())){
            return null;
        }
        else {
            return categoryRepository.save(category);
        }

    }

    @Override
    public void deleteCategory(Category category) {
        categoryRepository.delete(category);
    }
}
