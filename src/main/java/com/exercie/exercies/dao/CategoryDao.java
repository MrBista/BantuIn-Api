package com.exercie.exercies.dao;

import com.exercie.exercies.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryDao {
    Optional<Category> findById(Long id);
    List<Category>findAllCategory();

    Optional<Category> findByIdWithProducts(Long id);
}
