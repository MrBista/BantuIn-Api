package com.exercie.exercies.mapper;

import com.exercie.exercies.dto.request.CategoryDtoReq;
import com.exercie.exercies.dto.response.CategoryDtoRes;
import com.exercie.exercies.model.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class CategoryMapper {
    public Category toEntity(CategoryDtoReq categoryDtoReq){
        Category category = new Category();
        category.setDescription(categoryDtoReq.getDescription());
        category.setName(categoryDtoReq.getName());
        // default kita nullkan
        category.setProducts(null);
        return category;
    }

    public CategoryDtoRes toDto(Category category){
        CategoryDtoRes categoryDtoRes = new CategoryDtoRes();
        categoryDtoRes.setId(category.getId());
        categoryDtoRes.setName(category.getName());
        categoryDtoRes.setDescription(category.getDescription());
        categoryDtoRes.setCreatedAt(category.getCreatedAt());
        return categoryDtoRes;
    }

    public List<CategoryDtoRes> toDtoList(Iterable<Category> categories){
        return StreamSupport.stream(categories.spliterator(), false)
                .map(this::toDto)
                .toList();
    }
}
