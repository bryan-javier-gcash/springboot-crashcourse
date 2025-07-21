package com.example.gcashtrainingspringboot.service;

import com.example.gcashtrainingspringboot.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAllProducts();
    Optional<Product> findProductByID(Long id);
    Product saveProduct(Product product);
    Optional<Product> update(Long id, Product newProduct);
    Optional<Product> patch(Long id, Product patchProduct);
    boolean deleteProduct(Long id);
}
