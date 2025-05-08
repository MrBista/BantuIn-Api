package com.exercie.exercies.dao;

import com.exercie.exercies.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDao {
    List<Product> findAll();
    List<Product> findAllByCategoryId(Long id);
    Optional<Product> findById(Long id);
    void saveDao(Product product);
    void update(Product product);
    void delete(Product product);
}
