package com.exercie.exercies.service;

import com.exercie.exercies.dao.CategoryDao;
import com.exercie.exercies.dto.request.CategoryDtoReq;
import com.exercie.exercies.dto.response.CategoryDtoRes;
import com.exercie.exercies.exception.ResourceNotFoundException;
import com.exercie.exercies.helper.DateHelper;
import com.exercie.exercies.mapper.CategoryMapper;
import com.exercie.exercies.model.Category;
import com.exercie.exercies.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CategoryDao categoryDao;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper, CategoryDao categoryDao) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.categoryDao = categoryDao;
    }

    public List<CategoryDtoRes> findAllCategory(){
        // expect near future will be complicated query
        List<Category> findAllCategory =  categoryDao.findAllCategory();

        return categoryMapper.toDtoList(findAllCategory);
    }

    public CategoryDtoRes findCategoryById(Long id){
        Category category = findOrThrowCategory(id);

        return categoryMapper.toDto(category);
    }

    public void deleteCategoryById(Long id){
        // cek category ada apa nggak

        findOrThrowCategory(id);

        Optional<Category> category = categoryDao
                .findByIdWithProducts(id);

        if (category.isPresent() && !category.get().getProducts().isEmpty()){

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is product on this category");
        }




        categoryRepository.deleteById(id);

    }

    public CategoryDtoRes saveCategory(CategoryDtoReq categoryDtoReq){
        Category categoryEntity = categoryMapper.toEntity(categoryDtoReq);
        categoryEntity.setCreatedAt(DateHelper.getCurrentDate());
        categoryEntity.setId(null);
        categoryRepository.save(categoryEntity);

        return categoryMapper.toDto(categoryEntity);
    }


    public CategoryDtoRes updateCategory(CategoryDtoReq categoryDtoReq, Long id){

        findOrThrowCategory(id);

        Category categoryEntity = categoryMapper.toEntity(categoryDtoReq);
        categoryEntity.setId(id);
        categoryRepository.save(categoryEntity);

        return categoryMapper.toDto(categoryEntity);
    }

    private Category findOrThrowCategory(Long id){
        return categoryRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }
}
