package com.qrcafe.service;

import com.qrcafe.entity.Category;
import com.qrcafe.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAll(Integer pageNo, Integer pageSize, String sortBy);

    Optional<Product> getProductById(Long id);

    List<Product> getProductByCategoryName(Category category, Integer pageNo, Integer pageSize, String sortBy);

    Product save(Product product);

    void delete(Product product);

    List<Product> searchProduct(String searchKey);

    boolean existedById(Long id);

}
