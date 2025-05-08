package com.exercie.exercies.controller;

import com.exercie.exercies.dto.request.CategoryDtoReq;
import com.exercie.exercies.dto.response.CategoryDtoRes;
import com.exercie.exercies.dto.response.CommonResponse;
import com.exercie.exercies.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    public ResponseEntity<?> findAllCategory(){

        return CommonResponse
                .generateResponse(categoryService.findAllCategory(), "Successfully get all category", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findCategoryById(@PathVariable("id") Long id){
        return CommonResponse
                .generateResponse(categoryService.findCategoryById(id), "Successfully get category by id", HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> saveCategory(@RequestBody CategoryDtoReq categoryDtoReq){
        return CommonResponse.generateResponse(categoryService.saveCategory(categoryDtoReq), "Successfulyy save new category", HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@RequestBody CategoryDtoReq categoryDtoReq, @PathVariable("id") Long id){
        return CommonResponse.generateResponse(categoryService.updateCategory(categoryDtoReq, id), "Successfully update category", HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id){
        categoryService.deleteCategoryById(id);

        return CommonResponse
                .generateResponse(true, "successfully delete category", HttpStatus.OK);
    }
}
