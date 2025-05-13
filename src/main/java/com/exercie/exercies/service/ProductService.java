package com.exercie.exercies.service;

import com.exercie.exercies.dao.ProductDao;
import com.exercie.exercies.dao.ProductDaoImpl;
import com.exercie.exercies.dto.request.ProductDtoReq;
import com.exercie.exercies.dto.response.ProductDtoRes;
import com.exercie.exercies.exception.ResourceNotFoundException;
import com.exercie.exercies.mapper.ProductMapper;
import com.exercie.exercies.model.Category;
import com.exercie.exercies.model.Product;
import com.exercie.exercies.repository.CategoryRepository;
import com.exercie.exercies.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductDao productDao;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductDaoImpl productDao, ProductMapper productMapper, CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.productDao = productDao;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public List<ProductDtoRes> getAllProduct(){
        List<Product> findAllProduct = productDao.findAll();

        List<ProductDtoRes> productDtoRes = productMapper
                .toDtoList(findAllProduct)
                .stream()
                .map(product -> {
                    categoryRepository.findById(product.getCategoryId())
                            .ifPresent(product::addDetailCategory);
                    return product;
                })
                .toList();

        logger.info("list user {}", productDtoRes);

        return productDtoRes;
    }

    @Transactional
    public ProductDtoRes saveProduct(ProductDtoReq productDtoReq){
        // 1. cari categorynya dulu
        // 2. kalau ga ada throw
        // 3. kalau ada insert
       Category category =  categoryIfExist(productDtoReq.getCategoryId());

        Product productEntity = productMapper.toEntity(productDtoReq);

        productDao.saveDao(productEntity);

        ProductDtoRes productDtoRes = productMapper.toDto(productEntity);
        productDtoRes.addDetailCategory(category);
        return productDtoRes;
    }


    @Transactional
    public ProductDtoRes updateProduct(ProductDtoReq productDtoReq, Long id){
        Category category = categoryIfExist(productDtoReq.getCategoryId());
        productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        Product productEntity = productMapper.toEntity(productDtoReq);

        productEntity.setId(id);

        productRepository.save(productEntity);

        ProductDtoRes productDtoRes = productMapper.toDto(productEntity);
        productDtoRes.addDetailCategory(category);

        return productDtoRes;
    }


    public void deleteProduct(Long id){
        productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        productRepository.deleteById(id);
    }


    private Category categoryIfExist(Long id){
        return categoryRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }


}
