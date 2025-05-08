package com.exercie.exercies.repository;

import com.exercie.exercies.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
