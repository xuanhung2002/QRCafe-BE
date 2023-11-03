package com.qrcafe.service.impl;

import com.qrcafe.entity.Category;
import com.qrcafe.entity.Product;
import com.qrcafe.repository.ProductRepository;
import com.qrcafe.service.CategoryService;
import com.qrcafe.service.ProductService;
import com.qrcafe.utils.PageableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryService categoryService;

    @Override
    public List<Product> getAll(Integer pageNo, Integer pageSize, String sortBy) {

        Pageable pageable = PageableUtils.getPageable(pageNo, pageSize, sortBy);

        Page<Product> pageResult = productRepository.findAll(pageable);

        pageResult.getContent();
        return pageResult.getContent();

    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> getProductByCategoryName(Category category, Integer pageNo, Integer pageSize, String sortBy) {
        Pageable pageable = PageableUtils.getPageable(pageNo, pageSize, sortBy);
        return productRepository.findByCategory(category, pageable);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void delete(Product product) {
        productRepository.delete(product);
    }

    @Override
    public List<Product> searchProduct(String searchKey) {
        List<Product> products = productRepository.searchProduct(searchKey);
        if (products.isEmpty()) {
            return null;
        } else {
            return products;
        }
    }

    @Override
    public boolean existedById(Long id) {
        return productRepository.existsById(id);
    }

}
